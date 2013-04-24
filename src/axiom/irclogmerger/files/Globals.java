package axiom.irclogmerger.files;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

public final class Globals {
	
	public static final String FILE_SEPARATOR = System.getProperty("file.separator");
	public static final String NEWLINE = System.getProperty("line.separator");
	
	public static File mainDir;

	public static Set<String> logNames = new HashSet<String>();
	public static Set<File> locations = new HashSet<File>();
	
}
