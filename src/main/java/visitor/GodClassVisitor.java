package visitor;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.TypeDeclaration;

import java.util.ArrayList;

public class GodClassVisitor extends ASTVisitor {
    private ArrayList<TypeDeclaration> types;

    public GodClassVisitor(){
        types = new ArrayList<TypeDeclaration>();
    }

    public boolean visit(TypeDeclaration type){
        types.add(type);
        return true;
    }

    public ArrayList<TypeDeclaration> getAllTypes() { return types;}
}
