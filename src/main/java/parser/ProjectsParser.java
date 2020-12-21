package parser;

import org.eclipse.jdt.core.dom.*;

import java.io.*;
import java.util.ArrayList;

public class ProjectsParser {
    protected ArrayList<String> javaFiles;

    public ProjectsParser(String[] sourcepathEntries){

        javaFiles = new ArrayList<String> ();
        for(String path : sourcepathEntries) {
            traverseFile(new File(path));
        }
    }

    private void traverseFile(File root){
        if(root.isFile()){
            if(root.getName().endsWith(".java"))
                javaFiles.add(root.getAbsolutePath());
            return ;
        }
        else if(root.isDirectory()){
            for(File f : root.listFiles())
                traverseFile(f);
        }
    }

}
