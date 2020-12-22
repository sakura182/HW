package metrics;

import org.eclipse.jdt.core.dom.*;

import java.util.ArrayList;

public class AccessToForeignData extends Metric {
    private TypeDeclaration currentClass;

    public AccessToForeignData(TypeDeclaration currentClass){
        super();
        this.currentClass = currentClass;
        name = "ATFD";
    }

    @Override
    public void calculate() {
        ITypeBinding typeBinding = currentClass.resolveBinding();
        if(typeBinding==null) return ;
        AccessToForeignDataVisitor visitor = new AccessToForeignDataVisitor(typeBinding);
        currentClass.accept(visitor);
        result.put(name,Double.valueOf(visitor.getAtfd()));
    }
}

class AccessToForeignDataVisitor extends ASTVisitor{
    private ITypeBinding currentClass;
    private ArrayList<IBinding> havaAccess;
    private int atfd;
    private IBinding vField;

    public AccessToForeignDataVisitor(ITypeBinding currentClass){
        this.currentClass = currentClass;
        havaAccess = new ArrayList<IBinding>();
        atfd=0;
        vField = null;
    }

    public boolean visit(PackageDeclaration node){
        return false;
    }

    public boolean visit(ImportDeclaration node){
        return false;
    }

    public boolean visit(FieldDeclaration node){
        return false;
    }

    public boolean visit(SuperFieldAccess node){
        return false;
    }

    public boolean visit(SuperConstructorInvocation node){
        return false;
    }

    public boolean visit(SuperMethodInvocation node){
        return false;
    }

    public boolean visit(QualifiedName node){
        if(node.resolveBinding() instanceof IVariableBinding){
            IVariableBinding variableBinding = (IVariableBinding) node.resolveBinding();
            if(variableBinding.isField() ){          //&& (variableBinding.getModifiers() & Modifier.FINAL) == 0
                ITypeBinding typeBinding = variableBinding.getDeclaringClass();
                if(typeBinding == null)
                    return true;
                if(!typeBinding.equals(currentClass)){
                    if(!havaAccess.contains(node.resolveBinding())){
                        havaAccess.add(node.resolveBinding());
                        atfd++;
                    }
                }
            }

        }
        return true;
    }

    public boolean visit(FieldAccess node){
        if(node.resolveFieldBinding() == null){
            return true;
        }
        ITypeBinding typeBinding = node.resolveFieldBinding().getDeclaringClass();
        if(typeBinding == null)
            return true;
        if(!typeBinding.equals(currentClass)){
            if(!havaAccess.contains(node.resolveFieldBinding())){
                havaAccess.add(node.resolveFieldBinding());
                atfd++;
            }
        }
        return true;
    }

    public boolean visit(MethodInvocation node){
        if(isGetterOrSetter(node)){
            ITypeBinding typeBinding = node.resolveMethodBinding().getDeclaringClass();
            if(typeBinding == null)
                return true;
            if(!typeBinding.equals(currentClass)){
                if(vField!=null&&!havaAccess.contains(vField)){
                    havaAccess.add(vField);
                    atfd++;
                }
            }

        }
        return true;
    }

    private  boolean isGetterOrSetter(MethodInvocation node){
        IMethodBinding binding = node.resolveMethodBinding();
        if(binding==null)
            return false;
        String methodName = binding.getName();
        if(methodName.length() <= 3)
            return false;
        if(methodName.startsWith("get")){
            String targetField = methodName.substring(3);
            IVariableBinding[] fields = node.resolveMethodBinding().getDeclaringClass().getDeclaredFields();
            for(IVariableBinding field : fields){
                if(field.getName().equalsIgnoreCase(targetField) && field.getType().equals(node.resolveMethodBinding().getReturnType()))
                {
                    vField = field;
                    return true;
                }
            }
        }
        if(methodName.startsWith("set")){
            String targetField = methodName.substring(3);
            IVariableBinding[] fields = node.resolveMethodBinding().getDeclaringClass().getDeclaredFields();
            for(IVariableBinding field : fields)
                if(field.getName().equalsIgnoreCase(targetField)){
                    ITypeBinding[] parameterTypes = node.resolveMethodBinding().getParameterTypes();
                    if(parameterTypes.length == 1 && field.getType().equals(parameterTypes[0]))
                    {
                        vField = field;
                        return true;
                    }
                }
        }
        return false;
    }

    public int getAtfd() {
        return atfd;
    }
}
