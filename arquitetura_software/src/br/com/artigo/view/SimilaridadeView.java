package br.com.artigo.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.border.MatteBorder;

import br.com.artigo.model.InfoClasse;
import br.com.artigo.model.InfoPacote;

public class SimilaridadeView extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6364517519563324843L;
	ArrayList<InfoPacote> pacotes, alterados;
	ArrayList<JPanel> jPanels = new ArrayList<>();

	public JFrame frame;
	private JPanel panel1;
	private JLabel lblPacote_1;
	private JPanel panel2;
	private JLabel label;
	private JTextField textField;
	private JTextField textField_1;
	private JTextField textField_2;
	private JPanel panel3;
	private JLabel label_1;
	private JTextField textField_3;
	private JTextField textField_4;
	private JTextField textField_5;

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					SimilaridadeView window = new SimilaridadeView(null,null);
					window.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public SimilaridadeView(ArrayList<InfoPacote> pacotes, ArrayList<InfoPacote> alterados) {
		this.pacotes = pacotes;
		this.alterados = alterados;
		initialize();
	}

	private void initialize() {

		setBounds(100, 100, 1200, 520);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		getContentPane().setLayout(null);
		Container container = this.getContentPane();
		container.setPreferredSize(new Dimension(800, 800));
		setResizable(false);
		setTitle("Similaridade Atual");

		JScrollPane scroll = new JScrollPane();
		getContentPane().add(scroll, BorderLayout.CENTER);
		
		
		JButton btnVerificarNovaSimilaridade = new JButton("Verificar Nova Similaridade");
		btnVerificarNovaSimilaridade.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				SimilaridadeView2 window = new SimilaridadeView2(pacotes,alterados);
				window.setVisible(true);
			}
		});
		btnVerificarNovaSimilaridade.setBounds(10, 449, 249, 25);
		getContentPane().add(btnVerificarNovaSimilaridade);

		int valorInicial = 10;

		if (pacotes != null) {

			int tamanhoTela = pacotes.size() * 300;

			setBounds(100, 100, tamanhoTela, 500);
			
			for (InfoPacote infoPacote : pacotes) {

				JPanel pac = new JPanel();
				pac.setLayout(null);
				pac.setBackground(Color.LIGHT_GRAY);
				pac.setBounds(valorInicial, 12, 250, 430);

				JLabel labelPacote;
				labelPacote = new JLabel(infoPacote.getNomePacote());
				labelPacote.setBounds(55, 5, 150, 15);
				pac.add(labelPacote);

				Circulo circulo = new Circulo(Color.decode(infoPacote.getPaleta().getC100()),
						infoPacote.getClassePadrao().getNomeClasse());
				circulo.setBorder(new MatteBorder(1, 1, 1, 1, (Color) new Color(0, 0, 0)));
				circulo.setBounds(86, 32, 85, 73);
				pac.add(circulo);
				circulo.setLayout(null);
				Linha l = new Linha();
				l.setBounds(0, 104, 251, 15);
				pac.add(l);

				getContentPane().add(pac);
				jPanels.add(pac);

				valorInicial += 300;

				int valorC2 = 140;
				int i = 0;
				// adiciona as classes
				for (InfoClasse infoClasse : infoPacote.getClassesPacote()) {

					if (!infoPacote.getClassePadrao().equals(infoClasse)) {
						
						String cor = infoClasse.getCorAtual();
						
						if (i % 2 == 0) {
							Circulo circulo_1 = new Circulo(Color.decode(cor), infoClasse.getNomeClasse(),infoClasse.getSimilaridadeClasse());
							circulo_1.setLayout(null);
							circulo_1.setBorder(new MatteBorder(1, 1, 1, 1, (Color) new Color(0, 0, 0)));
							circulo_1.setBounds(25, valorC2, 85, 73);
							pac.add(circulo_1);

						} else {
							Circulo circulo_1 = new Circulo(Color.decode(cor), infoClasse.getNomeClasse(),infoClasse.getSimilaridadeClasse());
							circulo_1.setLayout(null);
							circulo_1.setBorder(new MatteBorder(1, 1, 1, 1, (Color) new Color(0, 0, 0)));
							circulo_1.setBounds(155, valorC2, 85, 73);
							pac.add(circulo_1);
							valorC2 += 97;
						}
						i++;
					}

				}

			}
		} else {

			panel1 = new JPanel();
			panel1.setAutoscrolls(true);
			panel1.setLayout(null);
			panel1.setBackground(Color.LIGHT_GRAY);
			panel1.setBounds(10, 12, 250, 430);
			getContentPane().add(panel1);
			lblPacote_1 = new JLabel("Pacote 2");
			lblPacote_1.setBounds(86, 5, 61, 15);
			panel1.add(lblPacote_1);
			Circulo circulo = new Circulo(Color.RED);
			circulo.setBorder(new MatteBorder(1, 1, 1, 1, (Color) new Color(0, 0, 0)));
			circulo.setBounds(86, 32, 85, 73);
			panel1.add(circulo);
			circulo.setLayout(null);
			Linha l = new Linha();
			l.setBounds(0, 104, 251, 15);
			panel1.add(l);

			Circulo circulo_1 = new Circulo(Color.RED);
			circulo_1.setLayout(null);
			circulo_1.setBorder(new MatteBorder(1, 1, 1, 1, (Color) new Color(0, 0, 0)));
			circulo_1.setBounds(25, 140, 85, 73);
			panel1.add(circulo_1);

			Circulo circulo_2 = new Circulo(Color.RED);
			circulo_2.setLayout(null);
			circulo_2.setBorder(new MatteBorder(1, 1, 1, 1, (Color) new Color(0, 0, 0)));
			circulo_2.setBounds(154, 140, 85, 73);
			panel1.add(circulo_2);

			Circulo circulo_3 = new Circulo(Color.RED);
			circulo_3.setLayout(null);
			circulo_3.setBorder(new MatteBorder(1, 1, 1, 1, (Color) new Color(0, 0, 0)));
			circulo_3.setBounds(25, 237, 85, 73);
			panel1.add(circulo_3);

			Circulo circulo_4 = new Circulo(Color.RED);
			circulo_4.setLayout(null);
			circulo_4.setBorder(new MatteBorder(1, 1, 1, 1, (Color) new Color(0, 0, 0)));
			circulo_4.setBounds(154, 237, 85, 73);
			panel1.add(circulo_4);

			panel2 = new JPanel();
			panel2.setLayout(null);
			panel2.setBackground(Color.LIGHT_GRAY);
			panel2.setBounds(310, 12, 250, 372);
			getContentPane().add(panel2);

			label = new JLabel("Pacote 2");
			label.setBounds(86, 5, 61, 15);
			panel2.add(label);

			textField = new JTextField();
			textField.setText("Classe Padrao 2");
			textField.setColumns(10);
			textField.setBackground(Color.YELLOW);
			textField.setBounds(51, 32, 114, 19);
			panel2.add(textField);

			textField_1 = new JTextField();
			textField_1.setText("Classe P 1");
			textField_1.setColumns(10);
			textField_1.setBackground(new Color(0, 22, 250));
			textField_1.setBounds(51, 63, 114, 19);
			panel2.add(textField_1);

			textField_2 = new JTextField();
			textField_2.setText("Classe P 2");
			textField_2.setColumns(10);
			textField_2.setBounds(51, 94, 114, 19);
			panel2.add(textField_2);

			panel3 = new JPanel();
			panel3.setLayout(null);
			panel3.setBackground(Color.LIGHT_GRAY);
			panel3.setBounds(610, 12, 251, 372);
			getContentPane().add(panel3);

			label_1 = new JLabel("Pacote 2");
			label_1.setBounds(86, 5, 61, 15);
			panel3.add(label_1);

			textField_3 = new JTextField();
			textField_3.setText("Classe Padrao 2");
			textField_3.setColumns(10);
			textField_3.setBackground(Color.YELLOW);
			textField_3.setBounds(51, 32, 114, 19);
			panel3.add(textField_3);

			textField_4 = new JTextField();
			textField_4.setText("Classe P 1");
			textField_4.setColumns(10);
			textField_4.setBackground(new Color(0, 22, 250));
			textField_4.setBounds(51, 63, 114, 19);
			panel3.add(textField_4);

			textField_5 = new JTextField();
			textField_5.setText("Classe P 2");
			textField_5.setColumns(10);
			textField_5.setBounds(51, 94, 114, 19);
			panel3.add(textField_5);

		}
	}

	
}
