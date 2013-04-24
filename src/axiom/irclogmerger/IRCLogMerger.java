package axiom.irclogmerger;

import javax.swing.SwingUtilities;

import axiom.irclogmerger.ui.Gui;

public class IRCLogMerger {

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				try {
					Gui gui = new Gui();
					gui.setLocationRelativeTo(null);
					gui.setResizable(true);
					gui.setVisible(true);
				} catch(Exception x) {
					x.printStackTrace();
				}
			}
		});
	}

}
