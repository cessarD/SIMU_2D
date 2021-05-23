package com.fem1d.clases;

import java.util.ArrayList;



public class mesh {
    enum lines {NOLINE,SINGLELINE,DOUBLELINE};
    enum modes {NOMODE,INT_FLOAT,INT_INT_INT};
    enum parameter {THERMAL_CONDUCTIVITY,HEAT_SOURCE};
    enum size {NODES,ELEMENTS,DIRICHLET,NEUMANN};

    double parameters[] = new double[2];
    int sizes[] = new int[4];
    int indices[];
    ArrayList<node> nodes = new ArrayList<node>();
    ArrayList<element> elements = new ArrayList<element>();
    ArrayList<condition> dirichlet = new ArrayList<condition>();
    ArrayList<condition> neumann = new ArrayList<condition>();



        //revisar enums



    public  void setParameters(double k,double Q){

        parameters[parameter.THERMAL_CONDUCTIVITY.ordinal()]=k;
        parameters[parameter.HEAT_SOURCE.ordinal()]=Q;
    }
    public void setSizes(int nnodes,int neltos,int ndirich,int nneu){
        sizes[size.NODES.ordinal()] = nnodes;
        sizes[size.ELEMENTS.ordinal()] = neltos;
        sizes[size.DIRICHLET.ordinal()] = ndirich;
        sizes[size.NEUMANN.ordinal()] = nneu;
        indices = new int[ndirich];
    }
    public   int getSize(int s){
        return sizes[s];
    }
    public   int[] getIndices(){
        return indices;
    }
    public   double getParameter(int p){
        return parameters[p];
    }

    /* funcion no necesaria, los arraylist no necesitan saber el tamaño
    public   void createData(){
        node_list = new node[sizes[NODES]];
        element_list = new element[sizes[ELEMENTS]];
        dirichlet_list = new condition[sizes[DIRICHLET]];
        neumann_list = new condition[sizes[NEUMANN]];
    }*/

    public ArrayList<node> getNodes(){
        return nodes;
    }
    public ArrayList<element> getElements(){
        return elements;
    }
    public ArrayList<condition> getDirichlet(){
        return dirichlet;
    }
    public ArrayList<condition> getNeumann(){
        return neumann;
    }
    public node getNode(int i){
        return nodes.get(i);
    }
    public element getElement(int i){
        return elements.get(i);
    }
    public condition getCondition(int i, int type){
        if(type == size.DIRICHLET.ordinal()) return dirichlet.get(i);
        else return neumann.get(i);
    }

}
