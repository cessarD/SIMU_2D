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

public class Main {

    public static void main(String[] args) {
        //instanciar archivos externos
        SEL tools= new SEL();


        mesh m = new mesh();
        Vector localb=new Vector();
        Vector localk=new Vector();
        //revisar tamaño de matriz final
        double [][] k = new double[2][2];
        Vector b=new Vector();
        Vector T=new Vector();
	    System.out.println("hi");
        ReadMeshandConditions(m);
        tools.crearSistemasLocales(m,localk,localb);


        //no se como revisar que el localK este cargado
        // System.out.println(localk.elementAt(0)[0][0]);



        System.out.println(localb.get(2));
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
            //System.out.println(entrydata.exists());

                    entrydata= new File("C:/Users/cesar/IdeaProjects/SIMU2/src/com/fem1d/clases/problem.msh");

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
            for(int i=0;i<3;i++){
                reader.nextLine();
            }


            for (int i=0;i<nnodes;i++){
                node n = new node();
                n.setnode(reader.nextInt(), reader.nextFloat());
                m.getNodes().add(i,n);

            }
            for(int i=0;i<4;i++){
                reader.nextLine();
            }
            for (int i=0;i<neltos;i++){
                element n = new element();
                n.setelement(reader.nextInt(), reader.nextInt(), reader.nextInt());
                m.getElements().add(i,n);

            }
            for(int i=0;i<4;i++){
                reader.nextLine();
            }
            for (int i=0;i<ndirich;i++){
                condition n = new condition();
                n.setcondition(reader.nextInt(), reader.nextFloat());
                m.getDirichlet().add(i,n);

            }
            for(int i=0;i<4;i++){
                reader.nextLine();
            }
            for (int i=0;i<nneuman;i++){
                condition n = new condition();
                n.setcondition(reader.nextInt(), reader.nextFloat());
                m.getNeumann().add(i,n);

            }






        }catch (FileNotFoundException e){
            System.out.println(e);
            e.printStackTrace();
        }


    }



}
