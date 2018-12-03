package br.com.artigo.ast;

import java.util.List;

import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.ClassInstanceCreation;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.ExpressionStatement;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.PackageDeclaration;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.VariableDeclarationStatement;

import br.com.artigo.handlers.SampleHandler;
import br.com.artigo.model.InfoClasse;
import br.com.artigo.model.InfoPacote;

public class Visitor extends ASTVisitor {

	private CompilationUnit fullClass;
	private InfoPacote infoPacote;
	private InfoClasse infoClasse;
	private String nomeClasse;

	@SuppressWarnings("deprecation")
	public Visitor(ICompilationUnit unit) throws JavaModelException {

		// constrói parser da AST
		ASTParser parser = ASTParser.newParser(AST.JLS4);
		parser.setKind(ASTParser.K_COMPILATION_UNIT);
		parser.setSource(unit);
		parser.setCompilerOptions(JavaCore.getOptions());
		parser.setProject(unit.getJavaProject());
		parser.setResolveBindings(true);
		parser.setBindingsRecovery(true);
		
		nomeClasse = unit.getElementName().toString();
		
		this.fullClass = (CompilationUnit) parser.createAST(null);// parse
		this.fullClass.accept(this);

		

	}

	/*
	 * Recupera o nome do pacote
	 */
	@Override
	public boolean visit(PackageDeclaration node) {

		// TODO adiciona o pacote
		if (!this.verificaPacote(node.getName().toString())) {
			infoPacote = new InfoPacote();
			infoPacote.setNomePacote(node.getName().toString());
			SampleHandler.pacotes.add(infoPacote);

		} else {
			this.recuperaPacote(node.getName().toString());
		}

		// TODO adiciona a classe ao pacote atual
		this.infoClasse = new InfoClasse();
		this.infoClasse.setNomeClasse(this.nomeClasse);
		this.infoClasse.setPacoteAtual(infoPacote);
		this.infoPacote.getClassesPacote().add(infoClasse);
		return super.visit(node);
	}

	@Override
	public boolean visit(TypeDeclaration node) {

		String superClass = "";

		try {
			superClass = node.getSuperclassType().toString();
			verificaExistencia(superClass, this.infoClasse);
		} catch (Exception e) {
			
		}

		return super.visit(node);
	}

	@Override
	public boolean visit(FieldDeclaration node) {

		verificaExistencia(node.getType().toString(), this.infoClasse);
		return super.visit(node);
	}

	@Override
	public boolean visit(MethodDeclaration node) {

		try {
			String retorno = node.getReturnType2().toString();
			if (!retorno.equals(null) && !retorno.equals("void")) {
				verificaExistencia(retorno, this.infoClasse);
			}
		} catch (Exception e) {
			// TODO: handle exception
		}

		@SuppressWarnings("unchecked")
		List<SingleVariableDeclaration> n = node.parameters();

		for (SingleVariableDeclaration v : n) {
			verificaExistencia(v.getType().toString(), this.infoClasse);
		}
		return super.visit(node);
	}

	@Override
	public boolean visit(Block node) {
		// TODO Auto-generated method stub

		for (int i = 0; i < node.statements().size(); i++) {
			try {
				VariableDeclarationStatement aux = (VariableDeclarationStatement) node.statements().get(i);
				verificaExistencia(aux.getType().toString(), this.infoClasse);
			} catch (Exception e) {
				// TODO: handle exception
			}

			/*
			 * try { ReturnStatement aux = (ReturnStatement) node.statements().get(i);
			 * verificaExistencia(aux.getExpression(), this.info); } catch (Exception e) {
			 * // TODO: handle exception }
			 */

		}

		return super.visit(node);
	}
	
	@Override
	public boolean visit(ExpressionStatement node) {
		// TODO Auto-generated method stub
		return super.visit(node);
	}
	
	@Override
	public boolean visit(ClassInstanceCreation node) {
		// TODO Auto-generated method stub
		this.verificaExistencia(node.getType().toString(),this.infoClasse);
		return super.visit(node);
	}

	private void verificaExistencia(String nome, final InfoClasse aux) {
		
	//	if(nome.equals("byte") || nome.equals("short") || nome.equals("int") || nome.equals("long") || nome.equals("float") 
	//			|| nome.equals("double") || nome.equals("char") || nome.equals("boolean") || nome.equals("String")) {
	//		return;
	//	}

		boolean existe = false;

		for (String nmClasse : aux.getTipos()) {
			if (nmClasse.equals(nome)) {
				existe = true;
				break;
			}
		}
		

		if (!existe) {
			aux.getTipos().add(nome);
		}

	}
	
	/*
	 * Verifica se o pacote ja foi adicionado
	 */
	private boolean verificaPacote(String nomePacote) {
		// verifica se o pacote ja existe
		for (InfoPacote pacote : SampleHandler.pacotes) {
			if (pacote.getNomePacote().equals(nomePacote)) {
				return true;
			}
		}
		return false;
	}
	
	/*
	 * Recupera pacote
	 */
	private void recuperaPacote(String nomePacote) {
		// recupera o pacote da classe atual
		for (InfoPacote pacote : SampleHandler.pacotes) {
			if (pacote.getNomePacote().equals(nomePacote)) {
				this.infoPacote = pacote;
			}
		}
	}


}