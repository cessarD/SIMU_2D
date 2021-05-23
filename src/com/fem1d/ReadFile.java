package com.fem1d;

import com.fem1d.clases.condition;
import com.fem1d.clases.element;
import com.fem1d.clases.mesh;
import com.fem1d.clases.node;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class ReadFile {
    public static void ReadMeshandConditions(mesh m){
        int nnodes, neltos, ndirich, nneuman;
        Scanner in = new Scanner(System.in);
        String filename;
        File entrydata;
        double  k,Q;
        System.out.println("hola");
        do {

            entrydata= new File("src/com/fem1d/clases/problem2d.msh");
        }while(!entrydata.exists());

        //archivo abierto, buscando variables de archivo msh

        try {
            Scanner reader= new Scanner(entrydata);
            //sacando valores de l q k


            k=Double.parseDouble(reader.next());
            Q=Double.parseDouble(reader.next());
            System.out.println("k="+k+"Q="+Q);

            //Obteniendo Condiciones
            reader.nextLine();
            nnodes= reader.nextInt();
            neltos= reader.nextInt();
            ndirich= reader.nextInt();
            nneuman= reader.nextInt();
            System.out.println("nodos="+nnodes+"\nElementos="+neltos+"\nCondiciones de Dirichlet="+ndirich+"\nCondiciones de Nneuman="+nneuman);

            m.setParameters(k,Q);
            m.setSizes(nnodes,neltos,ndirich,nneuman);
            for(int i=0;i<3;i++){
                reader.nextLine();
            }


            for (int i=0;i<nnodes;i++){
                node n = new node();
                n.setnode(reader.nextInt(), Float.parseFloat(reader.next()), Float.parseFloat(reader.next()));

                m.getNodes().add(i,n);
            }
            for(int i=0;i<4;i++){
                reader.nextLine();
            }
            for (int i=0;i<neltos;i++){
                element n = new element();
                n.setelement(reader.nextInt(), reader.nextInt(), reader.nextInt(), reader.nextInt());

                m.getElements().add(i,n);

            }
            for(int i=0;i<4;i++){
                reader.nextLine();
            }
            for (int i=0;i<ndirich;i++){
                condition n = new condition();
                n.setcondition(reader.nextInt(), Float.parseFloat(reader.next()));
                m.getDirichlet().add(i,n);

            }
            for(int i=0;i<4;i++){
                reader.nextLine();
            }
            for (int i=0;i<nneuman;i++){
                condition n = new condition();

                n.setcondition(reader.nextInt(), Float.parseFloat(reader.next()));
                m.getNeumann().add(i,n);

            }
            //correct conditions

            for(int i=0;i<ndirich;i++)
                m.getIndices()[i] = m.getDirichlet().get(i).getNode1();

            for(int i=0;i<ndirich-1;i++){
                int pivot = m.getDirichlet().get(i).getNode1();
                for(int j=i;j<ndirich;j++)
                    if(m.getDirichlet().get(j).getNode1()>pivot)
                m.getDirichlet().get(j).setNode1(m.getDirichlet().get(j).getNode1()-1);
            }


            //cerrando archivo
            reader.close();

        }catch (FileNotFoundException e){
            System.out.println(e);
            e.printStackTrace();
        }


    }




}
