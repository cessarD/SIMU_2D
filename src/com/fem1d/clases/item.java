package com.fem1d.clases;

public class item {
    private int id;
    private float x;
    private int node1;
    private int node2;
    private float value;

    public int getId(){
        return id;
    }
    public float getX(){
        return x;
    }

    public int getNode1() {
        return node1;
    }

    public int getNode2() {
        return node2;
    }

    public float getValue() {
        return value;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setX(float x) {
        this.x = x;
    }

    public void setNode1(int node1) {
        this.node1 = node1;
    }

    public void setNode2(int node2) {
        this.node2 = node2;
    }

    public void setValue(float value) {
        this.value = value;
    }
}
