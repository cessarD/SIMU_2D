package com.fem1d;

import com.sun.net.httpserver.Authenticator;

import java.awt.*;
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
    double[][] getMinor(double[][] M, int i, int j){
        SEL sl = new SEL();

        //eliminación de columna j y fila i
        //System.out.println("normal");
        //sl.showMatrix(M);


        M=sl.removerFila(M,i);
        //System.out.println("- fila");
        ///sl.showMatrix(M);


        M=sl.removerColumna(M,j);
        //System.out.println("-fila & columna ");
        //sl.showMatrix(M);



       return M;
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


        for (int l = 0; l < M.length; l++) {
            for (int j = 0; j < M[0].length; j++) {
                System.out.print( String.format("%.2f",M[l][j])+"\t");
            }
            System.out.println();
        }

        //determinante signo malo.
        double det = -1*determinant(M);

        if(det==0)
            System.out.println("falla");
        else
            System.out.println("\n"+det);




        //confactors


        Cof= cofactor(M, Cof);
        SEL SL= new SEL();

        System.out.println("Cof");
        SL.showMatrix(Cof);
        double[][] Adj= new double[Cof.length][Cof[0].length];

        // transpose
        transpose(Cof,Adj);
        System.out.println("Trans");
        SL.showMatrix(Adj);


        //product real matrix

        productRealMatrix(1/det,Adj,Minv);
        //float det
        return Minv;
    }

    void productMatrixVector(double[][] A, Vector v, Vector R){
        //Se aplica básicamente la formulación que puede
        //consultarse en el siguiente enlace (entrar con cuenta UCA):
        //          https://goo.gl/PEzWWe

        //Se itera una cantidad de veces igual al número de filas de la matriz
        for(int f=0;f<A.length;f++){
            //Se inicia un acumulador
            double cell = 0.0;
            //Se calcula el valor de la celda de acuerdo a la formulación
            for(int c=0;c<v.size();c++){
                double ma= A[f][c];
                double va= Double.parseDouble(v.elementAt(c).toString());
                cell += (ma * va);
              //  cell += A.at(f).at(c)*v.at(c);
            }
            double actual=Double.parseDouble(R.get(f).toString());
            actual += cell;
            R.remove(f);
            //Se coloca el valor calculado en su celda correspondiente en la respuesta
            R.add(f,cell);
           // R.at(f) += cell;
        }
    }
    void productRealMatrix(double real,double[][] M,double[][] R){
        //Se prepara la matriz de respuesta con las mismas dimensiones de la
        //matriz
        zeroesm(R,M.length);
        //Se recorre la matriz original
        for(int i=0;i<M.length;i++)
            for(int j=0;j<M[0].length;j++)
                //La celda actual se multiplica por el real, y se almacena
                //el resultado en la matriz de respuesta
                R[i][j]=real * M[i][j];
               // R.at(i).at(j) = real*M.at(i).at(j);
    }

}
