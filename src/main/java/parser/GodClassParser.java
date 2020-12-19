package parser;

import entity.GodClassEntity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;

public class GodClassParser extends ProjectsParser {

    public GodClassParser(String projectRoot, String[] sourceFilePaths, String[] encodings, String[] classPaths){
        super(projectRoot,sourceFilePaths,encodings,classPaths);
    }

    public void parse() throws FileNotFoundException, IOException {
        for(String path : javaFiles){
            System.out.println(path);
            GodClassEntity entity = new GodClassEntity(path, astParser);
            break;
        }
    }
}
