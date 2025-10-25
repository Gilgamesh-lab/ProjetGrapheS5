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
        algoMenu.getItems().addAll("BFS", "DFS");
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
                    Resultat bfs = graphe.getBFS("Paris");
                    System.out.println("BFS arêtes : " + bfs.getGraphe().getAretes().size());
                    grapheView.runAlgorithmStepByStep(bfs.getGraphe());
                    break;
                case "DFS":
                    Resultat dfs = graphe.getDFS("Paris");
                    System.out.println("DFS arêtes : " + dfs.getGraphe().getAretes().size());
                    grapheView.runAlgorithmStepByStep(dfs.getGraphe());
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
