package com.fem1d;

import com.fem1d.clases.condition;
import com.fem1d.clases.element;
import com.fem1d.clases.mesh;
import com.fem1d.clases.node;

import javax.lang.model.util.Elements;
import javax.swing.text.Element;
import java.util.*;
public class SEL {
    enum lines {NOLINE,SINGLELINE,DOUBLELINE};
    enum modes {NOMODE,INT_FLOAT,INT_INT_INT};
    enum parameter {THERMAL_CONDUCTIVITY,HEAT_SOURCE};
    enum size {NODES,ELEMENTS,DIRICHLET,NEUMANN};


    MATH_TOOLS mtools = new MATH_TOOLS();

    double calculateLocalD(int i,mesh m){
        float D,a,b,c,d;

        element e = m.getElement(i);

        node n1 = m.getNode(e.getNode1()-1);
        node n2 = m.getNode(e.getNode2()-1);
        node n3 = m.getNode(e.getNode3()-1);

        a=n2.getX()-n1.getX();b=n2.getY()-n1.getY();
        c=n3.getX()-n1.getX();d=n3.getY()-n1.getY();
        D = a*d - b*c;

        return D;
    }

    double calculateLocalArea(int i,mesh m){
        //Se utiliza la fórmula de Herón
        double A,s,a,b,c;
        element e = m.getElement(i);
        node n1 = m.getNode(e.getNode1()-1);
        node n2 = m.getNode(e.getNode2()-1);
        node n3 = m.getNode(e.getNode3()-1);

        a = calculateMagnitude(n2.getX()-n1.getX(),n2.getY()-n1.getY());
        b = calculateMagnitude(n3.getX()-n2.getX(),n3.getY()-n2.getY());
        c = calculateMagnitude(n3.getX()-n1.getX(),n3.getY()-n1.getY());
        s = (a+b+c)/2;

        A = Math.sqrt(s*(s-a)*(s-b)*(s-c));

        return A;
    }
    double calculateMagnitude(float v1, float v2){
        return Math.sqrt(Math.pow(v1,2)+Math.pow(v2,2));
    }


    double[][] calculateLocalA(int i,double[][] A,mesh m){
        element e = m.getElement(i);
        node n1 = m.getNode(e.getNode1()-1);
        node n2 = m.getNode(e.getNode2()-1);
        node n3 = m.getNode(e.getNode3()-1);

        A[0][0] = n3.getY()-n1.getY(); A[0][1] = n1.getY()-n2.getY();
        A[1][0] = n1.getX()-n3.getX();  A[1][1] = n2.getX()-n1.getX();

        return A;
    }
    double[][] calculateB(double[][] A){

        A[0][0] = -1; A[0][1] = 1;A[0][2] = 0;
        A[1][0] = -1; A[1][1] = 0;A[1][2] = 1;

        return A;
    }

    double[][] createLocalK(int element,mesh m){
        //Se prepara la matriz y sus dos filas (se sabe que es una matriz 3x3)
        double [][] K = new double[3][3];
        double [][] A = new double[2][2];
        double [][] B = new double[2][3];


        double D,Ae,k=m.getParameter(parameter.THERMAL_CONDUCTIVITY.ordinal());



        D = calculateLocalD(element,m);
        Ae = calculateLocalArea(element,m);
        mtools.zeroesm(A,2,2);
        mtools.zeroesm(B,2,3);

        double [][] At= new double[2][2];
        double [][] Bt= new double[3][2];
        A=calculateLocalA(element,A,m);
        B=calculateB(B);


        At= mtools.transpose(A,At);
        Bt= mtools.transpose(B,Bt);


        K= mtools.productRealMatrix(k*Ae/(D*D),mtools.productMatrixMatrix(Bt,mtools.productMatrixMatrix(At,mtools.productMatrixMatrix(A,B,2,2,3),2,2,3),3,2,3),K);

        return K;
    }



    float calculateLocalJ(int i,mesh m){
        float J,a,b,c,d;
        element e = m.getElement(i);
        node n1 = m.getNode(e.getNode1()-1);
        node n2 = m.getNode(e.getNode2()-1);
        node n3 = m.getNode(e.getNode3()-1);

        a=n2.getX()-n1.getX();b=n3.getX()-n1.getX();
        c=n2.getY()-n1.getY();d=n3.getY()-n1.getY();
        J = a*d - b*c;

        return J;
    }

    Vector createLocalb(int element,mesh m){
        Vector b = new Vector();;

        double Q = m.getParameter(parameter.HEAT_SOURCE.ordinal()),J,b_i;
        J = calculateLocalJ(element,m);

        b_i = Q*J/6;
        b.add(b_i);
        b.add(b_i);
        b.add(b_i);
        return b;


    }



    void crearSistemasLocales(mesh m, Vector localKs, Vector localbs){


        for(int i=0;i<m.getSize(size.ELEMENTS.ordinal());i++){


            localKs.addElement(createLocalK(i,m));

           localbs.addElement(createLocalb(i,m));

        }

    }

    //Assembly

    void assemblyK(element e,double[][] localK, double[][] K){
        //determinar indices de K Global
        int index1= e.getNode1()-1;
        int index2= e.getNode2()-1;
        int index3= e.getNode3()-1;

        //definir submatrices
        K[index1][index1]   +=  localK[0][0];
        K[index1][index2]   +=  localK[0][1];
        K[index1][index3]   += localK[0][2];


        K[index2][index1]   +=  localK[1][0];
        K[index2][index2]   +=  localK[1][1];
        K[index2][index3]   += localK[1][2];

        K[index3][index1]   +=  localK[2][0];
        K[index3][index2]   +=  localK[2][1];
        K[index3][index3]   +=  localK[2][2];

    }

    void assemblyB(element e,Vector localB, Vector B){
        int index1= e.getNode1()-1;
        int index2= e.getNode2()-1;
        int index3= e.getNode3()-1;

        //according to code main

        double a= Double.parseDouble(B.elementAt(index1).toString())+Double.parseDouble(localB.elementAt(0).toString());
        double b= Double.parseDouble(B.elementAt(index2).toString())+Double.parseDouble(localB.elementAt(1).toString());
        double c= Double.parseDouble(B.elementAt(index3).toString())+Double.parseDouble(localB.elementAt(2).toString());


        B.remove(index3);
        B.add(index3,c);
       B.remove(index2);
        B.add(index2,b);
       B.remove(index1);



        B.add(index1,a);



    }

    void Assembly(mesh m, Vector<double[][]> localKs, Vector<Vector> localBs, double[][] K, Vector B){
        for (int i = 0; i < m.getSize(size.ELEMENTS.ordinal()); i++) {
            //extraer elemento actual
            element e= m.getElement(i);

            //K y B Ensamblaje
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
        System.out.println("]\n");
    }

    void showVectorArray(Vector<Vector> bb){
        for (int i = 0; i < bb.size(); i++) {
            System.out.println("b del elemento #"+i+1);
            showVector(bb.elementAt(i));
            System.out.println("*********************************\n");
        }
    }

    //mostrar matrices

    void showMatrix(double[][] K){
        for (int i = 0; i <K.length ; i++) {
            System.out.print("[\t");
            for (int j = 0; j < K[0].length; j++) {
                System.out.print(String.format("%.2f",K[i][j])+"\t");
            }
            System.out.println("]");
        }
        System.out.println("\n");
    }

    void ShowKs(Vector<double[][]> Ks){
        for (int i = 0; i < Ks.size(); i++) {
            System.out.print("K del elemento #" +i);
            showMatrix(Ks.elementAt(i));
            System.out.println("*********************************\n");
        }
    }

    void applyNeumann(mesh m,Vector b){
        //Se recorren las condiciones de Neumann, una por una
        for(int i=0;i<m.getSize(size.NEUMANN.ordinal());i++){
            //Se extrae la condición de Neumann actual
            condition c = m.getCondition(i,size.NEUMANN.ordinal());
            //En la posición de b indicada por el nodo de la condición,
            //se agrega el valor indicado por la condición
            float n = new Float(b.get(c.getNode1()-1).toString());

            n += c.getValue();
            b.remove(c.getNode1()-1);
            b.add(c.getNode1()-1,n);
        }
    }

     double[][] removerFila(double[][] matriz, int fila) {
        if (fila < 0 || fila >= matriz.length) {
            return matriz;
        } else {
            double[][] nueva = new double[matriz.length-1][matriz[0].length];

            int b=0;
            for (int l =0; l < matriz.length-1; l++) {

                if((fila)==b){
                    b++;
                }


                    for (int j = 0; j < matriz[0].length; j++) {


                        nueva[l][j]=matriz[b][j];


                    }
                    b++;

                }

            return nueva;
        }
    }
    Vector removerelemento(mesh m,Vector b,int index){
        Vector a = new Vector();

        for(int i = 0; i<b.size(); i++){

            if(i!=index){

                a.add(b.get(i));
            }
        }

        return a;
    }

    double[][] removerColumna(double[][] matriz, int columna) {
        double[][] nueva = new double[matriz.length][matriz[0].length-1];
        if (columna < 0 || columna >= matriz.length) {
            return matriz;
        } else {
            for (int l =0; l < nueva.length; l++) {

                int b=0;

                    for (int j = 0; j < nueva[0].length; j++) {
                        if(columna==b){
                            b++;
                        }

                        nueva[l][j]=matriz[l][b];
                        b++;



                }


        }

        }
        return nueva;
    }

    void calculate(double[][] k, Vector b, Vector T){
        MATH_TOOLS mt= new MATH_TOOLS();

        double[][] kinv= new double[9][9];

        //showMatrix(k);
        //invertir matrix

        k=mt.inverseMatrix(k,kinv);




        //calcular producto
        System.out.println("Ultimos ajustes");
        mt.productMatrixVector(k,b,T);
    }
}
