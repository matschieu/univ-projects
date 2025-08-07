package com.github.matschieu.color.selector.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JToolBar;
import javax.swing.UIManager;

import com.github.matschieu.color.selector.model.ColorAutoModel;
import com.github.matschieu.color.selector.model.ColorCustomModel;
import com.github.matschieu.color.selector.model.ColorList;
import com.github.matschieu.color.selector.model.MColor;

/**
 * @author Matschieu
 */
public class GoodColorSelectorFrame extends JFrame {

	private ColorAutoModel autoModel;		// Modèle pour les couleurs générées automatiquement
	private ColorCustomModel customModel;	// Modèle pour les couleurs générées à partir de couleurs
	private JPanel middlePane;				// Panel du milieu de la fenêtre
	private JLabel stateBar;				// Barre d'état
	
	public GoodColorSelectorFrame(ColorAutoModel autoModel, ColorCustomModel colorModel) {
		this.autoModel = autoModel;
		this.customModel = colorModel;
		// Initialisation de la fenêtre
		try { UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel"); }
		catch (Exception e) { }
		this.setTitle("GoodColorGenerator");
		// Ajout de la barre de menus
		this.setJMenuBar(this.initMenuBar());
		// Ajout de la barre d'icones
		this.add(this.initToolBar(), BorderLayout.NORTH);
		// Le panel qui contiendra les panels de proposition de couleurs
		this.middlePane = new JPanel();
		this.add(new JScrollPane(this.middlePane), BorderLayout.CENTER);
		// On ajoute la barre d'état
		this.stateBar = new JLabel(" ");
		this.add(stateBar, BorderLayout.SOUTH);
		// Définit une taille par défaut qui est celle d'un panel de proposition de couleur
		this.setMinimumSize(new Dimension(670, 120));
		this.setMaximumSize(new Dimension(670, 800));
		this.pack();
		// Positionne la fenêtre
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setResizable(false);
		this.setVisible(true);
	}
	
	/* INITIALISATION DES COMPOSANTS */

	private JMenuBar initMenuBar() {
		// Définit les objets du menu fichier
		JMenuItem newAutoMenu = new JMenuItem("Couleurs auto");
		newAutoMenu.addActionListener(new NewAutoActionListener());
		JMenuItem newMenu = new JMenuItem("Couleurs personnalisées");
		newMenu.addActionListener(new NewActionListener());
		JMenuItem exitMenu = new JMenuItem("Quitter");
		exitMenu.addActionListener(new ExitActionListener());
		// Ajoute les objets du menu fichier
		JMenu file = new JMenu("Fichier");
		file.add(newAutoMenu);
		file.add(newMenu);
		file.add(exitMenu);
		// Définit les objets du menu Aide
		JMenuItem helpMenu = new JMenuItem("Aide");
		helpMenu.addActionListener(new HelpActionListener());
		JMenuItem aboutMenu = new JMenuItem("A propos");
		aboutMenu.addActionListener(new AboutActionListener());
		// Ajoute les objets du menu fichier
		JMenu help = new JMenu("?");
		help.add(helpMenu);
		help.add(aboutMenu);
		// Ajoute les menus à la barre
		JMenuBar menuBar = new JMenuBar();
		menuBar.add(file);
		menuBar.add(help);
		return menuBar;
	}

	private JToolBar initToolBar() {
		// On crée les icônes de la barre d'outils
		ImageIcon newAutoIco = new ImageIcon("./icons/new_auto.png");
		ImageIcon newIco = new ImageIcon("./icons/new.png");
		ImageIcon helpIco = new ImageIcon("./icons/help.png");
		ImageIcon aboutIco = new ImageIcon("./icons/about.png");
		ImageIcon exitIco = new ImageIcon("./icons/exit.png");
		if (getClass().getResource("/icons/new_auto.png") != null)
			newAutoIco = new ImageIcon(getClass().getResource("/icons/new_auto.png"));
		if (getClass().getResource("/icons/new.png") != null)
			newIco = new ImageIcon(getClass().getResource("/icons/new.png"));
		if (getClass().getResource("/icons/help.png") != null)
			helpIco = new ImageIcon(getClass().getResource("/icons/help.png"));		
		if (getClass().getResource("/icons/about.png") != null)
			aboutIco = new ImageIcon(getClass().getResource("/icons/about.png"));
		if (getClass().getResource("/icons/exit.png") != null)
			exitIco = new ImageIcon(getClass().getResource("/icons/exit.png"));		
		// Crée les boutons
		JButton newAutoButton = new JButton(newAutoIco);
		newAutoButton.setToolTipText("Couleurs auto");
		newAutoButton.addActionListener(new NewAutoActionListener());
		JButton newButton = new JButton(newIco);
		newButton.setToolTipText("Couleurs personnalisées");
		newButton.addActionListener(new NewActionListener());
		JButton helpButton = new JButton(helpIco);
		helpButton.setToolTipText("Aide");
		helpButton.addActionListener(new HelpActionListener());
		JButton aboutButton = new JButton(aboutIco);
		aboutButton.setToolTipText("A propos");
		aboutButton.addActionListener(new AboutActionListener());
		JButton exitButton = new JButton(exitIco);
		exitButton.setToolTipText("Quitter");
		exitButton.addActionListener(new ExitActionListener());
		// Ajoute les boutons à la barre
		JToolBar toolBar = new JToolBar();
		toolBar.setFloatable(false);
		toolBar.add(newAutoButton);
		toolBar.add(newButton);
		toolBar.addSeparator();
		toolBar.add(helpButton);
		toolBar.add(aboutButton);
		toolBar.addSeparator();
		toolBar.add(exitButton);
		return toolBar;
	}
	
	private void initAutoGenPanel() {
		int i = 0;
		List<ColorList> list = this.autoModel.getColorsList();
		this.middlePane.removeAll();
		this.middlePane.setLayout(new GridLayout(list.size(), 1));
		for(ColorList cl : list) {
			JPanel p = new ColorAutoGeneratedPanel(cl);
			if (i++ % 2 == 0)
				p.setBackground(new MColor(225, 225, 225));
			this.middlePane.add(p);
		}
		this.repaint();
	}
	
	private void initColorChooserPanel() {
		int i = 0;
		List<ColorList> list = this.customModel.getColorsList();
		this.middlePane.removeAll();
		this.middlePane.setLayout(new GridLayout(list.size(), 1));
		for(ColorList cl : list) {
			JPanel p = new ColorCustomGeneratedPanel(this.customModel.getInitialColors().get(i), cl);
			if (i++ % 2 == 0)
				p.setBackground(new MColor(225, 225, 225));
			this.middlePane.add(p);
		}
		this.repaint();
	}
	
	/* TRAITEMENTS */
	
	public void repaint() {
		super.repaint();
		this.pack();
		this.setLocationRelativeTo(null);
	}
	
	public void newColorAutoGeneration() {
		this.stateBar.setText(" ");
		int nbColor = askNbColor();
		if (nbColor < 1)
			return;
		long start = System.currentTimeMillis();
		this.autoModel.setNbColor(nbColor);
		this.autoModel.generate();
		long execTime = (System.currentTimeMillis() - start) / 1000;
		this.stateBar.setText(nbColor + " couleur" + (nbColor > 1 ? "s" : "") + " générée" + (nbColor > 1 ? "s" : "") + " en environs " + execTime + " secondes");
		this.initAutoGenPanel();
	}

	public void newCustomColorGeneration() {
		this.stateBar.setText(" ");
		JButton nextButton = new JButton("Suivant");
		nextButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int nbColor = customModel.getInitialColors().size();
				if (nbColor < 1) {
					String msg = "Choisissez au moins une couleur";
					JOptionPane.showMessageDialog(null, msg, "Erreur", JOptionPane.ERROR_MESSAGE);
					return;
				}
				long start = System.currentTimeMillis();
				customModel.generate();
				long execTime = (System.currentTimeMillis() - start) / 1000;
				stateBar.setText(nbColor + " couleur" + (nbColor > 1 ? "s" : "") + " générée" + (nbColor > 1 ? "s" : "") + " en environs " + execTime + " secondes");
				initColorChooserPanel();
			}
		});
		JPanel p = new JPanel();
		p.setLayout(new FlowLayout(FlowLayout.RIGHT));
		p.add(nextButton);
		this.middlePane.removeAll();
		this.middlePane.setLayout(new BorderLayout());
		this.middlePane.add(new ColorChooserPanel(this.customModel), BorderLayout.CENTER);
		this.middlePane.add(p, BorderLayout.SOUTH);
		this.repaint();
	}
	
	private int askNbColor() {
		String msg = "Combien de couleur(s)\nsouhaitez vous générer ?";
		String tmp = JOptionPane.showInputDialog(null, msg, null, JOptionPane.QUESTION_MESSAGE);
		if (tmp == null)
			return -1;
		try {
			int n = Integer.parseInt(tmp);
			if (n < 0) {
				msg = "Nombre de couleurs invalide\nVeuillez choisir un nombre de couleurs compris entre 0 et " + ColorCustomModel.MAX_INIT_COLOR;
				JOptionPane.showMessageDialog(null, msg, "Erreur", JOptionPane.ERROR_MESSAGE);
				return -1;
			}
			if (n > ColorCustomModel.MAX_INIT_COLOR) {
				msg = "Nombre de couleurs trop élevé\nVeuillez choisir un nombre de couleurs <= " + ColorCustomModel.MAX_INIT_COLOR;
				JOptionPane.showMessageDialog(null, msg, "Erreur", JOptionPane.ERROR_MESSAGE);
				return -1;
			}
			return n;
		}
		catch(NumberFormatException e) {
			msg = "Nombre de couleurs incorrect\nSaisissez un entier positif";
			JOptionPane.showMessageDialog(null, msg, "Erreur", JOptionPane.ERROR_MESSAGE);
			return -1;
		}
	}
	
	/* LISTENERS */
	
	class NewAutoActionListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			newColorAutoGeneration();
		}		
	}
	
	class NewActionListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			newCustomColorGeneration();
		}		
	}
	
	class HelpActionListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			String msg = "* \"Couleurs auto\" génère un nombre défini de couleurs ayant des niveaux de gris différents\n";
			msg += "* \"Couleurs personnalisées\" génère des couleurs ayant des niveaux de gris différents à partir de couleurs précédemment séléctionnées";
			JOptionPane.showMessageDialog(null, msg, "Aide", JOptionPane.PLAIN_MESSAGE);
		}
	}
	
	class AboutActionListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			String msg = "Matschieu\nTP d'IHM\nM1S2 informatique Lille1";
			JOptionPane.showMessageDialog(null, msg, "A propos", JOptionPane.PLAIN_MESSAGE);
		}
	}
	
	class ExitActionListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			System.exit(0);
		}
	}
	
}
