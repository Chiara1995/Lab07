package it.polito.tdp.dizionario.model;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.jgrapht.UndirectedGraph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;
import org.jgrapht.traverse.BreadthFirstIterator;

import it.polito.tdp.dizionario.db.WordDAO;

public class Model {
	
	private UndirectedGraph<String,DefaultEdge> grafo;

	/*
	 * Creazione grafo non orientato, semplice e non pesato
	 */
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
			//versione 1: utilizzo DB (ma lento!)
			List<String> archi=new ArrayList<String>();
			archi=wdao.getAllSimilarWords(s, numeroLettere);
			for(int j=0; j<archi.size(); j++)
				grafo.addEdge(s, archi.get(j));
			
			//versione 2: utilizzo algoritmo Java
		}
		return vertici;
	}

	/*
	 * Restituisce i vertici adiacenti a quello passato come parametro
	 */
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

	/*
	 * Ricerca del vertice con grado massimo 
	 */
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
	
	/*
	 * Ricerca della parola (vertice del grafo) all'interno del dizionario  
	 */
	public boolean parolaPresente(String parola){
		if(grafo.containsVertex(parola))
			return true;
		else
			return false;
	}
	
	/*
	 * Ricerca di tutti i nodi raggiungibili nel grafo a partire dal vertice selezionato
	 * Breadth First Visit (BFS)
	 * libreria JGraphT
	 */
	public List<String> trovaTuttiVicini(String parola){
		List<String> ltemp=new LinkedList<String>();
		BreadthFirstIterator<String, DefaultEdge> bfv=new BreadthFirstIterator<>(grafo, parola);
		while(bfv.hasNext()){
			ltemp.add(bfv.next());
		}
		return ltemp;
	}

	/*
	 * VERSIONE ITERATIVA
	public List<String> displayAllNeighboursIterative(String parolaInserita) {
		// Creo due liste: quella dei nodi visitati ..
		List<String> visited = new LinkedList<String>();
		// .. e quella dei nodi da visitare
		List<String> toBeVisited = new LinkedList<String>();
		// Aggiungo alla lista dei vertici visitati il nodo di partenza.
		visited.add(parolaInserita);
		// Aggiungo ai vertici da visitare tutti i vertici collegati a quello inserito
		toBeVisited.addAll(Graphs.neighborListOf(graph, parolaInserita));
		while (!toBeVisited.isEmpty()) {
			// Rimuovi il vertice in testa alla coda
			String temp = toBeVisited.remove(0);
			// Aggiungi il nodo alla lista di quelli visitati
			visited.add(temp);
			// Ottieni tutti i vicini di un nodo
			List<String> listaDeiVicini = Graphs.neighborListOf(graph, temp);
			// Rimuovi da questa lista tutti quelli che hai già visitato..
			listaDeiVicini.removeAll(visited);
			// .. e quelli che sai già che devi visitare.
			listaDeiVicini.removeAll(toBeVisited);
			// Aggiungi i rimanenenti alla coda di quelli che devi visitare.
			toBeVisited.addAll(listaDeiVicini);
		}
		// Ritorna la lista di tutti i nodi raggiungibili
		return visited;
		}
	 	*/
	
}
