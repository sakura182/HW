package model;

import net.sf.json.JSONObject;
import org.deeplearning4j.nn.graph.ComputationGraph;
import org.deeplearning4j.nn.modelimport.keras.KerasModel;
import org.deeplearning4j.nn.modelimport.keras.exceptions.InvalidKerasConfigurationException;
import org.deeplearning4j.nn.modelimport.keras.utils.KerasModelBuilder;

/*
模型文件是包含结构信息的HDF5文件
INDArray[] input = new INDArray[] {Nd4j.create(new float[3][15]),Nd4j.create(new float[3][2][1])};
INDArray[] res = graph.output(input);
System.out.println(res[0]);
 */
public class FeatureEnvyModel extends Model{
    private ComputationGraph graph;

    public FeatureEnvyModel(String path) {
        graph = null;
        try {
            KerasModelBuilder mb = new KerasModel().modelBuilder().modelHdf5Filename(path);
            mb.enforceTrainingConfig(true);
            mb.setModelJson(modifyJSON(mb.getModelJson()));
            KerasModel m = mb.buildModel();
            graph = m.getComputationGraph();
        }catch(InvalidKerasConfigurationException e){
            e.printStackTrace();
            System.out.println("The model configuration is invalid. Path: "+path);
            graph = null;
        }catch(Exception e){
            e.printStackTrace();
            System.out.println("The model file doesn't exist. Path: "+path);
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
