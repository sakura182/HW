package entity;

import net.sourceforge.metrics.calculators.AccessToForeignData;
import net.sourceforge.metrics.core.sources.TypeMetrics;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import visitor.GodClassVisitor;

import java.io.*;


public class GodClassEntity extends SmellEntity{
    CompilationUnit cu;

    public GodClassEntity(String path, ASTParser astParser) throws IOException{
        super(path,astParser);

        cu = parser();
        GodClassVisitor visitor = new GodClassVisitor();
        cu.accept(visitor);
        for(TypeDeclaration type : visitor.getAllTypes()){
            ITypeBinding typeBinding = type.resolveBinding();
            try {
                TypeMetrics tm = new TypeMetrics(type);
                AccessToForeignData atfd = new AccessToForeignData();
                atfd.calculate(tm);
                System.out.println(tm.getValue("ATFD"));
            }catch(Exception e){
                e.printStackTrace();
            }
        }

    }

    private CompilationUnit parser() throws IOException{
        String code = getCodeFromFile();
        astParser.setSource(code.toCharArray());
        return (CompilationUnit)astParser.createAST(null);
    }

}
