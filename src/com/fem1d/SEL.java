package com.fem1d;

import com.fem1d.clases.mesh;
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
        row1.add(0,k/l);row1.add(0,-k/l);
        row2.add(0,-k/l);row2.add(0,k/l);

        //Se insertan las filas en la matriz



        //POSIBLEMENTE ME MAME AQUI
        matriz[0][0]=new Double(row1.get(0).toString()); matriz[0][1]=new Double(row1.get(1).toString());
        matriz[1][0]=new Double(row2.get(0).toString()); matriz[1][1]=new Double(row2.get(1).toString());




       // for(int i=0;i<2;i++){
            //for(int j=0;j<2;j++){
               // System.out.println(matriz[i][j]);
          //  }
     //   }
      //  System.out.println("cambio de local");
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

        return b;
    }



    void crearSistemasLocales(mesh m, Vector localKs, Vector localbs){
        //Se recorren los elementos

        for(int i=0;i<m.getSize(size.ELEMENTS.ordinal());i++){
            //Por cada elemento, se crea su K y su b
            localKs.addElement(createLocalK(i,m));
           localbs.addElement(createLocalb(i,m));

        }



    }

}
