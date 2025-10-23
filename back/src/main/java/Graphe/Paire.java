package Graphe;

public class Paire {
	int poids;
	Sommet sommetS;
	Sommet sommetD;
	
	
	
	public Paire(int poids, Sommet sommetS, Sommet sommetD) {
		this.poids = poids;
		this.sommetS = sommetS;
		this.sommetD = sommetD;
	}
	
	public int getPoids() {
		return poids;
	}
	public void setPoids(int poids) {
		this.poids = poids;
	}
	
	

	public Sommet getSommetS() {
		return sommetS;
	}

	public void setSommetS(Sommet sommetS) {
		this.sommetS = sommetS;
	}

	public Sommet getSommetD() {
		return sommetD;
	}

	public void setSommetD(Sommet sommetD) {
		this.sommetD = sommetD;
	}

	@Override
	public String toString() {
		return "(" + sommetS.getNom() + ", " + poids + ")";
	}

	
	
	
	
	

}
