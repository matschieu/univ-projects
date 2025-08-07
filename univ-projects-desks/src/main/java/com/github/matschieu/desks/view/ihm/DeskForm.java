package com.github.matschieu.desks.view.ihm;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import com.github.matschieu.desks.model.util.Desk;

/**
 * @author Matschieu
 */
public class DeskForm extends JDialog {
	
	private boolean edit;
	private Desk desk;
	
	private JTextField numberTextField;
	private JTextField buildingTextField;
	private JTextField roomTextField;
	private JTextArea descriptionTextArea;

	private JButton okButton;
	private JButton cancelButton;
	
	public DeskForm(Desk desk) {
		this.desk = desk;
		this.edit = true;
		this.initGUI();
	}
	
	public DeskForm() {
		this.desk = new Desk();
		this.edit = false;
		this.initGUI();
	}
	
	private void initGUI() {
		JPanel formPanel = new JPanel();
		JPanel buttonsPanel = new JPanel();
		
		this.numberTextField = new JTextField(10);
		this.buildingTextField = new JTextField(10);
		this.roomTextField = new JTextField(10);
		this.descriptionTextArea = new JTextArea(3, 10);
		
		this.numberTextField.setText(this.desk.getNumber());
		this.buildingTextField.setText(this.desk.getBuilding());
		this.roomTextField.setText(this.desk.getRoom());
		this.descriptionTextArea.setText(this.desk.getDescription());
		
		this.descriptionTextArea.setLineWrap(true);
		this.descriptionTextArea.setWrapStyleWord(true);
		
		JPanel p1 = new JPanel();
		p1.setLayout(new GridLayout(3, 2, 0, 5));
		
		p1.add(new JLabel("Numéro du bureau : "));
		p1.add(this.numberTextField);

		p1.add(new JLabel("Bâtiment :"));
		p1.add(this.buildingTextField);

		p1.add(new JLabel("Salle :"));
		p1.add(this.roomTextField);
		

		JPanel p2 = new JPanel();
		p2.setLayout(new BorderLayout());
		p2.add(new JLabel("Description : "), BorderLayout.NORTH);
		p2.add(new JScrollPane(this.descriptionTextArea), BorderLayout.SOUTH);
		
		formPanel.setLayout(new BorderLayout(0, 5));
		formPanel.add(p1, BorderLayout.NORTH);
		formPanel.add(p2, BorderLayout.SOUTH);

		this.okButton = new JButton("Enregistrer");
		this.cancelButton = new JButton("Annuler");
		buttonsPanel.add(cancelButton);
		buttonsPanel.add(okButton);
	
		this.setLayout(new BorderLayout(5, 5));		
		this.add(new WindowTitle(this.edit ? "Modification d'un bureau" : "Création de bureau"), BorderLayout.NORTH);
		this.add(formPanel, BorderLayout.CENTER);
		this.add(buttonsPanel, BorderLayout.SOUTH);
		this.add(new JPanel(), BorderLayout.EAST);
		this.add(new JPanel(), BorderLayout.WEST);
		
		this.initListener();
		
		this.setModal(true);
		this.pack();
		this.setLocationRelativeTo(null);
		this.setResizable(false);
	}
	
	private void initListener() {
		this.cancelButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cancel();
			}
		});
		this.okButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				validation();
			}
		});
		
		this.numberTextField.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER)
					validation();
				if (e.getKeyCode() == KeyEvent.VK_ESCAPE)
					cancel();
			}
		});
		this.buildingTextField.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER)
					validation();
				if (e.getKeyCode() == KeyEvent.VK_ESCAPE)
					cancel();
			}
		});
		this.roomTextField.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER)
					validation();
				if (e.getKeyCode() == KeyEvent.VK_ESCAPE)
					cancel();
			}
		});
		this.descriptionTextArea.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ESCAPE)
					cancel();
			}
		});
		this.okButton.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER)
					validation();
				if (e.getKeyCode() == KeyEvent.VK_ESCAPE)
					cancel();
			}
		});
		this.cancelButton.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER || e.getKeyCode() == KeyEvent.VK_ESCAPE)
					cancel();
			}
		});

		this.numberTextField.addFocusListener(new FocusAdapter() {
			public void focusLost(FocusEvent e) {
				roomTextField.setText(numberTextField.getText());
			}
			public void focusGained(FocusEvent e) {
				numberTextField.selectAll();
			}
		});
		this.buildingTextField.addFocusListener(new FocusAdapter() {
			public void focusGained(FocusEvent e) {
				buildingTextField.selectAll();
			}
		});
		this.roomTextField.addFocusListener(new FocusAdapter() {
			public void focusGained(FocusEvent e) {
				roomTextField.selectAll();
			}
		});

		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				cancel();
			}	
		});		
	}
	
	public Desk showDialog() {
		this.setVisible(true);
		while(this.isVisible());
		return this.desk;
	}
	
	private void validation() {
		if (this.numberTextField.getText().length() == 0 || this.buildingTextField.getText().length() == 0 || this.roomTextField.getText().length() == 0) {
			JOptionPane.showMessageDialog(null, "L'un des champs est vide", "Erreur", JOptionPane.ERROR_MESSAGE);
			return;
		}
		this.desk.setNumber(this.numberTextField.getText());
		this.desk.setBuilding(this.buildingTextField.getText());
		this.desk.setRoom(this.roomTextField.getText());
		this.desk.setDescription(this.descriptionTextArea.getText());				
		this.dispose();
	}
	
	private void cancel() {
		this.desk = null;
		this.dispose();
	}
	 
}
