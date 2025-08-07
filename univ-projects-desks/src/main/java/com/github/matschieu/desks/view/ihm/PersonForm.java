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
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.github.matschieu.desks.model.util.Person;

/**
 * @author Matschieu
 */
public class PersonForm extends JDialog {
	
	private boolean edit;
	private Person person;
	
	private JTextField nameTextField;
	private JTextField firstNameTextField;
	private JTextField postTextField;
	private JCheckBox traineeCheckBox;

	private JButton okButton;
	private JButton cancelButton;
	
	public PersonForm(Person person) {
		this.person = person;
		this.edit = true;
		this.initGUI();
	}
	
	public PersonForm() {
		this.person = new Person();
		this.edit = false;
		this.initGUI();
	}
	
	private void initGUI() {
		JPanel formPanel = new JPanel();
		JPanel buttonsPanel = new JPanel();
		
		this.nameTextField = new JTextField(10);
		this.firstNameTextField = new JTextField(10);
		this.postTextField = new JTextField(10);
		this.traineeCheckBox = new JCheckBox("Stagiaire");
		
		this.nameTextField.setText(this.person.getName());
		this.firstNameTextField.setText(this.person.getFirstName());
		this.postTextField.setText(this.person.getPost());
		this.traineeCheckBox.setSelected(this.person.isTrainee());
		
		JPanel p1 = new JPanel();
		p1.setLayout(new GridLayout(3, 2, 0, 5));
		
		p1.add(new JLabel("Nom : "));
		p1.add(this.nameTextField);

		p1.add(new JLabel("Prénom :"));
		p1.add(this.firstNameTextField);

		p1.add(new JLabel("Poste :"));
		p1.add(this.postTextField);
		
		formPanel.setLayout(new BorderLayout(0, 5));
		formPanel.add(p1, BorderLayout.NORTH);
		formPanel.add(this.traineeCheckBox, BorderLayout.SOUTH);

		this.okButton = new JButton("Enregistrer");
		this.cancelButton = new JButton("Annuler");
		buttonsPanel.add(cancelButton);
		buttonsPanel.add(okButton);
	
		this.setLayout(new BorderLayout(5, 5));		
		this.add(new WindowTitle(this.edit ? "Modification d'une personne" : "Création de personne"), BorderLayout.NORTH);
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
		
		this.nameTextField.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER)
					validation();
				if (e.getKeyCode() == KeyEvent.VK_ESCAPE)
					cancel();
			}
		});
		this.firstNameTextField.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER)
					validation();
				if (e.getKeyCode() == KeyEvent.VK_ESCAPE)
					cancel();
			}
		});
		this.postTextField.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER)
					validation();
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

		this.nameTextField.addFocusListener(new FocusAdapter() {
			public void focusGained(FocusEvent e) {
				nameTextField.selectAll();
			}
		});
		this.firstNameTextField.addFocusListener(new FocusAdapter() {
			public void focusGained(FocusEvent e) {
				firstNameTextField.selectAll();
			}
		});
		this.postTextField.addFocusListener(new FocusAdapter() {
			public void focusGained(FocusEvent e) {
				postTextField.selectAll();
			}
		});

		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				cancel();
			}	
		});		
	}
	
	public Person showDialog() {
		this.setVisible(true);
		while(this.isVisible());
		return this.person;
	}
	
	private void validation() {
		if (this.nameTextField.getText().length() == 0 || this.firstNameTextField.getText().length() == 0) {
			JOptionPane.showMessageDialog(null, "L'un des champs est vide", "Erreur", JOptionPane.ERROR_MESSAGE);
			return;
		}
		this.person.setName(this.nameTextField.getText());
		this.person.setFirstName(this.firstNameTextField.getText());
		this.person.setPost(this.postTextField.getText());
		this.person.setTrainee(this.traineeCheckBox.isSelected());
		this.dispose();
	}
	
	private void cancel() {
		this.person = null;
		this.dispose();
	}
	 
}
