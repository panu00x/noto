import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.awt.geom.*;
import java.io.*;
import java.lang.Math;
import java.util.Random;
import java.util.ArrayList;
import java.util.Stack;     // Used in Interpreter class
import javax.imageio.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.SwingUtilities;
import javax.swing.undo.*;

import java.util.Scanner;

public class Network extends Diagram {
    private ArrayList<NetworkNode> nodes;


    public Network() {
        super();
        nodes = new ArrayList<NetworkNode>();
    }

    public void addData(String data) {
        Scanner dataScanner = new Scanner(data);
        dataScanner.useDelimiter("--");

        NetworkNode rootNode;
        String value = dataScanner.next().trim();

        if ((rootNode = findNode(value)) == null) {
            rootNode = new NetworkNode(value);
            nodes.add(rootNode);
        }

        NetworkNode otherNode;
        while (dataScanner.hasNext()) {
            value = dataScanner.next().trim();

            if ((otherNode = findNode(value)) == null) {
                otherNode = new NetworkNode(value);
                nodes.add(otherNode);
            }

            otherNode.addConnection(rootNode);
            rootNode.addConnection(otherNode);
        }
    }

    private NetworkNode findNode(String value) {
        for (NetworkNode node : nodes) {
            if (node.getValue().equals(value)) {
                return node;
            }
        }
        return null;
    }

    /* Drawing Methods */

    private int top = 0;
    private int indent = 50;
    private int height = 50;
    private int halfHeight = height / 2;
    private int nudge = 10;

    protected void draw() {
        // Width is equal to the widest an image can be
        int width = Html.PAGE_WIDTH;
        // Set height equal to the maximum depth level * a constant
        int height = (nodes.size() + 1) * 60;

        // Create a new image
        image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics g = image.getGraphics();

        // Fill in background with white
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, width, height);

        // Set image color
        g.setColor(Color.BLACK);

        // Reset the 'top' variable
        top = 0;

        // Iterative network node drawing, O(n^2)...not good
        NetworkNode node;
        for (int i=0; i<nodes.size(); i++) {
            node = nodes.get(i);
            node.x = 0;
            node.y = 60 * i;

            System.out.println("Node: " + node.getValue());
        }

        nodes.get(0).draw(g);
    }
}

class NetworkNode {
    private String value;
    private ArrayList<NetworkNode> connections;
    private boolean drawn;
    private int width, height;
    public int x, y;

    public NetworkNode(String value) {
        this.value = value;
        this.drawn = false;
        this.connections = new ArrayList<NetworkNode>();
    }

    public String getValue() {
        return value;
    }

    public void addConnection(NetworkNode node) {
        connections.add(node);
    }

    public ArrayList<NetworkNode> getConnections() {
        return connections;
    }

    public boolean isDrawn() {
        return drawn;
    }

    public void draw(Graphics g) {
        width = g.getFontMetrics().stringWidth(value) + 20;
        height = 50;

        g.drawString(value, x + 10, y + height / 2);
        g.drawRect(x, y, width, height);

        drawn = true;

        // Draw connection lines
        for (int i=0; i<connections.size(); i++) {
            NetworkNode node = connections.get(i);
            if (!node.isDrawn()) {
                node.draw(g);
            }
            // g.drawLine(x, y, connections[i].x, connections[i].y);
        }
    }
}
