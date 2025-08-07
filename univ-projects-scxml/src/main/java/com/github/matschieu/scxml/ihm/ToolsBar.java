package com.github.matschieu.scxml.ihm;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Observable;
import java.util.Observer;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JToolBar;

import com.github.matschieu.scxml.Robot;
import com.github.matschieu.scxml.RobotStateMachine;

/**
 *
 * @author mathieu
 */
public class ToolsBar extends JToolBar implements Observer {

	private Robot stateMachine;

	private JButton moveEastButton;
	private JButton moveWestButton;
	private JButton moveNorthButton;
	private JButton moveSouthButton;
	private JButton turnOnButton;
	private JButton turnOffButton;
	private JButton reloadButton;
	private JButton helpButton;
	private JButton infoButton;
	private JButton exitButton;

	/**
	 * Constructor
	 * @param stateMachine
	 * @param frame
	 */
	public ToolsBar(Robot stateMachine, final JFrame frame) {
		super();

		this.stateMachine = stateMachine;

		this.moveEastButton = new JButton(new ImageIcon(this.getClass().getResource("/img/move_east.png")));
		this.moveWestButton = new JButton(new ImageIcon(this.getClass().getResource("/img/move_west.png")));
		this.moveNorthButton = new JButton(new ImageIcon(this.getClass().getResource("/img/move_north.png")));
		this.moveSouthButton = new JButton(new ImageIcon(this.getClass().getResource("/img/move_south.png")));

		this.turnOnButton = new JButton(new ImageIcon(this.getClass().getResource("/img/turn_on.png")));
		this.turnOffButton = new JButton(new ImageIcon(this.getClass().getResource("/img/turn_off.png")));
		this.reloadButton = new JButton(new ImageIcon(this.getClass().getResource("/img/reload.png")));

		this.helpButton = new JButton(new ImageIcon(this.getClass().getResource("/img/help.png")));
		this.infoButton = new JButton(new ImageIcon(this.getClass().getResource("/img/info.png")));
		this.exitButton = new JButton(new ImageIcon(this.getClass().getResource("/img/exit.png")));

		this.moveEastButton.setToolTipText("Déplacer à l'est");
		this.moveWestButton.setToolTipText("Déplacer à l'ouest");
		this.moveNorthButton.setToolTipText("Déplacer au nord");
		this.moveSouthButton.setToolTipText("Déplacer au sud");

		this.turnOnButton.setToolTipText("Allumer");
		this.turnOffButton.setToolTipText("Eteindre");
		this.reloadButton.setToolTipText("Recharger batterie");

		this.helpButton.setToolTipText("Aide");
		this.infoButton.setToolTipText("A propos");
		this.exitButton.setToolTipText("Quitter");

		this.add(this.moveWestButton);
		this.add(this.moveNorthButton);
		this.add(this.moveEastButton);
		this.add(this.moveSouthButton);
		this.addSeparator();
		this.add(this.turnOnButton);
		this.add(this.turnOffButton);
		this.addSeparator();
		this.add(this.reloadButton);
		this.addSeparator();
		this.add(this.helpButton);
		this.add(this.infoButton);
		this.add(this.exitButton);

		final Robot tmpStateMachine = this.stateMachine;
		this.moveEastButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				tmpStateMachine.fireEvent(RobotStateMachine.EVENT_MOVE_EAST);
			}
		});
		this.moveWestButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				tmpStateMachine.fireEvent(RobotStateMachine.EVENT_MOVE_WEST);
			}
		});
		this.moveNorthButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				tmpStateMachine.fireEvent(RobotStateMachine.EVENT_MOVE_NORTH);
			}
		});
		this.moveSouthButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				tmpStateMachine.fireEvent(RobotStateMachine.EVENT_MOVE_SOUTH);
			}
		});

		this.turnOnButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				tmpStateMachine.fireEvent(RobotStateMachine.EVENT_TURN_ON);
			}
		});
		this.turnOffButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				tmpStateMachine.fireEvent(RobotStateMachine.EVENT_TURN_OFF);
			}
		});
		this.reloadButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				tmpStateMachine.fireEvent(RobotStateMachine.EVENT_RELOAD);
			}
		});

		this.helpButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				StringBuilder str = new StringBuilder();
				str.append("La fenêtre montre le robot (point vert) dans un\n");
				str.append("environnement torique (le cadre noir).\n\n");
				str.append("La jauge de batterie et les coordonnées actuelles\n");
				str.append("du robot sont visible dans le panneau de droite.\n\n");
				str.append("En bas, une console affiche tous les messages de log\n");
				str.append("du fichier SCXML décrivant le robot et ses intéractions.\n\n");
				str.append("Vous pouvez déplacer le robot si celui-ci a de la \n");
				str.append("batterie et est allumé, sinon il s'éteint et vous\n");
				str.append("devez recharger la batterie\n\n");
				str.append("Pour intégagir, vous pouvez utiliser les icônes de\n");
				str.append("la barre d'outils ou les touches du clavier :\n");
				str.append("- les 4 flèches permettent les déplacements au \n");
				str.append("  nord, sud, est et ouest.\n");
				str.append("- la touche entrée permet d'allumer/éteindre le\n");
				str.append("  robot\n");
				str.append("- la touche espace permet de le recharger le robot\n");
				str.append("  si besoin\n");
				ImageIcon icon = new ImageIcon(this.getClass().getResource("/img/help.png"));
				JOptionPane.showMessageDialog(null, str.toString(), "Aide de " + RobotFrame.APP_TITLE, JOptionPane.PLAIN_MESSAGE, icon);
			}
		});
		this.infoButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				StringBuilder str = new StringBuilder();
				str.append("Matschieu\n");
				str.append("Université Lille1\n");
				str.append("Master informatique E-Services\n");
				str.append("Projet de GLIHM\n\n");
				str.append("Le projet consiste à poser une interface graphique sur \n");
				str.append("un state-chart.\n\n");
				str.append("Ce projet utilise la librairie apache.commons.scxml\n");
				str.append("(http://commons.apache.org/scxml/) ainsi que ses\n");
				str.append("dépendances pour exécuter des state-charts décris\n");
				str.append("en XML (SCXML).\n\n");
				str.append("Pour plus d'information sur SCXML :\n");
				str.append("http://www.w3.org/TR/scxml/");
				ImageIcon icon = new ImageIcon(this.getClass().getResource("/img/info.png"));
				JOptionPane.showMessageDialog(null, str.toString(), "A propos de " + RobotFrame.APP_TITLE, JOptionPane.PLAIN_MESSAGE, icon);
			}
		});
		this.exitButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				frame.dispose();
			}
		});

		this.setFloatable(false);

		this.repaint();
	}

	@Override
	public void repaint() {
		super.repaint();

		if (this.stateMachine == null)
			return;

		if (this.stateMachine.getRobotState() == RobotStateMachine.ROBOT_STATE.ON) {
			this.moveEastButton.setEnabled(true);
			this.moveWestButton.setEnabled(true);
			this.moveNorthButton.setEnabled(true);
			this.moveSouthButton.setEnabled(true);
			this.turnOnButton.setEnabled(false);
			this.turnOffButton.setEnabled(true);
		}
		else {
			this.moveEastButton.setEnabled(false);
			this.moveWestButton.setEnabled(false);
			this.moveNorthButton.setEnabled(false);
			this.moveSouthButton.setEnabled(false);
			this.turnOffButton.setEnabled(false);
			if (this.stateMachine.getBatteryState() ==  RobotStateMachine.BATTERY_STATE.EMPTY)
				this.turnOnButton.setEnabled(false);
			else
				this.turnOnButton.setEnabled(true);
		}

		if (this.stateMachine.getBatteryState() == RobotStateMachine.BATTERY_STATE.EMPTY)
			this.reloadButton.setEnabled(true);
		else
			this.reloadButton.setEnabled(false);
	}

	/**
	 *
	 * @param o
	 * @param arg
	 */
	@Override
	public void update(Observable o, Object arg) {
		this.repaint();
	}

}
