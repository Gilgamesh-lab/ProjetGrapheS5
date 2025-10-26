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
                    Resultat dij = graphe.getDijkstra("Bordeaux", "Lille");
                    grapheView.runAlgorithmStepByStep(dij.getGraphe());
                    break;

                case "Bellman-Ford":
                    Graphe g2 = Graphe.getDefaultGrapheOrienterNegatif();
                    Resultat bf = g2.getBellmanFord("Bordeaux", "Lille");
                    grapheView.runAlgorithmStepByStep(bf.getGraphe());
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

    public static void main(String[] args) {
        launch(args);
    }
}
