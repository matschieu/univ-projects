package com.github.matschieu.temperature;

import com.github.matschieu.temperature.controller.TemperatureController;
import com.github.matschieu.temperature.model.TemperatureModel;
import com.github.matschieu.temperature.view.TemperatureVueCelsius;
import com.github.matschieu.temperature.view.TemperatureVueFarenheit;
import com.github.matschieu.temperature.view.TemperatureVueThermometre;

public class TemperatureMVC {

	public TemperatureMVC() {
		TemperatureModel tempmod = new TemperatureModel();
		TemperatureController tempcontrolC = new TemperatureController(tempmod);
		TemperatureController tempcontrolF = new TemperatureController(tempmod);
		TemperatureController tempcontrolTherm = new TemperatureController(tempmod);
		TemperatureVueCelsius pvc = new TemperatureVueCelsius(tempmod, tempcontrolC, 100, 200);
		TemperatureVueFarenheit tvf = new TemperatureVueFarenheit(tempmod, tempcontrolF, 100, 350);
		TemperatureVueThermometre tvt = new TemperatureVueThermometre(tempmod, tempcontrolTherm, 400, 200);
		tempcontrolC.addView(pvc);
		tempcontrolF.addView(tvf);
		tempcontrolTherm.addView(tvt);
	}

	public static void main(String args[]) {
		//Schedule a job for the event-dispatching thread:
		//creating and showing this application's GUI.
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				new TemperatureMVC();
			}
		});
	}

}
