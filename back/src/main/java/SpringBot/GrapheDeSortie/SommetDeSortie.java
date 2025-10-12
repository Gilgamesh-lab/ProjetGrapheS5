package SpringBot.GrapheDeSortie;
import java.util.ArrayList;
import java.util.Objects;

public class SommetDeSortie {
    private String nom;
    private String couleur;
    private ArrayList<AreteDeSortie> aretes;
    
    
    public SommetDeSortie(String nom,  ArrayList<AreteDeSortie> aretes, String couleur) {
    	this.nom = nom;
    	this.couleur = couleur;
    	this.aretes = aretes;
    }
    
    public SommetDeSortie(String nom, String couleur) {
    	this.nom = nom;
    	this.couleur = couleur;
    	this.aretes = new ArrayList<AreteDeSortie>();
    }
    
    
    public SommetDeSortie(String nom, String couleur, ArrayList<SommetDeSortie> sommets) {
    	this.nom = nom;
    	this.couleur = couleur;
    	this.aretes = new ArrayList<AreteDeSortie>();
    	sommets.add(this);
    }
    

    
    
    

    public String getNom() {
		return nom;
	}



	public void setNom(String nom) {
		this.nom = nom;
	}



	public ArrayList<AreteDeSortie> getAretes() {
		return aretes;
	}



	public void setAretes(ArrayList<AreteDeSortie> aretes) {
		this.aretes = aretes;
	}



	public String getCouleur() {
		return couleur;
	}



	public void setCouleur(String couleur) {
		this.couleur = couleur;
	}


    public void ajouterAreteSortie( AreteDeSortie AreteSortie){
        this.aretes.add(AreteSortie);
    }

    public void supprimerAreteSortie( AreteDeSortie AreteSortie){
        this.aretes.remove(AreteSortie);
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
		SommetDeSortie other = (SommetDeSortie) obj;
		return Objects.equals(nom, other.nom);
	}
    
    
    
    
}