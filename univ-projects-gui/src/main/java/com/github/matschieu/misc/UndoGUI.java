package com.github.matschieu.misc;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.undo.AbstractUndoableEdit;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import javax.swing.undo.UndoManager;

public class UndoGUI {

	private DefaultListModel listModel;
	private JTextField text;
	private JList list;
	private JButton boutonAnnuler;
	private JButton boutonRefaire;
	private JButton boutonSupprimer;
	private JMenuItem menuItemAnnuler;
	private JMenuItem menuItemRefaire;
	private JMenuItem menuItemSupprimer;
	private UndoManager undoManager;

	UndoGUI() {
		undoManager = new UndoManager();
		// JFrame
		JFrame fen = new JFrame("Undo/Redo");
		fen.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// Liste
		listModel = new DefaultListModel();
		listModel.addElement("Universite Lille 1");
		listModel.addElement("Master informatique");
		listModel.addElement("IHM");
		list = new JList(listModel);
		JScrollPane listScroller = new JScrollPane(list);
		text = new JTextField();
		text.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent arg0) {
				if (arg0.getKeyCode() == KeyEvent.VK_ENTER)
					ajouter();
			}
		});
		JButton boutonAjouter = new JButton("Ajouter");
		boutonAjouter.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				ajouter();
			}
		});

		// Menu
		JMenuBar menuBar = new JMenuBar();
		JMenu menu = new JMenu("Edition");
		menuBar.add(menu);
		menuItemSupprimer = new JMenuItem("Supprimer");
		menuItemSupprimer.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				supprimer();
			}
		});
		menu.add(menuItemSupprimer);
		menuItemAnnuler = new JMenuItem("Annuler");
		menuItemAnnuler.setEnabled(false);
		menuItemAnnuler.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				undo();
			}
		});
		menu.add(menuItemAnnuler);
		menuItemRefaire = new JMenuItem("Refaire");
		//menuItemRefaire.setEnabled(false);
		menuItemRefaire.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				redo();
			}
		});
		menu.add(menuItemRefaire);

		// ToolBar
		JToolBar toolBar = new JToolBar("Barre d'outils");
		boutonSupprimer = new JButton("Supprimer");
		boutonSupprimer.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
			    supprimer();
			}
		});

		boutonAnnuler = new JButton("Annuler");
		boutonAnnuler.setEnabled(false);
		boutonAnnuler.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
			    undo();
			}
		});
		boutonRefaire = new JButton("Refaire");
		boutonRefaire.setEnabled(false);
		boutonRefaire.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
			    redo();
			}
		});
		toolBar.add(boutonSupprimer);
		toolBar.add(boutonAnnuler);
		toolBar.add(boutonRefaire);

		fen.setJMenuBar(menuBar);
		fen.add(toolBar);
		fen.getContentPane().setLayout(new BoxLayout(fen.getContentPane(),BoxLayout.Y_AXIS));
		fen.getContentPane().add(listScroller);
		fen.getContentPane().add(text);
		fen.getContentPane().add(boutonAjouter);
		fen.pack();
		fen.setVisible(true);
	}

	private void undo() {
		undoManager.undo();
	    updateUndoRedoButtons();
	}

	private void redo() {
		undoManager.redo();
	    updateUndoRedoButtons();
	}

	private void supprimer() {
	    int index = list.getSelectedIndex();
	    if (index != -1) {
		    undoManager.addEdit(new DellUndoableListe(listModel, index, (String)listModel.get(index)));
	    	listModel.remove(index);
		    updateUndoRedoButtons();
	    }
	}

	private void ajouter() {
		if (text.getText().length() > 0) {
			listModel.addElement(text.getText());
			undoManager.addEdit(new AddUndoableListe(listModel, listModel.getSize() - 1, text.getText()));
			text.setText("");
			updateUndoRedoButtons();
		}
	}

	private void updateUndoRedoButtons() {
		if (undoManager.canUndo()) {
			boutonAnnuler.setEnabled(true);
			menuItemAnnuler.setEnabled(true);
		}
		else {
			boutonAnnuler.setEnabled(false);
			menuItemAnnuler.setEnabled(false);
		}
		if (undoManager.canRedo()) {
			boutonRefaire.setEnabled(true);
			menuItemRefaire.setEnabled(true);
		}
		else {
			boutonRefaire.setEnabled(false);
			menuItemRefaire.setEnabled(false);
		}
		System.out.println(undoManager.getUndoPresentationName());
	}

	public static void main(String[] args) {
	    //Schedule a job for the event-dispatching thread:
	    //creating and showing this application's GUI.
	    javax.swing.SwingUtilities.invokeLater(new Runnable() {
		    @Override
			public void run() {
			new UndoGUI();
		    }
		});
	}

}

/**
 * @author Matschieu
 */
class AddUndoableListe extends AbstractUndoableEdit {

	private DefaultListModel model;
	private String text;
	private int idx;

	public AddUndoableListe(DefaultListModel model, int idx, String text) {
		this.model = model;
		this.idx = idx;
		this.text = text;
	}

	@Override
	public void redo() throws CannotRedoException {
		super.redo();
		model.add(idx, text);
	}

	@Override
	public void undo() throws CannotUndoException {
		super.undo();
		model.remove(idx);
	}

	@Override
	public String getPresentationName() {
		return "Ajouter " + text;
	}

}

/**
 * @author Matschieu
 */
class DellUndoableListe extends AbstractUndoableEdit {

	private DefaultListModel model;
	private String text;
	private int idx;

	public DellUndoableListe(DefaultListModel model, int idx, String text) {
		this.model = model;
		this.idx = idx;
		this.text = text;
	}

	@Override
	public void redo() throws CannotRedoException {
		super.redo();
		model.remove(idx);
	}

	@Override
	public void undo() throws CannotUndoException {
		super.undo();
		model.add(idx, text);
	}

	@Override
	public String getPresentationName() {
		return "Supprimer " + text;
	}

}
