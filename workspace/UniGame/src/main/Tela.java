package main;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;

import javax.swing.ButtonGroup;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import client.TCPClient;
import server.TCPServer;
import sun.print.resources.serviceui;
import telas.Janela;
import telas.Jogo;

import javax.swing.JRadioButton;

import java.awt.Panel;

import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;

import java.awt.event.ActionListener;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.awt.event.ActionEvent;

import javax.swing.JButton;

public class Tela extends JFrame {

	private JPanel contentPane;
	private JTextField txtIp;
	private JTextField txtPorta;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Tela frame = new Tela();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public Tela() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 330, 174);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);

		JPanel panel = new JPanel();
		contentPane.add(panel, BorderLayout.NORTH);

		final JRadioButton rdbtnServer = new JRadioButton("Server");
		rdbtnServer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				txtIp.setEnabled(false);
			}
		});
		panel.add(rdbtnServer);

		final JRadioButton rdbtnClient = new JRadioButton("Client");
		rdbtnClient.setSelected(true);
		rdbtnClient.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				txtIp.setEnabled(true);
			}
		});
		panel.add(rdbtnClient);

		ButtonGroup bg = new ButtonGroup();

		bg.add(rdbtnClient);
		bg.add(rdbtnServer);

		Panel panel_1 = new Panel();
		contentPane.add(panel_1, BorderLayout.CENTER);

		JLabel lblIp = new JLabel("IP:");

		txtIp = new JTextField();
		txtIp.setColumns(10);

		JLabel lblPorta = new JLabel("Porta:");

		txtPorta = new JTextField();
		txtPorta.setText("9999");
		txtPorta.setColumns(10);

		JButton btnOk = new JButton("Ok");
		btnOk.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Configuracao c = new Configuracao();
				try {
					c.setIp(InetAddress.getByName(txtIp.getText()));
					c.setPorta(Integer.parseInt(txtPorta.getText()));
				} catch (UnknownHostException ev) {
					JOptionPane.showMessageDialog(null, "Falha ao iniciar o servidor");
					ev.printStackTrace();
					return;
				}
				if (rdbtnServer.isSelected()) {
					TCPServer server = new TCPServer(c);
					Thread serv = new Thread(server);
					serv.start();
				} else if (rdbtnClient.isSelected()) {
					TCPClient client = new TCPClient(c);
					Thread clie = new Thread(client);
					clie.start();
					
				}
				Janela t = new Janela(1200, 650, "Game Unifil - Grupo UniGame", new Jogo());
				
				
				dispose();
			}
		});

		JButton btnCancelar = new JButton("Cancelar");
		btnCancelar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		GroupLayout gl_panel_1 = new GroupLayout(panel_1);
		gl_panel_1.setHorizontalGroup(gl_panel_1.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_panel_1.createSequentialGroup().addContainerGap()
						.addGroup(gl_panel_1.createParallelGroup(Alignment.TRAILING)
								.addGroup(gl_panel_1.createSequentialGroup().addComponent(lblIp)
										.addPreferredGap(ComponentPlacement.RELATED)
										.addComponent(txtIp, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
												GroupLayout.PREFERRED_SIZE)
								.addPreferredGap(ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
								.addComponent(lblPorta).addPreferredGap(ComponentPlacement.RELATED)
								.addComponent(txtPorta, GroupLayout.PREFERRED_SIZE, 76, GroupLayout.PREFERRED_SIZE))
						.addGroup(gl_panel_1.createSequentialGroup().addComponent(btnCancelar).addGap(18)
								.addComponent(btnOk)))
						.addGap(33)));
		gl_panel_1.setVerticalGroup(gl_panel_1.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_1.createSequentialGroup().addGap(23)
						.addGroup(gl_panel_1.createParallelGroup(Alignment.BASELINE).addComponent(lblPorta)
								.addComponent(txtPorta, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
										GroupLayout.PREFERRED_SIZE)
						.addComponent(txtIp, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
								GroupLayout.PREFERRED_SIZE).addComponent(lblIp))
						.addGap(18).addGroup(gl_panel_1.createParallelGroup(Alignment.BASELINE).addComponent(btnOk)
								.addComponent(btnCancelar))
						.addContainerGap(40, Short.MAX_VALUE)));
		panel_1.setLayout(gl_panel_1);
	}
}
