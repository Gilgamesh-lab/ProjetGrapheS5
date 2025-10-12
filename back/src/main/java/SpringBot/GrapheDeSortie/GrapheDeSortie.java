package SpringBot.GrapheDeSortie;


import java.util.ArrayList;
import java.util.Comparator;
import java.util.stream.Collectors;


public class GrapheDeSortie {
    private ArrayList<SommetDeSortie> sommets;

	public GrapheDeSortie(ArrayList<SommetDeSortie> sommets) {
		this.sommets = sommets;
	}
	
	public GrapheDeSortie() {
		this.sommets = new ArrayList<SommetDeSortie>();
	}

	public ArrayList<SommetDeSortie> getSommetsTrier() {
		return (ArrayList<SommetDeSortie>) this.sommets.stream().sorted(Comparator.comparing(sommet -> sommet.getNom())).collect(Collectors.toList());
	}

	public void setSommets(ArrayList<SommetDeSortie> sommets) {
		this.sommets = sommets;
	}
	
	public void ajouterSommet(SommetDeSortie sommet) {
		this.sommets.add(sommet);
	}
	
	public SommetDeSortie getSommetParNom(String nom) {
		return this.getSommetsTrier().stream().filter(sommet -> sommet.getNom() == nom).findFirst().get();
	}
	
	public void afficher() {
		ArrayList<Integer> dejaVue = new ArrayList<Integer>();
		
		for (SommetDeSortie sommet : sommets) {
			ArrayList<AreteDeSortie> AreteDeSorties = sommet.getAretes();
			AreteDeSorties.stream().filter(AreteDeSortie -> !dejaVue.contains(AreteDeSortie.getId())).forEach(AreteDeSortie -> {AreteDeSortie.afficher(); dejaVue.add(AreteDeSortie.getId());});
		}
		 
	}
	
	/**
	 * Renvoie le graphe du réseau routier
	 * @return le graphe du réseau routier
	 */
	public static GrapheDeSortie getDefaultGraphe() {
		
		ArrayList<SommetDeSortie> sommets = new ArrayList<SommetDeSortie>();
		SommetDeSortie bordeaux = new SommetDeSortie("Bordeaux", null, sommets);
		SommetDeSortie rennes = new SommetDeSortie("Rennes", null, sommets);
		SommetDeSortie nantes = new SommetDeSortie("Nantes", null, sommets);
		
		SommetDeSortie caen = new SommetDeSortie("Caen", null, sommets);
		SommetDeSortie paris = new SommetDeSortie("Paris", null, sommets);
		SommetDeSortie dijon = new SommetDeSortie("Dijon", null, sommets);
		
		SommetDeSortie lyon = new SommetDeSortie("Lyon", null, sommets);
		SommetDeSortie lille = new SommetDeSortie("Lille", null, sommets);
		SommetDeSortie nancy = new SommetDeSortie("Nancy", null, sommets);
		
		SommetDeSortie grenoble = new SommetDeSortie("Grenoble", null, sommets);
		
		new AreteDeSortie(bordeaux, rennes, false, 130);
		new AreteDeSortie(bordeaux, nantes, false, 90);
		new AreteDeSortie(bordeaux, lyon, false, 100);
		new AreteDeSortie(bordeaux, paris, false, 150);
		
		new AreteDeSortie(rennes, nantes, false, 45);
		new AreteDeSortie(rennes, caen, false, 75);
		new AreteDeSortie(rennes, paris, false, 110);
		
		new AreteDeSortie(nantes, paris, false, 80);
		new AreteDeSortie(caen, lille, false, 65);
		new AreteDeSortie(caen, paris, false, 50);
		
		new AreteDeSortie(paris, lille, false, 70);
		new AreteDeSortie(paris, dijon, false, 60);
		new AreteDeSortie(dijon, lille, false, 120);
		
		new AreteDeSortie(dijon, nancy, false, 75);
		new AreteDeSortie(dijon, grenoble, false, 75);
		new AreteDeSortie(dijon, lyon, false, 70);
		
		new AreteDeSortie(lyon, nancy, false, 90);
		new AreteDeSortie(lyon, grenoble, false, 40);
		new AreteDeSortie(grenoble, nancy, false, 80);
		new AreteDeSortie(lille, nancy, false, 100);
		
		
		GrapheDeSortie graphe = new GrapheDeSortie(sommets);
		
		return graphe;
	}

	@Override
	public String toString() {
		return "Graphe [sommets=" + sommets + "]";
	}
	
	
    
    

}