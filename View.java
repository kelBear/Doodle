import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;

import javax.swing.*;
import javax.swing.filechooser.*;

public class View extends JFrame implements Observer {

	private final String NEW = "New Doodle", LOAD = "Load Doodle", SAVE = "Save Doodle", EXIT = "Exit", ORIGINAL = "Original Size", FIT = "Fit to Screen";
	private final String BIN = "Binary Files", TXT = "Text Files";
	private Model model;
	private JFrame frame = new JFrame("Doodle");
	private Dimension screenSize;
	private JMenuItem saveItem;

	//private JPanel colourPanel, linePanel;
	private ColourPanel cp;
	private LinePanel lp;
	private DrawPanel dp;
	private BotPanel bp;
	private JPanel leftPanel;

	private JFileChooser fileDialogue = new JFileChooser();

	View(Model model) {
		this.model = model;
		screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		screenSize = new Dimension((int)(screenSize.getWidth()), (int)(screenSize.getHeight()*0.95));
		model.setOriginalSize(screenSize);
		Dimension minimum = new Dimension((int)(screenSize.getWidth()*0.55), (int)(screenSize.getHeight()*0.55));
		// create UI
		frame.setResizable(true);
		frame.setSize(getScreenSize());
		frame.setMinimumSize(minimum);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setBackground(Color.WHITE);
		createMenu();

		frame.addComponentListener(new ComponentAdapter() {
			public void componentResized(ComponentEvent e) {
				resize();
			}
		});
		setPanels();
		setFileDialogue();

		frame.setVisible(true);
	}

	public void setPanels() {
		//======PANELS=======
		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new BorderLayout());
		JPanel topPanel = new JPanel();
		topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.X_AXIS));
		//JPanel botPanel;

		// ------ top panel panels -----
		leftPanel = new JPanel();
		leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
		//new DrawPanel(this);

		// --- Left Panel --------
		JScrollPane panelScroll = new JScrollPane(leftPanel, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		Dimension leftPanelSize = new Dimension((int)(screenSize.width*0.12), (int)(screenSize.height*0.75));
		leftPanel.setMaximumSize(leftPanelSize);
		leftPanel.setMinimumSize(leftPanelSize);
		leftPanel.setPreferredSize(leftPanelSize);
		leftPanel.setBackground(Color.LIGHT_GRAY);
		// --- Colour Panel ---
		cp = new ColourPanel(this);
		//colourPanel = ColourPanel.getColourPanel();
		// --- Line Panel ---
		lp = new LinePanel(this);
		//linePanel = LinePanel.getLinePanel();
		leftPanel.add(cp);
		leftPanel.add(lp);
		//=====================
		JToolBar tb = new JToolBar();
		tb.setRollover(true);
		tb.setFloatable(true);
		tb.add(panelScroll);

		dp = new DrawPanel(this);
		dp.setBackground(Color.WHITE);
		JScrollPane drawScroll = new JScrollPane(dp, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		model.addObserver(dp);

		//topPanel.add(panelScroll);
		topPanel.add(tb);
		topPanel.add(drawScroll);

		// --- Bot Panel --- 
		bp = new BotPanel(this);
		model.addObserver(bp);

		mainPanel.add(topPanel);
		mainPanel.add(bp, BorderLayout.SOUTH);

		frame.add(mainPanel);
		frame.setContentPane(mainPanel);
	}

	public void play() {
		dp.play();
	}

	public void start() {
		dp.start();
		//dp.play();
	}

	public void end() {
		//dp.end();	
		BotPanel.getSlider().setValue(BotPanel.getSlider().getMaximum());
		dp.repaint();
	}

	public void resize() {
		Dimension dim = frame.getSize();
		//screenSize = dim;
		if (model.isFit()) {
			Dimension newSize = new Dimension ((int)(dim.getWidth()*0.85), (int)(dim.getHeight()*0.75));
			model.setNewSize(dim);
			//cp.setPreferredSize(new Dimension((int)(dim.getWidth()*0.12), (int)(dim.getHeight()*0.75)));
			//lp.setPreferredSize(new Dimension((int)(dim.getWidth()*0.12), (int)(dim.getHeight()*0.75)));
			//leftPanel.setPreferredSize(new Dimension((int)(dim.getWidth()*0.12), (int)(dim.getHeight()*0.75)));
			//dp.setMinimumSize(newSize);
			dp.setPreferredSize(newSize);
			dp.repaint();
			repaint();
		}
		else if (model.isOriginal()) {
			Dimension newSize = new Dimension ((int)(screenSize.getWidth()*0.85), (int)(screenSize.getHeight()*0.75));
			//cp.setPreferredSize(new Dimension((int)(screenSize.getWidth()*0.12), (int)(screenSize.getHeight()*0.75)));
			//lp.setPreferredSize(new Dimension((int)(screenSize.getWidth()*0.12), (int)(screenSize.getHeight()*0.75)));
			dp.setPreferredSize(newSize);
			//dp.setMaximumSize(newSize);
			model.setMultiplierH(1.00);
			model.setMultiplierW(1.00);
			dp.repaint();
			repaint();
		}
	}
	
	public void setFileDialogue() {
		FileNameExtensionFilter filter = new FileNameExtensionFilter(TXT, "txt");
		fileDialogue.addChoosableFileFilter(filter);
		filter = new FileNameExtensionFilter(BIN, "bin");
		fileDialogue.addChoosableFileFilter(filter);
	}

	public void save() {
		int val = fileDialogue.showDialog(null, "Save");
		if (val == JFileChooser.APPROVE_OPTION) {
			File file = fileDialogue.getSelectedFile();
			if (fileDialogue.getFileFilter().getDescription().equals(BIN)) {
				String ext = ".bin";
				if (!file.getAbsolutePath().toLowerCase().endsWith(ext))
					file = new File(file + ext);
				if (!model.saveBinaryFile(file))
					JOptionPane.showMessageDialog(null, "An error occurred while saving.", "Error", JOptionPane.ERROR_MESSAGE);
			}
			else if (fileDialogue.getFileFilter().getDescription().equals(TXT)) {
				String ext = ".txt";
				if (!file.getAbsolutePath().toLowerCase().endsWith(ext))
					file = new File(file + ext);
				if (!model.saveTextFile(file))
					JOptionPane.showMessageDialog(null, "An error occurred while saving.", "Error", JOptionPane.ERROR_MESSAGE);
			}
			else {
				if (file.getAbsolutePath().toLowerCase().endsWith(".bin")) {
					if (!model.saveBinaryFile(file))
						JOptionPane.showMessageDialog(null, "An error occurred while saving.", "Error", JOptionPane.ERROR_MESSAGE);
				}
				else if (file.getAbsolutePath().toLowerCase().endsWith(".txt")) {
					if (!model.saveBinaryFile(file))
						JOptionPane.showMessageDialog(null, "An error occurred while saving.", "Error", JOptionPane.ERROR_MESSAGE);
				}
				else
					JOptionPane.showMessageDialog(null, "Unsupported file format.", "Error", JOptionPane.ERROR_MESSAGE);

			}
		}
	}

	public void open() {
		int retval;
		if (model.getNumStrokes() != 0 && !model.isSaved()) {
			retval = JOptionPane.showConfirmDialog(null, "Do you want to save the current Doodle?", "Save?", JOptionPane.YES_NO_CANCEL_OPTION);
			if (retval == JOptionPane.CANCEL_OPTION)
				return;
			else if (retval == JOptionPane.YES_OPTION)
				save();
		}
		int val = fileDialogue.showDialog(null, "Open");
		if (val == JFileChooser.APPROVE_OPTION) {
			File file = fileDialogue.getSelectedFile();

			if (fileDialogue.getFileFilter().getDescription().equals(BIN)) {
				if (!model.openBinaryFile(file))
					JOptionPane.showMessageDialog(null, "An error occurred while opening the file.", "Error", JOptionPane.ERROR_MESSAGE);
				else
					model.setOpenFile(false);
			}
			else if (fileDialogue.getFileFilter().getDescription().equals(TXT)) {
				if (!model.openTextFile(file))
					JOptionPane.showMessageDialog(null, "An error occurred while opening the file.", "Error", JOptionPane.ERROR_MESSAGE);
				else
					model.setOpenFile(false);
			}
			else {
				if (!model.openBinaryFile(file)) {
					if (!model.openTextFile(file))
						JOptionPane.showMessageDialog(null, "Unsupported file format.", "Error", JOptionPane.ERROR_MESSAGE);
					else
						model.setOpenFile(false);
				}
			}
		}
	}

	public void newDoodle() {
		int retval;
		if (model.getNumStrokes() != 0) {
			retval = JOptionPane.showConfirmDialog(null, "Do you want to save the current Doodle?", "Save?", JOptionPane.YES_NO_CANCEL_OPTION);
			if (retval == JOptionPane.YES_OPTION) {
				save();
			}
			else if (retval == JOptionPane.CANCEL_OPTION)
				return;
		}
		model.newDoodle();
	}

	public void exit() {
		int retval;
		if (model.getNumStrokes() != 0 && !model.isSaved()) {
			retval = JOptionPane.showConfirmDialog(null, "Do you want to save the current Doodle?", "Save?", JOptionPane.YES_NO_CANCEL_OPTION);
			if (retval == JOptionPane.YES_OPTION) {
				save();
			}
			else if (retval == JOptionPane.CANCEL_OPTION)
				return;
		}
		frame.dispose();
		System.exit(0);
	}

	private void createMenu() {
		JMenuBar menuBar;
		JMenu fileMenu, viewMenu;//, submenu;
		JMenuItem menuItem;
		JRadioButtonMenuItem imageSize;

		menuBar = new JMenuBar();
		fileMenu = new JMenu("File");
		viewMenu = new JMenu("View");

		//File:
		//New Doodle - Save!
		//Load Doodle
		//Save Doodle
		//Exit
		menuItem = new JMenuItem(NEW);
		menuItem.addActionListener(new MenuActionListener());
		fileMenu.add(menuItem);
		menuItem = new JMenuItem(LOAD);
		menuItem.addActionListener(new MenuActionListener());
		fileMenu.add(menuItem);
		saveItem = new JMenuItem(SAVE);
		saveItem.addActionListener(new MenuActionListener());
		saveItem.setEnabled(false);
		fileMenu.add(saveItem);
		fileMenu.addSeparator();
		menuItem = new JMenuItem(EXIT);
		menuItem.addActionListener(new MenuActionListener());
		fileMenu.add(menuItem);
		menuBar.add(fileMenu);

		//View
		//Fit to Window
		//Original Size
		ButtonGroup group = new ButtonGroup();
		imageSize = new JRadioButtonMenuItem(FIT);
		imageSize.setSelected(true);
		imageSize.addActionListener(new MenuActionListener());
		group.add(imageSize);
		viewMenu.add(imageSize);
		imageSize = new JRadioButtonMenuItem(ORIGINAL);
		group.add(imageSize);
		imageSize.addActionListener(new MenuActionListener());
		viewMenu.add(imageSize);

		menuBar.add(viewMenu);

		frame.setJMenuBar(menuBar);

	}

	@Override
	public void update(Observable o, Object arg) {
		ColourPanel.getCurColour().setBackground(model.getCurColour());
		if (model.getNumStrokes() > 0)
			saveItem.setEnabled(true);
		else
			saveItem.setEnabled(false);
	}

	public Model getModel() {
		return model;
	}

	public Dimension getScreenSize() {
		return screenSize;
	}

	class MenuActionListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			if (e.getActionCommand() == NEW)
				newDoodle();
			else if (e.getActionCommand() == LOAD)
				open();
			else if (e.getActionCommand() == SAVE) {
				save();
			}
			else if (e.getActionCommand() == EXIT) {
				exit();
			}
			else if (e.getActionCommand() == ORIGINAL) {
				model.setOriginal(true);
				model.setFit(false);
				resize();
			}
			else if (e.getActionCommand() == FIT) {
				model.setOriginal(false);
				model.setFit(true);
				resize();
			}
		}
	}
}

