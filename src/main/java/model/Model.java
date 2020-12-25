package model;

import net.sf.json.JSONObject;

public class Model {
    protected String name;

    public Model(){

    }

    protected String modifyJSON(String json){
        JSONObject jsonObject = JSONObject.fromObject(json);
        if(jsonObject.get("class_name") != null)
            jsonObject.put("class_name","Model");
        return jsonObject.toString();
    }

}
