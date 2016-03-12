import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

public class ColourPanel extends JPanel{
	
	private final int COLOURPANELNUM = 10;
	private static JButton curColour = new JButton();
	private Dimension colourSize;
	//private static JPanel colourPanel;
	
	ColourPanel(View v) {
		colourSize = new Dimension((int)(v.getScreenSize().getHeight()*0.05), (int)(v.getScreenSize().getHeight()*0.05));
		JButton moreColours = new JButton("<html><p>More<br>Colours</p></html>");
		moreColours.setHorizontalTextPosition(SwingConstants.LEFT);
		moreColours.setFont(new Font("Arial", Font.PLAIN, (int)(v.getScreenSize().getHeight()*0.0125)));
		moreColours.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Color temp = JColorChooser.showDialog(null, "Colour Chooser", Color.BLACK);
				if (temp != null) {
					v.getModel().setCurColour(temp);
				}
			}
		});
		ColourButton[] colours = new ColourButton[COLOURPANELNUM];
		setLayout(new GridLayout(7, 2));
		TitledBorder border = new TitledBorder("Colours");
		border.setTitleFont(new Font("Arial", Font.PLAIN, (int)(v.getScreenSize().getHeight()*0.02)));
		setBorder(BorderFactory.createCompoundBorder(border, new EmptyBorder(10, 10, 10, 10)));

		curColour.setBackground(v.getModel().getCurColour());
		curColour.setEnabled(false);	

		for (int i = 0; i < COLOURPANELNUM; i++) {
			ColourButton btn = new ColourButton();
			colours[i] = btn;
			switch(i){
			case 0:
				colours[i].setBackground(Color.BLACK);
				break;
			case 1:
				colours[i].setBackground(Color.LIGHT_GRAY);
				break;
			case 2:
				colours[i].setBackground(Color.RED);
				break;
			case 3:
				colours[i].setBackground(Color.PINK);
				break;
			case 4:
				colours[i].setBackground(Color.YELLOW);
				break;
			case 5:
				colours[i].setBackground(Color.ORANGE);
				break;
			case 6:
				colours[i].setBackground(Color.GREEN);
				break;
			case 7:
				colours[i].setBackground(Color.CYAN);
				break;
			case 8:
				colours[i].setBackground(Color.BLUE);
				break;
			case 9:
				colours[i].setBackground(Color.MAGENTA);
			}
			btn.setMaximumSize(colourSize);
			btn.addMouseListener(new MouseAdapter() {
				public void mousePressed(MouseEvent e) {
					btn.setBackground(btn.getBackground());
					curColour.setBackground(btn.getBackground());
					v.getModel().setCurColour(btn.getBackground());
				}
			}); 
			add(btn);
		}

		JLabel cur = new JLabel("<html><p>Current<br>Colour:</p></html>");
		cur.setVerticalAlignment(JLabel.BOTTOM);
		cur.setFont(new Font("Arial", Font.PLAIN, (int)(v.getScreenSize().getHeight()*0.015)));
		add(cur);
		add(new JLabel(""));

		add(curColour);
		add(moreColours);
	}
	
	public static JButton getCurColour() {
		return curColour;
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
	}
}
