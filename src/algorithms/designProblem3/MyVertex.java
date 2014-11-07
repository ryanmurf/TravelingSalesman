package algorithms.designProblem3;

public class MyVertex {
	String name;
	double cost;
	
	public MyVertex(String name, double cost) {
		this.name = name;
		this.cost = cost;
	}
	
	public String toString() {
		return this.name;// + ":" + String.valueOf(cost);
	}
}
