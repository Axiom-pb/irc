package axiom.irclogmerger.ui;

import axiom.irclogmerger.files.Globals;

public class LogAppender {
	
	public static void log(final String msg) {
		Gui.log.append(msg + Globals.NEWLINE);
	}

}
