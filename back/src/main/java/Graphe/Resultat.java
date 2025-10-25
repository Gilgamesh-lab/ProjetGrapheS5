package Graphe;


public class Resultat {
	private Graphe graphe;
	private String chemin;
	private int poids;
	
	
	public Resultat(Graphe graphe, String chemin) {
		this.graphe = graphe;
		this.chemin = chemin;
	}
	
	public Resultat() {
		
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
