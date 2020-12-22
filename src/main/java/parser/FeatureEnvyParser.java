package parser;

import candidate.FeatureEnvyCandidate;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import utils.Utils;
import visitor.FeatureEnvyVisitor;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class FeatureEnvyParser extends ProjectsParser {
    private String[] sourcepathEntries;
    private String[] encodings;
    private String[] classpathEntries;

    private ArrayList<FeatureEnvyCandidate> featureEnvyCandidates;

    public FeatureEnvyParser(String[] sourcepathEntries, String[] encodings, String[] classpathEntries){
        super(sourcepathEntries);
        if(encodings.length!=sourcepathEntries.length){
            System.out.println("The encodings length must match the length of sourcepathEntries.");
            return ;
        }
        this.sourcepathEntries=sourcepathEntries;
        this.encodings=encodings;
        this.classpathEntries=classpathEntries;

        featureEnvyCandidates = new ArrayList<FeatureEnvyCandidate>();
    }

    public void parse() throws IOException {
        for(String path : javaFiles){
            ASTParser astParser = Utils.getNewASTParser(sourcepathEntries,encodings,classpathEntries);
            String code = getCodeFromFile(new File(path));
            astParser.setSource(code.toCharArray());
            CompilationUnit cu = (CompilationUnit)astParser.createAST(null);
            FeatureEnvyVisitor visitor = new FeatureEnvyVisitor();
            cu.accept(visitor);
            for(MethodDeclaration method : visitor.getAllMethods()){
                FeatureEnvyCandidate candidate = new FeatureEnvyCandidate(method,path);
                if(candidate.isValidCandidate()) {
//                    HashMap<String,Object> features = candidate.getFeatures();
//                    System.out.println(features.get("methodName")+"||"+features.get("sourceDistance"));
                    featureEnvyCandidates.add(candidate);
                }
            }
        }
    }

    public ArrayList<FeatureEnvyCandidate> getFeatureEnvyCandidates() {
        return featureEnvyCandidates;
    }

}
