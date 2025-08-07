package com.github.matschieu.temperature.view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Observable;

import com.github.matschieu.temperature.controller.TemperatureController;
import com.github.matschieu.temperature.model.TemperatureModel;

public class TemperatureVueFarenheit extends AbstractTemperatureVue {

	public TemperatureVueFarenheit(TemperatureModel modele,
			TemperatureController controleur, int posX, int posY) {
		super("Temperature Farenheit",modele, controleur, posX, posY);
		setDisplay(""+model.getF());
		addUpListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				controller.augmenteDegresF();
			}});
		addDownListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				controller.diminueDegresF();
			}});
		addDisplayListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				double tempF = getDisplay();
				controller.fixeDegresF(tempF);
			}});
	}

	@Override
	public void update(Observable s, Object o) {
		setDisplay(""+model().getF());
	}

}
