package entity;

import metrics.Metric;

import org.eclipse.jdt.core.dom.*;
import utils.Utils;
import visitor.GodClassVisitor;

import java.io.*;
import java.lang.reflect.Constructor;


public class GodClassEntity extends SmellEntity{
    private CompilationUnit cu;
    private String[] Metrics;

    public GodClassEntity(String path, ASTParser astParser) throws IOException{
        super(path,astParser);
        String code = getCodeFromFile();
        cu = parser(code);
        GodClassVisitor visitor = new GodClassVisitor();
        cu.accept(visitor);
        for(TypeDeclaration type : visitor.getAllTypes()){
            try {
                for(String metricname : Utils.getGodClassMetrics()) {
                    Class mc = Class.forName("metrics." + metricname);
                    Constructor con = mc.getConstructor(type.getClass());
                    Metric metric = (Metric) con.newInstance(type);
                    metric.setSourceCode(code);
                    metric.calculate();
                    for(String metricName : metric.getMetrics().keySet()){
                        System.out.println(metricName+"    "+metric.getMetrics().get(metricName));
                    }
                }
                break;
            }
            catch(Exception e){
                e.printStackTrace();
            }
        }

    }

    private CompilationUnit parser(String code) throws IOException{
        astParser.setSource(code.toCharArray());
        return (CompilationUnit)astParser.createAST(null);
    }

}
