package metrics;

import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.TypeDeclaration;

public class WeightedMethodCount extends Metric{
    private TypeDeclaration currentClass;
    //private

    public WeightedMethodCount(TypeDeclaration currentClass){
        super();
        this.currentClass = currentClass;
        name = "WMC";
    }

    @Override
    public void calculate(){
        if(sourceCode == null){
            System.out.println("Must set sourceCode");
            return ;
        }
        Double res = Double.valueOf(0);
        for(MethodDeclaration method : currentClass.getMethods()){
            McCabeCyclomaticComplexity mcb = new McCabeCyclomaticComplexity(method,sourceCode);
            mcb.calculate();
            res += (Double)mcb.result.get("MCC");
        }
        result.put(name,res);
    }

}
