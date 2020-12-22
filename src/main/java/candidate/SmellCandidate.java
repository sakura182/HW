package candidate;

import java.util.HashMap;

/*
features：候选实体的所有初始特征
info：候选实体的相关信息
 */
public abstract class SmellCandidate {
    protected HashMap<String,Object> features;
    protected HashMap<String,String> info;

    public SmellCandidate() {
        features = new HashMap<String,Object>();
        info = new HashMap<String, String>();
    }

    public HashMap<String, String> getInfo() {
        return info;
    }

    public HashMap<String, Object> getFeatures() {
        return features;
    }

    //用于添加候选实体的信息（比如检测结果等等）
    public void addInfo(String key, String value){
        info.put(key,value);
    }

    //判断候选实体是否需要进入下一步检测
    public abstract boolean isValidCandidate();

}
