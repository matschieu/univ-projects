package com.github.matschieu.color.selector.model;

import java.util.LinkedList;

/**
 * @author Matschieu
 */
public class ColorList extends LinkedList<MColor> {
	
	public String toString() {
		StringBuffer str = new StringBuffer();
		for(MColor c : this)
			str.append(c.toString() + "\n");
		return str.toString();
	}
	
	public boolean contains_(MColor color) {
		for(MColor c : this)
			if (c.equals_(color))
				return true;
		return false;
	}

}
