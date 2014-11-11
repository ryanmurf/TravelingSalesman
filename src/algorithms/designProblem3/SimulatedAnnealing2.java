package algorithms.designProblem3;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Random;

import org.jgrapht.graph.SimpleWeightedGraph;

import com.sun.org.apache.bcel.internal.generic.NEW;

public class SimulatedAnnealing2 {

	double HIGH_TEMP;
	double LOW_TEMP;
	double COOLING_FACTOR;
	double k;
	
	double temperature;
	double verticesCost;
	int nIterations;
	int nVertices;
	Random rand = new Random();
	
	List<MyVertex> solution = new ArrayList<MyVertex>();
	SimpleWeightedGraph<MyVertex, MyWeightedEdge> graph;
	
	public SimulatedAnnealing2(SimpleWeightedGraph<MyVertex, MyWeightedEdge> graph, double verticesCost) {
		this.graph = graph;
		this.verticesCost = verticesCost;
		this.nVertices = graph.vertexSet().size();
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
		List<MyVertex> minSolution = new ArrayList<MyVertex>(state);
		double minCost = energy;
		temperature = HIGH_TEMP;
		ExecutionTimer timer = new ExecutionTimer();
		while(temperature > LOW_TEMP) {
			for(int i=0; i<nIterations; i++) {
				List<MyVertex> newState = neighbor(state);
				double newEnergy = getCost(newState);
				
				if(newEnergy < energy || Math.exp((energy-newEnergy)/(k*temperature)) >= rand.nextDouble() ) {
					state = newState;
					energy = newEnergy;
					
					if(newEnergy < minCost) {
						minCost = newEnergy;
						minSolution = new ArrayList<MyVertex>(newState);
					}
				}
			}
			//System.out.println("Energy : "+energy+" min cost found so far "+minCost);
			temperature *= COOLING_FACTOR;
		}
		timer.end();
		System.out.println("Found solution in "+String.valueOf(((double) timer.duration()) / 1000));
		if(minCost < getCost(state))
			solution = new ArrayList<MyVertex>(minSolution);
		else
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
		/*List<MyVertex> newState = new ArrayList<MyVertex>();
		
		int cut = (int) (rand.nextGaussian()*(4.0)+(((double)nVertices / (double)state.size()) * (double)nVertices));
		cut = Math.abs(cut);
		if(cut >= (state.size()-1))
			cut = state.size()-2;
		
		for(int i=0; i<cut; i++) {
			newState.add(state.get(i));
		}
		randomSolution(newState);
		
		return newState;*/
		
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
		
		//if(!graph.vertexSet().equals(new HashSet<MyVertex>(solution)))
		//	return 10000;
		
		for (int j = 0; j < solution.size() - 1; j++) {
			MyWeightedEdge edge = graph.getEdge(solution.get(j), solution.get(j + 1));
			if(edge != null)
				cost += edge.getdWeight();
			else
				return 10000;
		}
		return (cost);
	}
	public void randomSolution(List<MyVertex> state) {
		if(nVertices < 13)
			randomSolution1(state);
		else
			randomSolution2(state);
	}
	//This will return a random solution. One visit of each city
	public boolean randomSolution1(List<MyVertex> state) {
		if(state.size() == 0) {
			//Set a random vertex as first city
			state.add(graph.vertexSet().iterator().next());
			randomSolution(state);
		}
		//end if we have all the vertices
		if(graph.vertexSet().equals(new HashSet<MyVertex>(state))) {
			return true;
		}
		
		//if(state.get(0) != graph.vertexSet().iterator().next())
		//	System.out.println("ahh");
		
		ArrayList<MyWeightedEdge> edges = new ArrayList<MyWeightedEdge>(graph.edgesOf(state.get(state.size() - 1)));
		Collections.shuffle(edges);
		for (MyWeightedEdge myWeightedEdge : edges) {
			if (state.get(state.size()-1) != myWeightedEdge.getV2()) {//!state.contains(myWeightedEdge.getV2())) {
				//if(state.contains(myWeightedEdge.getV2()))
					//if(rand.nextDouble() > .5)
						//continue;
				state.add(myWeightedEdge.getV2());
				if(randomSolution1(state))
					return true;
				else
					state.remove(myWeightedEdge.getV2());
			} else {//!state.contains(myWeightedEdge.getV1())) {
				//if(state.contains(myWeightedEdge.getV1()))
					//if(rand.nextDouble() > .5)
						//continue;
				state.add(myWeightedEdge.getV1());
				if(randomSolution1(state))
					return true;
				else
					state.remove(myWeightedEdge.getV1());
			}
		}
		return false;
	}
	
	public boolean randomSolution2(List<MyVertex> state) {
		if(state.size() == 0) {
			//Set a random vertex as first city
			//List<MyVertex> r = new ArrayList<MyVertex>(graph.vertexSet());
			//Collections.shuffle(r);
			state.add(graph.vertexSet().iterator().next());
			randomSolution(state);
			return true;
		}
		if(state.size() == graph.vertexSet().size()) {
			return true;
		}
		
		ArrayList<MyWeightedEdge> edges = new ArrayList<MyWeightedEdge>(graph.edgesOf(state.get(state.size() - 1)));
		Collections.shuffle(edges);
		for (MyWeightedEdge myWeightedEdge : edges) {
			if (!state.contains(myWeightedEdge.getV2())) {
				state.add(myWeightedEdge.getV2());
				if(randomSolution2(state))
					return true;
				else
					state.remove(myWeightedEdge.getV2());
			}
			if (!state.contains(myWeightedEdge.getV1())) {
				state.add(myWeightedEdge.getV1());
				if(randomSolution2(state))
					return true;
				else
					state.remove(myWeightedEdge.getV1());
			}
		}
		return false;
	}
}
