package com.github.matschieu.desks.view.ihm;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.KeyStroke;
import javax.swing.ListSelectionModel;
import javax.swing.UIManager;
import javax.swing.undo.UndoManager;

import com.github.matschieu.desks.controller.PersonManagementController;
import com.github.matschieu.desks.model.DataBase;
import com.github.matschieu.desks.model.util.Person;
import com.github.matschieu.desks.model.util.Reservation;
import com.github.matschieu.desks.view.copypaste.CopyPaste;
import com.github.matschieu.desks.view.tablemodel.PersonsTableModel;
import com.github.matschieu.desks.view.undo.AddUndoable;
import com.github.matschieu.desks.view.undo.DellUndoable;
import com.github.matschieu.desks.view.undo.InnerObservable;
import com.github.matschieu.desks.view.undo.UndoObservable;
import com.github.matschieu.desks.view.undo.UpdateUndoable;

/**
 * @author Matschieu
 */
public class PersonManagementPanel extends JPanel implements Observer, UndoObservable {

	private static final String HELP_MSG = "<html>Après avoir sélectionné une ligne dans la table, " +
											"vous pouvez utiliser les touches suivantes :<br>" +
											"&nbsp;&nbsp;* '+' pour ajouter une personne<br>" +
											"&nbsp;&nbsp;* 'suppr' pour supprimer une personne<br>" +
											"&nbsp;&nbsp;* 'entrée' pour modifier une personne<br>" +
											"&nbsp;&nbsp;* 'c' pour copier les données d'une personne<br>" +
											"&nbsp;&nbsp;* 'p' pour coller les données d'une personne<br>" +
											"</html>";
	
	private DataBase db;
	private PersonManagementController ctr;
	
	private JTable personsTable;
	private JButton addPersonButton;
	private JButton delPersonButton;
	private JButton updatePersonButton;
	private JButton listReservationsButton;
	private JButton helpButton;
	private JScrollPane scrollPane;	

	private UndoManager undoManager;
	private InnerObservable observable;
	private CopyPaste<Person> cp;

	/**
	 * Creates a new person management panel
	 * @param db the model
	 * @param ctr the controller
	 */
	public PersonManagementPanel(DataBase db, PersonManagementController ctr, UndoManager undoManager) {
		this.db = db;
		this.db.addObserver(this);
		this.ctr = ctr;
		this.undoManager = undoManager;
		this.observable = new InnerObservable();
		this.cp = new CopyPaste<Person>();
		this.initGUI();
		this.initActionListener();
	}
	
	/**
	 * Initializes the GUI
	 */
	private void initGUI() {
		this.addPersonButton = new JButton("Nouvelle personne");
		this.delPersonButton = new JButton("Supprimer");
		this.updatePersonButton = new JButton("Modifier");
		this.listReservationsButton = new JButton("Réservations");
		this.helpButton = new JButton("Aide");
		
		this.personsTable = new JTable(new PersonsTableModel(db.getAllPerson()));
		this.personsTable.getTableHeader().setReorderingAllowed(false);
		this.personsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		this.personsTable.setAutoscrolls(true);
		
		if (this.personsTable.getRowCount() > 0)
			this.personsTable.setRowSelectionInterval(0, 0);
		
		if (this.personsTable.getModel().getRowCount() == 0) {
			this.delPersonButton.setEnabled(false);
			this.updatePersonButton.setEnabled(false);
			this.listReservationsButton.setEnabled(false);
		}
		
		JPanel p = new JPanel();
		p.add(this.addPersonButton);
		p.add(this.updatePersonButton);
		p.add(this.delPersonButton);
		p.add(this.listReservationsButton);
		p.add(this.helpButton);
		
		this.scrollPane = new JScrollPane(this.personsTable);
		this.scrollPane.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLoweredBevelBorder(), "Liste du personnel connu"));
		this.scrollPane.setBackground(UIManager.getColor("Panel.background"));
		
		this.setLayout(new BorderLayout(10, 10));
		this.add(new WindowTitle("Gestion du personnel"), BorderLayout.NORTH);
		this.add(this.scrollPane, BorderLayout.CENTER);
		this.add(p, BorderLayout.SOUTH);
		this.add(new JPanel(), BorderLayout.EAST);
		this.add(new JPanel(), BorderLayout.WEST);
	}
	
	/**
	 * Initializes all action listener
	 */
	public void initActionListener() {
		this.addPersonButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				addPerson();
			}
		});
		this.delPersonButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				delPerson();
			}
		});
		this.updatePersonButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				updatePerson();
			}
		});
		this.listReservationsButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				displayReservations();
			}
		});
		this.helpButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				help();
			}
		});
		
		this.personsTable.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2) {
					updatePerson();
				}
			}
		});
		
	//	System.out.println(this.personsTable.getActionMap().allKeys());
	//	for(int i = 0; i < this.personsTable.getActionMap().allKeys().length; i++)
	//		System.out.println(this.personsTable.getActionMap().allKeys()[i]);

		this.personsTable.getInputMap().remove(KeyStroke.getKeyStroke(KeyEvent.VK_C, InputEvent.CTRL_MASK));
		this.personsTable.getInputMap().remove(KeyStroke.getKeyStroke(KeyEvent.VK_V, InputEvent.CTRL_MASK));

		this.personsTable.getActionMap().remove(KeyStroke.getKeyStroke(KeyEvent.VK_C, InputEvent.CTRL_MASK));
		this.personsTable.getActionMap().remove(KeyStroke.getKeyStroke(KeyEvent.VK_V, InputEvent.CTRL_MASK));

//		this.personsTable.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_V, InputEvent.CTRL_MASK), "paste");
//		this.personsTable.getActionMap().put("paste", TransferHandler.getPasteAction());
		
		this.personsTable.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				
				switch(e.getKeyCode()) {
					case KeyEvent.VK_PLUS :
						addPerson();
						break;					
					case KeyEvent.VK_ENTER :
						updatePerson();
						break;					
					case KeyEvent.VK_DELETE :
						delPerson();
						break;
					case KeyEvent.VK_C :
						copyPerson();
						break;
					case KeyEvent.VK_P :
						pastePerson();
						break;
				}
			}
		});
	}
	
	private void addPerson() {
		Person p = (new PersonForm()).showDialog();
		this.ctr.addPerson(p);
		
		this.undoManager.addEdit(new AddUndoable<Person>(this.ctr, p));		
		this.observable.setChanged();
		this.observable.notifyObservers();
	}
	
	private void delPerson() {
		if (showErrorMessage())
			return;
		
		int idx = this.personsTable.getSelectedRow();
		Person p = ((PersonsTableModel)this.personsTable.getModel()).getDataAtRow(idx);
		this.ctr.removePerson(p);

		this.undoManager.addEdit(new DellUndoable<Person>(this.ctr, p));
		this.observable.setChanged();
		this.observable.notifyObservers();
		
		idx -= 1;
		if (idx > - 1)
			this.personsTable.setRowSelectionInterval(idx, idx);
	}
	
	private void updatePerson() {
		if (showErrorMessage())
			return;
		
		Person p = (Person)((PersonsTableModel)this.personsTable.getModel()).getDataAtRow(this.personsTable.getSelectedRow());
		Person p_ = p.duplicate();
		p = (new PersonForm(p)).showDialog();
		this.ctr.updatePerson(p);
		
		this.undoManager.addEdit(new UpdateUndoable<Person>(this.ctr, p_, p));
		this.observable.setChanged();
		this.observable.notifyObservers();
	}
	
	private void copyPerson() {
		Person p = ((PersonsTableModel)this.personsTable.getModel()).getDataAtRow(this.personsTable.getSelectedRow());
		this.cp.setClipboardContents(p);
	}
	
	private void pastePerson() {
		try {
			Person p = (Person)this.cp.getClipboardContents();
			if (p == null)
				return;
			
			p.setName(p.getName() + " (2)");
			Person p_ = p.duplicate();
			p_.setId(-1);
			this.ctr.add(p_);
			
			this.undoManager.addEdit(new AddUndoable<Person>(this.ctr, p_));		
			this.observable.setChanged();
			this.observable.notifyObservers();		
		}
		catch(ClassCastException e) { }
	}
	
	private void displayReservations() {
		Person p = ((PersonsTableModel)this.personsTable.getModel()).getDataAtRow(this.personsTable.getSelectedRow());
		List<Reservation> list = this.db.getReservations(p);
		if (list.size() == 0) {
			JOptionPane.showMessageDialog(null, "Aucune réservation pour " + p.getFirstName() + " " + p.getName(), "Erreur", JOptionPane.WARNING_MESSAGE);
			return;
		}
		new ReservationsView(list, "Réservation de " + p.getFirstName() + " " + p.getName());
	}
	
	private void help() {
		JOptionPane.showMessageDialog(null, HELP_MSG, "Aide", JOptionPane.INFORMATION_MESSAGE);		
	}
	
	/**
	 * Displays an error message if the selected value is invalid
	 * @return true if an error occurred
	 */
	private boolean showErrorMessage() {
		int idx = this.personsTable.getSelectedRow();
		int listSize = this.personsTable.getModel().getRowCount();
		if (listSize <= 0) {
			JOptionPane.showMessageDialog(null, "Aucune personne n'existe", "Erreur", JOptionPane.ERROR_MESSAGE);
			return true;
		}
		if (idx < 0 || idx >= listSize) {
			JOptionPane.showMessageDialog(null, "Veuillez sélectionner une personne", "Erreur", JOptionPane.ERROR_MESSAGE);
			return true;
		}
		return false;
	}
	
	/**
	 * Update the GUI when an observable notify this
	 * @param o
	 * @param arg
	 */
	public void update(Observable o, Object arg) {
		this.personsTable.setModel(new PersonsTableModel(db.getAllPerson()));
		if (this.personsTable.getModel().getRowCount() == 0) {
			this.delPersonButton.setEnabled(false);
			this.updatePersonButton.setEnabled(false);
			this.listReservationsButton.setEnabled(false);
		}
		else {
			this.delPersonButton.setEnabled(true);
			this.updatePersonButton.setEnabled(true);
			this.listReservationsButton.setEnabled(true);
		}
		if (this.personsTable.getRowCount() > 0)
			this.personsTable.setRowSelectionInterval(0, 0);
		this.repaint();
	}

	public Observable getObservable() {
		return this.observable;
	}

}
