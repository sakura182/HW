package visitor;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.MethodDeclaration;

import java.util.ArrayList;

public class FeatureEnvyVisitor extends ASTVisitor {
    ArrayList<MethodDeclaration> methods;

    public FeatureEnvyVisitor() {
        methods = new ArrayList<MethodDeclaration>();
    }

    public boolean visit(MethodDeclaration node){
        methods.add(node);
        return true;
    }

    public ArrayList<MethodDeclaration> getAllMethods() {
        return methods;
    }

}
