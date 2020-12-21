package metrics;

import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import utils.Utils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class CohesionAmongMethodsOfClass extends Metric {
    private TypeDeclaration currentClass;

    public CohesionAmongMethodsOfClass(TypeDeclaration currentClass){
        super();
        this.currentClass = currentClass;
        name="CAM";
    }

    @Override
    public void calculate(){
        MethodDeclaration[] methods = currentClass.getMethods();
        ArrayList<MethodDeclaration> filteredMethods = new ArrayList<MethodDeclaration>();
        Set<String> classParameterTypes = new HashSet<String>();
        double sumIntersection = 0.0;

        for(MethodDeclaration method : methods){
            if(!method.isConstructor() && !Utils.isStatic(method))
                filteredMethods.add(method);
        }

        for(MethodDeclaration method : filteredMethods){
            for(Object obj : method.parameters()) if(obj instanceof SingleVariableDeclaration){
                SingleVariableDeclaration para = (SingleVariableDeclaration)obj;
                String pType = para.toString().replace(" "+para.getName().toString(),"");
                if(!pType.contains(currentClass.getName().toString()))
                    classParameterTypes.add(pType);

            }
        }

        if(classParameterTypes.isEmpty()){
            result.put(name,Double.valueOf(0));
            return ;
        }

        for(MethodDeclaration method : filteredMethods){
            Set<String> parametros  = new HashSet<String>();
            for(Object obj : method.parameters()) if(obj instanceof SingleVariableDeclaration){
                SingleVariableDeclaration para = (SingleVariableDeclaration)obj;
                String pType = para.toString().replace(" "+para.getName().toString(),"");
                if(!pType.contains(currentClass.getName().toString()))
                    parametros.add(pType);
            }
            sumIntersection+=parametros.size();
        }
        result.put(name,Double.valueOf(sumIntersection / (filteredMethods.size()*classParameterTypes.size())));
    }
}
