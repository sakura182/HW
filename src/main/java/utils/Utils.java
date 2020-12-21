package utils;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;

public class Utils {
    public static ASTParser getNewASTParser(String[] sourcepathEntries, String[] encodings, String[] classpathEntries){
        ASTParser astParser;
        astParser = ASTParser.newParser(AST.JLS15);
        astParser.setKind(ASTParser.K_COMPILATION_UNIT);
        astParser.setEnvironment(classpathEntries,sourcepathEntries,encodings,true);
        astParser.setResolveBindings(true);
        astParser.setBindingsRecovery(true);
        astParser.setUnitName("");
        return astParser;
    }
}
