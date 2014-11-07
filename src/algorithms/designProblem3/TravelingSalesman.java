package algorithms.designProblem3;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JScrollPane;

import org.jgraph.JGraph;
import org.jgrapht.ListenableGraph;
import org.jgrapht.alg.interfaces.MinimumSpanningTree;
import org.jgrapht.ext.JGraphModelAdapter;
import org.jgrapht.graph.ListenableUndirectedWeightedGraph;
import org.jgrapht.graph.SimpleWeightedGraph;


public class TravelingSalesman {
	
	static int nMinSolutions = 0;
	static List<List<MyVertex>> solutions = new ArrayList<List<MyVertex>>();

	public static void main(String[] args) {
		Input in = new Input(args[0]);
		int threads = Integer.valueOf(args[1]);

		SimpleWeightedGraph<MyVertex, MyWeightedEdge> graph = in.getGraph();
		
		SimulatedAnnealing anneal = new SimulatedAnnealing(graph, in.vertexCost);
		
		
		//BruteForce nn = new BruteForce(graph, in.vertexCost, threads);
		
		
		//displayGraph(graph);
		
		
		/*List<MyVertex> cities = new ArrayList<MyVertex>();
		for (MyVertex myVertex : graph.vertexSet()) {
			cities.add(myVertex);
			nearestNeighbor(cities, graph);
			cities.remove(myVertex);
			String solutionsDisplay = "";
			if(nMinSolutions > 0)
				solutionsDisplay =  " Found "+String.valueOf(nMinSolutions)+" solutions of cost "+String.valueOf(getCost(solutions.get(0), graph)+((int) in.vertexCost));
			System.out.println("Finished checking "+myVertex.toString()+" as start."+solutionsDisplay);
		}
		
		
		for (List<MyVertex> solution : solutions) {
			double cost = 0;
			for (int i = 0; i < solution.size(); i++) {
				System.out.print(solution.get(i).toString());
			}
			cost += in.vertexCost;
			cost += getCost(solution, graph);
			System.out.print(" (" + String.valueOf((int) cost) + ")\n");
			System.out.println("");
		}*/
		
		
		//displayGraph(graph);
		
		//PrimMinimumSpanningTree<MyVertex, MyWeightedEdge> p = new PrimMinimumSpanningTree<MyVertex, MyWeightedEdge>(graph);
		//System.out.println(p.getMinimumSpanningTreeEdgeSet().toString());
		//System.out.println(p.getMinimumSpanningTreeTotalWeight());
		
		//displayGraph(p, in.getVertices());

		//KruskalMinimumSpanningTree<MyVertex, MyWeightedEdge> k = new KruskalMinimumSpanningTree<MyVertex, MyWeightedEdge>(graph);
		//System.out.println(k.getMinimumSpanningTreeEdgeSet().toString());
		//System.out.println(k.getMinimumSpanningTreeTotalWeight());
		
		//displayGraph(k, in.getVertices());
	}
	
	public static int getCost(List<MyVertex> solution, SimpleWeightedGraph<MyVertex, MyWeightedEdge> graph) {
		
		double cost = 0;
		for (int j = 0; j < solution.size(); j++) {
			if (j < (solution.size() - 1)) {
				cost += graph.getEdge(solution.get(j), solution.get(j + 1)).getdWeight();
			}
		}
		return ((int) cost);
	}
	
	public static void nearestNeighbor(List<MyVertex> cities, SimpleWeightedGraph<MyVertex, MyWeightedEdge> graph) {		
		if(cities.size() == graph.vertexSet().size()) {
			if(solutions.size() == 0) {
				solutions.add(new ArrayList<MyVertex>(cities));
				nMinSolutions = 1;
			} else {
				int oldCost = getCost(solutions.get(0), graph);
				int newCost = getCost(cities, graph);
				if(newCost < oldCost) {
					solutions.clear();
					solutions.add(new ArrayList<MyVertex>(cities));
					nMinSolutions = 1;
				} else if(newCost == oldCost) {
					solutions.add(new ArrayList<MyVertex>(cities));
					nMinSolutions++;
				}
			}
			return;
		}
		
		ArrayList<MyWeightedEdge> edges = new ArrayList<MyWeightedEdge>(graph.edgesOf(cities.get(cities.size() - 1)));
		Collections.sort(edges);
		for (MyWeightedEdge myWeightedEdge : edges) {
			if (!cities.contains(myWeightedEdge.getV2())) {
				cities.add(myWeightedEdge.getV2());
				nearestNeighbor(cities, graph);
				cities.remove(myWeightedEdge.getV2());
			}
			if (!cities.contains(myWeightedEdge.getV1())) {
				cities.add(myWeightedEdge.getV1());
				nearestNeighbor(cities, graph);
				cities.remove(myWeightedEdge.getV1());
			}
		}
	}
	
	public static void displayGraph(SimpleWeightedGraph<MyVertex, MyWeightedEdge> graph) {
		ListenableGraph<MyVertex, MyWeightedEdge> g = new ListenableUndirectedWeightedGraph<MyVertex, MyWeightedEdge>(graph);
		JGraph jgraph = new JGraph(new JGraphModelAdapter<MyVertex, MyWeightedEdge>(g));

		JScrollPane scroller = new JScrollPane(jgraph);
		JFrame frame = new JFrame("The Body");
		frame.setSize(600, 600);
		frame.add(scroller);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		jgraph.getGraphLayoutCache().reload();
		jgraph.repaint();
	}
	
	public static void displayGraph(MinimumSpanningTree<MyVertex, MyWeightedEdge> tree, List<MyVertex> vertices) {
		SimpleWeightedGraph<MyVertex, MyWeightedEdge> graph = new SimpleWeightedGraph<MyVertex, MyWeightedEdge>(MyWeightedEdge.class);
		
		for (int i = 0; i < vertices.size(); i++)
			if(vertices.get(i).name.compareTo("") != 0)
				graph.addVertex(vertices.get(i));
		
		Collection<MyWeightedEdge> edges = tree.getMinimumSpanningTreeEdgeSet();
		
		for (MyWeightedEdge myWeightedEdge : edges) {
			MyWeightedEdge e = graph.addEdge(myWeightedEdge.getV1(), myWeightedEdge.getV2());
			graph.setEdgeWeight(e, myWeightedEdge.getdWeight());
		}
		
		ListenableGraph<MyVertex, MyWeightedEdge> g = new ListenableUndirectedWeightedGraph<MyVertex, MyWeightedEdge>(graph);
		JGraph jgraph = new JGraph(new JGraphModelAdapter<MyVertex, MyWeightedEdge>(g));

		JScrollPane scroller = new JScrollPane(jgraph);
		JFrame frame = new JFrame("The Body");
		frame.setSize(600, 600);
		frame.add(scroller);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		jgraph.getGraphLayoutCache().reload();
		jgraph.repaint();
	}

}
