package com.fem1d.clases;

public class item {
    private int id;
    private float x;
    private float y;
    private int node1;
    private int node2;
    private int node3;
    private float value;

    public int getId(){
        return id;
    }
    public float getX(){
        return x;
    }
    public float getY(){
        return y;
    }

    public int getNode1() {
        return node1;
    }

    public int getNode2() {
        return node2;
    }
    public int getNode3() {
        return node3;
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
    public void setY(float y) {
        this.y = y;
    }

    public void setNode1(int node1) {
        this.node1 = node1;
    }

    public void setNode2(int node2) {
        this.node2 = node2;
    }
    public void setNode3(int node3) {
        this.node3 = node3;
    }

    public void setValue(float value) {
        this.value = value;
    }
}
