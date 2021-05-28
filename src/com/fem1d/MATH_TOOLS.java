package com.fem1d;

import com.fem1d.clases.condition;
import com.fem1d.clases.mesh;
import com.sun.net.httpserver.Authenticator;

import java.awt.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Vector;

public class MATH_TOOLS {
    void zeroesm(double[][] M,int n,int m){

        for(int i=0;i<n;i++){
            for(int j=0;j<m;j++){

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

        zeroesm(copy, A.length,A.length);
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
                //System.out.println(det);
            }
            return det;
        }
    }

    double[][] cofactor(double[][] M, double[][]Cof ) {

        zeroesm(Cof, M.length,M.length);

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

    double[][] transpose(double[][] M, double[][] T){

        zeroesm(T, M.length,M.length);
        for (int i = 0; i <M.length ; i++) {
            for (int j = 0; j <M[0].length ; j++) {
                T[j][i]=M[i][j];

            }
        }
        return T;

    }
    double[][] inverseMatrix(double[][] M, double[][] Minv){
        double[][] Cof= new double[M.length][M[0].length];
        //System.out.println(M.length);
        //System.out.println(M[0].length);
        zeroesm(Cof,M.length,M[0].length );




        double det =determinant(M);

        if(det==0)
            System.out.println("Error en determinante");

        //confactors


        Cof= cofactor(M, Cof);
        SEL tools = new SEL();

        SEL SL= new SEL();


        double[][] Adj= new double[Cof.length][Cof[0].length];

        // transpose
        transpose(Cof,Adj);

        System.out.println("adjunta");
        tools.showMatrix(Adj);


        //product real matrix

        productRealMatrix(1/det,Adj,Minv);
        System.out.println("adjunta*det");
        tools.showMatrix(Minv);
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
    double[][] productRealMatrix(double real,double[][] M,double[][] R){

        zeroesm(R,M.length,M.length);
        //Se recorre la matriz original
        for(int i=0;i<M.length;i++){
            for(int j=0;j<M[0].length;j++){
                R[i][j]=real * M[i][j];}}
        return R;
    }

    double calculateMember(int i,int j,int r,double[][] A,double[][] B){
        double member = 0;
        for(int k=0;k<r;k++)
            member += A[i][k]*B[k][j];

        return member;
    }


    double[][] productMatrixMatrix(double[][] A,double[][] B,int n,int r,int m){
        double[][] R= new double[n][m];

        zeroesm(R,n,m);
        for(int i=0;i<n;i++)
            for(int j=0;j<m;j++)
                R[i][j] = calculateMember(i,j,r,A,B);


        return R;
    }

    boolean findIndex(int v, int s, ArrayList<condition> arr){
        for (int i = 0; i < s; i++)
            if (arr.get(i).getValue()==v)return true;
            return false;
    }

    void writeResults(mesh m, Vector T, String filename){

        int[] dirichIndices= m.getIndices();
        ArrayList<condition> dirich= m.getDirichlet();
        //creando o revisando si existe el archivo
        try {
            File Obj= new File(filename);
            if (Obj.createNewFile()){
                System.out.println("file creado: "+Obj.getName());
            }else {
                System.out.println("File Already Exist!");
            }
        }catch (Exception e){
            System.out.println("An error occurred.");
            e.printStackTrace();
        }

        //llenando archivo
        try {
            FileWriter fw = new FileWriter(filename);
            //headers
            fw.write("GiD Post Results File 1.0\n");
            fw.write("Result \"Temperature\" \"Load Case 1\" 1 Scalar OnNodes\nComponentNames \"T\"\nValues\n");
            //data
            int Tpos=0;
            int Dpos=0;

            int n= m.getSize(SEL.size.NODES.ordinal());
            int nd= m.getSize(SEL.size.DIRICHLET.ordinal());
            // logica de ingreso de datos
            for (int i = 0; i <9    ; i++) {
                if(findIndex(i+1, nd,dirich)){
                    int num=1+i;
                    fw.write(""+num+"\t"+dirich.get(Dpos).getValue()+"\n");
                    Dpos++;
                }else{
                    int num=1+i;
                    fw.write(""+num+"\t"+T.elementAt(Tpos)+"\n");
                    Tpos++;
                }
            }
            fw.write("End Values \n");
            fw.close();
        }catch (IOException e){
            System.out.println("Error Escribiendo archivo");
            e.printStackTrace();
        }


    }

}
