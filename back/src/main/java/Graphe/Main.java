package Graphe;

import java.util.ArrayList;
import java.util.HashMap;

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
		System.out.println("Algorithme de Dijkstra entre Bordeaux et Lille");
		System.out.println();
		
		System.out.println();
		Resultat resultat = graphe.getDijkstra("Bordeaux", "Lille");
		
		System.out.println("Le chemin le plus court trouvé entre Bordeaux et Lille avec l’algorithme de Dijkstra est : " + resultat.getChemin() + " avec un poids minimun de " + resultat.getPoids() );
		
		System.out.println();
		System.out.println("Algorithme de Bellman-Ford entre Bordeaux et Lille");
		System.out.println();
		
		graphe = Graphe.getDefaultGrapheOrienterNegatif();
		resultat = graphe.getBellmanFord("Bordeaux", "Lille");
		
		System.out.println("Sur un graphe orienté avec des poids négatifs, le chemin le plus court trouvé entre Bordeaux et Lille avec l’algorithme de Bellman-Ford est : " + resultat.getChemin() + " avec un poids minimun de " + resultat.getPoids() );

		
		System.out.println();
		System.out.println("Algorithme de Floyd-Warshall");
		System.out.println();
		
		graphe.getFloydWarshall();
		
		
		
		resultat = graphe.getFloydWarshall();
		
		
		System.out.println();
		
		HashMap<String, HashMap<String, Integer>> w = resultat.getMatriceDistance();
		HashMap<String, HashMap<String, Sommet>> p = resultat.getMatricePere();
		
		System.out.println();
		System.out.println("Matrice des distances : ");
		System.out.println();
		
		for (String key : w.keySet() ){
			System.out.print(key + " : ");
			for (String key2 : w.get(key).keySet() ){
				if(w.get(key).get(key2) >= Integer.MAX_VALUE / 10001) {
					System.out.print("∞(" + key2 + ") , ");
				}
				else {
					System.out.print(w.get(key).get(key2) + "(" + key2 + ") , ");
				}
				
			}
			System.out.println();
		}
		System.out.println();
		
		System.out.println();
		System.out.println("Matrice des Pères : ");
		System.out.println();
		
		for (String key : p.keySet() ){
			System.out.print(key + " : ");
			for (String key2 : p.keySet() ){
				if(p.get(key).get(key2) != null) {
					System.out.print(p.get(key).get(key2).getNom() + " , ");
				}
				else {
					System.out.print(null + " , ");
				}
			}
			System.out.println();
		}
		System.out.println();
		
		
		System.out.println();
		System.out.println("Matrice des PCCs : ");
		System.out.println();
		
		HashMap<String, HashMap<String, String>> pccs = new HashMap<String, HashMap<String, String>>();
		
		for (String key : p.keySet() ){
			pccs.put(key, new HashMap<String, String>());
			for (String key2 : p.keySet() ){
				pccs.get(key).put(key2, graphe.cheminFloydWarshall(p,key , key2));
			}
		}
		
		for (String key : pccs.keySet() ){
			System.out.print(key + " : ");
			for (String key2 : pccs.keySet() ){
				System.out.print(pccs.get(key).get(key2)+ " , ");
			}
			System.out.println();
		}
		System.out.println();
		
	}

}
