package Graphe;

import java.util.ArrayList;

public class Main  {
	public static void main(String[] args) {
		/*Graphe graphe = Graphe.getDefaultGraphe();
		
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
		System.out.println("Poids total du plus court chemin trouvé = " + aretes.stream().mapToInt(arete -> arete.getPoids()).sum());*/
		
		ArrayList<Sommet> sommets = new ArrayList<Sommet>();
		
		Sommet a = new Sommet("a", null, sommets);
		Sommet b = new Sommet("b", null, sommets);
		Sommet c = new Sommet("c", null, sommets);
		
		new Arete(a, b, true, 130);
		new Arete(b, c, true, 90);
		new Arete(c, a, true, 100);
		
		Graphe graphe = new Graphe(sommets);
		

		

	}

}
