package metrics;

import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.TypeDeclaration;

public class DepthOfInheritanceTree extends Metric{
    private TypeDeclaration currentClass;

    public DepthOfInheritanceTree(TypeDeclaration currentClass){
        super();
        this.currentClass = currentClass;
        name = "DIT";
    }

    @Override
    public void calculate(){
        ITypeBinding typeBinding = currentClass.resolveBinding();
        if(typeBinding == null) return ;
        int h = 0;
        while(typeBinding!=null){
            typeBinding = typeBinding.getSuperclass();
            h++;
        }
        result.put(name,Double.valueOf(h));
    }

}
