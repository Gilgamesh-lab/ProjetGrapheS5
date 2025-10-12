package SpringBot.GrapheDeSortie;


public class AreteDeSortie {
	private int id;
	private static int compteur = 1;
    private String source;
    private String destination;
    private boolean estOrienter;
    private Integer poids;
    
    
	public AreteDeSortie (SommetDeSortie source, SommetDeSortie destination, boolean estOrienter, Integer poids) {
		this.id = compteur;
		compteur++;
		this.source = source.getNom();
		this.destination = destination.getNom();
		this.estOrienter = estOrienter;
		this.poids = poids;
		
		destination.ajouterAreteSortie(this);
		source.ajouterAreteSortie(this);
	}
	
	
	
	


	public static int getCompteur() {
		return compteur;
	}






	public static void setCompteur(int compteur) {
		AreteDeSortie.compteur = compteur;
	}






	public int getId() {
		return id;
	}




	public void setId(int id) {
		this.id = id;
	}




	public boolean isEstOrienter() {
		return estOrienter;
	}
	
	


	public String getSource() {
		return source;
	}




	public void setSource(String source) {
		this.source = source;
	}


	public String getDestination() {
		return destination;
	}


	public void setDestination(String destination) {
		this.destination = destination;
	}


	public boolean EstOrienter() {
		return estOrienter;
	}


	public void setEstOrienter(boolean estOrienter) {
		this.estOrienter = estOrienter;
	}


	public Integer getPoids() {
		return poids;
	}


	public void setPoids(Integer poids) {
		this.poids = poids;
	}

	public void afficher() {
		if(this.estOrienter) {
			System.out.println(this.source+  "->" + this.destination);
		}
		else {
			System.out.println(this.source +  "-" + this.destination);
		}
	}
	
			

}
