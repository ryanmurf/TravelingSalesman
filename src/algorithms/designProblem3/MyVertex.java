package algorithms.designProblem3;

public class MyVertex {
	String name;
	
	public MyVertex(String name, double cost) {
		this.name = name;
	}
	
	public String toString() {
		return this.name;// + ":" + String.valueOf(cost);
	}
}
