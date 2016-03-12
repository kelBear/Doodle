import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;

import javax.swing.*;
import javax.swing.event.*;

public class BotPanel extends JPanel implements Observer {

	static private JSlider slider;
	static View v;

	BotPanel(View v) {

		BotPanel.v = v;

		setMinimumSize(new Dimension((int)(v.getScreenSize().width), (int)(v.getScreenSize().height*0.25)));
		setMaximumSize(new Dimension((int)(v.getScreenSize().width), (int)(v.getScreenSize().height*0.25)));
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		JPanel sliderPanel = new JPanel();
		sliderPanel.setLayout(new BoxLayout(sliderPanel, BoxLayout.X_AXIS));

		Icon playIcon = new ImageIcon(this.getClass().getResource("/play.png"));
		Icon startIcon = new ImageIcon(this.getClass().getResource("/start.png"));
		Icon endIcon = new ImageIcon(this.getClass().getResource("/end.png"));
		JButton playbtn = new JButton(playIcon);
		JButton startbtn = new JButton(startIcon);
		JButton endbtn = new JButton(endIcon);
		playbtn.setBackground(Color.WHITE);
		startbtn.setBackground(Color.WHITE);
		endbtn.setBackground(Color.WHITE);

		slider = new JSlider(JSlider.HORIZONTAL, 0, 0, 0);
		setSlider();
		v.getModel().setSliderPosition(slider.getValue());

		slider.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				double tick = v.getModel().getMinorTicks()/1.00;
				if(slider.getValue() % tick != 0) {
					double percentage = slider.getValue()/tick - Math.floor(slider.getValue()/v.getModel().getMinorTicks());
					int size = (int)(v.getModel().getStrokes().elementAt(slider.getValue()/v.getModel().getMinorTicks()).getX().size()*percentage);
					if (size < 2)
						slider.setValue(slider.getValue()/v.getModel().getMinorTicks());
				}
				v.getModel().sliderChange(slider.getValue());
				if (slider.getValue() == 0) {
					startbtn.setEnabled(false);
				}
				else {
					startbtn.setEnabled(true);
				}
				if (slider.getValue() == slider.getMaximum()) {
					playbtn.setEnabled(false);
					endbtn.setEnabled(false);
				}
				else {
					playbtn.setEnabled(true);
					endbtn.setEnabled(true);
				}
			}
		});

		playbtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				v.play();
			}
		});
		startbtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				v.start();
			}
		});
		endbtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				v.end();
			}
		});
		
		playbtn.setEnabled(false);
		startbtn.setEnabled(false);
		endbtn.setEnabled(false);

		sliderPanel.add(playbtn);
		sliderPanel.add(Box.createHorizontalStrut(10));
		sliderPanel.add(slider);
		sliderPanel.add(Box.createHorizontalStrut(10));
		sliderPanel.add(startbtn);
		sliderPanel.add(endbtn);

		this.add(sliderPanel);
		this.add(Box.createVerticalStrut(10));

	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
	}

	public void setSlider() {
		Hashtable<Integer, JLabel> label = new Hashtable<Integer, JLabel>();
		for (int i = 0; i <= slider.getMaximum(); i+=v.getModel().getMinorTicks()) {
			String text = (i/v.getModel().getMinorTicks()) + "";
			label.put(i, new JLabel(text));
		}
		slider.setLabelTable(label);
		slider.setMajorTickSpacing(v.getModel().getMinorTicks());
		slider.setSnapToTicks(false);
		slider.setPaintLabels(true);
		slider.setPaintTicks(true);
	}

	@Override
	public void update(Observable o, Object arg) {
		slider.setMaximum((v.getModel().getNumStrokes())*v.getModel().getMinorTicks());
		if (v.getModel().isOpenFile()) {
			slider.setValue(slider.getMaximum());
			v.getModel().setOpenFile(false);
		}
		v.getModel().setSliderPosition(slider.getValue());
		setSlider();
		this.repaint();
	}

	public static JSlider getSlider() {
		return slider;
	}


}
