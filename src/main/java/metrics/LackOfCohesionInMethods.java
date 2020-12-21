package metrics;

import org.eclipse.jdt.core.Flags;
import org.eclipse.jdt.core.ToolFactory;
import org.eclipse.jdt.core.compiler.IScanner;
import org.eclipse.jdt.core.compiler.ITerminalSymbols;
import org.eclipse.jdt.core.compiler.InvalidInputException;
import org.eclipse.jdt.core.dom.*;
import utils.Utils;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;


public class LackOfCohesionInMethods extends Metric{
    private TypeDeclaration currentClass;
    private HashMap<String, Set<String>> buckets;

    private FieldDeclaration[] fields;
    private MethodDeclaration[] methods;

    public LackOfCohesionInMethods(TypeDeclaration currentClass){
        super();
        this.currentClass = currentClass;
        name = "LCOM";
        buckets = new HashMap<String, Set<String>>();
    }

    @Override
    public void calculate() {
        fields = currentClass.getFields();
        methods = currentClass.getMethods();
        result.put(name,Double.valueOf(0));
        if(fields.length > 1 && methods.length > 1){
            initBuckets(fields);
            if(buckets.size()>0){
                visitMethods(methods);
                result.put(name,Double.valueOf(calculateResult()));
            }
        }
    }

    private void initBuckets(FieldDeclaration[] fields) {
        buckets.clear(); // BUG #867594
        for (FieldDeclaration fd : fields) {
            if(Utils.isStatic(fd)) continue;
            for(Object obj : fd.fragments()) if(obj instanceof VariableDeclarationFragment){
                VariableDeclarationFragment var = (VariableDeclarationFragment)obj;
                buckets.put(var.getName().toString(), new HashSet<String>());
            }
        }
    }

    private void add(String field, String method) {
        if (buckets.containsKey(field)) {
            Set<String> methods = buckets.get(field);
            methods.add(method);
        }
    }

    private void visitMethods(MethodDeclaration[] methods) {
        for (MethodDeclaration method2 : methods) {
            String methodName = method2.getName().toString();
            try {
                if ((method2.getFlags() & Flags.AccStatic) == 0) {
                    IScanner s = ToolFactory.createScanner(false, false, false, false);
                    s.setSource(method2.toString().toCharArray());
                    while (true) {
                        int token = s.getNextToken();
                        if (token == ITerminalSymbols.TokenNameEOF) {
                            break;
                        }
                        if (token == ITerminalSymbols.TokenNameIdentifier) {
                            add(new String(s.getCurrentTokenSource()), methodName);
                        }
                    }
                }
            } catch (InvalidInputException e) {
                System.out.println("LCOM:Invalid scanner input for method" + methodName);
            }
        }
    }

    private double calculateResult() {
        int sum = 0;
        int a = 0;
        Set<String> allMethods = new HashSet<String>();
        for (Iterator<Set<String>> i = buckets.values().iterator(); i.hasNext(); a++) {
            Set<String> methods = i.next();
            allMethods.addAll(methods);
            sum += methods.size();
        }
        int m = allMethods.size();
        if (m == 1) {
            return 0;
        }
        double avg = (double) sum / (double) a;
        return Math.abs((avg - m) / (1 - m));
    }

}