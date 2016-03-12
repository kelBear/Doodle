import java.awt.*;
import java.awt.List;
import java.awt.event.*;
import java.util.*;

import javax.swing.*;

public class DrawPanel extends JPanel implements Observer {

	private int startX, startY, endX, endY, newX, newY;
	Graphics2D g2;
	private static View v;
	private DrawStroke ds;
	private Dimension panelSize;
	
	DrawPanel(View v) {
		DrawPanel.v = v;

		panelSize = (new Dimension((int)(v.getScreenSize().width*0.85), (int)(v.getScreenSize().height*0.75)));
		setPreferredSize(panelSize);
		//setMaximumSize(panelSize);

		this.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e){
				startX = e.getX();
				startY = e.getY();
				newX = startX;
				newY = startY;
				ds = new DrawStroke();
				long time = System.currentTimeMillis();
				ds.addX(new TimedCoordinate(startX, time));
				ds.addY(new TimedCoordinate(startY, time));
				g2 = (Graphics2D) getGraphics();
				if(g2 != null) {
					g2.setStroke(new BasicStroke(v.getModel().getThickness()));
					g2.setColor(v.getModel().getCurColour());
				}
			}
			public void mouseReleased(MouseEvent e) {
				if (g2!= null) {
					endX = e.getX();
					endY = e.getY();
					ds.setColour(g2.getColor());
					ds.setStroke(g2.getStroke());
					ds.setStrokeNum(v.getModel().getThickness());
					long time = System.currentTimeMillis();
					ds.addX(new TimedCoordinate(endX, time));
					ds.addY(new TimedCoordinate(endY, time));
					v.getModel().newStroke(ds);
					BotPanel.getSlider().setValue(BotPanel.getSlider().getMaximum());
				}
			}
		});
		this.addMouseMotionListener(new MouseMotionAdapter() {
			public void mouseDragged(MouseEvent e){
				endX = e.getX();
				endY = e.getY();
				g2 = (Graphics2D) getGraphics();
				if(g2 != null) {
					g2.setStroke(new BasicStroke(v.getModel().getThickness()));
					g2.setColor(v.getModel().getCurColour());
					g2.drawLine(newX, newY, endX, endY);
				}
				newX = endX;
				newY = endY;
				long time = System.currentTimeMillis();
				ds.addX(new TimedCoordinate(endX, time));
				ds.addY(new TimedCoordinate(endY, time));
			}
		});
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		drawAll(g);
	}

	public void start() {
		g2 = (Graphics2D) getGraphics();
		Color oldC = g2.getColor();
		Stroke oldS = g2.getStroke();
		g2.setColor(Color.WHITE);
		g2.fillRect(0, 0, this.getWidth(), this.getHeight());
		BotPanel.getSlider().setValue(0);
		g2.setStroke(oldS);
		g2.setColor(oldC);
	}
	
	public void play() {
		int sliderPosition = BotPanel.getSlider().getValue();
		int start = sliderPosition/v.getModel().getMinorTicks();
		g2 = (Graphics2D) getGraphics();
		Color oldC = g2.getColor();
		Stroke oldS = g2.getStroke();
		for (int i = start; i < v.getModel().getNumStrokes(); i++) {
			DrawStroke temp = v.getModel().getStrokes().get(i);
			int thickness = (int)(temp.getStrokeNum()*(v.getModel().getMultiplierH()+v.getModel().getMultiplierW())/2);
			if (thickness < 1)
				thickness = 1;
			g2.setStroke(new BasicStroke(thickness));
			g2.setColor(temp.getColour());
			int[] x = new int[temp.getX().size()];
			int[] y = new int[temp.getY().size()];
			for (int j = 0; j < temp.getX().size(); j++) {
				x[j] = (int)(temp.getX().get(j).getCoordinate()*v.getModel().getMultiplierW());
				y[j] = (int)(temp.getY().get(j).getCoordinate()*v.getModel().getMultiplierH());
			}
			for (int k = 0; k < temp.getX().size()-1; k++) {
				long delay = temp.getX().elementAt(k+1).getTime() - temp.getX().elementAt(k).getTime();
				long curTime = System.currentTimeMillis();
				while (System.currentTimeMillis()-curTime < delay);
				g2.drawLine(x[k], y[k], x[k+1], y[k+1]);    
			}
			BotPanel.getSlider().setValue((i+1)*v.getModel().getMinorTicks());
		}
		g2.setStroke(oldS);
		g2.setColor(oldC);
	}

/*	public void end() {
		if (BotPanel.getSlider().getValue() != BotPanel.getSlider().getMaximum()) {
			g2 = (Graphics2D) getGraphics();
			Color oldC = g2.getColor();
			Stroke oldS = g2.getStroke();
			for (int i = 0; i < v.getModel().getNumStrokes(); i++) {
				DrawStroke temp = v.getModel().getStrokes().get(i);
				g2.setStroke(temp.getStroke());
				g2.setColor(temp.getColour());
				int[] x = new int[temp.getX().size()];
				int[] y = new int[temp.getY().size()];
				for (int j = 0; j < temp.getX().size(); j++) {
					x[j] = temp.getX().get(j).getCoordinate();
					y[j] = temp.getY().get(j).getCoordinate();
				}
				g2.drawPolyline(x, y, temp.getX().size());
			}
			g2.setStroke(oldS);
			g2.setColor(oldC);
		}
	}*/

	public void drawAll(Graphics g) {
		g2 = (Graphics2D) g;
		Color oldC = g2.getColor();
		Stroke oldS = g2.getStroke();
		if (v.getModel().getUndo()) {
			g2.setColor(Color.WHITE);
			g2.fillRect(0, 0, this.getWidth(), this.getHeight());
		}
		if (BotPanel.getSlider().getValue() % v.getModel().getMinorTicks() == 0) {
			for (int i = 0; i < BotPanel.getSlider().getValue()/v.getModel().getMinorTicks(); i++) {
				DrawStroke temp = v.getModel().getStrokes().get(i);
				int thickness = (int)(temp.getStrokeNum()*(v.getModel().getMultiplierH()+v.getModel().getMultiplierW())/2);
				if (thickness < 1)
					thickness = 1;
				g2.setStroke(new BasicStroke(thickness));
				g2.setColor(temp.getColour());
				int[] x = new int[temp.getX().size()];
				int[] y = new int[temp.getY().size()];
				for (int j = 0; j < temp.getX().size(); j++) {
					x[j] = (int)(temp.getX().get(j).getCoordinate()*v.getModel().getMultiplierW());
					y[j] = (int)(temp.getY().get(j).getCoordinate()*v.getModel().getMultiplierH());
				}
				g2.drawPolyline(x, y, temp.getX().size());
			}
			v.getModel().setPortionIndex(-1);
		}
		else {
			int num = -1;
			for (int i = 0; i < BotPanel.getSlider().getValue()/v.getModel().getMinorTicks() + 1; i++) {
				DrawStroke temp = v.getModel().getStrokes().get(i);
				//g2.setStroke(temp.getStroke());
				int thickness = (int)(temp.getStrokeNum()*(v.getModel().getMultiplierH()+v.getModel().getMultiplierW())/2);
				if (thickness < 1)
					thickness = 1;
				g2.setStroke(new BasicStroke(thickness));
				g2.setColor(temp.getColour());
				int[] x = new int[temp.getX().size()];
				int[] y = new int[temp.getY().size()];
				for (int j = 0; j < temp.getX().size(); j++) {
					x[j] = (int)(temp.getX().get(j).getCoordinate()*v.getModel().getMultiplierW());
					y[j] = (int)(temp.getY().get(j).getCoordinate()*v.getModel().getMultiplierH());
				}
				if (i == BotPanel.getSlider().getValue()/v.getModel().getMinorTicks()) {
					double tick = v.getModel().getMinorTicks()/1.00;
					double percentage = BotPanel.getSlider().getValue()/tick - Math.floor(BotPanel.getSlider().getValue()/v.getModel().getMinorTicks());
					int size = (int)(v.getModel().getStrokes().elementAt(BotPanel.getSlider().getValue()/v.getModel().getMinorTicks()).getX().size()*percentage);
					if (v.getModel().getStrokes().elementAt(BotPanel.getSlider().getValue()/v.getModel().getMinorTicks()).getX().size()*percentage - Math.floor(v.getModel().getStrokes().elementAt(BotPanel.getSlider().getValue()/v.getModel().getMinorTicks()).getX().size()*percentage) > 0.5)
						size++;
					for (int j = 0; j < size-1; j++) {
						g2.drawLine((int)(temp.getX().elementAt(j).getCoordinate()*v.getModel().getMultiplierW()), (int)(temp.getY().elementAt(j).getCoordinate()*v.getModel().getMultiplierH()), (int)(temp.getX().elementAt(j+1).getCoordinate()*v.getModel().getMultiplierW()), (int)(temp.getY().elementAt(j+1).getCoordinate()*v.getModel().getMultiplierH()));
					}
					num = size-1;
				}
				else {
					g2.drawPolyline(x, y, temp.getX().size());
				}
			}
			v.getModel().setPortionIndex(num);
		}
		g2.setStroke(oldS);
		g2.setColor(oldC);
	}

	@Override
	public void update(Observable o, Object arg) {
		if (v.getModel().getDrawUpdate())
			//drawAll();
			repaint();
	}

	public Dimension getPanelSize() {
		return panelSize;
	}

}
