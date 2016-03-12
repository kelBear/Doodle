import java.awt.Graphics;

import javax.swing.JButton;

public class ColourButton extends JButton{

	public ColourButton() {
		this(null);
	}

	public ColourButton(String text) {
		super(text);
		super.setContentAreaFilled(false);
	}
	
	@Override
	protected void paintComponent(Graphics g) {
			g.setColor(getBackground());
		g.fillRect(0, 0, getWidth(), getHeight());
		super.paintComponent(g);
	}

	@Override
	public void setContentAreaFilled(boolean b) {
	}

}