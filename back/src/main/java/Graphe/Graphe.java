package Graphe;


import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;


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

	/**
	 * Permmet de définir la liste des sommets du graphe
	 * @param sommets
	 */
	public void setSommets(ArrayList<Sommet> sommets) {
		this.sommets = sommets;
	}
	
	/**
	 * Remet à false la valeur qui définit si un sommet a été marqué ou pas
	 */
	public void reset() {
		this.getSommetsTrier().forEach(sommet -> sommet.setMarquer(false));
	}
	
	/**
	 * Permmets d'ajouter un sommet au graphe
	 * @param sommet : le sommet à ajouter
	 */
	public void ajouterSommet(Sommet sommet) {
		this.sommets.add(sommet);
	}
	
	/**
	 * Permmets d'ajouter un nouveau sommet au graphe
	 * @param nomSommet : le nom du sommet à créer et à ajouter au graphe
	 */
	public void ajouterSommet(String nomSommet) {
		this.sommets.add(new Sommet(nomSommet));
	}
	
	/**
	 * Permet de retrouver un sommet grâce à son nom
	 * @param nom : le nom du sommet à rechercher
	 * @return le sommet trouvé
	 */
	public Sommet getSommetParNom(String nom) {
		return this.getSommetsTrier().stream().filter(sommet -> sommet.getNom() == nom).findFirst().get();
	}
	
	/**
	 * Affiche les arêtes du graphe
	 */
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
	 * Applique un parcours en largeur sur le graphe et renvoie le chemin et le graphe obtenue grâce au parcours en largeur
	 * @param nomPointDepart le nom du point à partir du quelle partir
	 * @return un objet de type Resultat contenant le chemin et le graphe obtenue grâce au parcours en largeur
	 */
	public Resultat getBFS(String nomPointDepart) {
		this.reset();
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
		
		return resultat;
	}
	
	
	
	/**
	 * Applique l'algorithme de Kruskal sur le graphe
	 * @return une liste d'arêtes représentant un arbre couvrant de poids minimal 
	 */
	public ArrayList<Arete> getKruskal() {
		ArrayList<Arete> aretesTrierParPoids = this.getAretesSansDoublons();
		aretesTrierParPoids.sort(Comparator.comparingInt(arete -> arete.getPoids()));
		 
		ArrayList<Sommet> sommetVisiter = new ArrayList<Sommet>();
		ArrayList<Arete> aretes = new ArrayList<Arete>();
		
		while (aretes.size() != (this.getSommetsTrier().size() - 1)) {
			Arete arete = null;
			try {
				arete = aretesTrierParPoids.stream().filter(arete2 -> !sommetVisiter.contains(arete2.getSource()) || !sommetVisiter.contains(arete2.getDestination()) ).findFirst().get();
			}
			catch (NoSuchElementException e) { // cas où deux arbres non connectés se sont crées
				ArrayList<String> sommets = this.getEnsemble(aretes);
				for (Arete arete2 : aretesTrierParPoids) {
					if(sommets.contains(arete2.getDestination().getNom()) != sommets.contains(arete2.getSource().getNom())){
						arete = arete2;
						break;
					}
				}
			}
			
			if(!sommetVisiter.contains(arete.getSource())) {
				sommetVisiter.add(arete.getSource());
			}
			
			if(!sommetVisiter.contains(arete.getDestination())) {
				sommetVisiter.add(arete.getDestination());
			}
			aretes.add(arete);
		}
		
		return aretes;
		
		
	}
	
	/**
	 * Applique l'algorithme de Prim sur le graphe
	 * @return une liste d'arêtes représentant un arbre couvrant de poids minimal 
	 */
	public ArrayList<Arete> getPrim(String nomPointDepart) {
		Sommet depart = this.getSommetsTrier().stream().filter(sommet -> sommet.getNom() == nomPointDepart).findFirst().get();
		ArrayList<Arete> listeAretes = new ArrayList<Arete>();
		ArrayList<Arete> aretesChoisi = new ArrayList<Arete>();
		ArrayList<Sommet> sommetVisiter = new ArrayList<Sommet>();
		
		depart.getAretes().stream().forEach(arete -> listeAretes.add(arete));
		sommetVisiter.add(depart);
		
		while (aretesChoisi.size() != (this.getSommetsTrier().size() - 1)) { //
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
	
	/**
	 * Applique un parcours en profondeur sur le graphe et renvoie le chemin et le graphe obtenue grâce au parcours en profondeur
	 * @param nomPointDepart le nom du point à partir du quelle partir
	 * @return un objet de type Resultat contenant le chemin et le graphe obtenue grâce au parcours en profondeur
	 */
	public Resultat getDFS(String nomPointDepart) {
		this.reset();
		Sommet depart = this.getSommetsTrier().stream().filter(sommet -> sommet.getNom() == nomPointDepart).findFirst().get();
		String chemin = depart.getNom();
		Graphe grapheDFS = new Graphe();
		Resultat resultat = new Resultat();
		
		grapheDFS.ajouterSommet(depart.getNom());
		chemin = this.visiterDFS(depart, chemin, grapheDFS);
		
		resultat.setChemin(chemin);
		resultat.setGraphe(grapheDFS);
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
	 * Applique l'algorithme de Dijkstra sur le graphe
	 * @param villeDeDepart : le nom de la ville de départ
	 * @param villeDArrive : le nom de la ville d'arrivé
	 * @return un objet Resultat contenant le chemin le plus court entre le point de départ et le point d'arrive ainsi que son poids
	 */
	public Resultat getDijkstra(String villeDeDepart, String villeDArrive) {
		this.reset();
		Sommet depart = this.getSommetsTrier().stream().filter(sommet -> sommet.getNom() == villeDeDepart).findFirst().get();
		Sommet sommet = depart;
		HashMap<String, Integer> distanceMinimale = new HashMap<String, Integer>();
		HashMap<String, Sommet> predecesseur = new HashMap<String, Sommet>();
		
		int infinie = Integer.MAX_VALUE / 10000; // représente la valeur infinie
		
		for (Sommet s : this.getSommetsTrier() ) {
			if(s == depart){
				distanceMinimale.put(s.getNom(), 0);
			}
			else {
				distanceMinimale.put(s.getNom(), infinie);
				predecesseur.put(s.getNom(), null);
			}
		}
		
		while(this.getSommetsTrier().stream().anyMatch(s -> !s.isMarquer())) {
			sommet = this.getSommetsTrier().stream().filter(s -> !s.isMarquer()).min(Comparator.comparingInt(s -> distanceMinimale.get(s.getNom()) )).get();
			sommet.setMarquer(true);
			
			for (Arete arete : sommet.getAretes()) {
				if(arete.getDestination() != sommet )  {
					if(  distanceMinimale.get(arete.getDestination().getNom()) > distanceMinimale.get(sommet.getNom())  + arete.getPoids()) {
						distanceMinimale.put(arete.getDestination().getNom(), distanceMinimale.get(sommet.getNom())  + arete.getPoids());
						predecesseur.put(arete.getDestination().getNom(), sommet);
					}
				}
				else {
					if( arete.EstOrienter() && distanceMinimale.get(arete.getSource().getNom()) > distanceMinimale.get(sommet.getNom())  + arete.getPoids()) {
						distanceMinimale.put(arete.getSource().getNom(), distanceMinimale.get(sommet.getNom())  + arete.getPoids());
						predecesseur.put(arete.getSource().getNom(), sommet);
					}
				}
			}
		}
		
		Sommet arrive = this.getSommetsTrier().stream().filter(s -> s.getNom() == villeDArrive).findFirst().get();
		Sommet prede = predecesseur.get(arrive.getNom());
		String chemin = prede.getNom() + " -> " + arrive.getNom()  ;
		
		while (prede != depart) {
			prede = predecesseur.get(prede.getNom());
			chemin =  prede.getNom() + " -> " + chemin;
		}
		
		
		Resultat resultat = new Resultat();
		resultat.setChemin(chemin);
		resultat.setPoids(Long.valueOf(distanceMinimale.get(arrive.getNom())));
		return resultat;
		
		
	}
	
	/**
	 * Applique l'algorithme de Bellman-Ford sur le graphe
	 * @param villeDeDepart : le nom de la ville de départ
	 * @param villeDArrive : le nom de la ville d'arrivé
	 * @return un objet Resultat contenant le chemin le plus court entre le point de départ et le point d'arrive ainsi que son poids, renvoie null si pas de chemin trouvé 
	 */
	public Resultat getBellmanFord(String villeDeDepart, String villeDArrive) {
		Sommet depart = this.getSommetsTrier().stream().filter(sommet -> sommet.getNom() == villeDeDepart).findFirst().get();
		HashMap<String, Long> distanceMinimale = new HashMap<String, Long>();
		HashMap<String, Sommet> predecesseur = new HashMap<String, Sommet>();
		
		long infinie = Long.MAX_VALUE / 10000; // représente la valeur infinie
		
		for (Sommet s : this.getSommetsTrier() ) {
			if(s == depart){
				distanceMinimale.put(s.getNom(), 0L);
			}
			else {
				distanceMinimale.put(s.getNom(), infinie);
				predecesseur.put(s.getNom(), null);
			}
			
		}
		
		boolean changement = true;
		while(changement) {
			changement = false;
			for (Arete arete : this.getAretesSansDoublons()) {
				if(  distanceMinimale.get(arete.getDestination().getNom()) > distanceMinimale.get(arete.getSource().getNom())  + arete.getPoids()) {
					distanceMinimale.put(arete.getDestination().getNom(), distanceMinimale.get(arete.getSource().getNom())  + arete.getPoids());
					predecesseur.put(arete.getDestination().getNom(), arete.getSource());
					changement = true;
				}
			}
		}
		
		Sommet arrive = this.getSommetsTrier().stream().filter(s -> s.getNom() == villeDArrive).findFirst().get();
		try {
			Sommet prede = predecesseur.get(arrive.getNom());
			String chemin = prede.getNom() + " -> " + arrive.getNom()  ;
			
			while (prede != depart) {
				prede = predecesseur.get(prede.getNom());
				chemin =  prede.getNom() + " -> " + chemin;
			}
			Resultat resultat = new Resultat();
			resultat.setChemin(chemin);
			resultat.setPoids(distanceMinimale.get(arrive.getNom()));
			resultat.setMatriceDistanceBF(distanceMinimale);
			resultat.setMatricePereBF(predecesseur);
			return resultat;
		}
		catch (NullPointerException e) { // Si aucun chemin n'a pu être trouver entre le sommet de départ et le sommet d'arrivé
			Resultat resultat = null;
			return resultat;
		}
	}
	
	/**
	 * Applique l'algorithme de Floyd-Warshall sur le graphe
	 * @return un objet Resultat contenant la  matrice des distance et la matrice des pères correspondant à ce graphe
	 */
	public Resultat getFloydWarshall() {
		HashMap<String, HashMap<String, Long>> w = new HashMap<>();
		long infinie = Long.MAX_VALUE / 10000; // représente la valeur infinie si point d'arrivé hors de portée
		Arete arete;
		HashMap<String, HashMap<String, Sommet>> p = new HashMap<String, HashMap<String, Sommet>>();
		
		
		for (Sommet s : this.getSommetsTrier()) {
		    w.put(s.getNom(), new HashMap<>());
		    p.put(s.getNom(), new HashMap<>());
		}
			
		for (Sommet key : this.getSommetsTrier() ){
			for (Sommet key2 : this.getSommetsTrier() ){
				if (key == key2) { // si le sommet de départ est le sommet d'arrivée
					w.get(key.getNom()).put(key2.getNom(), 0L);
					p.get(key.getNom()).put(key2.getNom(), null);
				}
				else{
					arete = key.getAretes().stream().filter(arete2 -> arete2.getDestination() == key2 || (!arete2.EstOrienter() && arete2.getDestination() == key  && arete2.getSource() == key2)).findFirst().orElse(null);
					if(arete != null) { // si pas de chemin
						w.get(key.getNom()).put(key2.getNom(), Long.valueOf(arete.getPoids()) );
						p.get(key.getNom()).put(key2.getNom(), key);
						if(!arete.EstOrienter()) {
							w.get(key2.getNom()).put(key.getNom(), Long.valueOf(arete.getPoids()));
						    p.get(key2.getNom()).put(key.getNom(), key2);
						}
					}
					else {
						w.get(key.getNom()).put(key2.getNom(), infinie); 
						p.get(key.getNom()).put(key2.getNom(), null);
					}
				}
			}
		}
		
		String k;
		String i;
		String j;
		for (Sommet key1 : this.getSommetsTrier() ){
			k = key1.getNom();
			for (Sommet key2 : this.getSommetsTrier() ){
				i = key2.getNom();
				for (Sommet key3 : this.getSommetsTrier() ){
					j = key3.getNom();
					if(w.get(i).get(k) != infinie && w.get(k).get(j) != infinie 
						    && w.get(i).get(k) + w.get(k).get(j) < w.get(i).get(j)) {
						w.get(i).put(j, w.get(i).get(k) + w.get(k).get(j));
						p.get(i).put(j, p.get(k).get(j));
					}
				}
			}
		}
		
		for (Sommet s : this.getSommetsTrier()) {
		    String nom = s.getNom();
		    if (w.get(nom).get(nom) < 0) {
		        System.out.println("⚠️ Cycle négatif détecté impliquant " + nom);
		    }
		}
		
		Resultat resultat = new Resultat();
		resultat.setMatriceDistance(w);
		resultat.setMatricePere(p);
		
		return resultat;
		
		
		
	}
	
	/**
	 * Reconstruit le chemin le plus court entre deux points
	 * @param matricePere : la matrice pere de ce graphe issue de l'algorithme de Floyd-Warshall
	 * @param depart : le point de départ
	 * @param arrive : le point d'arrive
	 * @return le chemin le plus court entre le point de départ et le point d'arrive au format : sommet1 -> sommet2
	 */
	public String cheminFloydWarshall(HashMap<String, HashMap<String, Sommet>> matricePere, String depart, String arrive) {
		ArrayList<String> listeSommet = new ArrayList<String>();
		
		listeSommet = trouverCheminFloydWarshall( matricePere, depart,  arrive, listeSommet);
		if(listeSommet == null) {
			return null;
		}
		
		else {
			String chemin = listeSommet.get(listeSommet.size() -1);
			for (int i = listeSommet.size() -2 ; i >= 0 ; i--) {
				chemin += "->" + listeSommet.get(i) ;
			}
			return chemin;
		}
	}
	
	/**
	 * Trouve le chemin le plus court entre deux points
	 * @param matricePere : la matrice pere de ce graphe issue de l'algorithme de Floyd-Warshall
	 * @param depart : le point de départ
	 * @param arrive : le point d'arrive
	 * @param chemin : liste des sommets parcourue en emprutant le chemin le plus court
	 * @return une liste de sommet correspondant au chemin le plus court entre le point de départ et le point d'arrive
	 */
	public ArrayList<String> trouverCheminFloydWarshall(HashMap<String, HashMap<String, Sommet>> matricePere, String depart, String arrive, ArrayList<String> chemin) {
		chemin.add(arrive);
		if(matricePere.get(depart).get(arrive) == null) {
			return null;
		}
		else {
			trouverCheminFloydWarshall(matricePere, depart, matricePere.get(depart).get(arrive).getNom(), chemin);
			return chemin;
		}
	}
	
	
	
	
	/**
	 * Renvoie une liste de string correspondant à tout les sommets appartenant à l'arbre de la première aretes
	 * @param aretes une liste d'arete
	 * @return une liste de nom de sommet
	 */
	public ArrayList<String> getEnsemble (ArrayList<Arete> aretes){
		ArrayList<String> aretesChoisi = new ArrayList<String>();
		aretesChoisi.add(aretes.get(0).getSource().getNom());
		
		boolean nouveauSommetDetecter = true;
		while(nouveauSommetDetecter) {
			nouveauSommetDetecter = false;
			for (Arete arete : aretes) {
				if(!aretesChoisi.contains(arete.getSource().getNom()) &&  aretesChoisi.contains(arete.getDestination().getNom())) {
					aretesChoisi.add(arete.getSource().getNom());
					nouveauSommetDetecter = true;
				}
				else if(aretesChoisi.contains(arete.getSource().getNom()) &&  !aretesChoisi.contains(arete.getDestination().getNom())) {
					aretesChoisi.add(arete.getDestination().getNom());
					nouveauSommetDetecter = true;
				}
			}
		}
		
		return aretesChoisi;
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
	
	/**
	 * Contruit et renvoie une version du graphe de départ orienté et avec des poids négatifs
	 * @return le graphe de départ orienté et avec des poids négatifs
	 */
	public static Graphe getDefaultGrapheNegatif() {
		
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
		
		new Arete(rennes, nantes, false, -45);
		new Arete(rennes, caen, false, 75);
		new Arete(rennes, paris, false, 110);
		
		new Arete(nantes, paris, false, 80);
		new Arete(caen, lille, false, 65);
		new Arete(caen, paris, false, 50);
		
		new Arete(paris, lille, false, 70);
		new Arete(paris, dijon, false, -60);
		new Arete(dijon, lille, false, 120);
		
		new Arete(dijon, nancy, false, 75);
		new Arete(dijon, grenoble, false, 75);
		new Arete(dijon, lyon, false, 70);
		
		new Arete(lyon, nancy, false, 90);
		new Arete(lyon, grenoble, false, -40);
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