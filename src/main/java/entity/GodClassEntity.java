package entity;

import org.eclipse.jdt.core.IType;
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
            System.out.println(type.getName());
            ITypeBinding typeBinding = type.resolveBinding();
            IType iType = (IType)typeBinding.getJavaElement();
            System.out.println(iType.getElementName());
        }
    }

    private CompilationUnit parser() throws IOException{
        String code = getCodeFromFile();
        astParser.setSource(code.toCharArray());
        return (CompilationUnit)astParser.createAST(null);
    }

}
