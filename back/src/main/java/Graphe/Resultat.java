package Graphe;

import java.util.HashMap;

public class Resultat {
	private Graphe graphe;
	private String chemin;
	private int poids;
	private HashMap<String, HashMap<String, Integer>> matriceDistance;
	private HashMap<String, HashMap<String, Sommet>> matricePere;
	
	
	public Resultat(Graphe graphe, String chemin) {
		this.graphe = graphe;
		this.chemin = chemin;
	}
	
	public Resultat() {
		
	}
	
	
	
	


	public HashMap<String, HashMap<String, Integer>> getMatriceDistance() {
		return matriceDistance;
	}

	public void setMatriceDistance(HashMap<String, HashMap<String, Integer>> matriceDistance) {
		this.matriceDistance = matriceDistance;
	}

	public HashMap<String, HashMap<String, Sommet>> getMatricePere() {
		return matricePere;
	}

	public void setMatricePere(HashMap<String, HashMap<String, Sommet>> matricePere) {
		this.matricePere = matricePere;
	}

	public int getPoids() {
		return poids;
	}

	public void setPoids(int poids) {
		this.poids = poids;
	}

	public Graphe getGraphe() {
		return graphe;
	}


	public void setGraphe(Graphe graphe) {
		this.graphe = graphe;
	}


	public String getChemin() {
		return chemin;
	}


	public void setChemin(String chemin) {
		this.chemin = chemin;
	}
	
	
	
	

}
