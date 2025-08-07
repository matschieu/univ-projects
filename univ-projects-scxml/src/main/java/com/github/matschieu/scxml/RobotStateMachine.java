
package com.github.matschieu.scxml;

import java.awt.Dimension;
import java.awt.Point;
import org.apache.commons.scxml.Context;
import org.apache.commons.scxml.env.AbstractStateMachine;

/**
 *
 * @author mathieu
 * http://commons.apache.org/scxml/
 */
public class RobotStateMachine extends AbstractStateMachine {

	/**
	 * Default XML file containing the SCXML
	 */
	public static final String XML_FILE = "/xml/robot.xml";

	/**
	 * Enum of all possible robot/battery states
	 */
	public static enum ROBOT_STATE   { ON, OFF };
	public static enum BATTERY_STATE { OK, EMPTY };

	/**
	 * Events that it's possible to "fire"
	 */
	public static final String EVENT_TURN_OFF   = "turnOff";
	public static final String EVENT_TURN_ON    = "turnOn";
	public static final String EVENT_MOVE_NORTH = "moveNorth";
	public static final String EVENT_MOVE_SOUTH = "moveSouth";
	public static final String EVENT_MOVE_EAST  = "moveEast";
	public static final String EVENT_MOVE_WEST  = "moveWest";
	public static final String EVENT_RELOAD     = "reload";

	private ROBOT_STATE robotState;
	private BATTERY_STATE batteryState;

	/**
	 * Constructor
	 * @param xmlFilename the XML file containing SCXML
	 */
	public RobotStateMachine(String xmlFilename) {
		super(RobotStateMachine.class.getResource(xmlFilename));
		this.batteryState = this.getBatteryLevel() > 0 ? BATTERY_STATE.OK : BATTERY_STATE.EMPTY;
		this.robotState = ROBOT_STATE.OFF;
	}

	/**
	 * Returns the dimension of the environment where the robot is
	 * @return Dimension
	 */
	public Dimension getSpaceDimension() {
		Context ctx = this.getEngine().getRootContext();
		return new Dimension(((Number)ctx.get("sizeX")).intValue(), ((Number)ctx.get("sizeY")).intValue());
	}

	/**
	 * Returns the robot's current battery level
	 * @return int (between 0 and 100)
	 */
	public int getBatteryLevel() {
		Context ctx = this.getEngine().getRootContext();
		return ((Number)ctx.get("battery")).intValue();
	}

	/**
	 * Returns the robot's current position
	 * @return Point
	 */
	public Point getPosition() {
		Context ctx = this.getEngine().getRootContext();
		int x = ((Number)ctx.get("posX")).intValue();
		int y = ((Number)ctx.get("posY")).intValue();
		return new Point(x, y);
	}

	/**
	 * Returns the robot's current battery state (OK or EMPTY)
	 * @return RobotStateMachine.BATTERY_STATE
	 */
	public BATTERY_STATE getBatteryState() {
		return batteryState;
	}

	/**
	 * Returns the robot's current state (ON or OFF)
	 * @return RobotStateMachine.ROBOT_STATE
	 */
	public ROBOT_STATE getRobotState() {
		return robotState;
	}

	/**
	 * Method called when the state machine is in the state "on"
	 */
	public void on() {
		this.robotState = ROBOT_STATE.ON;
	}

	/**
	 * Method called when the state machine is in the state "position"
	 */
	public void position() { }

	/**
	 * Method called when the state machine is in the state "off"
	 */
	public void off() {
		this.robotState = ROBOT_STATE.OFF;
	}

	/**
	 * Method called when the state machine is in the state "batteryOk"
	 */
	public void batteryOK() {
		this.batteryState = BATTERY_STATE.OK;
	}

	/**
	 * Method called when the state machine is in the state "batteryEmpty"
	 */
	public void batteryEmpty() {
		this.batteryState = BATTERY_STATE.EMPTY;
	}

	/**
	 * Method called when the state machine is in the state "offInit"
	 */
	public void offInit() { }

	/**
	 * Method called when the state machine is in the state "onInit"
	 */
	public void onInit() { }

}
