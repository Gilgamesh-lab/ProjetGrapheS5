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
		
		System.out.println();
		System.out.println("Algorithme de Dijkstra entre Bordeaux et Lille");
		System.out.println();
		
		System.out.println();
		Resultat resultat = graphe.getDijkstra("Bordeaux", "Lille");
		
		System.out.println("Le chemin le plus court trouvé entre Bordeaux et Lille avec l’algorithme de Dijkstra est : " + resultat.getChemin() + " avec un poids minimun de " + resultat.getPoids() );
		
		System.out.println();
		System.out.println("Algorithme de Bellman-Ford entre Bordeaux et Lille");
		System.out.println();
		
		graphe = Graphe.getDefaultGrapheNegatif();
		resultat = graphe.getBellmanFord("Bordeaux", "Lille");
		
		HashMap<String, Long> matriceDistance = resultat.getMatriceDistanceBF();
		HashMap<String, Sommet> matricePredecesseur = resultat.getMatricePereBF();
		
		System.out.println();
		System.out.println("Matrice des distances : ");
		System.out.println();
		
		for (String key : matriceDistance.keySet() ){
			System.out.print(key + " : " + matriceDistance.get(key)  + " , ");
		}
		
		System.out.println();
		System.out.println();
		System.out.println("Matrice des predecesseur : ");
		System.out.println();
		
		for (String key : matricePredecesseur.keySet() ){
			System.out.print(key + " : " + matricePredecesseur.get(key).getNom()  + " , ");
		}
		System.out.println();
		System.out.println();
		System.out.println("Sur un graphe orienté avec des poids négatifs, le chemin le plus court trouvé entre Bordeaux et Lille avec l’algorithme de Bellman-Ford est : " + resultat.getChemin() + " avec un poids minimun de " + resultat.getPoids() );

		
		System.out.println();
		System.out.println("Algorithme de Floyd-Warshall");
		System.out.println();
		
		
		
		graphe = Graphe.getDefaultGraphe();
		resultat = graphe.getFloydWarshall();
		
		
		System.out.println();
		
		HashMap<String, HashMap<String, Long>> w = resultat.getMatriceDistance();
		HashMap<String, HashMap<String, Sommet>> p = resultat.getMatricePere();
		
		System.out.println();
		System.out.println("Matrice des distances : ");
		System.out.println();
		
		for (String key : w.keySet() ){
			System.out.print(key + " : ");
			for (String key2 : w.get(key).keySet() ){
				if(w.get(key).get(key2) == Long.MAX_VALUE / 10000) {
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
