public class Doodle {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		Model m = new Model();
		View v = new View(m);
		m.addObserver(v);
		m.notifyObservers();
	}

}
