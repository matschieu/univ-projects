package com.github.matschieu.scxml.pingpong;

import org.apache.commons.scxml.env.AbstractStateMachine;

public class PingPong extends AbstractStateMachine {

	public static final String EVENT_PING = "pongtoping";
	public static final String EVENT_PONG = "pingtopong";

	public PingPong() {
		super(PingPong.class.getResource("/xml/pingpong.xml"));
	}

	public void pong() { }

	public void ping() { }

}