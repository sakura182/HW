package model;

import org.deeplearning4j.nn.graph.ComputationGraph;
import org.deeplearning4j.nn.modelimport.keras.KerasModelImport;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;


/*
模型文件是包含结构信息的HDF5文件
INDArray[] input = new INDArray[] {Nd4j.create(new float[3][15]),Nd4j.create(new float[3][2][1])};
INDArray[] res = graph.output(input);
System.out.println(res[0]);
 */
public class FeatureEnvyModel extends Model{
    private ComputationGraph graph;

    public FeatureEnvyModel(String path) {
        try {
            graph = KerasModelImport.importKerasModelAndWeights(path);
        }catch(Exception e){
            System.out.println("The model file doesn't exist. Path: "+path);
            graph = null;
        }
    }

    

}
