package com.fem1d;

import com.sun.net.httpserver.Authenticator;

import java.awt.*;
import java.util.Vector;

public class MATH_TOOLS {
    void zeroesm(double[][] M,int n){

        for(int i=0;i<n;i++){
            for(int j=0;j<n;j++){

                M[i][j]=0.0;
            }

        }
    }
    void zeroesv(Vector v, int n){

        for(int i=0;i<n;i++){

            v.addElement(0.0);
        }
    }
    double[][] getMinor(double[][] M, int i, int j){
        SEL sl = new SEL();
        M=sl.removerFila(M,i);
        M=sl.removerColumna(M,j);
       return M;
    }

    void copyMatrix(double[][] A, double[][] copy ){

        zeroesm(copy, A.length);
        for (int i = 0; i < A.length; i++) {
            for (int j = 0; j < A[0].length; j++) {
                copy[i][j]= A[i][j];

            }
        }

    }


    double determinant(double[][] M){

        if (M.length==1){
            return M[0][0];
        }
        else {
            double det= 0.0;

            for (int i = 0; i < M.length; i++) {
                double[][] minor= new double[M.length][M[0].length];
                copyMatrix(M,minor);
                double[][] minor2= getMinor(minor,0,i);

                //cal det
                det+= Math.pow(-1, i )*(M[0][i])*(determinant(minor2));
            }
            return det;
        }
    }

    double[][] cofactor(double[][] M, double[][]Cof ) {

        zeroesm(Cof, M.length);

        for (int i = 0; i < M.length; i++) {
            for (int j = 0; j < M[0].length; j++) {
                double[][] minor= new double[M.length][M[0].length];
                double[][] minor2;
                copyMatrix(M,minor);
                minor2=getMinor(M,i,j);

                Cof[i][j]= Math.pow(-1,i+j)*determinant(minor2);
            }

        }


        return Cof;
    }

    void transpose(double[][] M, double[][] T){

        zeroesm(T, M.length);
        for (int i = 0; i <M.length ; i++) {
            for (int j = 0; j <M[0].length ; j++) {
                T[j][i]=M[i][j];

            }
        }

    }
    double[][] inverseMatrix(double[][] M, double[][] Minv){
        double[][] Cof= new double[M.length][M[0].length];


        //determinante signo malo.
        double det = -1*determinant(M);

        if(det==0)
            System.out.println("Error en determinante");





        //confactors


        Cof= cofactor(M, Cof);
        SEL SL= new SEL();


        double[][] Adj= new double[Cof.length][Cof[0].length];

        // transpose
        transpose(Cof,Adj);



        //product real matrix

        productRealMatrix(1/det,Adj,Minv);
        //float det
        return Minv;
    }

    void productMatrixVector(double[][] A, Vector v, Vector R){

        for(int f=0;f<A.length;f++){
            //Se inicia un acumulador
            double cell = 0.0;
            //Se calcula el valor de la celda de acuerdo a la formulación
            for(int c=0;c<v.size();c++){
                double ma= A[f][c];
                double va= Double.parseDouble(v.elementAt(c).toString());
                cell += (ma * va);

            }
            double actual=Double.parseDouble(R.get(f).toString());
            actual += cell;
            R.remove(f);

            R.add(f,actual);

        }
    }
    void productRealMatrix(double real,double[][] M,double[][] R){

        zeroesm(R,M.length);
        //Se recorre la matriz original
        for(int i=0;i<M.length;i++)
            for(int j=0;j<M[0].length;j++)
                R[i][j]=real * M[i][j];

    }

}
