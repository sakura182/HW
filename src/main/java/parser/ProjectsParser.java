package parser;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;

import java.io.File;
import java.util.ArrayList;

public class ProjectsParser {
    protected ArrayList<String> javaFiles;
    protected ASTParser astParser;

    public ProjectsParser(String projectRoot, String[] sourceFilePaths, String[] encodings, String[] classPaths){
        javaFiles = new ArrayList<String> ();
        traverseJavaFile(new File(projectRoot));
        astParser = ASTParser.newParser(AST.JLS15);
        astParser.setKind(ASTParser.K_COMPILATION_UNIT);
        astParser.setResolveBindings(true);
        astParser.setBindingsRecovery(true);
        astParser.setEnvironment(classPaths,sourceFilePaths,encodings,true);
    }

    private void traverseJavaFile(File root){
        if(root.isFile()){
            if(root.getName().endsWith(".java"))
                javaFiles.add(root.getAbsolutePath());
            return ;
        }
        else if(root.isDirectory()){
            for(File f : root.listFiles())
                traverseJavaFile(f);
        }
    }

}
