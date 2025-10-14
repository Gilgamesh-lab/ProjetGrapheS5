package Graphe;


import java.util.ArrayList;
import java.util.Comparator;
import java.util.stream.Collectors;

import SpringBot.GrapheDeSortie.AreteDeSortie;
import SpringBot.GrapheDeSortie.GrapheDeSortie;
import SpringBot.GrapheDeSortie.SommetDeSortie;


public class Graphe {
    private ArrayList<Sommet> sommets;

	public Graphe(ArrayList<Sommet> sommets) {
		this.sommets = sommets;
	}
	
	public Graphe() {
		this.sommets = new ArrayList<Sommet>();
	}

	/**
     * Renvoie la liste des sommets d'un graphe par ordre lexicographique
     * @return la liste des sommets
     */
	public ArrayList<Sommet> getSommetsTrier() {
		return (ArrayList<Sommet>) this.sommets.stream().sorted(Comparator.comparing(sommet -> sommet.getNom())).collect(Collectors.toList());
	}

	public void setSommets(ArrayList<Sommet> sommets) {
		this.sommets = sommets;
	}
	
	public void ajouterSommet(Sommet sommet) {
		this.sommets.add(sommet);
	}
	
	public void ajouterSommet(String nomSommet) {
		this.sommets.add(new Sommet(nomSommet));
	}
	
	public Sommet getSommetParNom(String nom) {
		return this.getSommetsTrier().stream().filter(sommet -> sommet.getNom() == nom).findFirst().get();
	}
	
	public boolean containSommetParNom(String nom) {
		return this.getSommetsTrier().stream().anyMatch(sommet -> sommet.getNom() == nom);
	}
	
	public void afficher() {
		ArrayList<Integer> dejaVue = new ArrayList<Integer>();
		
		for (Sommet sommet : this.sommets) {
			ArrayList<Arete> aretes = sommet.getAretes();
			aretes.stream().filter(arete -> !dejaVue.contains(arete.getId())).forEach(arete -> {arete.afficher(); dejaVue.add(arete.getId());});
		}
		 
	}
	
	/**
	 * Renvoie toutes les aretes d'un graphe (y compris les doublons)
	 * @return les aretes
	 */
	public ArrayList<Arete> getAretes(){
		ArrayList<Arete> aretes = new ArrayList<Arete>();
		this.getSommetsTrier().stream().forEach(sommet -> sommet.getAretes().stream().forEach(arete -> aretes.add(arete)));
		return aretes;
	}
	
	/**
	 * Renvoie toutes les aretes d'un graphe (sans les doublons)
	 * @return les aretes
	 */
	public ArrayList<Arete> getAretesSansDoublons(){
		ArrayList<Arete> aretes = new ArrayList<Arete>();
		this.getSommetsTrier().stream().forEach(sommet -> sommet.getAretes().stream().forEach(arete -> aretes.add(arete)));
		return (ArrayList<Arete>) this.getAretes().stream().distinct().collect(Collectors.toList());
	}
	
	/**
	 * Convertit un Graphe en GrapheDeSorti (un format qui peut-être envoyé au front)
	 * @return le graphe de sortie
	 */
	public GrapheDeSortie getGrapheSortie() {
		GrapheDeSortie grapheDeSortie = new GrapheDeSortie();
		SommetDeSortie sommmetDeSortie;
		
		for (Sommet sommet : this.sommets) {
			sommmetDeSortie = new SommetDeSortie(sommet.getNom(), null);
			grapheDeSortie.ajouterSommet(sommmetDeSortie);
		}
		ArrayList<Integer> dejaVue = new ArrayList<Integer>();
		for (Arete arete : this.getAretes()) {
			if(!dejaVue.contains(arete.getId())) {
				new AreteDeSortie(grapheDeSortie.getSommetParNom(arete.getSource().getNom()), grapheDeSortie.getSommetParNom(arete.getDestination().getNom()), arete.isEstOrienter() , arete.getPoids());
				dejaVue.add(arete.getId());
			}
			
		}
		
		return grapheDeSortie;
	}
	
	/**
	 * Applique un parcours en largeur sur le graphe et renvoie le chemin et le graphe obtenue grâce au parcours en largeur
	 * @param nomPointDepart le nom du point à partir du quelle partir
	 * @return un objet de type Resultat contenant le chemin et le graphe obtenue grâce au parcours en largeur
	 */
	public Resultat getBFS(String nomPointDepart) {
		Sommet s = this.getSommetsTrier().stream().filter(sommet -> sommet.getNom() == nomPointDepart).findFirst().get();
		String chemin = s.getNom();
		Graphe grapheBFS = new Graphe();
		Resultat resultat = new Resultat();
		
		ArrayList<Sommet> sommetAVisiter = new ArrayList<Sommet>();
		sommetAVisiter.add(s);
		s.setMarquer(true);
		Sommet aVisiter;
		grapheBFS.ajouterSommet(s.getNom());
		
		while (!sommetAVisiter.isEmpty()) {
			aVisiter = sommetAVisiter.get(0);
			sommetAVisiter.remove(0);
			for (Sommet voisin : aVisiter.getVoisins()) {
				if(!voisin.isMarquer()) {
					grapheBFS.ajouterSommet(voisin.getNom());
					new Arete(grapheBFS.getSommetParNom(aVisiter.getNom()), grapheBFS.getSommetParNom(voisin.getNom()));
					sommetAVisiter.add(voisin);
					voisin.setMarquer(true);
					chemin += "->" + voisin.getNom();
				}
			}
		}
		resultat.setChemin(chemin);
		resultat.setGraphe(grapheBFS);
		this.getSommetsTrier().forEach(sommet -> sommet.setMarquer(false)); // reset
		return resultat;
	}
	
	public ArrayList<Arete> getKruskal() {
		ArrayList<Arete> aretesTrierParPoids = this.getAretesSansDoublons();
		aretesTrierParPoids.sort(Comparator.comparingInt(arete -> arete.getPoids()));
		 
		ArrayList<Sommet> sommetVisiter = new ArrayList<Sommet>();
		ArrayList<Arete> aretes = new ArrayList<Arete>();
		
		int i = 0 ;
		while (sommetVisiter.size() != (this.getSommetsTrier().size() - 1)) {
			Arete arete = aretesTrierParPoids.get(i);
			
			if(!sommetVisiter.contains(arete.getSource())) {
				aretes.add(arete);
				sommetVisiter.add(arete.getSource());
			}
			
			else if(!sommetVisiter.contains(arete.getDestination())) {
				aretes.add(arete);
				sommetVisiter.add(arete.getDestination());
			}
			
			
			i++;
		}
		
		return aretes;
		
		
	}
	
	public ArrayList<Arete> getPrim(String nomPointDepart) {
		Sommet depart = this.getSommetsTrier().stream().filter(sommet -> sommet.getNom() == nomPointDepart).findFirst().get();
		ArrayList<Arete> listeAretes = new ArrayList<Arete>();
		ArrayList<Arete> aretesChoisi = new ArrayList<Arete>();
		ArrayList<Sommet> sommetVisiter = new ArrayList<Sommet>();
		
		
		depart.getAretes().stream().forEach(arete -> listeAretes.add(arete));
		sommetVisiter.add(depart);
		
		while (aretesChoisi.size() != (this.getSommetsTrier().size() - 1)) { //
			//System.out.println(listeAretes);
			int idMinimun = listeAretes.stream()
					.filter(arete -> !sommetVisiter.contains(arete.getDestination()) || !sommetVisiter.contains(arete.getSource()) )
					.min(Comparator.comparing(Arete::getPoids))
					.map(arete -> arete.getId())
					.get();
			
			Arete arete =  listeAretes.stream().filter(arete2 -> arete2.getId() == idMinimun).findFirst().get();
			
			if(!sommetVisiter.contains(arete.getDestination())) {
				arete.getDestination().getAretes().stream().forEach(arete2 -> listeAretes.add(arete2));
				aretesChoisi.add(arete);
				sommetVisiter.add(arete.getDestination());
			}
			
			else if(!sommetVisiter.contains(arete.getSource())) {
				arete.getSource().getAretes().stream().forEach(arete2 -> listeAretes.add(arete2));
				aretesChoisi.add(arete);
				sommetVisiter.add(arete.getSource());
			}
			
		}
		
		return aretesChoisi;
		
		
	}
	
	public void getPlusPetiteArete(ArrayList<Arete> aretes) {
		
	}
	
	/**
	 * Applique un parcours en profondeur sur le graphe et renvoie le chemin et le graphe obtenue grâce au parcours en profondeur
	 * @param nomPointDepart le nom du point à partir du quelle partir
	 * @return un objet de type Resultat contenant le chemin et le graphe obtenue grâce au parcours en profondeur
	 */
	public Resultat getDFS(String nomPointDepart) {
		Sommet depart = this.getSommetsTrier().stream().filter(sommet -> sommet.getNom() == nomPointDepart).findFirst().get();
		String chemin = depart.getNom();
		Graphe grapheDFS = new Graphe();
		Resultat resultat = new Resultat();
		
		grapheDFS.ajouterSommet(depart.getNom());
		chemin = this.visiterDFS(depart, chemin, grapheDFS);
		
		resultat.setChemin(chemin);
		resultat.setGraphe(grapheDFS);
		this.getSommetsTrier().forEach(sommet -> sommet.setMarquer(false)); // reset
		return resultat;
		
	}
	
	/**
	 * Visite de façon récursive un sommet et tout ses enfants
	 * @param sommet le sommet à visiter et qui va servir de point de départ du parcours en profondeur
	 * @param chemin variable  qui stocke le chemin obtenue
	 * @param grapheDFS variable qui stocke le graphe obtenue
	 * @return le chemin obtenue
	 */
	public String visiterDFS(Sommet sommet, String chemin, Graphe grapheDFS) {
		sommet.setMarquer(true);
		for (Sommet voisin : sommet.getVoisins()) {
			if(!voisin.isMarquer()) {
				grapheDFS.ajouterSommet(voisin.getNom());
				new Arete(grapheDFS.getSommetParNom(sommet.getNom()), grapheDFS.getSommetParNom(voisin.getNom()));
				chemin += "->" + voisin.getNom();
				chemin = visiterDFS(voisin, chemin, grapheDFS);
			}
		}
		
		return chemin;
	}
	
	/**
	 * Contruit et renvoie le graphe de départ
	 * @return le graphe de départ
	 */
	public static Graphe getDefaultGraphe() {
		
		ArrayList<Sommet> sommets = new ArrayList<Sommet>();
		Sommet bordeaux = new Sommet("Bordeaux", null, sommets);
		Sommet rennes = new Sommet("Rennes", null, sommets);
		Sommet nantes = new Sommet("Nantes", null, sommets);
		
		Sommet caen = new Sommet("Caen", null, sommets);
		Sommet paris = new Sommet("Paris", null, sommets);
		Sommet dijon = new Sommet("Dijon", null, sommets);
		
		Sommet lyon = new Sommet("Lyon", null, sommets);
		Sommet lille = new Sommet("Lille", null, sommets);
		Sommet nancy = new Sommet("Nancy", null, sommets);
		
		Sommet grenoble = new Sommet("Grenoble", null, sommets);
		
		new Arete(bordeaux, rennes, false, 130);
		new Arete(bordeaux, nantes, false, 90);
		new Arete(bordeaux, lyon, false, 100);
		new Arete(bordeaux, paris, false, 150);
		
		new Arete(rennes, nantes, false, 45);
		new Arete(rennes, caen, false, 75);
		new Arete(rennes, paris, false, 110);
		
		new Arete(nantes, paris, false, 80);
		new Arete(caen, lille, false, 65);
		new Arete(caen, paris, false, 50);
		
		new Arete(paris, lille, false, 70);
		new Arete(paris, dijon, false, 60);
		new Arete(dijon, lille, false, 120);
		
		new Arete(dijon, nancy, false, 75);
		new Arete(dijon, grenoble, false, 75);
		new Arete(dijon, lyon, false, 70);
		
		new Arete(lyon, nancy, false, 90);
		new Arete(lyon, grenoble, false, 40);
		new Arete(grenoble, nancy, false, 80);
		new Arete(lille, nancy, false, 100);
		
		
		Graphe graphe = new Graphe(sommets);
		
		return graphe;
	}

	@Override
	public String toString() {
		return "Graphe [sommets=" + sommets + "]";
	}
	
	
    
    

}