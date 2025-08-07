package com.github.matschieu.desks.controller;

import com.github.matschieu.desks.model.util.Data;

/**
 * @author Matschieu
 */
public interface DataController<D extends Data> {

	public void add(D d);
	public void remove(D d);
	public void update(D d);
	
}
