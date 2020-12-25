package model;

import org.deeplearning4j.nn.graph.ComputationGraph;
import org.deeplearning4j.nn.modelimport.keras.KerasModel;
import org.deeplearning4j.nn.modelimport.keras.utils.KerasModelBuilder;

public class GodClassModel extends Model {
    private ComputationGraph graph;

    public GodClassModel(String path){
        super();
        name = "GodClassModel";
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

}
