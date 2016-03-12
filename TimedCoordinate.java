import java.io.Serializable;

public class TimedCoordinate implements Serializable{
	
	private int coordinate;
	private long time;
	
	TimedCoordinate(int coordinate, long time) {
		this.coordinate = coordinate;
		this.time = time;
	}
	public int getCoordinate() {
		return coordinate;
	}
	public void setCoordinate(int coordinate) {
		this.coordinate = coordinate;
	}
	public long getTime() {
		return time;
	}
	public void setTime(long time) {
		this.time = time;
	}
	
	public String toString() {
		return (coordinate + " " + time);
	}
}