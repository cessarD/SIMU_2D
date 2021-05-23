package com.fem1d;
/*
public class Results {

    public
    void writeResults(mesh m,Vector T,char *filename){
        char outputfilename[150];
        int *dirich_indices = m.getDirichletIndices();
        condition *dirich = m.getDirichlet();
        ofstream file;

        addExtension(outputfilename,filename,".post.res");
        file.open(outputfilename);

        file << "GiD Post Results File 1.0\n";
        file << "Result \"Temperature\" \"Load Case 1\" 1 Scalar OnNodes\nComponentNames \"T\"\nValues\n";

        int Tpos = 0;
        int Dpos = 0;
        int n = m.getSize(NODES);
        int nd = m.getSize(DIRICHLET);
        for(int i=0;i<n;i++){
            if(findIndex(i+1,nd,dirich_indices)){
                file << i+1 << " " << dirich[Dpos].getValue() << "\n";
                Dpos++;
            }else{
                file << i+1 << " " << T.at(Tpos) << "\n";
                Tpos++;
            }
        }

        file << "End values\n";

        file.close();
    }
}

 */
