package it.polito.tdp.genes.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;

import it.polito.tdp.genes.db.GenesDao;

public class Model {

	private Graph<Integer,DefaultWeightedEdge> grafo;
	private int narchi;
	private int nvertici;
	private double max;
	private GenesDao dao=new GenesDao();
	private double min;
	private List<Integer> best;
	private Map<Integer,Integer > m;
	public int getNarchi() {
		return narchi;
	}
	public int getNvertici() {
		return nvertici;
	}
	public List<Integer> getchromo(){
		return dao.getAllchromo();
	}
	public List<Genes> getgenes(){
		return dao.getAllGenes();
	}
	public double getpeso(int i,int o) {
		Double z=0.0;
		List<Double> l=new ArrayList<Double>(dao.getAllpesi(i, o));
		for(Double u:l) {
			z=z+u;
		}
		return z;
	}
	
	public double getMax() {
		return max;
	}
	

	public void creagrafo(){
		grafo=new SimpleDirectedWeightedGraph<Integer,DefaultWeightedEdge>(DefaultWeightedEdge.class);
		Graphs.addAllVertices(grafo, getchromo());
		this.max=0;
		this.min=1000000;
		List<Integer> lista=new ArrayList<Integer>(getchromo());
		for(Integer g1:lista) {
			for(Integer g2:lista) {
				if(grafo.getEdge(g2, g1)==null && g1!=g2 ) {
					
						grafo.addEdge(g1, g2);
						grafo.setEdgeWeight(g1, g2,getpeso(g1, g2));
						if(getpeso(g1, g2)>max) {
							this.max=getpeso(g1, g2);
						}
						if(getpeso(g1, g2)<min) {
							this.min=getpeso(g1, g2);
						}
					
				}	
			}
		}
		this.narchi=grafo.edgeSet().size();
		this.nvertici=grafo.vertexSet().size();
	}
	public int countarchi(double soglia) {
		int count=0;
		for(DefaultWeightedEdge e: grafo.edgeSet()) {
			if(grafo.getEdgeWeight(e)>soglia) {
				count++;
			}
		}
		return count;
	}
	public double getMin() {
		
		return this.min;
	}
	
	public double gettot(List<Integer> li) {
		double x=0;
		for(int i=0;i<li.size()-1;i++) {
			if(x==0) {
				x=x+grafo.getEdgeWeight(grafo.getEdge( li.get(i),li.get(1+i)));
			}else {
				x=x+grafo.getEdgeWeight(grafo.getEdge( li.get(i),li.get(1+i)));
			}
		}
		return x; 
	}
	public List<Integer> getpercorsomax(double soglia){
		m= new HashMap<Integer,Integer>();
		best=new LinkedList<>();
		List<Integer> parziale =new LinkedList<>();
		cerca(parziale,soglia);
		
		return this.best;
	}
	


	private void cerca(List<Integer> parziale,double soglia) {
		// condizione terminazione
		if(gettot(parziale)> gettot(best) ) {
			
			best=new ArrayList<Integer>(parziale);
			System.out.println("size "+best.size());
		}
			
		// scorro i vicini dell'ultimo inserito ed esploro
		
		for(Integer v:grafo.vertexSet()) {
			 	if(parziale.size()==0) {
			 		parziale.add(v);
			 		cerca(parziale,soglia);
			 	}else {
				 // evito cicli
				 if(grafo.getEdge(parziale.get(parziale.size()-1), v)!= null) {
					if(grafo.getEdgeWeight(grafo.getEdge(parziale.get(parziale.size()-1),v))>soglia) {
					 
					 parziale.add(v);
					 cerca(parziale,soglia);
					
					 parziale.remove(parziale.size()-1);
					 
				 
				}
			 } 
		 }
		}
		
	}
	
	
	
}