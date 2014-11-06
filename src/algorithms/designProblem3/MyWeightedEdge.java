package algorithms.designProblem3;

import org.jgrapht.graph.DefaultWeightedEdge;

public class MyWeightedEdge extends DefaultWeightedEdge {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public String getV1() {
		return this.getSource().toString();
	}
	public String getV2() {
		return this.getTarget().toString();
	}
	public Double getdWeight() {
		return this.getWeight();
	}
	@Override
	public String toString() {
		return Double.toString(getWeight());
	}
}
