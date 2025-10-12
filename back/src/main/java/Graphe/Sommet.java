package Graphe;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Objects;
import java.util.stream.Collectors;


public class Sommet {
    private String nom;
    private ArrayList<Arete> aretes;
    private String couleur;
    private boolean marquer;
    
    public Sommet(String nom,  ArrayList<Arete> aretes, String couleur) {
    	this.nom = nom;
    	this.couleur = couleur;
    	this.aretes = aretes;
    	this.marquer = false;
    }
    
    public Sommet(String nom, String couleur) {
    	this.nom = nom;
    	this.couleur = couleur;
    	this.aretes = new ArrayList<Arete>();
    	this.marquer = false;
    }
    
    public Sommet(String nom) {
    	this.nom = nom;
    	this.couleur = null;
    	this.aretes = new ArrayList<Arete>();
    	this.marquer = false;
    }
    
    
    public Sommet(String nom, String couleur, ArrayList<Sommet> sommets) {
    	this.nom = nom;
    	this.couleur = couleur;
    	this.aretes = new ArrayList<Arete>();
    	sommets.add(this);
    	this.marquer = false;
    }
    

    /**
     * Renvoie la liste des voisin d'un sommet par ordre lexicographique
     * @return la liste des voisins
     */
    public ArrayList<Sommet> getVoisins(){
    	ArrayList<Sommet> voisins = new ArrayList<Sommet>();
    	for (Arete arete : this.aretes) {
    		if(arete.getDestination() != this) {
    			voisins.add(arete.getDestination());
    		}
    		else {
    			voisins.add(arete.getSource());
    		}
    	}
    	voisins.sort(Comparator.comparing(sommet -> sommet.getNom()));
    	return voisins;
    }
    

    public boolean isMarquer() {
		return marquer;
	}

	public void setMarquer(boolean marquer) {
		this.marquer = marquer;
	}

	public String getNom() {
		return nom;
	}



	public void setNom(String nom) {
		this.nom = nom;
	}


	/**
	 * Renvoie la liste des arêtes d'un sommet par ordre de création
	 * @return la liste des arêtes
	 */
	public ArrayList<Arete> getAretes() {
		return (ArrayList<Arete>) this.aretes.stream().sorted(Comparator.comparingInt(arete -> arete.getId())).collect(Collectors.toList());
	}



	public void setAretes(ArrayList<Arete> aretes) {
		this.aretes = aretes;
	}



	public String getCouleur() {
		return couleur;
	}



	public void setCouleur(String couleur) {
		this.couleur = couleur;
	}




    public void ajouterArete( Arete arete){
        this.aretes.add(arete);
    }

    public void supprimerArete( Arete arete){
        this.aretes.remove(arete);
    }

	@Override
	public int hashCode() {
		return Objects.hash(nom);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Sommet other = (Sommet) obj;
		return Objects.equals(nom, other.nom);
	}
    
    
    
    
}