package br.com.artigo.handlers;

import java.util.ArrayList;

import javax.swing.JOptionPane;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.internal.resources.Project;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceVisitor;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.internal.core.JavaProject;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.handlers.HandlerUtil;

import br.com.artigo.ast.Visitor;
import br.com.artigo.model.Coupling;
import br.com.artigo.model.InfoClasse;
import br.com.artigo.model.InfoPacote;

@SuppressWarnings("restriction")
public class SampleHandler extends AbstractHandler {

	public static ExecutionEvent event;

	public static ArrayList<InfoPacote> pacotes;

	public static ArrayList<InfoPacote> altPacotes;

	public static ArrayList<InfoClasse> classe;

	public boolean classeAutomatica;

	IWorkbenchWindow window;

	// TODO Comeca aqui a execucao do programa
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		SampleHandler.event = event;

		window = HandlerUtil.getActiveWorkbenchWindowChecked(event);
		pacotes = new ArrayList<InfoPacote>();
		altPacotes = new ArrayList<InfoPacote>();
		SampleHandler.classe = new ArrayList<>();

		this.hideView();

		// TODO Pega o projeto selecionado no workspace
		IProject iProject = getProjectFromWorkspace(event);
		
		if(iProject == null)
			return null;

		try {

			// TODO recupera informacoes de pacotes e classe
			this.recuperaInformacoes(iProject);

			// TODO calcula a similaridade dos pacotes atuais add a media
			this.calcSimPacoteAtual();

			// TODO adiciona as classes Padroes nos pacotes
			this.adicionaClassePadrao();

			// TODO calcula a similaridade de classe com as classes padroes do pacote
			this.calcSimClassePadrao();

			// TODO calcula novas similaridades
			this.calcSimClasses();

			// TODO calcula Afferent Coupling
			this.afferentCoupling();

			// TODO calcula Efferent Coupling
			this.efferentCoupling();

			this.coupling();

			this.openView();

		} catch (CoreException e) {
			e.printStackTrace();
		}
		MessageDialog.openInformation(window.getShell(), "Obrigado!",
				"Obrigado por utilizar a ferramente de similaridade");
		return null;
	}

	private void coupling() {

		for (InfoPacote infoPacote : pacotes) {

			for (InfoClasse infoclasse : infoPacote.getClassesPacote()) {

				if (infoclasse.getPacoteAlterado() != null) {
					// calcula afferent em relação ao pacote atual
					infoclasse.setAfferentAtualCl(afferent(infoclasse, infoclasse.getPacoteAtual(), true));
					// calcula afferent em relacao ao novo pacote
					infoclasse.setAfferentAtualizadoCl(afferent(infoclasse, infoclasse.getPacoteAlterado(), false));
					// calcula efferent em relação ao pacote atual
					infoclasse.setEfferentAtualCl(efferent(infoclasse, infoclasse.getPacoteAtual(), true));
					// calcula efferent em relacao ao novo pacote
					infoclasse.setEfferentAtualizadoCl(efferent(infoclasse, infoclasse.getPacoteAlterado(), false));
				}

			}
		}
	}

	private Coupling afferent(InfoClasse infoClasse, InfoPacote infoPacote, boolean isAtual) {

		Coupling coupling = new Coupling();
		coupling.setNomePacote(infoPacote.getNomePacote());

		if (!isAtual) {
			coupling.setQtdClassePacote(infoPacote.getClassesPacote().size() + 1);
		} else {
			coupling.setQtdClassePacote(infoPacote.getClassesPacote().size());
		}

		ArrayList<String> classes = new ArrayList<>();

		for (InfoClasse cl : infoPacote.getClassesPacote()) {

			if (!cl.equals(infoClasse)) {
				for (String tipo : cl.getTipos()) {
					tipo += ".java";
					if (tipo.equals(infoClasse.getNomeClasse())) {
						if (!this.verificaExistencia(cl.getNomeClasse(), classes)) {
							classes.add(cl.getNomeClasse());
						}
					}
				}
			}
		}
		coupling.setValor(classes.size());
		return coupling;
	}

	private Coupling efferent(InfoClasse infoClasse, InfoPacote infoPacote, boolean isAtual) {

		Coupling coupling = new Coupling();
		coupling.setNomePacote(infoPacote.getNomePacote());

		if (!isAtual) {
			coupling.setQtdClassePacote(infoPacote.getClassesPacote().size() + 1);
		} else {
			coupling.setQtdClassePacote(infoPacote.getClassesPacote().size());
		}

		ArrayList<String> classes = new ArrayList<>();

		for (String tipo : infoClasse.getTipos()) {
			if (this.isClasse(tipo, infoPacote, infoClasse)) {
				if (!this.verificaExistencia(tipo, classes)) {
					classes.add(tipo);
				}
			}
		}

		coupling.setValor(classes.size());

		return coupling;
	}

	private void afferentCoupling() {
		// TODO Ca representa a contagem de quantas classes diferentes referem-se à
		// classe atual, por meio de campos ou parâmetros
		for (InfoPacote infoPacote : pacotes) {
			for (InfoClasse infoClasse : infoPacote.getClassesPacote()) {
				this.calculaAfferent(infoClasse);
			}
		}
	}

	private void efferentCoupling() {
		// TODO Ce representa a contagem de quantas classes diferentes a classe atual
		// faz referência, por meio de campos ou parâmetros
		for (InfoPacote infoPacote : pacotes) {
			for (InfoClasse infoClasse : infoPacote.getClassesPacote()) {
				this.calculaEfferent(infoClasse);
			}
		}
	}

	private void calculaEfferent(InfoClasse infoClasse) {
		ArrayList<String> classes;
		Coupling coupling;
		for (InfoPacote infoPacote : pacotes) {
			classes = new ArrayList<>();
			coupling = new Coupling();
			coupling.setNomePacote(infoPacote.getNomePacote());
			coupling.setQtdClassePacote(infoPacote.getClassesPacote().size());

			for (String tipo : infoClasse.getTipos()) {
				if (this.isClasse(tipo, infoPacote, infoClasse)) {
					if (!this.verificaExistencia(tipo, classes)) {
						classes.add(tipo);
					}
				}
			}
			coupling.setValor(classes.size());
			infoClasse.getEfferentCoupling().add(coupling);
		}
	}

	private boolean verificaExistencia(String tipo, ArrayList<String> classes) {

		for (String string : classes) {
			if (string.equals(tipo)) {
				return true;
			}
		}
		return false;
	}

	private boolean isClasse(String tipo, InfoPacote infoPacote, InfoClasse infoClasse) {

		// percorre o pacote verificando se o tipo é uma classe do mesmo
		for (InfoClasse cl : infoPacote.getClassesPacote()) {
			// verifica se não é a classe que estou atualizando o acoplamento
			if (!cl.equals(infoClasse)) {
				String nome = tipo + ".java";
				if (cl.getNomeClasse().equals(nome)) {
					return true;
				}
			}
		}
		return false;
	}

	private void calculaAfferent(InfoClasse infoClasse) {

		ArrayList<String> classes;
		Coupling coupling;
		for (InfoPacote infoPacote : pacotes) {
			classes = new ArrayList<>();
			coupling = new Coupling();
			coupling.setNomePacote(infoPacote.getNomePacote());
			coupling.setQtdClassePacote(infoPacote.getClassesPacote().size());

			for (InfoClasse cl : infoPacote.getClassesPacote()) {

				if (!cl.equals(infoClasse)) {
					for (String tipo : cl.getTipos()) {
						tipo += ".java";
						if (tipo.equals(infoClasse.getNomeClasse())) {
							if (!this.verificaExistencia(cl.getNomeClasse(), classes)) {
								classes.add(cl.getNomeClasse());
							}
						}
					}
				}

			}

			coupling.setValor(classes.size());
			infoClasse.getAfferentCoupling().add(coupling);
		}
	}

	private void calcSimClasses() {

		// TODO adiciona pacotes e classes padroes atuais
		for (InfoPacote infoPacote : pacotes) {
			InfoPacote pacAux = new InfoPacote();
			pacAux.setClassePadrao(infoPacote.getClassePadrao());
			pacAux.setNomePacote(infoPacote.getNomePacote());
			pacAux.getClassesPacote().add(pacAux.getClassePadrao());
			SampleHandler.altPacotes.add(pacAux);
		}

		// TODO percorre as os pacotes atuais e verifica a similaridade das classes com
		// outras classes padroes e add ao pacote

		for (InfoPacote infoPacote : pacotes) {

			for (InfoClasse infoClasse : infoPacote.getClassesPacote()) {
				// TODO verifica se a classe não é padrao
				if (!infoClasse.equals(infoPacote.getClassePadrao())) {
					this.calcOutrosPacotes(infoClasse, infoPacote);

				}
			}
		}
	}

	private void calcOutrosPacotes(InfoClasse infoClasse, final InfoPacote pacoteAtual) {

		Float simiMaior = infoClasse.getSimilaridadeClasse();
		Float simiAux = (float) 0;
		InfoPacote aux = pacoteAtual;

		for (InfoPacote infoPacote : pacotes) {
			// TODO verifica se não é o pacote que a classe já está
			if (!infoPacote.equals(pacoteAtual)) {
				simiAux = this.calcJaccard(infoClasse.getTipos(), infoPacote.getClassePadrao().getTipos());
				// TODO verifica qual a maior similaridade
				if (simiAux > simiMaior) {
					simiMaior = simiAux;
					aux = infoPacote;
				}
			}
		}

		// TODO verifica se ouve alteração de pacote
		if (!pacoteAtual.equals(aux)) {
			// TODO adiciona ao novo pacote
			this.addClasseNovoPacote(aux.getNomePacote(), infoClasse);
			infoClasse.setPacoteAlterado(aux);
			infoClasse.setSimNovoPacote(simiMaior);

			// TODO adiciona a lista de classes que podem ser alteradas
			SampleHandler.classe.add(infoClasse);
		} else {
			// TODO classe continua no mesmo pacote
			this.addClasseNovoPacote(pacoteAtual.getNomePacote(), infoClasse);
		}
	}

	private void addClasseNovoPacote(String nomePacote, InfoClasse infoClasse) {

		for (InfoPacote infoPacote : altPacotes) {
			if (infoPacote.getNomePacote().equals(nomePacote)) {
				infoPacote.getClassesPacote().add(infoClasse);
			}
		}
	}

	private void adicionaClassePadrao() {

		// TODO recupera a classe padrão com o usuário ou
		// verifica qual a maior similaridade e add a classe Padrao ao pacote
		for (InfoPacote infoPacote : pacotes) {
			if (infoPacote.getClassesPacote().size() > 0) {
				this.geraClassePadrao(infoPacote);
			}
		}
	}

	private InfoClasse retornaClasseByNome(String nomeClasse, InfoPacote pacote) {

		for (InfoClasse classe : pacote.getClassesPacote()) {

			String nomeUpper = classe.getNomeClasse().toUpperCase();

			if (nomeClasse.equals(nomeUpper)) {
				return classe;
			}
		}

		return null;
	}

	private void geraClassePadrao(InfoPacote infoPacote) {

		InfoClasse classeAux = infoPacote.getClassesPacote().get(0);
		for (InfoClasse infoClasse : infoPacote.getClassesPacote()) {
			if (infoClasse.getmSimilPacoteAtual() > classeAux.getmSimilPacoteAtual()) {
				classeAux = infoClasse;
			}
		}
		infoPacote.setClassePadrao(classeAux);
	}

	private void recuperaInformacoes(final IProject project) throws CoreException {
		project.accept(new IResourceVisitor() {

			@Override
			public boolean visit(IResource resource) throws JavaModelException {
				// add pacotes
				if (resource instanceof IFile && resource.getName().endsWith(".java")) {
					// cria a unidade de compilação
					ICompilationUnit unit = ((ICompilationUnit) JavaCore.create((IFile) resource));
					new Visitor(unit); // instancia AST
				}
				return true;
			}
		});
	}

	private void calcSimPacoteAtual() {

		// TODO percorre todos os pacotes
		for (InfoPacote infoPacote : pacotes) {
			/*
			 * percorre todas as classes do pacote e realiza a soma da media de similaridade
			 */
			for (InfoClasse classe : infoPacote.getClassesPacote()) {
				classe.setmSimilPacoteAtual(this.calcClasseCPacote(classe, infoPacote.getClassesPacote()));
			}

		}

	}

	private void calcSimClassePadrao() {
		for (InfoPacote infoPacote : pacotes) {

			for (InfoClasse infoClasse : infoPacote.getClassesPacote()) {
				infoClasse.setSimilaridadeClasse(
						this.calcJaccard(infoPacote.getClassePadrao().getTipos(), infoClasse.getTipos()));
			}

		}
	}

	private float calcClasseCPacote(InfoClasse classeAux, ArrayList<InfoClasse> listaClasses) {

		if (listaClasses.size() == 1) {
			return 1;
		}

		float soma = 0;

		for (InfoClasse infoClasse : listaClasses) {
			if (!infoClasse.equals(classeAux)) {
				soma += this.calcJaccard(classeAux.getTipos(), infoClasse.getTipos());
			}
		}

		if (soma == 0) {
			return soma;
		} else {
			return (soma / (listaClasses.size() - 1));
		}
	}

	private float calcJaccard(ArrayList<String> listaA, ArrayList<String> listaB) {

		float totalA = listaA.size();
		float totalB = listaB.size();

		float a = 0;
		float b = 0;
		float c = 0;

		// calcula tipos iguais
		for (String stringA : listaA) {
			for (String stringB : listaB) {
				// verifica se os tipos sao iguais
				if (stringA.equals(stringB)) {
					a += 1;
				}
			}
		}

		// calcula diferença
		b = totalA - a;

		// calcula diferença de b
		c = totalB - a;

		if (a == 0) {
			return 0;
		}

		return (a / (a + b + c));

	}

	private IProject getProjectFromWorkspace(ExecutionEvent event) {

		TreeSelection selection = (TreeSelection) HandlerUtil.getCurrentSelection(event);

		if (selection == null || selection.getFirstElement() == null) {
			MessageDialog.openInformation(HandlerUtil.getActiveShell(event), "Atenção!", "Selecione um projeto");
			return null;
		}

		JavaProject jp;
		Project p;

		try {
			jp = (JavaProject) selection.getFirstElement();
			return jp.getProject();
		} catch (ClassCastException e) {
			try {
			p = (Project) selection.getFirstElement();
			return p.getProject();
			} catch (Exception e1) {
				MessageDialog.openInformation(HandlerUtil.getActiveShell(event), "Atenção!", "Selecione a raiz do projeto");
				return null;
			}
		}
	}

	private void hideView() {
		IWorkbenchPage wp = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
		IViewPart myView = wp.findView("br.com.artigo.view.SampleView");
		wp.hideView(myView);
	}

	private void openView() {
		try {
			PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage()
					.showView("br.com.artigo.view.SampleView");
		} catch (PartInitException e) {
			e.printStackTrace();
		}
	}

}