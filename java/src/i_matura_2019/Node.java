package i_matura_2019;
import java.util.Set;
import java.util.TreeSet;

/**
 * Der (Dijkstra-)Knoten eines (ungerichteten) Graphens 
 * mit den Kantengewichten zu seinen Nachbarn.<br>
 * 
 * @author DI Franz Breunig, HTL3R, Oktober 2015
 */
public class Node implements Comparable<Node>{
	/** der Name des Knotens (dieser muss im Graphen eindeutig sein) */
	private String id;
	
	
	/** Die Nachbarn des Knotens mit den Kantengewichten */
	private Set<Neighbour> neighbours = new TreeSet<>();


	/** Gesamtdistanze zum Start-Knoten */
	private int totalDistance;


	/** Kennezeichnet, ob der Weg Start-Knoten zu diesem Knoten schon gefunden wurde. */
	private boolean isVisited;


	/** 
	 * Der Vorgaenger-Knoten, um zum Start-Knoten zu gelangen.
	 */
	private Node previous;
	
	
	/**
	 * Konstruktor
	 * 
	 * @param id der Name des Knotens (dieser muss im Graphen eindeutig sein)
	 */
	@SuppressWarnings("WeakerAccess")
	public Node(String id) {
		this.id = id;
		totalDistance = Integer.MAX_VALUE;
	}

	
	@Override
	public String toString() {
		if (! hasNeighbours()) {
			return String.format("%s: has no neighbours", id);
		}
	
		StringBuilder b = new StringBuilder();
		
		for (Neighbour n: neighbours) {
			b.append(", ").append(n);
		}
	
		if (isStartNode()) {
			return String.format("%s----> is start node %s", id, b.substring(2));
		}
		
		if (totalDistance == Integer.MAX_VALUE) {
			return String.format("%s [totalDistance: ?] %s", id, b.substring(2));
		}
		
		return String.format("%s [totalDistance: %d] %s", id, totalDistance, b.substring(2));		
	}


	@Override
	public int compareTo(Node other) {
		return id.compareTo(other.id);
	}
	
	
	/** getter fuer die {@link #id} */
	@SuppressWarnings("WeakerAccess")
	public String getId() {
		return id;
	}
	
	
	/**
	 * Ermittelt ob es mindestens einen Nachbarn gibt.
	 * 
	 * @return true --> es gibt mindestens einen Nachbarn
	 */
	@SuppressWarnings("WeakerAccess")
	public boolean hasNeighbours() {
		return ! neighbours.isEmpty();
	}
	
	
	/** getter fuer die Gesamtdistanze ({@link #totalDistance}) zum Startknoten  */
	@SuppressWarnings("WeakerAccess")
	public int getTotalDistance() {
		return totalDistance;
	}


	/** setter fuer die Gesamtdistanze ({@link #totalDistance}) zum Startknoten */
	@SuppressWarnings("WeakerAccess")
	public void setTotalDistance(int totalDistance) {
		this.totalDistance = totalDistance;
	}


	/** setter fuer die VorgaengerKnoten ({@link #previous}) */
	@SuppressWarnings("WeakerAccess")
	public void setPrevious(Node previous) {
		this.previous = previous;
	}


	/** getter fuer {@link #isVisited } */
	@SuppressWarnings("WeakerAccess")
	public boolean isVisited() {
		return isVisited;
	}


	/**
	 * Liefert, ob dieser Knoten der Startknoten im Graphen ist.
	 * 
	 * @return true --> dieser Knoten ist der Startknoten
	 */
	@SuppressWarnings("WeakerAccess")
	public boolean isStartNode() {
		return previous == null && isVisited;
	}


	/**
	 * Fuegt dem Knoten einen Nachbarn hinzu.<br>
	 * Falls schon eine Verbindung zu diesem Knoten bestand,
	 * erfolgt keine Aenderung.<br>
	 * 
	 * Beim Nachbarknoten wird dieser Knoten ebenfalls als Nachbar vermerkt.<br>
	 *  
	 * @param neighbour der Nachbar
	 * 
	 * @throws IllegalArgumentException Der eigene Knoten kann nicht 
	 *                                  als Nachbar eingetragen werden.
	 */
	void addNeighbour(Neighbour neighbour) {
		if (equals(neighbour.getNeighbour())) {
			throw new IllegalArgumentException(String.format(
					"node (%s) can't be its own neighbour", id));
		}
		
		if (neighbours.add(neighbour)) {
			neighbour.getNeighbour().addNeighbour(new Neighbour(this, neighbour.getDistance()));
		}
	}


	/**
	 * Initialisiert den Knoten, um den Dijkstra-Algorithumus neu zu starten.
	 */
	@SuppressWarnings("WeakerAccess")
	public void init() {
		totalDistance  = Integer.MAX_VALUE;
		previous  = null;
		isVisited = false;
	}


	/**
	 * Macht diesen Knoten zum Startknoten des Dijkstra-Graphen.<br>
	 * 
	 * Alle Knoten des Graphen müssen vorher mittels
	 * {@link #visit(IChangeDistance)} für den Dijkstra-Algorithmus
	 * vorbereitet werden worden sein.
	 * 
	 * @param cd um diesen Knoten in die Priority-Queue geben zu können
	 */
	@SuppressWarnings("WeakerAccess")
	public void setStartNode(IChangeDistance cd) {
		cd.changeDistance(this, null, 0);
	}


	/**
	 * Markiert diesen Knoten als vom "previous"-Knoten besucht. <br>
	 *
	 * In allen Nachbarknoten wird die Distanze mittels des Parameters cd
	 * ({@link IChangeDistance#changeDistance(Node node, Node previous, int distance)}) ausgebessert.
	 * 
	 * @param cd um fuer alle Nachbarn die Distanz auszubessern
	 */
    @SuppressWarnings("WeakerAccess")
	public void visit(IChangeDistance cd) {
		isVisited = true;
		for (Neighbour n: neighbours) {
			Node neighbour = n.getNeighbour();
			
			if ( ! neighbour.isVisited()) {
				cd.changeDistance(neighbour, this, totalDistance + n.getDistance());
			}
		}
	}


	/**
	 * Liefert den Pfad vom Startknoten bis zu diesem 
	 * inkl der Distanzen zum Startknoten.<br><br>
	 * 
	 * Beispiel (Taubstummengasse ist der Startknoten, dieser (= Ziel) ist Landstrasze):<br>
	 *   Taubstummengasse (2) Karlsplatz (4) Stadtpark (5) Landstrasze <br>
	 *   Die Gesamtdistanze vom Startknoten weg steht in den runden Klammern.
	 *   
	 * @return der Pfad als String
	 */
	public String getPath() {
		if ( ! isVisited) {
			return "no path available for " + toString();
		}
		
		if ( isStartNode()) {
			return id + ": is start node";
		}
		
		StringBuilder b = new StringBuilder();
		
		return getPath(b).toString();
	}


	/**
	 * Liefert den Pfad vom Startknoten bis zu diesem 
	 * inkl der Distanzen zum Startknoten.<br><br>
	 * 
	 * Beispiel (Taubstummengasse ist der Startknoten, dieser (= Ziel) ist Landstrasze):<br>
	 *   Taubstummengasse (2) Karlsplatz (4) Stadtpark (5) Landstrasze <br>
	 *   Die Gesamtdistanze vom Startknoten weg steht in den runden Klammern.
	 *   
	 * @param b der StringBuilder fuer den Pfad, der bis zum Startknoten durchgereicht werden muss
	 * @return der Pfad
	 */
    @SuppressWarnings("WeakerAccess")
	public StringBuilder getPath(StringBuilder b) {
		if ( ! isVisited) {
			return b.append("[ERROR no path available for ").append(this).append("!]");
		}
		
		if (isStartNode()) {
			return b.append(getId());
		}
		
		
		return previous.getPath(b).append(" --(").append(totalDistance).append(")-> ").append(getId());
	}
}
