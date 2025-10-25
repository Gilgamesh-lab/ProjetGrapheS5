package Graphe;

import java.util.ArrayList;

public class Main  {
	public static void main(String[] args) {
		Graphe graphe = Graphe.getDefaultGraphe();
		
		System.out.println();
		System.out.println("Parcours en largeur");
		System.out.println();
		
		Resultat reponse  = graphe.getBFS("Rennes");
		reponse.getGraphe().afficher();
		System.out.println(reponse.getChemin());
		
		System.out.println();
		System.out.println("Parcours en profondeur");
		System.out.println();
		
		reponse  = graphe.getDFS("Rennes");
		reponse.getGraphe().afficher();
		System.out.println(reponse.getChemin());
		
		
		System.out.println();
		System.out.println("Algorithme de Kruskal");
		System.out.println();
		
		ArrayList<Arete> aretes = graphe.getKruskal(); 
		aretes.stream().forEach(arete -> System.out.println(arete.getSource().getNom() + " -> " + arete.getDestination().getNom() + " : " + arete.getPoids()));
		System.out.println();
		System.out.println("Poids total du plus court chemin trouvé = " + aretes.stream().mapToInt(arete -> arete.getPoids()).sum());
		
		System.out.println();
		System.out.println("Algorithme de Prim à partir de Rennes");
		System.out.println();
		
		aretes = graphe.getPrim("Rennes"); 
		aretes.stream().forEach(arete -> System.out.println(arete.getSource().getNom() + " -> " + arete.getDestination().getNom() + " : " + arete.getPoids()));
		System.out.println();
		System.out.println("Poids total du plus court chemin trouvé = " + aretes.stream().mapToInt(arete -> arete.getPoids()).sum());
		
		/*ArrayList<Sommet> sommets = new ArrayList<Sommet>();
		
		Sommet s1 = new Sommet("s1", null, sommets);
		Sommet s2 = new Sommet("s2", null, sommets);
		Sommet s3 = new Sommet("s3", null, sommets);
		Sommet s4 = new Sommet("s4", null, sommets);
		Sommet s5 = new Sommet("s5", null, sommets);
		
		new Arete(s1, s4, true, 3);
		new Arete(s1, s2, true, 1);
		new Arete(s1, s3, true, 5);
		
		new Arete(s2, s4, true, 1);
		new Arete(s2, s5, true, 3);
		
		new Arete(s3, s5, true, 2);
		new Arete(s3, s2, true, 3);
		
		new Arete(s4, s5, true, 1);
		new Arete(s4, s3, true, 2);*/
		
		//Graphe graphe = new Graphe(sommets);
		
		System.out.println();
		Resultat resultat = graphe.getDijkstra("Bordeaux", "Lille");
		
		System.out.println("Le chemin le plus court trouvé entre Bordeaux et Lille avec l’algorithme de Dijkstra est : " + resultat.getChemin() + " avec un poids minimun de " + resultat.getPoids() );
		
		System.out.println();
		graphe = Graphe.getDefaultGrapheOrienterNegatif();
		resultat = graphe.getBellmanFord("Bordeaux", "Lille");
		
		System.out.println("Sur un graphe orienté avec des poids négatifs, le chemin le plus court trouvé entre Bordeaux et Lille avec l’algorithme de Bellman-Ford est : " + resultat.getChemin() + " avec un poids minimun de " + resultat.getPoids() );

		
		System.out.println();
		resultat = graphe.getBellmanFord("Lille", "Bordeaux");
		if(resultat == null) {
			System.out.println("Aucun chemin n'a pu être trouvé entre Lille et Bordeaux");
		}
		else {
			System.out.println("Sur un graphe orienté avec des poids négatifs, le chemin le plus court trouvé entre Bordeaux et Lille avec l’algorithme de Bellman-Ford est : " + resultat.getChemin() + " avec un poids minimun de " + resultat.getPoids() );
		}

	}

}
