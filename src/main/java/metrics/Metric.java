package metrics;

import java.util.HashMap;

/*
result：度量结果，可能有多个
name：度量名称，如果多个，只存其中一个
sourceCode：实体所在Java文件的代码
 */
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
