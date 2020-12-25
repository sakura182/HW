package model;

import net.sf.json.JSONObject;
import org.deeplearning4j.nn.graph.ComputationGraph;
import org.deeplearning4j.nn.modelimport.keras.KerasModel;
import org.deeplearning4j.nn.modelimport.keras.exceptions.InvalidKerasConfigurationException;
import org.deeplearning4j.nn.modelimport.keras.utils.KerasModelBuilder;

public class GodClassModel extends Model {
    private ComputationGraph graph;

    public GodClassModel(String path){
        graph = null;
        try {
            KerasModelBuilder mb = new KerasModel().modelBuilder().modelHdf5Filename(path);
            mb.enforceTrainingConfig(true);
            mb.setModelJson(modifyJSON(mb.getModelJson()));
            KerasModel m = mb.buildModel();
            graph = m.getComputationGraph();
        }catch(Exception e){
            e.printStackTrace();
            graph = null;
        }
    }

    private String modifyJSON(String json){
        JSONObject jsonObject = JSONObject.fromObject(json);
        if(jsonObject.get("class_name") != null)
            jsonObject.put("class_name","Model");
        return jsonObject.toString();
    }

}
