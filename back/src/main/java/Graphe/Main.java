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
		System.out.println("Poids total du plus court chemin trouvé = " + aretes.stream().map(arete -> arete.getPoids()).reduce((x, y ) -> x + y).get());
		
		System.out.println();
		System.out.println("Algorithme de Prim");
		System.out.println();
		
		aretes = graphe.getPrim("Rennes"); 
		aretes.stream().forEach(arete -> System.out.println(arete.getSource().getNom() + " -> " + arete.getDestination().getNom() + " : " + arete.getPoids()));
		System.out.println();
		System.out.println("Poids total du plus court chemin trouvé = " + aretes.stream().map(arete -> arete.getPoids()).reduce((x, y ) -> x + y).get());
		

	}

}
