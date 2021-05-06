package com.fem1d;

import com.fem1d.clases.element;
import com.fem1d.clases.mesh;

import javax.lang.model.util.Elements;
import javax.swing.text.Element;
import java.util.*;
public class SEL {
    enum lines {NOLINE,SINGLELINE,DOUBLELINE};
    enum modes {NOMODE,INT_FLOAT,INT_INT_INT};
    enum parameter {ELEMENT_LENGTH,THERMAL_CONDUCTIVITY,HEAT_SOURCE};
    enum size {NODES,ELEMENTS,DIRICHLET,NEUMANN};


    double[][] createLocalK(int element,mesh m){
        //Se prepara la matriz y sus dos filas (se sabe que es una matriz 2x2)
        double [][] matriz = new double[2][2];
        Vector row1=new Vector(), row2=new Vector();

        //De acuerdo a la formulación, la matriz local K tiene la forma:
        //          (k/l)*[ 1 -1 ; -1 1 ]

        //Se extraen del objeto mesh los valores de k y l
        float k = m.getParameter(parameter.THERMAL_CONDUCTIVITY.ordinal()), l = m.getParameter(parameter.ELEMENT_LENGTH.ordinal());
        //Se crean las filas
        //System.out.println(k+"/"+l+"=="+k/l);
        row1.add(0,k/l);row1.add(0,-k/l);
        row2.add(0,-k/l);row2.add(0,k/l);

        //Se insertan las filas en la matriz

        matriz[0][0]=new Double(row1.get(0).toString()); matriz[0][1]=new Double(row1.get(1).toString());
        matriz[1][0]=new Double(row2.get(0).toString()); matriz[1][1]=new Double(row2.get(1).toString());

        //showMatrix(matriz);
        //System.out.println("cambio de local");
        return matriz;
    }

    Vector createLocalb(int element,mesh m){
        //Se prepara el vector b (se sabe que será un vector 2x1)
        Vector b=new Vector();;

        //Se sabe que el vector local b tendrá la forma:
        //          (Q*l/2)*[ 1 ; 1 ]

        //Se extraen del objeto mesh los valores de Q y l
        float Q = m.getParameter(parameter.HEAT_SOURCE.ordinal()), l = m.getParameter(parameter.ELEMENT_LENGTH.ordinal());
        //Se insertan los datos en el vector
        b.add(Q*l/2); b.add(Q*l/2);



        //System.out.println("vector");
        showVector(b);

        return b;
    }



    void crearSistemasLocales(mesh m, Vector localKs, Vector localbs){
        //Se recorren los elementos
        System.out.println(m.getSize(size.ELEMENTS.ordinal()));
        for(int i=0;i<m.getSize(size.ELEMENTS.ordinal());i++){
            //Por cada elemento, se crea su K y su b
            localKs.addElement(createLocalK(i,m));
            localbs.addElement(createLocalb(i,m));

        }
    }

    //Assembly

    void assemblyK(element e,double[][] localK, double[][] K){
        //determinar indices de K Global
        int index1= e.getNode1()-1;
        int index2= e.getNode2()-1;

        //definir submatrices
        K[index1][index1]   +=  localK[0][0];
        K[index1][index2]   +=  localK[0][1];
        K[index2][index1]   +=  localK[1][0];
        K[index2][index2]   +=  localK[1][1];
    }

    void assemblyB(element e,Vector localB, Vector B){
        int index1= e.getNode1()-1;
        int index2= e.getNode2()-1;


        double a= Double.parseDouble(B.elementAt(index1).toString());
        double b= Double.parseDouble(B.elementAt(index2).toString());

        a += Double.parseDouble(localB.elementAt(0).toString());
        b += Double.parseDouble(localB.elementAt(1).toString());

        B.add(index1,a);
        B.add(index2,b);

       // System.out.println(localB.get(0).getClass().getName());
     //   System.out.println(B.get(0).getClass().getName());

        //System.out.println(Double.parseDouble(B.elementAt(index1).toString()));
//        definir celdas de subvector
       //B.elementAt(index1) +=  double.parsedouble(localB.elementAt(0));
      // B.elementAt(index2).toString() +=  Double.parseDouble(localB.elementAt(1).toString());

    }

    void Assembly(mesh m, Vector<double[][]> localKs, Vector<Vector> localBs, double[][] K, Vector B){
        for (int i = 0; i < m.getSize(size.ELEMENTS.ordinal()); i++) {
            //extraer elemento actual
            element e= m.getElement(i);

            //K y B Ensamblaje
            //showMatrix(localKs.elementAt(i));
            //ShowKs(localKs);
            assemblyK(e, localKs.get(i), K);

            assemblyB(e, localBs.elementAt(i), B);
        }

    }


    //mostrar vector

    void showVector(Vector b){
        System.out.print("[\t");
        for (int i = 0; i < b.size(); i++) {
            System.out.print(b.elementAt(i)+"\t");
        }
        System.out.print("]\n");
    }

    void showVectorArray(Vector<Vector> bb){
        for (int i = 0; i < bb.size(); i++) {
            System.out.println("b del elemento #"+i+1);
            showVector(bb.elementAt(i));
            System.out.println("*********************************\n");
        }
    }

    //mostrar matrices

    void showMatrix(Double[][] K){
        for (int i = 0; i <K.length ; i++) {
            System.out.print("[\t");
            for (int j = 0; j < K.length; j++) {
                System.out.print(K[i][j]+"\t");
            }
            System.out.println("]");
        }
        System.out.println("\n");
    }

    void ShowKs(Vector<Double[][]> Ks){
        for (int i = 0; i < Ks.size(); i++) {
            System.out.print("K del elemento #" +i);
            showMatrix(Ks.elementAt(i));
            System.out.println("*********************************\n");
        }
    }




}
