package metrics;

import org.eclipse.jdt.core.dom.IMethodBinding;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.IVariableBinding;

import java.util.HashSet;

public class TypeEntitySet extends Metric{
    private ITypeBinding currentClass;

    public TypeEntitySet(ITypeBinding currentClass){
        super();
        this.currentClass = currentClass;
        name = "EntitySet";
    }

    @Override
    public void calculate(){
        HashSet<String> entitySet = new HashSet<String>();
        if(currentClass == null){
            result.put(name,entitySet);
            return ;
        }
        for(IMethodBinding method : currentClass.getDeclaredMethods())
            entitySet.add(currentClass.getQualifiedName()+"."+method.getName());
        for(IVariableBinding field : currentClass.getDeclaredFields())
            entitySet.add(currentClass.getQualifiedName()+"."+field.getName());
        result.put(name,entitySet);
    }

}
