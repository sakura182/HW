package entity;

import org.eclipse.jdt.core.dom.ASTParser;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;

public class SmellEntity {
    protected ArrayList<HashMap<String,Object> > features;
    protected File javaFile;
    protected ASTParser astParser;

    public SmellEntity(String path, ASTParser astParser) throws FileNotFoundException{
        this.javaFile = new File(path);
        if(!javaFile.exists()) throw new FileNotFoundException();
        this.astParser=astParser;
        features = new ArrayList<HashMap<String,Object> >();
    }

    protected String getCodeFromFile() throws FileNotFoundException,IOException {
        BufferedInputStream bufferedInputStream = new BufferedInputStream(new FileInputStream(javaFile));
        byte[] input = new byte[bufferedInputStream.available()];
        bufferedInputStream.read(input);
        bufferedInputStream.close();
        return new String(input);
    }
}
