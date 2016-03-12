import java.awt.*;
import java.io.*;
import java.util.Vector;

public class DrawStroke implements Serializable{
	private Stroke stroke;
	private Color colour;
	private Vector<TimedCoordinate> x = new Vector<>();
	private Vector<TimedCoordinate> y = new Vector<>();	
	private int strokeNum;
	
	DrawStroke() {}
	
	public Stroke getStroke() {
		return stroke;
	}
	public void setStroke(Stroke stroke) {
		this.stroke = stroke;
	}
	public Color getColour() {
		return colour;
	}
	public void setColour(Color colour) {
		this.colour = colour;
	}
	public Vector<TimedCoordinate> getX() {
		return x;
	}
	public void addX(TimedCoordinate x) {
		this.x.add(x);
	}
	public Vector<TimedCoordinate> getY() {
		return y;
	}
	public void addY(TimedCoordinate y) {
		this.y.add(y);
	}
	public int getStrokeNum() {
		return strokeNum;
	}
	public void setStrokeNum(int strokeNum) {
		this.strokeNum = strokeNum;
	}
	public String toString() {
		return (strokeNum + " " + colour.getRed() + " " + colour.getGreen() + " " + colour.getBlue() + " " + x.size() + " " + x.toString() + " " + y.toString());
	}
	private void writeObject(ObjectOutputStream stream) throws IOException {
		stream.writeInt(strokeNum);
		stream.writeObject(colour);
		stream.writeObject(x);
		stream.writeObject(y);
	}
	private void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException {
		strokeNum = stream.readInt();
		colour = (Color)stream.readObject();
		x = (Vector<TimedCoordinate>)stream.readObject();
		y = (Vector<TimedCoordinate>)stream.readObject();
		stroke = new BasicStroke(strokeNum);
	}
}
