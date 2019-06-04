package com.eyes.eyes.Dijkstra;

public class Edge {
    private double weight;
    private Vertex startVertex;
    private Vertex targetVertex;
    private String direction;

    public Edge(double weight, Vertex startVertex, Vertex targetVertex,String direction) {
        this.weight = weight;
        this.startVertex = startVertex;
        this.targetVertex = targetVertex;
        this.direction = direction;

    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public Vertex getStartVertex() {
        return startVertex;
    }

    public void setStartVertex(Vertex startVertex) {
        this.startVertex = startVertex;
    }

    public Vertex getTargetVertex() {
        return targetVertex;
    }

    public String getDir(){
        return direction;
    }

    public void setTargetVertex(Vertex targetVertex) {
        this.targetVertex = targetVertex;
    }
}