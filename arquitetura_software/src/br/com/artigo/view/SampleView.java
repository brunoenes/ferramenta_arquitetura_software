package br.com.artigo.view;

import java.text.DecimalFormat;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.ui.handlers.HandlerUtil;
import org.eclipse.ui.part.ViewPart;

import br.com.artigo.handlers.SampleHandler;
import br.com.artigo.model.InfoClasse;

public class SampleView extends ViewPart {

	public static final String ID = "br.com.artigo.view.SampleView";

	private TableViewer viewer;
	private Action doubleClickAction;

	public void createPartControl(Composite parent) {
		GridLayout layout = new GridLayout(4, false);
		parent.setLayout(layout);
		viewer = new TableViewer(parent, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL | SWT.FULL_SELECTION | SWT.BORDER);

		DecimalFormat df = new DecimalFormat("0.00");

		String[] titles = { "Nome Classe", "Pacote Atual", "Similaridade Atual", "Pacote Destino",
				"Similaridade Destino" };
		int[] bounds = { 220, 220, 150, 220, 150 };

		// TODO nome classe
		TableViewerColumn col = createTableViewerColumn(titles[0], bounds[0], 0);
		col.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				InfoClasse classe = (InfoClasse) element;
				return classe.getNomeClasse();
			}

		});

		// TODO pacote atual
		TableViewerColumn col1 = createTableViewerColumn(titles[1], bounds[1], 1);
		col1.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				InfoClasse classe = (InfoClasse) element;
				return classe.getPacoteAtual().getNomePacote();
			}
		});

		// TODO similaridade atual
		TableViewerColumn col2 = createTableViewerColumn(titles[2], bounds[2], 2);
		col2.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				InfoClasse classe = (InfoClasse) element;
				return df.format(classe.getSimilaridadeClasse() * 100) + "%";
			}
		});

		// TODO pacote destino
		TableViewerColumn col3 = createTableViewerColumn(titles[3], bounds[3], 3);
		col3.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				InfoClasse classe = (InfoClasse) element;
				return classe.getPacoteAlterado().getNomePacote();
			}
		});

		// TODO similarida destino
		TableViewerColumn col4 = createTableViewerColumn(titles[4], bounds[4], 4);
		col4.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				InfoClasse classe = (InfoClasse) element;
				return df.format(classe.getSimNovoPacote() * 100) + "%";
			}
		});

		viewer.refresh();

		final Table table = viewer.getTable();
		table.setHeaderVisible(true);
		table.setLinesVisible(true);

		viewer.setContentProvider(ArrayContentProvider.getInstance());

		// lista criada com os elementos a serem exibidos
		viewer.setInput(SampleHandler.classe);
		getSite().setSelectionProvider(viewer);

		GridData gridData = new GridData();
		gridData.verticalAlignment = GridData.FILL;
		gridData.horizontalSpan = 2;
		gridData.grabExcessHorizontalSpace = true;
		gridData.grabExcessVerticalSpace = true;
		gridData.horizontalAlignment = GridData.FILL;
		viewer.getControl().setLayoutData(gridData);

		hookDoubleClickAction();

	}

	public void setFocus() {
		viewer.getControl().setFocus();
	}

	private TableViewerColumn createTableViewerColumn(String title, int bound, final int colNumber) {
		final TableViewerColumn viewerColumn = new TableViewerColumn(viewer, SWT.NONE);
		final TableColumn column = viewerColumn.getColumn();
		column.setText(title);
		column.setWidth(bound);
		column.setResizable(true);
		column.setMoveable(true);
		return viewerColumn;
	}

	private void hookDoubleClickAction() {
		viewer.addDoubleClickListener(new IDoubleClickListener() {
			public void doubleClick(DoubleClickEvent event) {
				doubleClickAction.run();
			}
		});

		doubleClickAction = new Action() {
			public void run() {
				IStructuredSelection selection = (IStructuredSelection) viewer.getSelection();
				InfoClasse classe = (InfoClasse) selection.getFirstElement();
				DecimalFormat df = new DecimalFormat("0.00");
				String msg = "";

				// informações gerais

				msg += "Nome de Classe: " + classe.getNomeClasse() + "\n";
				msg += "Pacote Atual: " + classe.getPacoteAtual().getNomePacote() + " - "
						+ df.format(classe.getSimilaridadeClasse() * 100) + "% - " + classe.getPacoteAtual().getClassePadrao().getNomeClasse() + "\n";
				msg += "Pacote Destino: " + classe.getPacoteAlterado().getNomePacote() + " - "
						+ df.format(classe.getSimNovoPacote() * 100) + "% - " + classe.getPacoteAlterado().getClassePadrao().getNomeClasse() + "\n";

				
				msg += "\nAfferent Coupling: \n";
				msg += "-Pacote Atual: " + classe.getAfferentAtual().getPorcentagem() + "%, \n";
				msg += "-Pacote Destino: " + classe.getAfferentAtualizado().getPorcentagem() + "% \n";
				
				msg += "\nEfferent Coupling: \n ";
				msg += "-Pacote Atual: " + classe.getEfferentAtual().getPorcentagem() + "%, \n";
				msg += "-Pacote Destino: " + classe.getEfferentAtualizado().getPorcentagem() + "% ";
				
				
			/*	msg += "\nAfferent Coupling: \n {\n";

				for (Coupling coupling : classe.getAfferentCoupling()) {
					msg += "   " + coupling.getNomePacote() + " - " + coupling.getPorcentagem() + "%,\n";
				}

				msg += "}\n\nEfferent Coupling: \n{";

				for (Coupling coupling : classe.getEfferentCoupling()) {
					msg += "   " + coupling.getNomePacote() + " - " + coupling.getPorcentagem() + ",\n";
				}
				msg += "}"; */

				MessageDialog.openInformation(HandlerUtil.getActiveShell(SampleHandler.event), "Informações da Classe",
						msg);
			}

		};
	}
}