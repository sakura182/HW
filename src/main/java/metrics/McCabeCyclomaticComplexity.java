package metrics;

import org.eclipse.jdt.core.dom.*;

public class McCabeCyclomaticComplexity extends Metric {
    private MethodDeclaration currentMethod;
    private String sourceCode;

    public McCabeCyclomaticComplexity(MethodDeclaration currentMethod, String sourceCode){
        super();
        this.currentMethod = currentMethod;
        this.sourceCode = sourceCode;
        name = "MCC";
    }

    @Override
    public void calculate(){
        Block body = currentMethod.getBody();
        if(body == null){
            result.put(name,Double.valueOf(0));
            return ;
        }

        McCabeVisitor mcb = new McCabeVisitor(sourceCode);
        currentMethod.accept(mcb);
        result.put(name,Double.valueOf(mcb.getMCC()));
    }

}

class McCabeVisitor extends ASTVisitor {
    private int cyclomatic = 1;
    private String source;

    McCabeVisitor(String source) {
        this.source = source;
    }

    // McCabe CC is computed as method level. there fore while parsing code
    // if we found TypeDeclaration, AnnotationTypeDeclaration,
    // EnumDeclaration or AnonymousClassDeclaration

    public boolean visit(AnonymousClassDeclaration node) {
        return false; // XXX
    }

    public boolean visit(TypeDeclaration node) {
        return false; // XXX same as above
    }

    public boolean visit(AnnotationTypeDeclaration node) {
        return false; // XXX same as above
    }

    public boolean visit(EnumDeclaration node) {
        return false; // XXX same as above
    }

    public boolean visit(CatchClause node) {
        cyclomatic++;
        return true;
    }

    public boolean visit(ConditionalExpression node) {
        cyclomatic++;
        inspectExpression(node.getExpression());
        return true;
    }

    public boolean visit(DoStatement node) {
        cyclomatic++;
        inspectExpression(node.getExpression());
        return true;
    }

    public boolean visit(EnhancedForStatement node) {
        cyclomatic++;
        inspectExpression(node.getExpression());
        return true;
    }

    public boolean visit(ForStatement node) {
        cyclomatic++;
        inspectExpression(node.getExpression());
        return true;
    }

    public boolean visit(IfStatement node) {
        cyclomatic++;
        inspectExpression(node.getExpression());
        return true;
    }

    public boolean visit(SwitchCase node) {
        if (!node.isDefault()) {
            cyclomatic++;
        }
        return true;
    }

    public boolean visit(WhileStatement node) {
        cyclomatic++;
        inspectExpression(node.getExpression());
        return true;
    }

    public boolean visit(ExpressionStatement node) {
        inspectExpression(node.getExpression());
        return false;
    }

    public boolean visit(VariableDeclarationFragment node) {
        inspectExpression(node.getInitializer());
        return true;
    }

    private void inspectExpression(Expression ex) {
        if ((ex != null) && (source != null)) {
            int start = ex.getStartPosition();
            int end = start + ex.getLength();
            String expression = source.substring(start, end);
            char[] chars = expression.toCharArray();
            for (int i = 0; i < chars.length - 1; i++) {
                char next = chars[i];
                if ((next == '&' || next == '|') && (next == chars[i + 1])) {
                    cyclomatic++;
                }
            }
        }
    }

    public int getMCC(){
        return cyclomatic;
    }

}