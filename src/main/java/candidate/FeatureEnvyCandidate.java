package candidate;

import metrics.TypeEntitySet;
import org.eclipse.jdt.core.dom.*;
import utils.Utils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;


public class FeatureEnvyCandidate extends SmellCandidate implements Serializable {
    private MethodDeclaration method;
    private HashSet<String> methodEntitySet;
    private ITypeBinding sourceClassBinding;
    private boolean valid;

    /*
    features由方法，方法所在类和一系列方法的可能目标类组成，各key的含义如下：
    methodName：被检测方法名字，String
    sourceClassName：被检测方法所在类的名字，不含包路径，String
    sourceDistance：被检测方法与所在类的距离，距离由JDeodorant定义，Double
    targetClassNames：被检测方法的所有可能目标类的类名，含包路径（方便后续目标类定位），ArrayList<String>
    targetDistances：被检测方法和所有可能目标类的距离，距离由JDeodorant定义，ArrayList<Double>
     */
    public FeatureEnvyCandidate(MethodDeclaration method,String path) {
        super();
        this.method = method;
        methodEntitySet = new HashSet<String>();
        sourceClassBinding = null;
        valid = true;

        //获取可能的目标类
        HashSet<ITypeBinding> targetClassBindings = parseTargetCandidate();

        if(targetClassBindings.size() == 0){
            valid = false;
            return ;
        }

        //计算方法和所在类的距离
        TypeEntitySet sourceClassMetric = new TypeEntitySet(sourceClassBinding);
        sourceClassMetric.calculate();
        HashSet<String> sourceClassEntitySet = (HashSet<String>) sourceClassMetric.getMetrics().get(sourceClassMetric.getName());

        features.put("methodName",method.getName().toString());
        features.put("sourceClassName",sourceClassBinding.getName());
        features.put("sourceDistance", calDistance(Utils.getIntersection(methodEntitySet,sourceClassEntitySet),Utils.getUnion(methodEntitySet,sourceClassEntitySet)));
        info.put("Path",path);
        info.put("SourceClass",sourceClassBinding.getQualifiedName());
        String methodDeclarationStament = method.toString().replace(method.getBody().toString(),"");
        if(method.getJavadoc() != null)
            methodDeclarationStament = methodDeclarationStament.replace(method.getJavadoc().toString(),"");

        info.put("Method",methodDeclarationStament);


        ArrayList<String> targetNames = new ArrayList<String>();
        ArrayList<Double> targetDistances = new ArrayList<Double>();

        //计算方法和各个目标类的距离
        for(ITypeBinding targetClassBinding : targetClassBindings){
            TypeEntitySet targetClassMetric = new TypeEntitySet(targetClassBinding);
            targetClassMetric.calculate();
            HashSet<String> targetClassEntitySet = (HashSet<String>) targetClassMetric.getMetrics().get(targetClassMetric.getName());
            targetNames.add(targetClassBinding.getQualifiedName());
            targetDistances.add(calDistance(Utils.getIntersection(methodEntitySet,targetClassEntitySet),Utils.getUnion(methodEntitySet,targetClassEntitySet)));
        }
        features.put("targetClassNames",targetNames);
        features.put("targetDistances",targetDistances);
    }

    //获取方法的所有可能目标类
    private HashSet<ITypeBinding> parseTargetCandidate(){
        IMethodBinding methodBinding = method.resolveBinding();
        if(methodBinding == null) return new HashSet<ITypeBinding>();
//        System.out.println("--------------------------------------");
//        System.out.println(methodBinding.getDeclaringClass().getQualifiedName()+"   "+methodBinding.getName());
        //source class
        sourceClassBinding = methodBinding.getDeclaringClass();
        if(sourceClassBinding == null) return new HashSet<ITypeBinding>();
        TargetVisitor visitor = new TargetVisitor(sourceClassBinding);
        method.accept(visitor);
        return visitor.getTargetClasses();
    }

    private Double calDistance(HashSet<String> intersection, HashSet<String> union){
        if(union.size() == 0) return Double.valueOf(1);
        return 1 - intersection.size() * 1.0 / union.size();
    }

    private class TargetVisitor extends ASTVisitor{
        private HashSet<ITypeBinding> targetClasses;
        private ITypeBinding currentClass;

        public TargetVisitor(ITypeBinding currentClass){
            targetClasses = new HashSet<ITypeBinding>();
            this.currentClass = currentClass;
        }

        public HashSet<ITypeBinding> getTargetClasses() {
            return targetClasses;
        }

        public boolean visit(MethodInvocation node){
            IMethodBinding methodBinding = node.resolveMethodBinding();
            if(methodBinding == null) return true;
            ITypeBinding typeBinding = methodBinding.getDeclaringClass();
            if(typeBinding == null) return true;
            methodEntitySet.add(typeBinding.getQualifiedName()+"."+methodBinding.getName());
            if(!typeBinding.equals(currentClass) && typeBinding.isFromSource()) {
                targetClasses.add(typeBinding);
            }
            return true;
        }

        public boolean visit(FieldAccess node){
            IVariableBinding variableBinding = node.resolveFieldBinding();
            if(variableBinding == null) return true;
            ITypeBinding typeBinding = variableBinding.getDeclaringClass();
            if(typeBinding == null) return true;
            methodEntitySet.add(typeBinding.getQualifiedName()+"."+variableBinding.getName());
            if(!typeBinding.equals(currentClass) && typeBinding.isFromSource())
                targetClasses.add(typeBinding);
            return true;
        }

        public boolean visit(QualifiedName node){
            if(node.resolveBinding() instanceof IVariableBinding){
                IVariableBinding variableBinding = (IVariableBinding) node.resolveBinding();
                if(variableBinding.isField() ){
                    ITypeBinding typeBinding = variableBinding.getDeclaringClass();
                    if(typeBinding == null)
                        return true;
                    methodEntitySet.add(typeBinding.getQualifiedName()+"."+variableBinding.getName());
                    if(!typeBinding.equals(currentClass) && typeBinding.isFromSource())
                        targetClasses.add(typeBinding);
                }

            }
            return true;
        }

    }

    @Override
    public boolean isValidCandidate() {
        return valid;
    }

}
