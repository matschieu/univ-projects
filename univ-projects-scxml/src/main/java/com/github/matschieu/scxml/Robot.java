
package com.github.matschieu.scxml;

import java.awt.Dimension;
import java.awt.Point;
import java.util.Observable;

/**
 *
 * @author mathieu
 */
public class Robot extends Observable {
	
	private RobotStateMachine stateMachine;

	/**
	 * Constructor
	 */
	public Robot() {
		this.stateMachine = new RobotStateMachine(RobotStateMachine.XML_FILE);
	}

	/**
	 * Returns the dimension of the environment where the robot is
	 * @return Dimension
	 */
	public Dimension getSpaceDimension() {
		return this.stateMachine.getSpaceDimension();
	}

	/**
	 * Returns the robot's current battery level
	 * @return int (between 0 and 100)
	 */
	public int getBatteryLevel() {
		return this.stateMachine.getBatteryLevel();
	}

	/**
	 * Returns the robot's current position
	 * @return Point
	 */
	public Point getPosition() {
		return this.stateMachine.getPosition();
	}

	/**
	 * Returns the robot's current battery state (OK or EMPTY)
	 * @return RobotStateMachine.BATTERY_STATE
	 */
	public RobotStateMachine.BATTERY_STATE getBatteryState() {
		return this.stateMachine.getBatteryState();
	}

	/**
	 * Returns the robot's current state (ON or OFF)
	 * @return RobotStateMachine.ROBOT_STATE
	 */
	public RobotStateMachine.ROBOT_STATE getRobotState() {
		return this.stateMachine.getRobotState();
	}

	/**
	 * Throws an envent to the state machine and notifies all obervers
	 * @param event (defines in RobotStateMachine class as public static attribute)
	 * @return Whether the state machine has reached a "final" configuration
	 */
	public boolean fireEvent(String event) {
		boolean r = this.stateMachine.fireEvent(event);
		this.setChanged();
		this.notifyObservers();
		return r;
	}

}
