package i_matura_2019;
/**
 * "gewichtete Kante" zu einem Nachbarn
 * 
 * @author DI Franz Breunig, HTL3R, Oktober 2015
 */
public class Neighbour implements Comparable<Neighbour> {
	/** der Nachbar */
	private Node neighbour;
	
	/** die Distanze (= Kantengewicht) zum Nachbarn */
	private int distance;
	
	
	/**
	 * Konstruktor
	 * 
	 * @param neighbour der Nachbar
	 * @param distance  die Distanze (= Kantengewicht) zum Nachbarn
	 */
	@SuppressWarnings("WeakerAccess")
	public Neighbour(Node neighbour, int distance) {
		if (neighbour == null) {
			throw new NullPointerException("null poiner for neighbour is forbidden");
		}
		
		if (distance < 0) {
			throw new IllegalArgumentException(String.format(
					"distance (= %d) must be greater equal zero", distance));
		}
		
		this.neighbour = neighbour;
		this.distance  = distance;
	}
	
	
	@Override
	public int compareTo(Neighbour other) {
		return neighbour.compareTo(other.getNeighbour());
	}

	
	/** getter fuer den Nachbarn ({@link #neighbour}) */
	@SuppressWarnings("WeakerAccess")
	public Node getNeighbour() {
		return neighbour;
	}
	
	
	/** getter fuer die Distanz ({@link #distance}) zum Nachbarn ({@link #neighbour})*/
	public int getDistance() {
		return distance;
	}
	
	
	@Override
	public String toString() {
		return String.format("%s:%d", neighbour.getId(), distance);
	}
}
