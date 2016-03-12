import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

public class LinePanel extends JPanel{
	
	private static final int LINENUM = 4;
	private static LineButton[] lineButtons = new LineButton[LINENUM];
	//private static JPanel linePanel;
	
	LinePanel(View v) {
		setLayout(new GridLayout(4, 1));
		TitledBorder border = new TitledBorder("Lines");
		border.setTitleFont(new Font("Arial", Font.PLAIN, (int)(v.getScreenSize().getHeight()*0.02)));
		setBorder(BorderFactory.createCompoundBorder(border, new EmptyBorder(10, 10, 10, 10)));
		for (int i = 0; i < LINENUM; i++) {
			LineButton btn = new LineButton(i*3+1);
			btn.setBackground(Color.WHITE);
			btn.addMouseListener(new MouseAdapter() {
				public void mousePressed(MouseEvent e) {
					v.getModel().setThickness(btn.getThickness());
					for (int i = 0; i < LINENUM; i++) {
						lineButtons[i].setBackground(Color.WHITE);
					}
					btn.setBackground(new Color(255, 220, 220));
				}
			}); 
			lineButtons[i] = btn;
			lineButtons[i].setMinimumSize(btn.getSize());
			add(btn);
		}
		lineButtons[0].setBackground(new Color(255, 220, 220));
		v.getModel().setThickness(1);
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
	}
}
