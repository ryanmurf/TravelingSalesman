package algorithms.designProblem3;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.swing.JFrame;
import javax.swing.JScrollPane;

import org.jgraph.JGraph;
import org.jgrapht.ListenableGraph;
import org.jgrapht.alg.interfaces.MinimumSpanningTree;
import org.jgrapht.ext.JGraphModelAdapter;
import org.jgrapht.graph.ListenableUndirectedWeightedGraph;
import org.jgrapht.graph.SimpleWeightedGraph;
import org.jgrapht.traverse.DepthFirstIterator;


public class TravelingSalesman {

	public static void main(String[] args) {
		Input in = new Input(args[0]);

		SimpleWeightedGraph<MyVertex, MyWeightedEdge> graph = in.getGraph();
		
		//displayGraph(graph);
		
		//DepthFirstIterator<MyVertex, MyWeightedEdge> dfs = new DepthFirstIterator<MyVertex, MyWeightedEdge>(graph, graph.vertexSet().iterator().next());
		//while (dfs.hasNext()) {
		//	System.out.print(dfs.next());
		//}
		
		List<List<MyVertex>> solutions = new ArrayList<List<MyVertex>>();
		List<MyVertex> cities = new ArrayList<MyVertex>();
		for (MyVertex myVertex : graph.vertexSet()) {
			cities.add(myVertex);
			nearestNeighbor(solutions, cities, graph);
			cities.remove(myVertex);
			getMin(solutions, graph);
			System.out.println("Finished checking "+myVertex.toString()+" as start.");
		}
		
		for (List<MyVertex> solution : solutions) {
			double cost = 0;
			for(int i=0; i<solution.size(); i++) {
				cost += solution.get(i).cost;
				if(i<(solution.size()-1)) {
					cost += graph.getEdge(solution.get(i), solution.get(i+1)).getdWeight();
				}
				System.out.print(solution.get(i).toString());
			}
			System.out.print(" ("+String.valueOf((int)cost)+")");
			System.out.println();
		}
		
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
	
	public static void getMin(List<List<MyVertex>> solutions, SimpleWeightedGraph<MyVertex, MyWeightedEdge> graph) {
		List<MyVertex> minV = null;
		double minCost = 20000;
		for(int i=0; i<solutions.size(); i++) {
			List<MyVertex> solution = solutions.get(i);
			double cost = 0;
			for(int j=0; j<solution.size(); j++) {
				cost += solution.get(j).cost;
				if(j<(solution.size()-1)) {
					cost += graph.getEdge(solution.get(j), solution.get(j+1)).getdWeight();
				}
			}
			if(cost < minCost) {
				minV = solution;
				minCost = cost;
			}
		}
		
		solutions.clear();
		solutions.add(minV);
	}
	
	public static void nearestNeighbor(List<List<MyVertex>> solutions, List<MyVertex> cities, SimpleWeightedGraph<MyVertex, MyWeightedEdge> graph) {
		if(cities.size() == graph.vertexSet().size()) {
			solutions.add(new ArrayList<MyVertex>(cities));
			return;
		}
		
		/*Set inputSet = new HashSet(inputList);
        if(inputSet.size()< inputList.size())
            return true;
        }*/
		Set<MyVertex> du = new HashSet<MyVertex>(cities);
		if(du.size() < cities.size())
			return;
		
		//for (MyVertex myVertex : graph.vertexSet()) {
			//if(!cities.contains(myVertex)) {
				//cities.add(myVertex);
				ArrayList<MyWeightedEdge> edges = new ArrayList<MyWeightedEdge>(graph.edgesOf(cities.get(cities.size()-1)));
				Collections.sort(edges);
				for (MyWeightedEdge myWeightedEdge : edges) {
					if(!cities.contains(myWeightedEdge.getV2())) {
						cities.add(myWeightedEdge.getV2());
						nearestNeighbor(solutions, cities, graph);
						cities.remove(myWeightedEdge.getV2());
					}
					if(!cities.contains(myWeightedEdge.getV1())) {
						cities.add(myWeightedEdge.getV1());
						nearestNeighbor(solutions, cities, graph);
						cities.remove(myWeightedEdge.getV1());
					}
				}
			//}
		//}		
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
