package candidate;

import metrics.Metric;

import org.eclipse.jdt.core.dom.*;
import parser.GodClassParser;

import java.lang.reflect.Constructor;

public class GodClassCandidate extends SmellCandidate {
    private boolean valid;

    public GodClassCandidate(TypeDeclaration type, String sourceCode, String path){
        super();
        valid = true;
        try {
            info.put("Path",path);
            info.put("ClassName",type.getName().toString());
            for(String metricName : GodClassParser.getGodClassMetrics()) {
                Class mc = Class.forName("metrics." + metricName);
                Constructor con = mc.getConstructor(type.getClass());
                Metric metric = (Metric) con.newInstance(type);
                metric.setSourceCode(sourceCode);
                metric.calculate();
                if(metric.getMetrics().size() == 0){
                    valid = false;
                    break;
                }
                features.putAll(metric.getMetrics());
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public boolean isValidCandidate() {
        return valid;
    }

}
