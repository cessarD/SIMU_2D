package com.fem1d;

import com.fem1d.clases.condition;
import com.fem1d.clases.element;
import com.fem1d.clases.mesh;
import com.fem1d.clases.node;
import com.fem1d.SEL;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.*;

import static com.fem1d.ReadFile.ReadMeshandConditions;

public class Main {
    enum lines {NOLINE, SINGLELINE, DOUBLELINE}

    ;

    enum modes {NOMODE, INT_FLOAT, INT_INT_INT}

    ;

    enum parameter {ELEMENT_LENGTH, THERMAL_CONDUCTIVITY, HEAT_SOURCE}

    ;

    enum size {NODES, ELEMENTS, DIRICHLET, NEUMANN}

    ;

    public static void main(String[] args) {
        //instanciar archivos externos
        SEL tools = new SEL();
        MATH_TOOLS mtools = new MATH_TOOLS();


        mesh m = new mesh();
        Vector localb = new Vector();
        Vector localk = new Vector();
        //revisar tamaño de matriz final


        ReadMeshandConditions(m);
        tools.crearSistemasLocales(m, localk, localb);


        //comprobacion que localk y localb tienen datos
        //SE PUEDE BORRAR
        //double [][] j = (double[][]) localk.get(2);
        //System.out.println(j[0][0]);
        // System.out.println(localb.get(0));
        double[][] k = new double[m.getSize(size.NODES.ordinal())][m.getSize(size.NODES.ordinal())];
        Vector b = new Vector();
        Vector T = new Vector();
        double[][] kd = new double[m.getSize(size.NODES.ordinal())][m.getSize(size.NODES.ordinal())];
        Vector bd = new Vector();
        // System.out.println(k[0][0]);
        mtools.zeroesm(k, m.getSize(size.NODES.ordinal()));
        mtools.zeroesv(b, m.getSize(size.NODES.ordinal()));


        tools.Assembly(m, localk, localb, k, b);

        //    System.out.println("K antes de entrar al desvergue");
        //    for (int l = 0; l < k.length; l++) {
        //     for (int j = 0; j < k[0].length; j++) {
        //      System.out.print("[" + l + "][" + j + "]= " + k[l][j]);

        //    }
        //    System.out.println();
        //  }
    //    System.out.println("vector final B pre neumann");
     //   tools.showVector(b);

        tools.applyNeumann(m, b);
     //   System.out.println("vector final B con neumann");
      // tools.showVector(b);
       // System.out.println("vector final B pre diri");
      //  tools.showVector(b);
        //System.out.println();


        //DA ERROR CON OBJETOS
        //tools.applyDirichlet(m, k, b,kd,bd);

        System.out.println("Matriz Original");
        tools.showMatrix(k);

            // aplicacion de Dirichlet
        for(int i = 0; i<m.getSize(SEL.size.DIRICHLET.ordinal()); i++){
            condition c = m.getCondition(i, SEL.size.DIRICHLET.ordinal());
            int index = c.getNode1()-1;
            k=tools.removerFila(k,index);
            b=tools.removerelemento(m,b,index);
            for(int row=0; row<k.length;row++ ){
                double valor= k[row][index];
                double actual= Double.parseDouble(b.get(row).toString());
                double nuevo = actual + (1*c.getValue()*valor);
                b.remove(row);
                b.add(row,nuevo);
            }
            k=tools.removerColumna(k,index);
            }



        System.out.println("Matriz con Dirichlet eliminacion de fila y columna");
        tools.showMatrix(k);

        System.out.println("vector final B con diri");
        tools.showVector(b);
        System.out.println();

        // System.out.println("K despues de entrar al desvergue");
        // for (int l = 0; l < k.length; l++) {
        //    for (int j = 0; j < k[0].length; j++) {
       // System.out.print("[" + l + "][" + j + "]= " + k[l][j]);



        // Calcular
        tools.calculate(k,b,T);



    }

}









