package it.polito.tdp.genes.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.alg.connectivity.ConnectivityInspector;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.genes.db.GenesDao;

public class Model {
	
	private GenesDao dao;
	private Graph<String ,DefaultWeightedEdge> grafo;
	private Map<String, Classification> idMap;
	List<Classification> listaClass; 
	List<String> best;
	
	public Model() {
		this.dao= new GenesDao();
		this.idMap= new HashMap<String, Classification>();
		this.listaClass= new ArrayList<Classification>(this.dao.getClassifications(idMap));
	}
	
	public void creaGrafo() {
		
		this.grafo= new SimpleWeightedGraph<String, DefaultWeightedEdge>(DefaultWeightedEdge.class);
		Graphs.addAllVertices(this.grafo, this.dao.getVertici());
		for(String s1: this.dao.getVertici()) {
			for(String s2: this.dao.getVertici()) {
			if(!this.grafo.containsEdge(s1, s2) && s1.compareTo(s2)!=0) {
				Arco a=this.dao.getArco(s1, s2);
				if(a!=null) {
					Graphs.addEdgeWithVertices(this.grafo, s1, s2, a.getPeso());
				}
			}
			}
		}
	}
	
	public String nVertici() {
		return "Grafo creato!"+"\n"+"#verici: "+ this.grafo.vertexSet().size()+"\n";
	}
	
	public String nArchi() {
		return "#archi: "+ this.grafo.edgeSet().size()+"\n";
	}
	
	public List<String> getLocalizzazioni(){
		return this.dao.getVertici();
	}
	
	public String statistiche(String localizzazione) {
		String s="";
		List<Arco> list= new ArrayList<>();
		for(DefaultWeightedEdge e: this.grafo.edgesOf(localizzazione)) {
			String ss=Graphs.getOppositeVertex(this.grafo, e, localizzazione);
			Arco a= new Arco(localizzazione, ss,(int) this.grafo.getEdgeWeight(e));
			list.add(a);
		}
		
		for(Arco ai: list) {
			s+=ai.getInteraction2()+"     "+ai.getPeso()+"\n";
		}
			
		
		
		return "Adiacenti a: "+localizzazione+"\n\n"+s;
	}
	
	public List<String> camminoBest(String localizzazione){
		this.best= new ArrayList<String>();
		this.best.add(localizzazione);
		int peso=0;
		int pesop=0;
		List<String>parziale= new ArrayList<String>();
		parziale.add(localizzazione);
		cerca(parziale, peso, pesop);
		
		return best;
		
	}

	private void cerca(List<String> parziale,  int peso, int pesop) {
		if(pesop>peso) {
			peso=pesop;
			this.best= new ArrayList<String>(parziale);
			return;
		}
		for(String si: Graphs.neighborListOf(this.grafo, parziale.get(parziale.size()-1))) {
			if(!parziale.contains(si)) {
				parziale.add(si);
				DefaultWeightedEdge e= this.grafo.getEdge(parziale.get(parziale.size()-2), si);
				if(e!=null) {
				pesop+=this.grafo.getEdgeWeight(e);
				cerca(parziale, peso, pesop);
				pesop-=this.grafo.getEdgeWeight(e);
				}
			}
		}
		
		
	}

}