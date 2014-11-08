package algorithms.designProblem3;

public class MyVertex implements Comparable<MyVertex> {
	String name;
	
	public MyVertex(String name, double cost) {
		this.name = name;
	}
	
	public String toString() {
		return this.name;// + ":" + String.valueOf(cost);
	}

	@Override
	public int compareTo(MyVertex o) {
		return this.name.compareTo(o.name);
	}
}
