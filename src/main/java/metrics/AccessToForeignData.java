package metrics;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.IVariableBinding;
import org.eclipse.jdt.core.dom.QualifiedName;

public class AccessToForeignData extends ASTVisitor {
    private ITypeBinding currentClass;

    public AccessToForeignData(ITypeBinding currentClass){
        this.currentClass = currentClass;
    }

    public boolean visit(QualifiedName node){
        if(node.resolveBinding() instanceof IVariableBinding){
            IVariableBinding variableBinding = (IVariableBinding) node.resolveBinding();
            if(variableBinding.isField() ){          //&& (variableBinding.getModifiers() & Modifier.FINAL) == 0
                ITypeBinding typeBinding = variableBinding.getDeclaringClass();
                if(typeBinding == null)
                    return true;
                if(!typeBinding.equals(currentClass)){
                    for(String srcPackage : srcPackages){
                        if((!havaAccess.contains(node.resolveBinding()))&&srcPackage.equals(typeBinding.getPackage().getName())){
                            havaAccess.add(node.resolveBinding());
                            //luohui
                            //System.out.println("--------------------------luohui----------------------------------");
                            //System.out.println(typeBinding.getQualifiedName());
                            set.add(typeBinding.getQualifiedName());
                            //luohui
                            try {
                                if ((((IField)variableBinding.getJavaElement()).getFlags()& Flags.AccFinal) == 0)//(node.getFlags()& Flags.AccFinal )== 0)
                                    tipos++;
                            } catch (JavaModelException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                            break;
                        }
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
            for(String srcPackage : srcPackages){
                if((!havaAccess.contains(node.resolveFieldBinding()))&&srcPackage.equals(typeBinding.getPackage().getName())){
                    havaAccess.add(node.resolveFieldBinding());
                    //luohui
                    //System.out.println("--------------------------luohui----------------------------------");
                    //System.out.println(typeBinding.getQualifiedName());
                    set.add(typeBinding.getQualifiedName());
                    //luohui
                    try {
                        if ((((IField)node.resolveFieldBinding().getJavaElement()).getFlags()& Flags.AccFinal) == 0)
                            tipos++;
                    } catch (JavaModelException e) {
                        // TODO Auto-generated catch block
                        //				e.printStackTrace();
                    }
                    break;
                }
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
                for(String srcPackage : srcPackages){
                    if(vField!=null&&(!havaAccess.contains(vField))&&srcPackage.equals(typeBinding.getPackage().getName())){
                        havaAccess.add(vField);
                        //luohui
                        //System.out.println("--------------------------luohui----------------------------------");
                        //System.out.println(typeBinding.getQualifiedName());
                        set.add(typeBinding.getQualifiedName());
                        //luohui
                        tipos++;
                        break;
                    }
                }
            }

        }
        return true;
    }

}
