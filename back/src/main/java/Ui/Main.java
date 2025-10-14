package ui;

import Graphe.Graphe;
import Graphe.Resultat;
import Graphe.Sommet;
import Graphe.Arete;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.HashMap;
import java.util.Map;

public class Main extends Application {

    private BorderPane root;
    private javafx.scene.layout.Pane graphPane;
    private Graphe graphe;
    private Map<Sommet, Circle> sommetNodes = new HashMap<>();

    @Override
    public void start(Stage stage) {
        graphe = Graphe.getDefaultGraphe(); // graphe actuel en dur

        root = new BorderPane();
        graphPane = new javafx.scene.layout.Pane();
        graphPane.setPrefSize(800, 600);
        root.setCenter(graphPane);

        // Controls
        ComboBox<String> algoChoice = new ComboBox<>();
        algoChoice.getItems().addAll("Parcours en largeur (BFS)", "Parcours en profondeur (DFS)", "Kruskal");
        algoChoice.getSelectionModel().selectFirst();
        Button runButton = new Button("Exécuter BFS/DFS");

        HBox controls = new HBox(10, algoChoice, runButton);
        controls.setPadding(new Insets(10));
        root.setTop(controls);

        drawGraph(); // dessin initial du graphe

        runButton.setOnAction(e -> {
            String algo = algoChoice.getValue();
            Resultat res;
            String depart = "Rennes"; // tu peux changer le sommet de départ
            if(algo.equals("BFS")) {
                res = graphe.getBFS(depart);
            } else {
                res = graphe.getDFS(depart);
            }
            highlightResult(res);
        });

        Scene scene = new Scene(root, 1000, 700);
        stage.setScene(scene);
        stage.setTitle("Projet Graphes - Guibert - Amegadjen");
        stage.show();
    }

    private void drawGraph() {
        graphPane.getChildren().clear();
        sommetNodes.clear();

        // générer des positions simples pour chaque sommet (en grille)
        int cols = 4;
        int spacing = 150;
        int i = 0;
        for(Sommet sommet : graphe.getSommetsTrier()) {
            int x = 100 + (i % cols) * spacing;
            int y = 100 + (i / cols) * spacing;
            Circle circle = new Circle(x, y, 15, Color.LIGHTBLUE);
            Text label = new Text(x - 10, y - 20, sommet.getNom());
            graphPane.getChildren().addAll(circle, label);
            sommetNodes.put(sommet, circle);
            i++;
        }

        // dessiner les arêtes
        for(Arete arete : graphe.getAretes()) {
            Circle fromNode = sommetNodes.get(arete.getSource());
            Circle toNode = sommetNodes.get(arete.getDestination());
            Line line = new Line(fromNode.getCenterX(), fromNode.getCenterY(),
                    toNode.getCenterX(), toNode.getCenterY());
            line.setStroke(Color.GRAY);
            line.setStrokeWidth(2);
            graphPane.getChildren().add(line);

            // poids au milieu
            double midX = (fromNode.getCenterX() + toNode.getCenterX()) / 2;
            double midY = (fromNode.getCenterY() + toNode.getCenterY()) / 2;
            Text weightLabel = new Text(midX, midY, String.valueOf(arete.getPoids()));
            graphPane.getChildren().add(weightLabel);
        }
    }

    private void highlightResult(Resultat res) {
        drawGraph(); // reset

        Graphe cheminGraphe = res.getGraphe();
        for(Arete arete : cheminGraphe.getAretes()) {
            Circle fromNode = sommetNodes.get(arete.getSource());
            Circle toNode = sommetNodes.get(arete.getDestination());
            Line line = new Line(fromNode.getCenterX(), fromNode.getCenterY(),
                    toNode.getCenterX(), toNode.getCenterY());
            line.setStroke(Color.RED);
            line.setStrokeWidth(3);
            graphPane.getChildren().add(line);
        }

        // colorer les sommets du chemin
        for(Sommet s : cheminGraphe.getSommetsTrier()) {
            Circle c = sommetNodes.get(s);
            c.setFill(Color.ORANGERED);
        }
    }

    public static void main(String[] args) {
        launch();
    }
}
