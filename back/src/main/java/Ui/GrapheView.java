package Ui;

import Graphe.*;
import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;

import java.util.*;

public class GrapheView {

    class Node {
        Sommet sommet;
        double x, y, vx, vy;
        Circle circle;
        Text label;
    }

    private Graphe graphe;
    private Group root;
    private Map<String, Node> nodes;
    private List<Line> edges;
    private List<Text> edgeLabels;
    private AnimationTimer timer;
    private Runnable onStableCallback;

    // Timer pour animation des arêtes
    private Queue<Arete> pathQueue;
    private AnimationTimer pathTimer;

    // Paramètres physiques
    private static final double WIDTH = 1000;
    private static final double HEIGHT = 700;
    private static final double REPULSION = 60000;
    private static final double SPRING_LENGTH = 200;
    private static final double SPRING_STRENGTH = 0.02;
    private static final double DAMPING = 0.8;
    private static final double SCALE = 0.07;

    private int stableFrames = 0;
    private static final double STABLE_THRESHOLD = 3; // vitesse moyenne minimale
    private static final int STABLE_FRAME_COUNT = 8;   // nombre de frames calmes avant arrêt

    public GrapheView(Graphe graphe, Group root) {
        this.graphe = graphe;
        this.root = root;
        initNodesAndEdges();
    }

    public void setOnStable(Runnable callback) {
        this.onStableCallback = callback;
    }

    private void initNodesAndEdges() {
        nodes = new HashMap<>();
        edges = new ArrayList<>();
        edgeLabels = new ArrayList<>();
        Random rand = new Random();

        // Nœuds
        for (Sommet s : graphe.getSommetsTrier()) {
            Node n = new Node();
            n.sommet = s;
            n.x = rand.nextDouble() * WIDTH;
            n.y = rand.nextDouble() * HEIGHT;
            n.vx = n.vy = 0;
            n.circle = new Circle(n.x, n.y, 28, Color.LIGHTBLUE);
            n.circle.setVisible(true);
            n.label = new Text(s.getNom());
            n.label.setVisible(true);
            n.label.setFill(Color.BLACK);
            n.label.setStyle("-fx-font-weight: bold; -fx-font-size: 13px;");

            double textWidth = n.label.getLayoutBounds().getWidth();
            double textHeight = n.label.getLayoutBounds().getHeight();
            n.label.setX(n.x - textWidth / 2);
            n.label.setY(n.y + textHeight / 4);

            nodes.put(s.getNom(), n);
            root.getChildren().addAll(n.circle, n.label);
        }

        // Arêtes
        for (Arete a : graphe.getAretes()) {
            Node src = nodes.get(a.getSource().getNom());
            Node dst = nodes.get(a.getDestination().getNom());
            Line line = new Line(src.x, src.y, dst.x, dst.y);
            line.setStroke(Color.GRAY);
            edges.add(line);
            root.getChildren().add(0, line);

            double mx = (src.x + dst.x) / 2;
            double my = (src.y + dst.y) / 2;

            double dx = dst.x - src.x;
            double dy = dst.y - src.y;
            double dist = Math.sqrt(dx * dx + dy * dy);

            double nx = -dy / dist;
            double ny = dx / dist;
            double offset = 10;

            Text distanceText = new Text(mx + nx * offset, my + ny * offset, String.valueOf(a.getPoids()));
            distanceText.setFill(Color.DARKBLUE);
            distanceText.setStyle("-fx-font-size: 13px; -fx-font-weight: bold;");
            distanceText.setVisible(false);
            edgeLabels.add(distanceText);
            root.getChildren().add(distanceText);
        }
    }

    public void startSimulation() {
        timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                updatePhysics();   // fait bouger les nœuds
                updateNodeUI();    // mise à jour des positions des cercles et labels des sommets

                double totalV = 0;
                for (Node n : nodes.values()) {
                    totalV += Math.sqrt(n.vx * n.vx + n.vy * n.vy);
                }
                double avgV = totalV / nodes.size();

                if (avgV < STABLE_THRESHOLD) stableFrames++;
                else stableFrames = 0;

                // Dès que le graphe est stable
                if (stableFrames > STABLE_FRAME_COUNT) {
                    timer.stop();
                    positionEdgeLabels(); // positionne et fait apparaître les poids des arêtes

                    if (onStableCallback != null) {
                        onStableCallback.run();
                        onStableCallback = null;
                    }
                }
            }
        };
        timer.start();
    }

    public void stopSimulation() {
        if (timer != null) timer.stop();
    }

    private void updatePhysics() {
        // répulsion
        for (Node n1 : nodes.values()) {
            double fx = 0, fy = 0;
            for (Node n2 : nodes.values()) {
                if (n1 == n2) continue;
                double dx = n1.x - n2.x;
                double dy = n1.y - n2.y;
                double dist = Math.sqrt(dx*dx + dy*dy) + 0.1;
                double force = REPULSION / (dist*dist);
                fx += (dx / dist) * force;
                fy += (dy / dist) * force;
            }
            n1.vx += fx;
            n1.vy += fy;
        }

        // attraction
        for (Arete a : graphe.getAretes()) {
            Node src = nodes.get(a.getSource().getNom());
            Node dst = nodes.get(a.getDestination().getNom());
            double dx = dst.x - src.x;
            double dy = dst.y - src.y;
            double dist = Math.sqrt(dx*dx + dy*dy) + 0.1;
            double force = SPRING_STRENGTH * (dist - SPRING_LENGTH);
            double fx = (dx / dist) * force;
            double fy = (dy / dist) * force;

            src.vx += fx;
            src.vy += fy;
            dst.vx -= fx;
            dst.vy -= fy;
        }

        for (Node n : nodes.values()) {
            n.vx *= DAMPING;
            n.vy *= DAMPING;
            n.x += n.vx * SCALE;
            n.y += n.vy * SCALE;

            n.x = Math.max(50, Math.min(WIDTH-50, n.x));
            n.y = Math.max(50, Math.min(HEIGHT-50, n.y));
        }
    }

    // Mise à jour des nœuds uniquement (cercles + labels)
    private void updateNodeUI() {
        for (Node n : nodes.values()) {
            n.circle.setCenterX(n.x);
            n.circle.setCenterY(n.y);
            double textWidth = n.label.getLayoutBounds().getWidth();
            double textHeight = n.label.getLayoutBounds().getHeight();
            n.label.setX(n.x - textWidth / 2);
            n.label.setY(n.y + textHeight / 4);
        }

        // Les arêtes elles, sont mises à jour même pendant le placement
        for (int i = 0; i < graphe.getAretes().size(); i++) {
            Arete a = graphe.getAretes().get(i);
            Node src = nodes.get(a.getSource().getNom());
            Node dst = nodes.get(a.getDestination().getNom());
            Line line = edges.get(i);
            line.setStartX(src.x);
            line.setStartY(src.y);
            line.setEndX(dst.x);
            line.setEndY(dst.y);
        }
    }

    // Positionne et aligne les labels des arêtes après stabilisation
    private void positionEdgeLabels() {
        for (int i = 0; i < graphe.getAretes().size(); i++) {
            Arete a = graphe.getAretes().get(i);
            Node src = nodes.get(a.getSource().getNom());
            Node dst = nodes.get(a.getDestination().getNom());
            Text text = edgeLabels.get(i);

            double mx = (src.x + dst.x) / 2;
            double my = (src.y + dst.y) / 2;
            double dx = dst.x - src.x;
            double dy = dst.y - src.y;
            double dist = Math.sqrt(dx*dx + dy*dy);

            // angle pour que le texte suive l'arête
            double angle = Math.toDegrees(Math.atan2(dy, dx));
            if (angle > 90 || angle < -90) angle += 180; // texte toujours lisible
            text.setRotate(angle);

            // décalage perpendiculaire pour éviter chevauchement
            double nx = -dy / dist;
            double ny = dx / dist;
            double offset = 15;
            text.setX(mx + nx * offset);
            text.setY(my + ny * offset);

            text.setVisible(true);
        }
    }

    public void resetGraph() {
        stopSimulation();
        if (pathTimer != null) pathTimer.stop();
        for (Line line : edges) {
            line.setStroke(Color.GRAY);
            line.setStrokeWidth(1);
        }
    }
    public void runAlgorithmStepByStep(Graphe pathGraphe) {
        if (pathGraphe == null || pathGraphe.getAretes() == null) {
            System.out.println("Aucune arête à animer !");
            return;
        }
        System.out.println("DEBUG: Arêtes du graphe à animer = " + pathGraphe.getAretes().size());
        runAlgorithmStepByStep(pathGraphe.getAretes());
    }


    public void runAlgorithmStepByStep(List<Arete> edgesToAnimate) {
        if (edgesToAnimate == null || edgesToAnimate.isEmpty()) {
            System.out.println("Aucune arête à animer !");
            return;
        }

        resetGraph();
        pathQueue = new LinkedList<>(edgesToAnimate);

        pathTimer = new AnimationTimer() {
            private long lastUpdate = 0;
            @Override
            public void handle(long now) {
                if (now - lastUpdate < 300_000_000) return; // 300ms
                lastUpdate = now;

                Arete a = pathQueue.poll();
                if (a == null) {
                    stop();
                    return;
                }

                for (int i = 0; i < graphe.getAretes().size(); i++) {
                    Arete orig = graphe.getAretes().get(i);
                    if ((orig.getSource().getNom().equals(a.getSource().getNom()) &&
                            orig.getDestination().getNom().equals(a.getDestination().getNom())) ||
                            (orig.getSource().getNom().equals(a.getDestination().getNom()) &&
                                    orig.getDestination().getNom().equals(a.getSource().getNom()))) {

                        Line line = edges.get(i);
                        line.setStroke(Color.RED);
                        line.setStrokeWidth(3);
                        break;
                    }
                }
            }
        };

        System.out.println("DEBUG: Animation lancée pour " + edgesToAnimate.size() + " arêtes");
        pathTimer.start();
    }


}
