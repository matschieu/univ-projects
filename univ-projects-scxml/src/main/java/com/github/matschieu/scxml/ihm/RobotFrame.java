
package com.github.matschieu.scxml.ihm;

import java.awt.BorderLayout;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JFrame;
import javax.swing.JScrollPane;

import com.github.matschieu.scxml.Robot;
import com.github.matschieu.scxml.RobotStateMachine;

/**
 *
 * @author mathieu
 */
public class RobotFrame extends JFrame {

	/**
	 * Application title
	 */
	public static final String APP_TITLE = "SC-Robot";

	private Robot stateMachine;

	/**
	 * Constructor
	 * @param stateMachine
	 */
	public RobotFrame(Robot stateMachine) {
		this.stateMachine = stateMachine;

		this.setTitle(APP_TITLE);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		this.getContentPane().setLayout(new BorderLayout(5, 5));

		ToolsBar tb = new ToolsBar(stateMachine, this);
		this.stateMachine.addObserver(tb);
		this.getContentPane().add(tb, BorderLayout.NORTH);

		ConsolePanel console = new ConsolePanel(10, 5);
		this.getContentPane().add(console, BorderLayout.SOUTH);

		StatsPanel sp = new StatsPanel(this.stateMachine);
		this.stateMachine.addObserver(sp);
		this.getContentPane().add(sp, BorderLayout.EAST);

		//ControlPanel cp = new ControlPanel(this.stateMachine);
		//this.stateMachine.addObserver(cp);
		//this.getContentPane().add(cp, BorderLayout.WEST);
		
		ViewRobotPanel vrp = new ViewRobotPanel(this.stateMachine);
		this.stateMachine.addObserver(vrp);
		this.getContentPane().add(new JScrollPane(vrp), BorderLayout.CENTER);

		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent we) {
				dispose();
			}
		});

		this.setFocusable(true);
		final Robot stateMachine_ = stateMachine;
		KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(new KeyEventDispatcher() {
			public boolean dispatchKeyEvent(KeyEvent e) {
				if (e.getID() == KeyEvent.KEY_PRESSED) {
					switch(e.getKeyCode()) {
						case KeyEvent.VK_ENTER:
							if (stateMachine_.getRobotState() == RobotStateMachine.ROBOT_STATE.ON)
								stateMachine_.fireEvent(RobotStateMachine.EVENT_TURN_OFF);
							else
								stateMachine_.fireEvent(RobotStateMachine.EVENT_TURN_ON);
							break;
						case KeyEvent.VK_SPACE:
							stateMachine_.fireEvent(RobotStateMachine.EVENT_RELOAD);
							break;
						case KeyEvent.VK_LEFT:
							stateMachine_.fireEvent(RobotStateMachine.EVENT_MOVE_WEST);
							break;
						case KeyEvent.VK_UP:
							stateMachine_.fireEvent(RobotStateMachine.EVENT_MOVE_NORTH);
							break;
						case KeyEvent.VK_RIGHT:
							stateMachine_.fireEvent(RobotStateMachine.EVENT_MOVE_EAST);
							break;
						case KeyEvent.VK_DOWN:
							stateMachine_.fireEvent(RobotStateMachine.EVENT_MOVE_SOUTH);
							break;
					}
				}
				return true;
			}
		});

		this.pack();
		this.setLocationRelativeTo(null);
		this.setVisible(true);
	}

	@Override
	public void dispose() {
		super.dispose();
		System.exit(0);
	}

}
