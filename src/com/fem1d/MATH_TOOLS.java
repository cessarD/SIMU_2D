package com.fem1d;

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
}
