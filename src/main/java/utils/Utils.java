package utils;

import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.dom.*;

import java.util.HashSet;
import java.util.Map;

public class Utils {
    final static private String[] GodClassMetrics = {"AccessToForeignData","DirectClassCoupling","DepthOfInheritanceTree","TightClassCohesion","LackOfCohesionInMethods","CohesionAmongMethodsOfClass","WeightedMethodCount","LinesOfCode","NumberOfMethods","NumberofAttributes"};

    public static ASTParser getNewASTParser(String[] sourcepathEntries, String[] encodings, String[] classpathEntries){
        ASTParser astParser;
        astParser = ASTParser.newParser(AST.JLS15);
        astParser.setKind(ASTParser.K_COMPILATION_UNIT);
        astParser.setEnvironment(classpathEntries,sourcepathEntries,encodings,true);
        astParser.setResolveBindings(true);
        astParser.setBindingsRecovery(true);
        astParser.setUnitName("");
        Map options = JavaCore.getOptions();
        astParser.setCompilerOptions(options);
        return astParser;
    }

    static public HashSet<String> getSourceTypeNames(ITypeBinding type){
        HashSet<String> nomes = new HashSet<String>();

        if(type.isFromSource()){
            nomes.add(type.getQualifiedName());
        }

        //A[] returns if A is from Source
        if(type.isArray() && type.getElementType().isFromSource())
            nomes.add(type.getElementType().getQualifiedName());

        //T<A>, T<A,B> - true if A is from Source or if A or B are source types and its a Collection
        //Collections with source as parameters are considered because represents a relationship with a source type
        if(type.isParameterizedType()){
            ITypeBinding[] interfaces = type.getInterfaces();
            boolean isCollection = false;
            for (ITypeBinding iTypeBinding : interfaces) {
                isCollection = isCollection || iTypeBinding.getBinaryName().equals("java.util.Collection");
            }

            if(isCollection){
                for (ITypeBinding typeArg : type.getTypeArguments()) {
                    if(typeArg.isFromSource())
                        nomes.add(typeArg.getQualifiedName());
                }
            }
        }
        return nomes;
    }

    static public boolean isStatic(Object obj){
        HashSet<Modifier> modifiers = new HashSet<Modifier>();
        if(obj instanceof FieldDeclaration){
            FieldDeclaration field = (FieldDeclaration)obj;
            for(Object o : field.modifiers()){
                if(o instanceof Modifier){
                    Modifier modifier = (Modifier)o;
                    modifiers.add(modifier);
                }
            }
        }
        if(obj instanceof MethodDeclaration){
            MethodDeclaration method = (MethodDeclaration)obj;
            for(Object o : method.modifiers()){
                if(o instanceof Modifier){
                    Modifier modifier = (Modifier)o;
                    modifiers.add(modifier);
                }
            }
        }
        for(Modifier modifier : modifiers) if(modifier.isStatic())
            return true;
        return false;
    }

    public static String[] getGodClassMetrics() {
        return GodClassMetrics;
    }

}
