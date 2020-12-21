package metrics;

import org.eclipse.jdt.core.dom.*;

import java.util.*;

public class TightClassCohesion extends Metric{
    private TypeDeclaration currentClass;

    public TightClassCohesion(TypeDeclaration currentClass){
        super();
        this.currentClass = currentClass;
        name = "TCC";
    }

    @Override
    public void calculate(){
        ITypeBinding typeBinding = currentClass.resolveBinding();
        if(typeBinding==null) return ;
        TightClassCohesionVisitor visitor = new TightClassCohesionVisitor(typeBinding);
        currentClass.accept(visitor);
        HashMap<IMethodBinding, List<IVariableBinding>> mapOfMethodAndAtrributeBinding = visitor.getMapOfMethodAndAtrributeBinding();
        int relativeNumberOfMethodPairs = 0;
        int totalNumberOfMethods = mapOfMethodAndAtrributeBinding.size();
        if(totalNumberOfMethods == 0){
            result.put(name,Double.valueOf(0));
            return ;
        }
        Set<IMethodBinding> Methods = visitor.getMapOfMethodAndAtrributeBinding().keySet();
        IMethodBinding[] nameOfMethods = Methods.toArray(new IMethodBinding[Methods.size()]);
        for(int i = 0; i < nameOfMethods.length; ++i){
            for(int j = i + 1; j < nameOfMethods.length; ++j){
                IMethodBinding oneName = nameOfMethods[i];
                IMethodBinding anotherName = nameOfMethods[j];
                List<IVariableBinding> one = mapOfMethodAndAtrributeBinding.get(oneName);
                List<IVariableBinding> two = mapOfMethodAndAtrributeBinding.get(anotherName);
                if(one.size() == 0 || two.size() == 0)
                    continue;
                boolean ok =false;
                for(Iterator<IVariableBinding>itOne = one.iterator(); itOne.hasNext(); ){
                    if(ok == true){
                        break;
                    }
                    IVariableBinding one1 = itOne.next();
                    for(Iterator<IVariableBinding>itTwo = two.iterator(); itTwo.hasNext(); ){
                        IVariableBinding two1 = itTwo.next();
                        if(one1.equals(two1)){
                            ++relativeNumberOfMethodPairs;
                            ok = true;
                            break;
                        }
                    }
                }
            }
        }
        if (total(totalNumberOfMethods)!=0)
        {
            double tcc =((long)relativeNumberOfMethodPairs) * 1.0 / total(totalNumberOfMethods);
            result.put(name,Double.valueOf(tcc));
        }else
            result.put(name,Double.valueOf(0));
    }

    private static long total(int n){
        long ans;
        if(n % 2 == 1){
            ans =  (n - 1) / 2 * n;
        }else{
            ans = n / 2 * (n - 1);
        }
        return ans;
    }

}

class TightClassCohesionVisitor extends ASTVisitor {
    private ITypeBinding currentClass;
    private IMethodBinding currentMethod;

    private HashMap<IMethodBinding, List<IVariableBinding>> mapOfMethodAndAtrributeBinding;

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

    public TightClassCohesionVisitor(ITypeBinding currentClass){
        this.currentClass = currentClass;
        currentMethod = null;
        mapOfMethodAndAtrributeBinding = new HashMap<IMethodBinding, List<IVariableBinding>>();
    }

    public boolean visit(MethodDeclaration node) {
        if(node.isConstructor() || (node.getModifiers() & Modifier.ABSTRACT) != 0)
            return false;
        else{
            currentMethod = node.resolveBinding();

            ArrayList<IVariableBinding> list = new ArrayList<IVariableBinding>();
            mapOfMethodAndAtrributeBinding.put(currentMethod, list);
            return true;
        }
    }

    public  boolean visit(SimpleName node){
//			System.out.println("\nSimpleName:\t" + node);
        IBinding binding = node.resolveBinding();
        if(binding instanceof IVariableBinding){
            IVariableBinding variableBinding = (IVariableBinding) binding;
            if(variableBinding.isField()){
                ITypeBinding fromClass = variableBinding.getDeclaringClass();
                if(fromClass == null)
                    return true;
                if(fromClass.equals(currentClass)){

                    List<IVariableBinding> list = mapOfMethodAndAtrributeBinding.get(currentMethod);
                    if ((list!=null))
                    {
                        if((! list.contains(variableBinding))){
                            list.add(variableBinding);
                        }
                    }else
                    {

                        list = new ArrayList<IVariableBinding>();
                        list.add(variableBinding);

                    }
                }
            }
        }
        return true;
    }

    public HashMap<IMethodBinding, List<IVariableBinding>> getMapOfMethodAndAtrributeBinding() {
        return mapOfMethodAndAtrributeBinding;
    }

}