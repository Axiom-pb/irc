package axiom.irclogmerger.io;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import axiom.irclogmerger.files.Globals;
import axiom.irclogmerger.ui.Gui;
import axiom.irclogmerger.ui.LogAppender;

public class LogReader {
	
	private int year = 2011;
	
	private List<String> linesArray;
	private Set<String> linesSet;
	
	public LogReader() {
		linesSet = new LinkedHashSet<String>();
	}
	
	public void readLogs() {
		Charset cs = Charset.forName("UTF-8");
		for(String filename : Globals.logNames) {
			for(File loc : Globals.locations) {
				if(loc.toString().contains(filename)) {
					try {
						BufferedReader reader = Files.newBufferedReader(loc.getAbsoluteFile().toPath(), cs);
						String line;
						while ((line = reader.readLine()) != null) {
							storeLine(line);
						}
					} catch(IOException iox) {
						iox.printStackTrace();
						LogAppender.log("Error reading file: " + filename);
					}
				}
			}
			sortLogs(filename);
			new LogWriter(filename, linesArray).writeLogs();
			linesArray.clear();
			linesSet.clear();
		}
		Gui.end = System.currentTimeMillis();
		LogAppender.log("Logs merged in: " + Long.toString(Gui.end-Gui.start) + "ms");
		File systemsDir = new File(Globals.mainDir + Globals.FILE_SEPARATOR + "Systems");
		LogAppender.log("Merged size: " + getDirSizeInMegabytes(systemsDir) + "MB");
	}
	
	private long getDirSize(File dir) {
		long size = 0;
		if (dir.isFile()) {
			size = dir.length();
		} else {
			File[] subFiles = dir.listFiles();
			for (File file : subFiles) {
				if (file.isFile()) {
					size += file.length();
				} else {
					size += this.getDirSize(file);
				}
			}
		}
		return size;
	}

	private long getDirSizeInMegabytes(File dir) {
		return this.getDirSize(dir) / 1024 / 1024;
	}

	private void storeLine(String line) {
		String yearString = Integer.toString(getYear(line));
		if(!line.contains("**** BEGIN") && !line.contains("**** ENDING")
				&& !line.isEmpty() && startsWithMonth(line)) {
			linesSet.add(yearString + " " + line);
		}
	}
	
	private boolean startsWithMonth(String line) {
		String month = line.substring(0, 3);
		switch(month) {
			case "Jan":
			case "Feb":
			case "Mar":
			case "Apr":
			case "May":
			case "Jun":
			case "Jul":
			case "Aug":
			case "Sep":
			case "Oct":
			case "Nov":
			case "Dec":
				return true;
			default:
				return false;
		}
	}
	
	private int getYear(String line) {
		if(line.contains("**** BEGIN") || line.contains("**** ENDING")) {
			year = Integer.parseInt(line.substring(line.lastIndexOf(':') + 4));
		}
		return year;
	}
	
	private void sortLogs(final String filename) {
		linesArray = new ArrayList<String>(linesSet);
		Collections.sort(linesArray, new Comparator<Object>() {
			@Override
			public int compare(Object o1, Object o2) {
				DateFormat df = new SimpleDateFormat("yyyy MMM dd HH:mm:ss");
				Date o1Date = null;
				Date o2Date = null;
				
				try {
					o1Date = df.parse(o1.toString().substring(0, 20));
					o2Date = df.parse(o2.toString().substring(0, 20));
				} catch (ParseException px) {
					px.printStackTrace();
					LogAppender.log("Error sorting log: " + filename);
				}
				
				if (o1Date.before(o2Date))
					return -1;
				else if (o1Date.after(o2Date))
					return 1;
				else
					return 0;
				
			}
		});
	}
	
}
