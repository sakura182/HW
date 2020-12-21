package metrics;

import org.eclipse.jdt.core.ToolFactory;
import org.eclipse.jdt.core.compiler.IScanner;
import org.eclipse.jdt.core.compiler.ITerminalSymbols;
import org.eclipse.jdt.core.compiler.InvalidInputException;
import org.eclipse.jdt.core.dom.TypeDeclaration;

import java.util.HashSet;
import java.util.Set;

public class LinesOfCode extends Metric{
    private TypeDeclaration currentClass;

    public LinesOfCode(TypeDeclaration currentClass){
        super();
        this.currentClass = currentClass;
        name = "LOC";
    }

    @Override
    public void calculate(){
        String typeCode = currentClass.toString();
        int res = calculateNumberOfLines(typeCode);
        if(res >= 0) result.put(name,Double.valueOf(res));
    }

    public int calculateNumberOfLines(String code) {
        Set<Integer> l_lineSet = new HashSet<Integer>();
        IScanner l_scanner = ToolFactory.createScanner(false, false, true, true);
        l_scanner.setSource(code.toCharArray());
        try {
            while (true) {
                int token = l_scanner.getNextToken();
                if (token == ITerminalSymbols.TokenNameEOF) {
                    break;
                }
                int l_startpos = l_scanner.getCurrentTokenStartPosition();
                int l_lineNb = l_scanner.getLineNumber(l_startpos);
                l_lineSet.add(Integer.valueOf(l_lineNb));
            }
        } catch (InvalidInputException e) {
            System.out.println("Invalid source in LinesOfCode");
            return -1;
        }
        return l_lineSet.size();
    }
}
