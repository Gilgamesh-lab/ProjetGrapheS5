package Graphe;

import java.util.HashMap;

public class Resultat { // classe pour stocker les différents résultats des algorithmes sous un même type
	private Graphe graphe;
	private String chemin;
	private Long poids;
	private HashMap<String, HashMap<String, Long>> matriceDistance;
	private HashMap<String, HashMap<String, Sommet>> matricePere;
	private HashMap<String, Long> matriceDistanceBF;
	private HashMap<String, Sommet> matricePereBF;
	
	
	public Resultat(Graphe graphe, String chemin) {
		this.graphe = graphe;
		this.chemin = chemin;
	}
	
	public Resultat() {
		
	}
	
	public HashMap<String, HashMap<String, Long>> getMatriceDistance() {
		return matriceDistance;
	}

	public void setMatriceDistance(HashMap<String,HashMap<String,Long>> w) {
		this.matriceDistance = w;
	}

	public HashMap<String, HashMap<String, Sommet>> getMatricePere() {
        System.out.println("DEBUG: matrice pere : " + matricePere);
        return matricePere;
	}

	public void setMatricePere(HashMap<String, HashMap<String, Sommet>> matricePere) {
		this.matricePere = matricePere;
	}

	public Long getPoids() {
		return poids;
	}

	public void setPoids(Long long1) {
		this.poids = long1;
	}

	public Graphe getGraphe() {
		return graphe;
	}
	
	
	

	public HashMap<String, Long> getMatriceDistanceBF() {
		return matriceDistanceBF;
	}

	public void setMatriceDistanceBF(HashMap<String, Long> matriceDistanceBF) {
		this.matriceDistanceBF = matriceDistanceBF;
	}

	public HashMap<String, Sommet> getMatricePereBF() {
		return matricePereBF;
	}

	public void setMatricePereBF(HashMap<String, Sommet> matricePereBF) {
		this.matricePereBF = matricePereBF;
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
