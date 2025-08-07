package com.github.matschieu.temperature.view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Observable;

import com.github.matschieu.temperature.controller.TemperatureController;
import com.github.matschieu.temperature.model.TemperatureModel;

public class TemperatureVueCelsius extends AbstractTemperatureVue {

	public TemperatureVueCelsius(TemperatureModel modele,
			TemperatureController controleur, int posX, int posY) {
		super("Temperature Celsuis",modele, controleur, posX, posY);
		setDisplay(""+model.getC());
		addUpListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				controller.augmenteDegresC();
			}});
		addDownListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				controller.diminueDegresC();
			}});
		addDisplayListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				double tempC = getDisplay();
				controller.fixeDegresC(tempC);
			}});
	}

	@Override
	public void update(Observable s, Object o) {
		setDisplay(""+model().getC());
	}

}
