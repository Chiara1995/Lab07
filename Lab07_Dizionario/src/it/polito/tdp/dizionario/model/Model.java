package it.polito.tdp.dizionario.model;

import java.util.ArrayList;
import java.util.List;

import org.jgrapht.UndirectedGraph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;

import it.polito.tdp.dizionario.db.WordDAO;

public class Model {
	
	private UndirectedGraph<String,DefaultEdge> grafo;

	public List<String> createGraph(int numeroLettere) {
		
		//Creo grafo non orientato
		grafo=new SimpleGraph<String,DefaultEdge>(DefaultEdge.class);
		WordDAO wdao=new WordDAO();
		List<String> vertici=new ArrayList<String>();
		vertici=wdao.getAllWordsFixedLength(numeroLettere);
		
		//Aggiungo vertici
		for(int k=0; k<vertici.size(); k++){
			grafo.addVertex(vertici.get(k));
		}

		//Aggiungo archi
		for(String s : grafo.vertexSet()){
			List<String> archi=new ArrayList<String>();
			archi=wdao.getAllSimilarWords(s, numeroLettere);
			for(int j=0; j<archi.size(); j++)
				grafo.addEdge(s, archi.get(j));
		}
		return vertici;
	}

	public List<String> displayNeighbours(String parolaInserita) {
		
		List<String> neighbours=new ArrayList<String>();

		for(DefaultEdge e : grafo.edgesOf(parolaInserita)){
			if(grafo.getEdgeSource(e).equals(parolaInserita))
				neighbours.add(grafo.getEdgeTarget(e));
			else
				neighbours.add(grafo.getEdgeSource(e));
		}
		return neighbours;
	}

	public String findMaxDegree() {
		int maxDegree=0;
		String vertex="";
		for(String s : grafo.vertexSet()){
			if(grafo.degreeOf(s)>maxDegree){
				maxDegree=grafo.degreeOf(s);
				vertex=s;
			}
		}
		String result="Grado massimo "+maxDegree+" relativo al vertice "+vertex+" con diretti vicini "+this.displayNeighbours(vertex);
		return result;
	}
	
	public boolean parolaPresente(String parola){
		if(grafo.containsVertex(parola))
			return true;
		else
			return false;
	}
	
}
