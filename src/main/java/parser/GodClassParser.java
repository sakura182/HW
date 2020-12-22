package parser;

import candidate.GodClassCandidate;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import utils.Utils;
import visitor.GodClassVisitor;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class GodClassParser extends ProjectsParser {
    final static private String[] GodClassMetrics = {"AccessToForeignData","DirectClassCoupling","DepthOfInheritanceTree","TightClassCohesion","LackOfCohesionInMethods","CohesionAmongMethodsOfClass","WeightedMethodCount","LinesOfCode","NumberOfMethods","NumberofAttributes"};

    private String[] sourcepathEntries;
    private String[] encodings;
    private String[] classpathEntries;

    private ArrayList<GodClassCandidate> godClassCandidates;

    public GodClassParser(String[] sourcepathEntries, String[] encodings, String[] classpathEntries){
        super(sourcepathEntries);
        if(encodings.length!=sourcepathEntries.length){
            System.out.println("The encodings length must match the length of sourcepathEntries.");
            return ;
        }
        this.sourcepathEntries=sourcepathEntries;
        this.encodings=encodings;
        this.classpathEntries=classpathEntries;
        godClassCandidates = new ArrayList<GodClassCandidate>();
    }

    public void parse() throws IOException {
        for(String path : javaFiles){
            ASTParser astParser = Utils.getNewASTParser(sourcepathEntries,encodings,classpathEntries);
            String code = getCodeFromFile(new File(path));
            astParser.setSource(code.toCharArray());
            CompilationUnit cu = (CompilationUnit)astParser.createAST(null);
            GodClassVisitor visitor = new GodClassVisitor();
            cu.accept(visitor);
            for(TypeDeclaration type : visitor.getAllTypes()) {
                GodClassCandidate candidate = new GodClassCandidate(type,code,path);
                if(candidate.isValidCandidate())
                    godClassCandidates.add(candidate);
            }
        }
    }

    public static String[] getGodClassMetrics(){
        return GodClassMetrics;
    }

    public ArrayList<GodClassCandidate> getGodClassCandidates() {
        return godClassCandidates;
    }

}
