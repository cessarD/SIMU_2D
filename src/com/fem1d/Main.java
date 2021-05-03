package com.fem1d;

import com.fem1d.clases.element;
import com.fem1d.clases.mesh;
import com.fem1d.clases.node;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        mesh m = new mesh();
	    System.out.println("hi");
        ReadMeshandConditions(m);
    }


    public static void ReadMeshandConditions(mesh m){
        int nnodes, neltos, ndirich, nneuman;
        Scanner in = new Scanner(System.in);
        String filename;
        File entrydata;
        float  l,k,Q;

        do {
            //System.out.println("Ingrese ruta de acceso del archivo:");
            //filename= in.nextLine();


            //entrydata= new File("/home/rene/Documents/Ciclo2021/codigosidequest/SIMU/src/com/fem1d/clases/problem.msh");
                    entrydata= new File("C:/Users/cesar/IdeaProjects/SIMU2/src/com/fem1d/clases/problem.msh");
            System.out.println(entrydata.exists());
        }while(!entrydata.exists());

        //archivo abierto, buscando variables de archivo msh

        try {
            Scanner reader= new Scanner(entrydata);
                //sacando valores de l q k
                l=reader.nextFloat();
                k=reader.nextFloat();
                Q=reader.nextFloat();
                //System.out.println("L="+l+"k="+k+"Q="+Q);

                //Obteniendo Condiciones
                reader.nextLine();
                nnodes= reader.nextInt();
                neltos= reader.nextInt();
                ndirich= reader.nextInt();
                nneuman= reader.nextInt();
                //System.out.println("nodos="+nnodes+"neltos="+neltos+"ndirich="+ndirich+"nneuman="+nneuman);

                m.setParameters(l,k,Q);
                m.setSizes(nnodes,neltos,ndirich,nneuman);
                //crear data
                //m.createData();

                reader.nextLine();
                reader.nextLine();
                reader.nextLine();
            for (int i=0;i<nnodes;i++){
                node n = new node();
                n.setnode(reader.nextInt(), reader.nextFloat());
                m.getNodes().add(i,n);

            }
            reader.nextLine();
            reader.nextLine();
            reader.nextLine();
            reader.nextLine();
            for (int i=0;i<neltos;i++){
                element n = new element();
                n.setelement(reader.nextInt(), reader.nextInt(), reader.nextInt());
                m.getElements().add(i,n);

            }
            System.out.println("elementos");
            for (int i=0;i<neltos;i++){

                System.out.println(m.getElements().get(i).getNode1());
            }





        }catch (FileNotFoundException e){
            System.out.println(e);
            e.printStackTrace();
        }


    }



}
