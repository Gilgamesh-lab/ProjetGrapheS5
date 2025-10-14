package ui;

import Graphe.*;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.Pane;

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
        runButton.setOnAction(e -> {
            String algo = algoMenu.getValue();
            switch(algo) {
                case "BFS":
                    Resultat bfs = graphe.getBFS("Paris");
                    grapheView.highlightPath(bfs.getGraphe());
                    break;
                case "DFS":
                    Resultat dfs = graphe.getDFS("Paris");
                    grapheView.highlightPath(dfs.getGraphe());
                    break;
            }
        });

        Button resetButton = new Button("Relancer simulation");
        resetButton.setDisable(true);
        resetButton.setOnAction(e -> grapheView.startSimulation());

        HBox controls = new HBox(10, algoMenu, runButton, resetButton);
        StackPane graphPane = new StackPane(graphRoot);
        graphPane.setPrefSize(1200, 700);

        root.getChildren().addAll(controls, graphPane);
        stage.setScene(scene);
        stage.setTitle("Simulation Graphe Routier - JavaFX");
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
