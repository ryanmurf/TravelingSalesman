package algorithms.designProblem3;

import org.jgrapht.graph.DefaultWeightedEdge;

public class MyWeightedEdge extends DefaultWeightedEdge implements Comparable<MyWeightedEdge> {
	private static final long serialVersionUID = 1L;

	
	
	public MyVertex getV1() {
		return (MyVertex) this.getSource();
	}
	public MyVertex getV2() {
		return (MyVertex) this.getTarget();
	}
	public Double getdWeight() {
		return this.getWeight();
	}
	@Override
	public String toString() {
		return Double.toString(getWeight());
	}
	
	@Override
	public int compareTo(MyWeightedEdge o) {
		return (int) (this.getWeight() - o.getWeight());
	}
}
