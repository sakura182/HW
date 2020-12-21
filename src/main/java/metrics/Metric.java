package metrics;

import java.util.HashMap;

public abstract class Metric {
    protected HashMap<String,Object> result;
    protected String name;
    protected String sourceCode;

    public Metric(){
        result = new HashMap<String, Object>();
        name = null;
        sourceCode = null;
    }

    public abstract void calculate();

    public HashMap<String,Object> getMetrics(){
        return result;
    }

    public String getName(){
        return name;
    }

    public void setSourceCode(String sourceCode){
        this.sourceCode = sourceCode;
    }

}
