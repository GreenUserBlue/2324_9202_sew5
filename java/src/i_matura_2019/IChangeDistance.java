package i_matura_2019;

/**
 * Das Interface dient dazu, dass ein Knoten, der besucht wird,
 * seine Nachbarn in die Priority-Queue hineingeben bzw.
 * updaten kann.
 * 
 * @author DI Franz Breunig, HTL3R, Oktober 2015
 */
public interface IChangeDistance {
	/**
	 * Updatet in der Priority-Queue bzw. gibt den Knoten in sie hinein.
	 * 
	 * @param node          der zu aendernde Knoten
	 * @param previous      aus welcher Richtung kommt man zu diesem Knoten
	 * @param totalDistanz  was ist die bisher bekannte Gesamt-Distanz vom Startknoten
	 */
	void changeDistance(Node node, Node previous, int totalDistanz);
}
