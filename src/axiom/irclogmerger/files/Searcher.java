package axiom.irclogmerger.files;

import java.io.File;

import axiom.irclogmerger.ui.LogAppender;

public class Searcher {

	public Searcher(File f) {
		File destination = new File(Globals.mainDir + Globals.FILE_SEPARATOR + "Systems");
		if(destination.exists()) {
			deleteDir(destination);
		}
		searchDir(f);
	}

	public static void searchDir(File f) {
		File[] subDir = f.listFiles();
		for(File file : subDir) {
			if(file.isFile() && file.toString().endsWith(".log")) {
				File location = file.getAbsoluteFile();
				Globals.locations.add(location);
				String name = file.getName();
				Globals.logNames.add(name);
			} else {
				searchDir(file);
			}
		}
	}

	private void deleteDir(File f) {
		LogAppender.log("Deleting \"Systems\" directory before starting merge");
		for(File file : f.listFiles()) {
			LogAppender.log("Deleting: " + file.getAbsoluteFile());
			if(file != null) {
				file.delete();
			}
		}
		f.delete();
	}

}
