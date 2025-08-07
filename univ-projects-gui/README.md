Misc GUI tests
==============

**Author: Matschieu**

# Running the project

* Build app using Maven:
```
mvn clean install
```

* Running the app using Maven:
	* MfileNavigator: `mvn exec:java -Dexec.mainClass="com.github.matschieu.mfile.MfileNavigator"`
	* RGB: `mvn exec:java -Dexec.mainClass="com.github.matschieu.rgb.RGB"`
	* RGBSelectorView: `mvn exec:java -Dexec.mainClass="com.github.matschieu.rgb.RGBSelectorView"`
	* ArdoiseMagique: `mvn exec:java -Dexec.mainClass="com.github.matschieu.misc.ArdoiseMagique"`
	* TextEditor: `mvn exec:java -Dexec.mainClass="com.github.matschieu.misc.TextEditor"`
	* UndoGUI: `mvn exec:java -Dexec.mainClass="com.github.matschieu.misc.UndoGUI"`
	* TemperatureMVC: `mvn exec:java -Dexec.mainClass="com.github.matschieu.temperature.TemperatureMVC"`

* Running the app using java command:
	* MfileNavigator: `java -cp target/classes com.github.matschieu.mfile.MfileNavigator`
	* RGB: `java -cp target/classes com.github.matschieu.rgb.RGB`
	* RGBSelectorView: `java -cp target/classes com.github.matschieu.rgb.RGBSelectorView`
	* ArdoiseMagique: `java -cp target/classes com.github.matschieu.misc.ArdoiseMagique`
	* TextEditor: `java -cp target/classes com.github.matschieu.misc.TextEditor`
	* UndoGUI: `java -cp target/classes com.github.matschieu.misc.UndoGUI`
	* TemperatureMVC: `java -cp target/classes com.github.matschieu.temperature.TemperatureMVC`
