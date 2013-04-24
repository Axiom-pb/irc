package axiom.irclogmerger.ui;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.text.DefaultCaret;

import axiom.irclogmerger.files.Globals;
import axiom.irclogmerger.files.Searcher;
import axiom.irclogmerger.io.LogReader;

public class Gui extends JFrame implements ActionListener {
	
	/**
	 * @author Mathew
	 */
	private static final long serialVersionUID = 1L;
	private JFileChooser chooser;
	private JPanel buttonPanel;
	private JButton open, merge;
	public static JTextArea log;
	private DefaultCaret caret;
	private JScrollPane scroll;
	
	public static long start, end;
	
	public Gui() {
		setTitle("Log Merger");
		setLayout(new BorderLayout());
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setMinimumSize(new Dimension(300, 180));
		Container window = getContentPane();

		chooser = new JFileChooser();
		chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
		chooser.setMultiSelectionEnabled(true);

		buttonPanel = new JPanel();
		buttonPanel.setLayout(new FlowLayout());
		window.add(buttonPanel);

		open = new JButton("Open");
		open.addActionListener(this);
		buttonPanel.add(open);

		merge = new JButton("Merge");
		merge.addActionListener(this);
		buttonPanel.add(merge);

		log = new JTextArea();
		log.setEditable(false);
		log.setMargin(new Insets(5,5,5,5));
		window.add(log);
		
		caret = (DefaultCaret) log.getCaret();
		caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);

		scroll = new JScrollPane(log);
		scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		window.add(scroll);

		add(buttonPanel, BorderLayout.PAGE_START);
		add(scroll, BorderLayout.CENTER);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == open) {
			int option = chooser.showOpenDialog(this);
			if(option == JFileChooser.APPROVE_OPTION) {
				Globals.mainDir = chooser.getSelectedFile();
				log.append("Directory chosen: " + Globals.mainDir.getAbsolutePath() + Globals.NEWLINE);
			} else {
				log.append("Directory selection cancelled." + Globals.NEWLINE);
			}
		} else {
			try {
				start = System.currentTimeMillis();
				new Thread() {
					public void run() {
						new Searcher(Globals.mainDir);
						new LogReader().readLogs();
					}
				}.start();
			} catch(Exception x) {
				x.printStackTrace();
			}
		}
	}

}
