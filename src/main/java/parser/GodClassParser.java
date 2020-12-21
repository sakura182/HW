package parser;

import entity.GodClassEntity;
import org.eclipse.jdt.core.dom.ASTParser;
import utils.Utils;

import java.io.FileNotFoundException;
import java.io.IOException;

public class GodClassParser extends ProjectsParser {
    private String[] sourcepathEntries;
    private String[] encodings;
    private String[] classpathEntries;

    public GodClassParser(String[] sourcepathEntries, String[] encodings, String[] classpathEntries){
        super(sourcepathEntries);
        if(encodings.length!=sourcepathEntries.length){
            System.out.println("The encodings is set to non null, its length must match the length of sourcepathEntries.");
            return ;
        }
        this.sourcepathEntries=sourcepathEntries;
        this.encodings=encodings;
        this.classpathEntries=classpathEntries;
    }

    public void parse() throws IOException {
        for(String path : javaFiles){
            ASTParser astParser = Utils.getNewASTParser(sourcepathEntries,encodings,classpathEntries);
            GodClassEntity entity = new GodClassEntity(path, astParser);

        }
    }
}
