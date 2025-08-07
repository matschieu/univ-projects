
package com.github.matschieu.scxml.log;

import javax.swing.JTextArea;
import org.apache.commons.logging.Log;

/**
 *
 * @author mathieu
 */
public class StaticTextAreaLog implements Log {

	public static JTextArea textArea = new JTextArea();

	/**
	 * Constructor
	 * @param name
	 */
	public StaticTextAreaLog(String name) { }

	public boolean isDebugEnabled() {
		return false;
	}

	public boolean isErrorEnabled() {
		return true;
	}

	public boolean isFatalEnabled() {
		return true;
	}

	public boolean isInfoEnabled() {
		return true;
	}

	public boolean isTraceEnabled() {
		return false;
	}

	public boolean isWarnEnabled() {
		return true;
	}

	public void trace(Object o) {
		if (this.isTraceEnabled())
			textArea.append("[TRACE] " + o + "\n");
	}

	public void trace(Object o, Throwable thrwbl) {
		if (this.isTraceEnabled())
			textArea.append("[TRACE] " + o + "\n");
	}

	public void debug(Object o) {
		if (this.isDebugEnabled())
			textArea.append("[DEBUG] " + o + "\n");
	}

	public void debug(Object o, Throwable thrwbl) {
		if (this.isDebugEnabled())
			textArea.append("[DEBUG] " + o + "\n");
	}

	public void info(Object o) {
		if (this.isInfoEnabled())
			textArea.append("[INFO] " + o + "\n");
	}

	public void info(Object o, Throwable thrwbl) {
		if (this.isInfoEnabled())
			textArea.append("[INFO] " + o + "\n");
	}

	public void warn(Object o) {
		if (this.isWarnEnabled())
			textArea.append("[WARN] " + o + "\n");
	}

	public void warn(Object o, Throwable thrwbl) {
		if (this.isWarnEnabled())
			textArea.append("[WARN] " + o + "\n");
	}

	public void error(Object o) {
		if (this.isErrorEnabled())
			textArea.append("[ERROR] " + o + "\n");
	}

	public void error(Object o, Throwable thrwbl) {
		if (this.isErrorEnabled())
			textArea.append("[ERROR] " + o + "\n");
	}

	public void fatal(Object o) {
		if (this.isFatalEnabled())
			textArea.append("[FATAL] " + o + "\n");
	}

	public void fatal(Object o, Throwable thrwbl) {
		if (this.isFatalEnabled())
			textArea.append("[FATAL] " + o + "\n");
	}

}
