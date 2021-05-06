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
    enum lines {NOLINE,SINGLELINE,DOUBLELINE};
    enum modes {NOMODE,INT_FLOAT,INT_INT_INT};
    enum parameter {ELEMENT_LENGTH,THERMAL_CONDUCTIVITY,HEAT_SOURCE};
    enum size {NODES,ELEMENTS,DIRICHLET,NEUMANN};

    public static void main(String[] args) {
        //instanciar archivos externos
        SEL tools= new SEL();
        MATH_TOOLS mtools= new MATH_TOOLS();




        mesh m = new mesh();
        Vector localb=new Vector();
        Vector localk=new Vector();
        //revisar tamaño de matriz final


        ReadMeshandConditions(m);
        tools.crearSistemasLocales(m,localk,localb);


        //comprobacion que localk y localb tienen datos
        //SE PUEDE BORRAR
        //double [][] j = (double[][]) localk.get(2);
        //System.out.println(j[0][0]);
       // System.out.println(localb.get(0));
        double [][] k = new double[m.getSize(size.NODES.ordinal())][m.getSize(size.NODES.ordinal())];
        Vector b=new Vector();
        Vector T=new Vector();

       // System.out.println(k[0][0]);
       mtools.zeroesm(k,m.getSize(size.NODES.ordinal()));
        mtools.zeroesv(b,m.getSize(size.NODES.ordinal()));



        tools.Assembly(m, localk,localb,k,b);
        System.out.println("vector final B");
        for (int i=0;i<10;i++){
            System.out.println(b.get(i));
        }
       // System.out.println(b.get(0));

    }






}
