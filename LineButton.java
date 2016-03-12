import java.awt.*;

import javax.swing.JButton;

public class LineButton extends JButton{

	private int thickness = 0;

	public LineButton(int t) {
		this(null, t);
		thickness = t;
	}

	public LineButton(String text, int t) {
		super(text);
		super.setContentAreaFilled(false);
		thickness = t;
	}

	public int getThickness() {
		return thickness;
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.setColor(getBackground());
		g.fillRect(0, 0, getWidth(), getHeight());
		Graphics2D g2 = (Graphics2D) g;
		Stroke oldStroke = g2.getStroke();
		Color oldColor = g2.getColor();
		g2.setColor(Color.BLACK);
		g2.setStroke(new BasicStroke(thickness, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
		g2.drawLine((int)(this.getX()+5), (int)(this.getHeight()/2), (int)(this.getX()+this.getWidth()-30), (int)(this.getHeight()/2));
		g2.setStroke(oldStroke);
		g2.setColor(oldColor);
	}

	@Override
	public void setContentAreaFilled(boolean b) {
	}

}