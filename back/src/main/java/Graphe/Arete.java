package Graphe;
import java.util.Objects;

public class Arete {
	private int id;
	private static int compteur = 1;
    private Sommet source;
    private Sommet destination;
    private boolean estOrienter;
    private Integer poids;
    
    /**
     * Créer une arete à partir des informations fournies et lie celle-ci à son sommet de départ et à son sommet de destination
     * @param source : son sommet de départ
     * @param destination : son sommet de destination
     * @param estOrienter : determine si l'arete est orienter ou pas
     * @param poids : poids de l'arete
     */
	public Arete(Sommet source, Sommet destination, boolean estOrienter, Integer poids) {
		this.id = compteur;
		compteur++;
		this.source = source;
		this.destination = destination;
		this.estOrienter = estOrienter;
		this.poids = poids;
		this.destination.ajouterArete(this);
		this.source.ajouterArete(this);
	}
	
	/**
	 * Créer une arete à partir des informations fournies et lie celle-ci à son sommet de départ et à son sommet de destination
	 * @param source : son sommet de départ
	 * @param destination : son sommet de destination
	 */
	public Arete(Sommet source, Sommet destination) {
		this.id = compteur;
		compteur++;
		this.source = source;
		this.destination = destination;
		this.estOrienter = false;
		this.poids = 0;
		this.destination.ajouterArete(this);
		this.source.ajouterArete(this);
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

	public Sommet getSource() {
		return source;
	}

	public void setSource(Sommet source) {
		this.source = source;
	}

	public Sommet getDestination() {
		return destination;
	}

	public void setDestination(Sommet destination) {
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
			System.out.println(this.source.getNom() +  "->" + this.destination.getNom());
		}
		else {
			System.out.println(this.source.getNom() +  "-" + this.destination.getNom());
		}
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Arete other = (Arete) obj;
		return id == other.id;
	}

	@Override
	public String toString() {
		return "Arete [id=" + id + ", source=" + source.getNom() + ", destination=" + destination.getNom() + ", estOrienter="
				+ estOrienter + ", poids=" + poids + "]";
	}
}