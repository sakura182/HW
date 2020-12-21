package metrics;

import org.eclipse.jdt.core.dom.*;
import utils.Utils;

import java.util.HashSet;

public class DirectClassCoupling extends Metric{
    private TypeDeclaration currentClass;

    public DirectClassCoupling(TypeDeclaration currentClass){
        super();
        this.currentClass = currentClass;
        name = "DCC";
    }

    @Override
    public void calculate(){
        HashSet<String> tipos = new HashSet<String>();
        FieldDeclaration[] fields = currentClass.getFields();
        MethodDeclaration[] methods = currentClass.getMethods();
        for(FieldDeclaration field : fields)
            tipos.addAll(Utils.getSourceTypeNames(field.getType().resolveBinding()));
        for(MethodDeclaration method : methods) {
            //泛型无法binding，返回值为null
            IMethodBinding methodBinding = method.resolveBinding();
            if(methodBinding == null) continue;
            for (ITypeBinding binding : method.resolveBinding().getParameterTypes())
                tipos.addAll(Utils.getSourceTypeNames(binding));
        }
        result.put(name,Double.valueOf(tipos.size()));
    }

}
