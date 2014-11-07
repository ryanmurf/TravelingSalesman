package algorithms.designProblem3;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import org.jgrapht.graph.SimpleWeightedGraph;

public class SimulatedAnnealing {
	double HIGH_TEMP;
	double LOW_TEMP;
	double COOLING_FACTOR;
	double k;
	
	double temperature;
	double verticesCost;
	int nIterations;
	Random rand = new Random();
	
	List<MyVertex> solution = new ArrayList<MyVertex>();
	SimpleWeightedGraph<MyVertex, MyWeightedEdge> graph;
	
	public SimulatedAnnealing(SimpleWeightedGraph<MyVertex, MyWeightedEdge> graph, double verticesCost) {
		this.graph = graph;
		this.verticesCost = verticesCost;
		HIGH_TEMP = 10000.0;
		LOW_TEMP = .00001;
		COOLING_FACTOR = .89;
		k = 1;
		nIterations = 100;
		anneal();
	}
	
	public void anneal() {
		List<MyVertex> state = new ArrayList<MyVertex>();
		randomSolution(state);
		double energy = getCost(state);
		temperature = HIGH_TEMP;
		
		while(temperature > LOW_TEMP) {
			for(int i=0; i<nIterations; i++) {
				List<MyVertex> newState = neighbor(state);
				double newEnergy = getCost(newState);
				
				if(newEnergy < energy || Math.exp((energy-newEnergy)/(k*temperature)) >= rand.nextDouble() ) {
					state = newState;
					energy = newEnergy;
				}
			}
			//System.out.println("Energy : "+energy);
			temperature *= COOLING_FACTOR;
		}
		solution = new ArrayList<MyVertex>(state);
		printSolutions();
	}
	
	private void printSolutions() {
		double cost = 0;
		for (int i = 0; i < solution.size(); i++) {
			System.out.print(solution.get(i).toString());
		}
		cost += verticesCost;
		cost += getCost(solution);
		System.out.print(" (" + String.valueOf((int) cost) + ")\n");
		System.out.println("");
	}
	
	//Normally we would swap 2 spots. The graph is sparsely connected
	//so lets instead generate a new solution with a partial of the original
	private List<MyVertex> neighbor(List<MyVertex> state) {
		List<MyVertex> newState = new ArrayList<MyVertex>(state);
		
		int cut = rand.nextInt((int) rand.nextGaussian()*(1/3 * state.size())+(state.size()/2));
		for(int i=(state.size()-1); i>cut; i--) {
			newState.remove(i);
		}
		randomSolution(newState);
		
		return newState;
	}
	
	public double getCost(List<MyVertex> solution) {
		double cost = 0;
		for (int j = 0; j < solution.size(); j++) {
			if (j < (solution.size() - 1)) {
				cost += graph.getEdge(solution.get(j), solution.get(j + 1)).getdWeight();
			}
		}
		return (cost);
	}
	
	//This will return a random solution. One visit of each city
	public boolean randomSolution(List<MyVertex> state) {
		if(state.size() == 0) {
			//Set a random vertex as first city
			List<MyVertex> r = new ArrayList<MyVertex>(graph.vertexSet());
			Collections.shuffle(r);
			state.add(r.get(0));
			randomSolution(state);
		}
		if(state.size() == graph.vertexSet().size()) {
			return true;
		}
		
		ArrayList<MyWeightedEdge> edges = new ArrayList<MyWeightedEdge>(graph.edgesOf(state.get(state.size() - 1)));
		Collections.shuffle(edges);
		for (MyWeightedEdge myWeightedEdge : edges) {
			if (!state.contains(myWeightedEdge.getV2())) {
				state.add(myWeightedEdge.getV2());
				if(randomSolution(state))
					return true;
				else
					state.remove(myWeightedEdge.getV2());
			}
			if (!state.contains(myWeightedEdge.getV1())) {
				state.add(myWeightedEdge.getV1());
				if(randomSolution(state))
					return true;
				else
					state.remove(myWeightedEdge.getV1());
			}
		}
		return false;
	}
}
