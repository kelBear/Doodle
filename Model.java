import java.util.*;
import java.util.List;
import java.awt.*;
import java.io.*;
import java.nio.charset.*;
import java.nio.file.*;

public class Model extends Observable {

	private Color currentColour = Color.BLACK;
	private int thickness = 0;
	private Vector<DrawStroke> strokes = new Vector<>();
	private int sliderPosition = 0, undoPosition = 0, portionIndex = 0;
	private boolean undo = false, drawUpdate = false, openFile = false, saved = false;
	private double multiplierW = 1.0, multiplierH = 1.0;
	private boolean fit = true, original = false;
	private final int MINORTICKS = 100;
	private Dimension originalSize;
	private Dimension newSize;

	Model () { setChanged(); }

	public boolean isSaved() {
		return saved;
	}

	public void setSaved(boolean saved) {
		this.saved = saved;
	}

	public void setOriginalSize(Dimension originalSize) {
		this.originalSize = originalSize;
	}
	
	public void setNewSize(Dimension newSize) {
		this.newSize = newSize;
		multiplierW = newSize.getWidth()/originalSize.getWidth();
		multiplierH = newSize.getHeight()/originalSize.getHeight();
	}
	
	public double getMultiplierW() {
		return multiplierW;
	}
	
	public double getMultiplierH() {
		return multiplierH;
	}
	
	public void setMultiplierW(double m) {
		multiplierW = m;
	}
	
	public void setMultiplierH(double m) {
		multiplierH = m;
	}

	public void setCurColour (Color c) {
		currentColour = c;
		drawUpdate = false;
		setChanged();
		notifyObservers();
	}

	public Color getCurColour() {
		return currentColour;
	}

	public void sliderChange(int value) {
		if (value >= sliderPosition) {
			if (value == strokes.size()*MINORTICKS) {
				undo = false;
				undoPosition = strokes.size()*MINORTICKS;
			}
			else
				undoPosition = value;
		}
		else if (value < sliderPosition) {
			undo = true;
			undoPosition = value;
		}
		drawUpdate = true;
		setChanged();
		notifyObservers();
	}

	public void setThickness(int t) {
		thickness = t;
		drawUpdate = false;
		setChanged();
		notifyObservers();
	}

	public void newStroke(DrawStroke ds) {
		drawUpdate = false;
		saved = false;
		if (undo) {
			if (undoPosition % MINORTICKS == 0) {
				for (int i = strokes.size()-1; i >= undoPosition/MINORTICKS; i--) {
					strokes.removeElementAt(i);
				}
			}
			else {
				for (int i = strokes.size()-1; i >= undoPosition/MINORTICKS+1; i--) {
					strokes.removeElementAt(i);
				}
				for (int i = strokes.elementAt(undoPosition/MINORTICKS).getX().size()-1; i> portionIndex; i--) {
					strokes.elementAt(undoPosition/MINORTICKS).getX().removeElementAt(i);
					strokes.elementAt(undoPosition/MINORTICKS).getY().removeElementAt(i);
				}
			}
			undo = false;
		}
		strokes.add(ds);
		setChanged();
		notifyObservers();
	}

	public boolean saveTextFile(File file) {
		try {
			PrintWriter fout = new PrintWriter(new FileWriter(file.getAbsolutePath()));
			fout.println(strokes.size());
			for (int i = 0; i < strokes.size(); i++) {
				fout.println(strokes.elementAt(i));
			}
			fout.flush();
			fout.close();
			saved = true;
			return true;
		}
		catch (Exception e) {
			return false;
		}
	}

	public boolean openTextFile(File file) {
		try {
			openFile = true;
			drawUpdate = true;
			List<String> lines = Files.readAllLines(Paths.get(file.getAbsolutePath()), StandardCharsets.UTF_8);
			int strokeSize = Integer.parseInt(lines.get(0));
			for (int i = 1; i <= strokeSize; i++) {
				DrawStroke ds = new DrawStroke();
				String line = lines.get(i);
				String[] split = line.split("\\s+");
				int thickness = Integer.parseInt(split[0]);
				Color colour = new Color(Integer.parseInt(split[1]), Integer.parseInt(split[2]), Integer.parseInt(split[3]));
				ds.setColour(colour);
				ds.setStroke(new BasicStroke(thickness));
				int numPoints = Integer.parseInt(split[4]);
				for (int j = 0; j < numPoints; j++) {
					TimedCoordinate tcX;
					TimedCoordinate tcY;
					if (numPoints == 1) {
						tcX = new TimedCoordinate(Integer.parseInt(split[j+5].substring(1)), Long.parseLong(split[j+6].substring(0, split[j+6].length()-1)));
						tcY = new TimedCoordinate(Integer.parseInt(split[j*2+5+numPoints*2].substring(1)), Long.parseLong(split[j*2+6+numPoints*2].substring(0, split[j*2+6+numPoints*2].length()-1)));
					}
					else {
						if (j == 0) {
							tcX = new TimedCoordinate(Integer.parseInt(split[j+5].substring(1)), Long.parseLong(split[j+6].substring(0, split[j+6].length()-1)));
							tcY = new TimedCoordinate(Integer.parseInt(split[j*2+5+numPoints*2].substring(1)), Long.parseLong(split[j*2+6+numPoints*2].substring(0,  split[j*2+numPoints*2].length()-1)));
						}
						else if (j == numPoints - 1) {
							tcX = new TimedCoordinate(Integer.parseInt(split[j*2+5]), Long.parseLong(split[j*2+6].substring(0, split[j*2+6].length()-1)));
							tcY = new TimedCoordinate(Integer.parseInt(split[j*2+5+numPoints*2]), Long.parseLong(split[j*2+6+numPoints*2].substring(0, split[j*2+6+numPoints*2].length()-1)));
						}
						else {
							tcX = new TimedCoordinate(Integer.parseInt(split[j*2+5]), Long.parseLong(split[j*2+6].substring(0, split[j*2+6].length()-1)));
							tcY = new TimedCoordinate(Integer.parseInt(split[j*2+5+numPoints*2]), Long.parseLong(split[j*2+6+numPoints*2].substring(0, split[j*2+6+numPoints*2].length()-1)));
						}
					}
					ds.getX().add(tcX);
					ds.getY().add(tcY);
				}
				strokes.add(ds);
			}
			setChanged();
			notifyObservers();
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public boolean saveBinaryFile(File file) {
		try {
			FileOutputStream fout = new FileOutputStream(file.getAbsolutePath());
			ObjectOutputStream oos = new ObjectOutputStream(fout);
			oos.writeObject(strokes);
			oos.close();
			saved = true;
			return true;
		}
		catch (Exception e) {
			return false;
		}
	}

	public boolean openBinaryFile(File file) {
		try {
			openFile = true;
			strokes.clear();
			FileInputStream fin = new FileInputStream(file.getAbsolutePath());
			ObjectInputStream ois = new ObjectInputStream(fin);
			strokes = (Vector<DrawStroke>)ois.readObject();
			ois.close();
			drawUpdate = true;
			setChanged();
			notifyObservers();
			return true;
		}
		catch (Exception e) {
			return false;
		}
	}

	public void newDoodle() {
		strokes.clear();
		drawUpdate = true;
		setChanged();
		notifyObservers();
	}

	public boolean isOpenFile() {
		return openFile;
	}

	public void setOpenFile(boolean openFile) {
		this.openFile = openFile;
	}

	public Vector<DrawStroke> getStrokes() {
		return strokes;
	}

	public int getNumStrokes() {
		return strokes.size();
	}

	public void setSliderPosition(int s) {
		sliderPosition = s;
	}

	public int getThickness() {
		return thickness;
	}

	public boolean getUndo() {
		return undo;
	}

	public boolean getDrawUpdate() {
		return drawUpdate;
	}

	public void setPortionIndex(int portionIndex) {
		this.portionIndex = portionIndex;
	}

	public int getMinorTicks() {
		return MINORTICKS;
	}

	public boolean isFit() {
		return fit;
	}

	public void setFit(boolean fit) {
		this.fit = fit;
	}

	public boolean isOriginal() {
		return original;
	}

	public void setOriginal(boolean original) {
		this.original = original;
	}

}
