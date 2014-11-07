package algorithms.designProblem3;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.jgrapht.graph.SimpleWeightedGraph;

public class Input {
	private Path filePath;
	private String fileName;
	private int nCities;
	private int[][] adjMatrix;
	private List<MyVertex> vertices;
	public double vertexCost = 0;
	
	public Input(String filePath) {
		this.filePath = Paths.get(filePath);
		if(Files.notExists(this.filePath)) {
			System.out.println("File does not exist. File: "+filePath);
			System.exit(1);
		}
		fileName = this.filePath.getFileName().toString();
		read();
	}
	
	private void read() {
		List<String> lines = null;
		try {
			 lines = Files.readAllLines(this.filePath, Charset.defaultCharset());
		} catch (IOException e) {
			System.out.println("File: "+this.fileName+" could not be opened.");
			System.exit(1);
			e.printStackTrace();
		}
		
		nCities = Integer.valueOf(lines.get(0).split("[ \t]+")[0]);
		lines.remove(0);
		String[] names = lines.get(0).trim().split("[ \t]+");
		vertices = new ArrayList<MyVertex>(names.length);
		for(int i=0; i<names.length; i++) {
			MyVertex temp = new MyVertex(names[i], 0.0);
			vertices.add(temp);
		}
		lines.remove(0);
		adjMatrix = new int[nCities][nCities];
		int i=0;
		for (String line : lines) {
			line = line.trim();
			line = line.replaceFirst(names[i]+" ", "");
			String[] values = line.split("[ \t]+");
			for(int j=0; j<values.length; j++) {
				if(values[j].compareTo("-")==0)
					adjMatrix[i][j] = -1;
				else
					adjMatrix[i][j] = Integer.valueOf(values[j]);
			}
			i++;
		}
	}
	
	public int[][] getAdjMatrix() {
		return this.adjMatrix;
	}
	
	public List<MyVertex> getVertices() {
		return this.vertices;
	}
	
	public SimpleWeightedGraph<MyVertex, MyWeightedEdge> getGraph() {
		SimpleWeightedGraph<MyVertex, MyWeightedEdge> graph = new SimpleWeightedGraph<MyVertex, MyWeightedEdge>(
				MyWeightedEdge.class);
				
		while(reduceSingleEdges());
		
		for (int i = 0; i < vertices.size(); i++)
			if(vertices.get(i).name.compareTo("") != 0)
				graph.addVertex(vertices.get(i));

		for (int i = 0; i < adjMatrix.length; i++) {
			for (int j = 0; j < adjMatrix[i].length; j++) {
				if (i != j && adjMatrix[i][j] != 0 && adjMatrix[i][j] != -1) {
					MyWeightedEdge e = graph.addEdge(vertices.get(i), vertices.get(j));
					double weight = 0;
					weight = (double) adjMatrix[i][j];
					graph.setEdgeWeight(e, weight);
				}
			}
		}
		return graph;
	}
	
	private boolean reduceSingleEdges() {
		boolean foundOne = false;
		
		for (int i = 0; i < vertices.size(); i++) {
			int edges = 0;
			int index = 0;
			for (int j = 0; j < adjMatrix[i].length; j++) {
				if (i != j) {
					if (adjMatrix[i][j] != 0 && adjMatrix[i][j] != -1) {
						edges++;
						index = j;
					}
					if(adjMatrix[j][i] != 0 && adjMatrix[j][i] != -1) {
						edges++;
						index = j;
					}
				}
			}
			if(edges == 1) {
				foundOne = true;
				vertices.get(index).name += vertices.get(i).name + vertices.get(index).name;
				vertices.get(i).name = "";
				vertexCost += (double) adjMatrix[i][index]*2;
				adjMatrix[i][index] = 0;
			}
		}
		
		return foundOne;
	}
}
