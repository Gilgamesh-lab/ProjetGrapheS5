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
        VBox root = new VBox(10);
        root.setPrefSize(1200, 800);
        Scene scene = new Scene(root, 1200, 800);

        Graphe graphe = Graphe.getDefaultGraphe();
        GrapheView grapheView = new GrapheView(graphe, graphRoot);

        // Menu déroulant pour algorithmes
        ComboBox<String> algoMenu = new ComboBox<>();
        algoMenu.getItems().addAll("BFS", "DFS", "Kruskal", "Prim", "Dijkstra", "Bellman-Ford", "Floyd-Warshall");
        algoMenu.setValue("BFS");
        algoMenu.setDisable(true);

        // ComboBox pour choisir sommet départ et arrivée
        ComboBox<String> departMenu = new ComboBox<>();
        ComboBox<String> arriveMenu = new ComboBox<>();
        List<String> nomsSommets = new ArrayList<>();
        for (Sommet s : graphe.getSommetsTrier()) nomsSommets.add(s.getNom());
        departMenu.getItems().addAll(nomsSommets);
        arriveMenu.getItems().addAll(nomsSommets);
        departMenu.setValue(nomsSommets.get(0));
        arriveMenu.setValue(nomsSommets.get(0));
        departMenu.setVisible(false);
        arriveMenu.setVisible(false);

        // Boutons stylisés
        Button runButton = new Button("Exécuter l'algo");
        Button resetButton = new Button("Relancer simulation");
        runButton.setDisable(true);
        resetButton.setDisable(true);
        runButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-weight: bold;");
        resetButton.setStyle("-fx-background-color: #f44336; -fx-text-fill: white; -fx-font-weight: bold;");

        // Ajuster visibilité des menus selon algo choisi
        algoMenu.setOnAction(e -> {
            String algo = algoMenu.getValue();
            switch (algo) {
                case "BFS":
                case "DFS":
                case "Prim":
                    departMenu.setVisible(true);
                    arriveMenu.setVisible(false); // pas nécessaire
                    break;

                case "Dijkstra":
                case "Bellman-Ford":
                case "Floyd-Warshall":
                    departMenu.setVisible(true);
                    arriveMenu.setVisible(true);
                    break;
                default:
                    departMenu.setVisible(false);
                    arriveMenu.setVisible(false);
                    break;
            }
        });

        runButton.setOnAction(e -> {
            String algo = algoMenu.getValue();
            String depart = departMenu.getValue();
            String arrivee = arriveMenu.getValue();
            grapheView.resetGraph();

            switch (algo) {
                case "BFS":
                    Resultat bfs = graphe.getBFS(depart);
                    grapheView.runAlgorithmStepByStep(bfs.getGraphe());
                    break;
                case "DFS":
                    Resultat dfs = graphe.getDFS(depart);
                    grapheView.runAlgorithmStepByStep(dfs.getGraphe());
                    break;
                case "Kruskal":
                    grapheView.runAlgorithmStepByStep(graphe.getKruskal());
                    break;
                case "Prim":
                    grapheView.runAlgorithmStepByStep(graphe.getPrim(depart));
                    break;
                case "Dijkstra":
                    Resultat resDij = graphe.getDijkstra(depart, arrivee);
                    List<Arete> edgesDij = buildEdgesFromPath(graphe, resDij.getChemin());
                    grapheView.runAlgorithmStepByStep(edgesDij);
                    break;
                case "Bellman-Ford":
                    Graphe gNeg = Graphe.getDefaultGrapheOrienterNegatif();
                    Resultat resBell = gNeg.getBellmanFord(depart, arrivee);
                    if (resBell != null) {
                        List<Arete> edgesBell = buildEdgesFromPath(gNeg, resBell.getChemin());
                        grapheView.runAlgorithmStepByStep(edgesBell);
                    }
                    break;
                case "Floyd-Warshall":
                    Resultat resFloyd = graphe.getFloydWarshall();
                    String cheminStr = graphe.cheminFloydWarshall(resFloyd.getMatricePere(), depart, arrivee);
                    if (cheminStr == null) {
                        System.out.println("Aucun chemin trouvé avec Floyd-Warshall.");
                        break;
                    }
                    // On inverse correctement pour buildEdgesFromPath
                    String[] nomsChemin = cheminStr.split("->");
                    List<String> nomsList = new ArrayList<>(Arrays.asList(nomsChemin));
                    Collections.reverse(nomsList);
                    String cheminCorrect = String.join(" -> ", nomsList);
                    List<Arete> edgesFloyd = buildEdgesFromPath(graphe, cheminCorrect);
                    if (edgesFloyd.isEmpty()) System.out.println("Aucune arête à animer !");
                    grapheView.runAlgorithmStepByStep(edgesFloyd);
                    break;
            }
        });

        resetButton.setOnAction(e -> {
            grapheView.resetGraph();
            grapheView.startSimulation();
        });

        HBox controls = new HBox(10, algoMenu, departMenu, arriveMenu, runButton, resetButton);
        StackPane graphPane = new StackPane(graphRoot);
        graphPane.setPrefSize(1200, 700);
        root.getChildren().addAll(controls, graphPane);

        stage.setScene(scene);
        stage.setTitle("Simulation Graphe");
        stage.show();

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
