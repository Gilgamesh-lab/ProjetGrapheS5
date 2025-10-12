package Graphe;

public class Main  {
	public static void main(String[] args) {
		Graphe graphe = Graphe.getDefaultGraphe();
		
		Resultat reponse  = graphe.getBFS("Rennes");
		reponse.getGraphe().afficher();
		System.out.println(reponse.getChemin());
		
		System.out.println("");
		
		reponse  = graphe.getDFS("Rennes");
		reponse.getGraphe().afficher();
		System.out.println(reponse.getChemin());

	}

}
