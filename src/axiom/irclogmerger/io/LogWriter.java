package axiom.irclogmerger.io;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import axiom.irclogmerger.files.Globals;
import axiom.irclogmerger.ui.LogAppender;

public class LogWriter {

	private String filename;
	private List<String> linesArray;

	public LogWriter(String filename, List<String> linesArray) {
		this.filename = filename;
		this.linesArray = linesArray;
	}

	@SuppressWarnings("unused")
	public void writeLogs() {
		try {
			boolean mergedDir = new File(Globals.mainDir + Globals.FILE_SEPARATOR + "Systems").mkdir();
			File merged = new File(Globals.mainDir + Globals.FILE_SEPARATOR
					+ "Systems" + Globals.FILE_SEPARATOR + filename);
			if(!merged.exists()) {
				merged.createNewFile();
			}
			BufferedWriter writer = new BufferedWriter(new FileWriter(merged, true));
			for(String line : linesArray) {
				writer.write(line);
				writer.newLine();
			}
			LogAppender.log("Succesfully merged: " + filename);
			writer.close();
		} catch(IOException x) {
			x.printStackTrace();
			LogAppender.log("Error writing file: " + filename);
		}
	}

}
