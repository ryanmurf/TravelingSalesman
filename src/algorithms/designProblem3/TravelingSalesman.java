package algorithms.designProblem3;

import java.util.Collection;

import javax.swing.JFrame;
import javax.swing.JScrollPane;

import org.jgraph.JGraph;
import org.jgrapht.ListenableGraph;
import org.jgrapht.WeightedGraph;
import org.jgrapht.alg.KruskalMinimumSpanningTree;
import org.jgrapht.alg.PrimMinimumSpanningTree;
import org.jgrapht.alg.interfaces.MinimumSpanningTree;
import org.jgrapht.ext.JGraphModelAdapter;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.ListenableUndirectedWeightedGraph;
import org.jgrapht.graph.SimpleWeightedGraph;

import com.jgraph.layout.JGraphFacade;
import com.jgraph.layout.organic.JGraphSelfOrganizingOrganicLayout;

public class TravelingSalesman {

	public static void main(String[] args) {
		Input in = new Input(args[0]);
		System.out.println("All done.");

		SimpleWeightedGraph<String, MyWeightedEdge> graph = new SimpleWeightedGraph<String, MyWeightedEdge>(
				MyWeightedEdge.class);

		int[][] matrix = in.getAdjMatrix();
		String[] names = in.getNames();

		for (int i = 0; i < names.length; i++)
			graph.addVertex(names[i]);

		for (int i = 0; i < matrix.length; i++) {
			for (int j = 0; j < matrix[i].length; j++) {
				if (i != j && matrix[i][j] != 0 && matrix[i][j] != -1) {
					MyWeightedEdge e = graph.addEdge(names[i], names[j]);
					double weight = 0;
					if (matrix[i][j] == -1)
						weight = 500;
					else
						weight = (double) matrix[i][j];
					graph.setEdgeWeight(e, weight);
				}
			}
		}

		displayGraph(graph);
		
		PrimMinimumSpanningTree<String, MyWeightedEdge> p = new PrimMinimumSpanningTree<String, MyWeightedEdge>(
				graph);
		System.out.println(p.getMinimumSpanningTreeEdgeSet().toString());
		System.out.println(p.getMinimumSpanningTreeTotalWeight());
		
		displayGraph(p, names);

		KruskalMinimumSpanningTree<String, MyWeightedEdge> k = new KruskalMinimumSpanningTree<String, MyWeightedEdge>(
				graph);
		System.out.println(k.getMinimumSpanningTreeEdgeSet().toString());
		System.out.println(k.getMinimumSpanningTreeTotalWeight());
	}
	
	public static void displayGraph(SimpleWeightedGraph<String, MyWeightedEdge> graph) {
		ListenableGraph<String, MyWeightedEdge> g = new ListenableUndirectedWeightedGraph<String, MyWeightedEdge>(graph);
		JGraph jgraph = new JGraph(new JGraphModelAdapter<String, MyWeightedEdge>(g));

		JScrollPane scroller = new JScrollPane(jgraph);
		JFrame frame = new JFrame("The Body");
		frame.setSize(600, 600);
		frame.add(scroller);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		jgraph.getGraphLayoutCache().reload();
		jgraph.repaint();
	}
	
	public static void displayGraph(MinimumSpanningTree<String, MyWeightedEdge> tree, String[] names) {
		SimpleWeightedGraph<String, MyWeightedEdge> graph = new SimpleWeightedGraph<String, MyWeightedEdge>(MyWeightedEdge.class);
		
		for (int i = 0; i < names.length; i++)
			graph.addVertex(names[i]);
		
		Collection<MyWeightedEdge> edges = tree.getMinimumSpanningTreeEdgeSet();
		
		for (MyWeightedEdge myWeightedEdge : edges) {
			MyWeightedEdge e = graph.addEdge(myWeightedEdge.getV1(), myWeightedEdge.getV2());
			graph.setEdgeWeight(e, myWeightedEdge.getdWeight());
		}
		
		ListenableGraph<String, MyWeightedEdge> g = new ListenableUndirectedWeightedGraph<String, MyWeightedEdge>(graph);
		JGraph jgraph = new JGraph(new JGraphModelAdapter<String, MyWeightedEdge>(g));

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
