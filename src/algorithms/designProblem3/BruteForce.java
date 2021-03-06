package algorithms.designProblem3;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.jgrapht.graph.SimpleWeightedGraph;

public class BruteForce {
	
	//int nMinSolutions = 0;
	List<List<MyVertex>> solutions = new ArrayList<List<MyVertex>>();
	
	SimpleWeightedGraph<MyVertex, MyWeightedEdge> graph;
	MyVertex start;
	MyVertex end;
	double verticesCost;
	
	public class WorkRunnable implements Runnable {
		
		List<MyVertex> start = new ArrayList<MyVertex>();
		List<List<MyVertex>> solutions = new ArrayList<List<MyVertex>>();
		//int nMinSolutions = 0;

		public WorkRunnable() {
			
		}

	    public void run() { 	
	    	ArrayList<MyWeightedEdge> edges = new ArrayList<MyWeightedEdge>(graph.edgesOf(start.get(start.size() - 1)));
			Collections.sort(edges);
			for (MyWeightedEdge myWeightedEdge : edges) {
				if (!start.contains(myWeightedEdge.getV2())) {
					start.add(myWeightedEdge.getV2());
					nearestNeighbor(solutions, start, graph);
					start.remove(myWeightedEdge.getV2());
				}
				if (!start.contains(myWeightedEdge.getV1())) {
					start.add(myWeightedEdge.getV1());
					nearestNeighbor(solutions, start, graph);
					start.remove(myWeightedEdge.getV1());
				}
			}
	    }
	}
	
	public BruteForce(SimpleWeightedGraph<MyVertex, MyWeightedEdge> graph, double verticesCost, MyVertex start, MyVertex end, int threads) {
		this.start = start;
		this.end = end;
		this.graph = graph;
		this.verticesCost = verticesCost;
		start(threads);
	}
	
	private void start(int nThreads) {
		ExecutionTimer timer = new ExecutionTimer();
		if(nThreads==0) {
			List<MyVertex> cities = new ArrayList<MyVertex>();
			//for (MyVertex myVertex : graph.vertexSet()) {
				timer.reset();
				cities.add(graph.vertexSet().iterator().next());
				nearestNeighbor(this.solutions, cities, graph);
				cities.remove(graph.vertexSet().iterator().next());
				timer.end();
				String solutionsDisplay = "";
				if(solutions.size() > 0)
					solutionsDisplay =  " Found "+String.valueOf(solutions.size())+" solutions of cost "+String.valueOf(getCost(solutions.get(0), graph)+((int) verticesCost));
				System.out.println("Finished checking A as start."+solutionsDisplay+". Completed in "+ String.valueOf(((double)timer.duration())/1000));
			//}
		} else if(nThreads >= 1) {
			List<MyVertex> vertices = new ArrayList<MyVertex>(graph.vertexSet());
			java.util.Iterator<MyVertex> iter = vertices.iterator();
			MyVertex next = iter.next();
			
			//All add A as start
			List<WorkRunnable> workers = new ArrayList<BruteForce.WorkRunnable>();
			for(int i=0; i<graph.degreeOf(next); i++) {
				WorkRunnable worker = new WorkRunnable();
				worker.start.add(next);
				workers.add(worker);
			}
			
			java.util.Iterator<MyWeightedEdge> edges = graph.edgesOf(next).iterator();
			String myVertices = "";
			List<Thread> threads = new ArrayList<Thread>();
			for (WorkRunnable myWorker : workers) {
				MyWeightedEdge edge = edges.next();
				if (edge.getV1() != next)
					myWorker.start.add(edge.getV1());
				else
					myWorker.start.add(edge.getV2());

				myVertices += "A"
						+ myWorker.start.get(myWorker.start.size() - 1)
								.toString() + " ";
				threads.add(new Thread(myWorker));
				if (!iter.hasNext())
					break;
			}
			timer.reset();
			for (Thread thread : threads) {
				thread.start();
			}
			for (Thread thread : threads) {
				try {
					thread.join();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			timer.end();
			int jobs = threads.size();
			threads.clear();
			for (WorkRunnable myWorker : workers) {
				solutions.addAll(myWorker.solutions);
				myWorker.solutions.clear();
				myWorker.start.clear();
			}
			if (solutions.size() > 0)
				getMin();
			if (jobs > 0) {
				String solutionsDisplay = "";
				if (solutions.size() > 0)
					solutionsDisplay = " Found "
							+ String.valueOf(solutions.size())
							+ " solutions of cost "
							+ String.valueOf(getCost(solutions.get(0), graph)
									+ ((int) verticesCost));
				System.out.println("Finished checking " + myVertices
						+ " as start." + solutionsDisplay + ". Completed in "
						+ String.valueOf(((double) timer.duration()) / 1000));
			}

		}
		printSolutions();
	}
	
	public void getMin() {
		List<Integer> costs = new ArrayList<Integer>();
		Iterator<List<MyVertex>> itor = solutions.iterator();
		while(itor.hasNext()) {
			costs.add(getCost(itor.next(), graph));
		}
		int minCost = Collections.min(costs).intValue();
		
		itor = solutions.iterator();
		Iterator<Integer> cItor = costs.iterator();
		while(itor.hasNext()) {
			itor.next();
			int cost = cItor.next().intValue();
			if(cost > minCost)
				itor.remove();
		}
		
		/*List<MyVertex> minV = solutions.get(0);
		List<Integer> remove = new ArrayList<Integer>();
		int minCost = getCost(minV, graph);
		
		for (int i = 0; i < solutions.size(); i++) {
			List<MyVertex> solution = solutions.get(i);
			int cost = getCost(solution, graph);
			if (cost < minCost) {
				for(int k=0; k<solutions.size(); k++) {
					if(k!=i) {
						if(getCost(solutions.get(k), graph) == minCost)
							remove.add(k);
					}
				}
				minV = solution;
				minCost = cost;
			} else if(cost > minCost){
				remove.add(i);
			}
		}
		Collections.sort(remove);
		for(int i=remove.size()-1; i>=0; i--) {
			solutions.remove(remove.get(i).intValue());
		}*/
	}
	
	private void printSolutions() {
		for (List<MyVertex> solution : solutions) {
			double cost = 0;
			for (int i = 0; i < solution.size(); i++) {
				System.out.print(solution.get(i).toString());
			}
			cost += verticesCost;
			cost += getCost(solution, graph);
			System.out.print(" (" + String.valueOf((int) cost) + ")\n");
			System.out.println("");
		}
	}
    
    public int getCost(List<MyVertex> solution, SimpleWeightedGraph<MyVertex, MyWeightedEdge> graph) {
		double cost = 0;
		for (int j = 0; j < solution.size(); j++) {
			if (j < (solution.size() - 1)) {
				cost += graph.getEdge(solution.get(j), solution.get(j + 1)).getdWeight();
			}
		}
		return ((int) cost);
	}
    
    public void nearestNeighbor(List<List<MyVertex>> solutions, List<MyVertex> cities, SimpleWeightedGraph<MyVertex, MyWeightedEdge> graph) {		
		if(cities.size() == graph.vertexSet().size()) {
			if(solutions.size() == 0) {
				solutions.add(new ArrayList<MyVertex>(cities));
			} else {
				int oldCost = getCost(solutions.get(0), graph);
				int newCost = getCost(cities, graph);
				if(newCost < oldCost) {
					solutions.clear();
					solutions.add(new ArrayList<MyVertex>(cities));
				} else if(newCost == oldCost) {
					solutions.add(new ArrayList<MyVertex>(cities));
				}
			}
			return;
		}
		
		ArrayList<MyWeightedEdge> edges = new ArrayList<MyWeightedEdge>(graph.edgesOf(cities.get(cities.size() - 1)));
		Collections.sort(edges);
		for (MyWeightedEdge myWeightedEdge : edges) {
			if (!cities.contains(myWeightedEdge.getV2())) {
				cities.add(myWeightedEdge.getV2());
				nearestNeighbor(solutions, cities, graph);
				cities.remove(myWeightedEdge.getV2());
			}
			if (!cities.contains(myWeightedEdge.getV1())) {
				cities.add(myWeightedEdge.getV1());
				nearestNeighbor(solutions, cities, graph);
				cities.remove(myWeightedEdge.getV1());
			}
		}
	}
}
