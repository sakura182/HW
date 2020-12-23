package metrics;

import org.eclipse.jdt.core.dom.*;

import utils.Utils;

import java.util.ArrayList;

public class NumberofAttributes extends Metric{
    private TypeDeclaration currentClass;

    public NumberofAttributes(TypeDeclaration currentClass){
        super();
        this.currentClass = currentClass;
        name = "NOA";
    }

    @Override
    public void calculate() {
        ArrayList<String> attributes = new ArrayList<String>();
        FieldDeclaration[] fields = currentClass.getFields();
        int noa = 0;
        int nopa = 0;
        for(FieldDeclaration field : fields) if(!Utils.isStatic(field)){
            boolean pub = false;
            for(Object o : field.modifiers()){
                if(o instanceof Modifier){
                    Modifier modifier = (Modifier)o;
                    if(modifier.isPublic()){
                        pub=true;
                        break;
                    }
                }
            }
            if(pub) nopa += field.fragments().size();
            noa += field.fragments().size();

            for(Object obj : field.fragments()) if(obj instanceof VariableDeclarationFragment){
                VariableDeclarationFragment var = (VariableDeclarationFragment)obj;
                attributes.add(var.getName().toString());
            }
        }
        result.put(name,Double.valueOf(noa));
        result.put("NOPA",Double.valueOf(nopa));
        result.put("Attributes",attributes);
    }
}
