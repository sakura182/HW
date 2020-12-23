package metrics;

import org.eclipse.jdt.core.dom.*;
import utils.Utils;

import java.util.ArrayList;

public class NumberOfMethods extends Metric{
    private TypeDeclaration currentClass;

    public NumberOfMethods(TypeDeclaration currentClass){
        super();
        this.currentClass = currentClass;
        name = "NOM";
    }

    @Override
    public void calculate() {
        ArrayList<String> gettersAndSetters = new ArrayList<String>();
        ArrayList<String> methodsName = new ArrayList<String>();
        int nom = 0;
        int noam = 0;
        FieldDeclaration[] fields = currentClass.getFields();
        MethodDeclaration[] methods = currentClass.getMethods();

        for(MethodDeclaration method : methods) if(!Utils.isStatic(method)){
            methodsName.add(method.getName().toString());
            nom++;
            String methodName = method.getName().toString();
            if(methodName.startsWith("set")||methodName.startsWith("get"))
                gettersAndSetters.add(methodName.toLowerCase());
        }

        for(FieldDeclaration field : fields){
            for(Object obj : field.fragments()) if(obj instanceof VariableDeclarationFragment){
                VariableDeclarationFragment var = (VariableDeclarationFragment)obj;
                String varName = var.getName().toString().toLowerCase();
                if(gettersAndSetters.contains("set"+varName) || gettersAndSetters.contains("get"+varName))
                    noam++;
            }
        }

        result.put(name,Double.valueOf(nom));
        result.put("NOAM",Double.valueOf(noam));
        result.put("Methods",methodsName);
    }

}
