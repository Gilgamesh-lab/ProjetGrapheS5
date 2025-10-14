package ui;

import Graphe.*;
import javafx.animation.AnimationTimer;
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

    // Paramètres physiques
    private static final double WIDTH = 1000;
    private static final double HEIGHT = 700;
    private static final double REPULSION = 60000;
    private static final double SPRING_LENGTH = 150;
    private static final double SPRING_STRENGTH = 0.02;
    private static final double DAMPING = 0.8;
    private static final double SCALE = 0.05;

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
            n.circle = new Circle(n.x, n.y, 18, Color.LIGHTBLUE);
            n.circle.setVisible(true); // toujours visible
            n.label = new Text(n.x - 15, n.y + 5, s.getNom());
            n.label.setVisible(true); // toujours visible
            nodes.put(s.getNom(), n);
            root.getChildren().addAll(n.circle, n.label);
        }

        // Arêtes
        for (Arete a : graphe.getAretes()) {
            Node src = nodes.get(a.getSource().getNom());
            Node dst = nodes.get(a.getDestination().getNom());
            Line line = new Line(src.x, src.y, dst.x, dst.y);
            line.setStroke(Color.GRAY); // visible dès le départ
            edges.add(line);
            root.getChildren().add(0, line);

            // texte distance
            Text distanceText = new Text((src.x + dst.x)/2, (src.y + dst.y)/2, String.valueOf(a.getPoids()));
            distanceText.setFill(Color.BLACK);
            distanceText.setVisible(false); // caché pendant simulation
            edgeLabels.add(distanceText);
            root.getChildren().add(distanceText);
        }
    }

    public void startSimulation() {
        timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                updatePhysics();
                updateUI();
                if (isStable()) stop();
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

        // mise à jour positions
        for (Node n : nodes.values()) {
            n.vx *= DAMPING;
            n.vy *= DAMPING;
            n.x += n.vx * SCALE;
            n.y += n.vy * SCALE;

            n.x = Math.max(50, Math.min(WIDTH-50, n.x));
            n.y = Math.max(50, Math.min(HEIGHT-50, n.y));
        }
    }

    private void updateUI() {
        for (Node n : nodes.values()) {
            n.circle.setCenterX(n.x);
            n.circle.setCenterY(n.y);
            n.label.setX(n.x - 15);
            n.label.setY(n.y + 5);
        }

        for (int i = 0; i < graphe.getAretes().size(); i++) {
            Arete a = graphe.getAretes().get(i);
            Node src = nodes.get(a.getSource().getNom());
            Node dst = nodes.get(a.getDestination().getNom());
            Line line = edges.get(i);
            line.setStartX(src.x);
            line.setStartY(src.y);
            line.setEndX(dst.x);
            line.setEndY(dst.y);

            Text text = edgeLabels.get(i);
            text.setX((src.x + dst.x)/2);
            text.setY((src.y + dst.y)/2);
        }
    }

    private boolean isStable() {
        double totalV = 0;
        for (Node n : nodes.values()) {
            totalV += Math.sqrt(n.vx*n.vx + n.vy*n.vy);
        }
        if (totalV < 0.5) {
            if (onStableCallback != null) {
                onStableCallback.run();
                onStableCallback = null;
            }
            // afficher les distances seulement
            for (Text t : edgeLabels) t.setVisible(true);
            return true;
        }
        return false;
    }

    public void highlightPath(Graphe pathGraphe) {
        stopSimulation();
        for (Line line : edges) {
            line.setStroke(Color.GRAY);
            line.setStrokeWidth(1);
        }
        for (Arete a : pathGraphe.getAretes()) {
            for (int i = 0; i < graphe.getAretes().size(); i++) {
                Arete orig = graphe.getAretes().get(i);
                if ((orig.getSource() == a.getSource() && orig.getDestination() == a.getDestination()) ||
                        (orig.getSource() == a.getDestination() && orig.getDestination() == a.getSource())) {
                    edges.get(i).setStroke(Color.RED);
                    edges.get(i).setStrokeWidth(3);
                }
            }
        }
    }
}
