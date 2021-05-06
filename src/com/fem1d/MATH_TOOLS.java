package com.fem1d;

import com.sun.net.httpserver.Authenticator;

import java.util.Vector;

public class MATH_TOOLS {
    void zeroesm(double[][] M,int n){
        //Se crean n filas
        for(int i=0;i<n;i++){
            for(int j=0;j<n;j++){
                //Se crea una fila de n ceros
                // vector<float> row(n,0.0);
                //Se ingresa la fila en la matriz
                //M.push_back(row);
                M[i][j]=0.0;
            }

        }
    }
    void zeroesv(Vector v, int n){
        //Se itera n veces
        for(int i=0;i<n;i++){
            //En cada iteración se agrega un cero al vector
            v.addElement(0.0);
        }
    }
    void getMinor(double[][] M, int i, int j){
        SEL sl = new SEL();

        //eliminación de columna j y fila i
        M=sl.removerFila(M,i);
        M=sl.removerColumna(M,j);

    }

    void copyMatrix(double[][] A, double[][] copy ){
        SEL sl = new SEL();
        zeroesm(copy, A.length);



        for (int i = 0; i < A.length; i++) {
            for (int j = 0; j < A[0].length; j++) {
                copy[i][j]= A[i][j];
                //System.out.println(A[i][j]+"cp"+"["+i+"]"+"["+j+"]"+copy[i][j]);
            }
        }

    }


    double determinant(double[][] M){

        if (M.length==1){
            return M[10][0];
        }
        else {
            double det= 0.0;

            for (int i = 0; i < M.length; i++) {
                double[][] minor= new double[M.length][M[0].length];
                copyMatrix(M,minor);
                //getMinor(minor,0,i);


                //cal det
                det+= Math.pow(-1, i )*(M[0][i]);//*(determinant(minor));
            }
            return det;
        }
    }

    void inverseMatrix(double[][] M, double[][] Minv){
        double[][] Cof, Adj;

        for (int l = 0; l < M.length; l++) {
            for (int j = 0; j < M[0].length; j++) {
                System.out.print( String.format("%.2f",M[l][j])+"\t");

            }
            System.out.println();
        }

        double det = determinant(M);
        if(det==0)
            System.out.println("falla");




        //confactors

        // transpose

        //product real matrix


        //float det =

    }
}
