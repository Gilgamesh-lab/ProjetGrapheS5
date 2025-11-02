package Ui;

import Graphe.*;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;



public class Main extends Application {

    @Override
    public void start(Stage stage) {
        Group graphRoot = new Group();
        VBox root = new VBox();
        root.setPrefSize(1200, 800);
        Scene scene = new Scene(root, 1200, 800);

        Graphe graphe = Graphe.getDefaultGraphe();
        GrapheView grapheView = new GrapheView(graphe, graphRoot);

        // Menu déroulant et boutons
        ComboBox<String> algoMenu = new ComboBox<>();
        algoMenu.getItems().addAll("BFS", "DFS", "Kruskal", "Prim", "Dijkstra", "Bellman-Ford", "Floyd-Warshall");
        algoMenu.setValue("BFS");
        algoMenu.setDisable(true); // désactivé au départ

        Button runButton = new Button("Exécuter l'algo");
        runButton.setDisable(true);

        Button resetButton = new Button("Relancer simulation");
        resetButton.setDisable(true);

        runButton.setOnAction(e -> {
            String algo = algoMenu.getValue();
            grapheView.resetGraph(); // reset automatique avant nouvel algo

            switch(algo) {
                case "BFS":
                    Resultat bfs = graphe.getBFS("Rennes");
                    grapheView.runAlgorithmStepByStep(bfs.getGraphe());
                    break;

                case "DFS":
                    Resultat dfs = graphe.getDFS("Rennes");
                    grapheView.runAlgorithmStepByStep(dfs.getGraphe());
                    break;

                case "Kruskal":
                    grapheView.runAlgorithmStepByStep(graphe.getKruskal());
                    break;

                case "Prim":
                    grapheView.runAlgorithmStepByStep(graphe.getPrim("Rennes"));
                    break;

                case "Dijkstra":
                    Resultat resDij = graphe.getDijkstra("Bordeaux", "Lille");
                    List<Arete> edgesDij = buildEdgesFromPath(graphe, resDij.getChemin());
                    grapheView.runAlgorithmStepByStep(edgesDij);
                    break;

                case "Bellman-Ford":
                    Graphe gNeg = Graphe.getDefaultGrapheOrienterNegatif();
                    Resultat resBell = gNeg.getBellmanFord("Bordeaux", "Lille");
                    if(resBell != null) {
                        List<Arete> edgesBell = buildEdgesFromPath(gNeg, resBell.getChemin());
                        grapheView.runAlgorithmStepByStep(edgesBell);
                    }
                    break;



                case "Floyd-Warshall":
                    Resultat resFloyd = graphe.getFloydWarshall();

                    String cheminStr = graphe.cheminFloydWarshall(resFloyd.getMatricePere(), "Rennes", "Dijon");
                    if (cheminStr == null) {
                        System.out.println("Aucun chemin trouvé avec Floyd-Warshall.");
                        break;
                    }

                    // On sépare correctement et on inverse
                    String[] noms = cheminStr.split("->");
                    List<String> nomsList = new ArrayList<>(Arrays.asList(noms));
                    Collections.reverse(nomsList);

                    // On reconstruit la chaîne avec les espaces autour de " -> " pour match buildEdgesFromPath
                    String cheminCorrect = String.join(" -> ", nomsList);

                    List<Arete> edgesFloyd = buildEdgesFromPath(graphe, cheminCorrect);
                    if (edgesFloyd.isEmpty()) {
                        System.out.println("Aucune arête à animer !");
                    }
                    grapheView.runAlgorithmStepByStep(edgesFloyd);
                    break;






            }
        });



        resetButton.setOnAction(e -> {
            grapheView.resetGraph();
            grapheView.startSimulation();
        });

        HBox controls = new HBox(10, algoMenu, runButton, resetButton);
        StackPane graphPane = new StackPane(graphRoot);
        graphPane.setPrefSize(1200, 700);

        root.getChildren().addAll(controls, graphPane);
        stage.setScene(scene);
        stage.setTitle("Simulation Graphe ");
        stage.show();

        // activer boutons après stabilisation
        grapheView.setOnStable(() -> {
            algoMenu.setDisable(false);
            runButton.setDisable(false);
            resetButton.setDisable(false);
        });

        grapheView.startSimulation();
    }
    private List<Arete> buildEdgesFromPath(Graphe graphe, String cheminStr) {
        List<Arete> edges = new ArrayList<>();
        if (cheminStr == null || cheminStr.isEmpty()) return edges;

        // Le chemin est sous forme "A -> B -> C -> D"
        String[] noms = cheminStr.split(" -> ");
        for (int i = 0; i < noms.length - 1; i++) {
            String nom1 = noms[i];
            String nom2 = noms[i + 1];

            for (Arete a : graphe.getAretes()) {
                if ((a.getSource().getNom().equals(nom1) && a.getDestination().getNom().equals(nom2)) ||
                        (a.getSource().getNom().equals(nom2) && a.getDestination().getNom().equals(nom1))) {
                    edges.add(a);
                    break;
                }
            }
        }
        return edges;
    }




    public static void main(String[] args) {
        launch(args);
    }
}
