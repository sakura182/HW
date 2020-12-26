package detector;

import candidate.GodClassCandidate;
import com.sun.xml.internal.fastinfoset.algorithm.FloatEncodingAlgorithm;
import model.GodClassModel;
import model.Word2VecModel;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;
import parser.GodClassParser;
import utils.DataPreprocess;
import utils.Utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class GodClassDetector extends AbstractDetector {
    private Word2VecModel embeddingModel;
    private ArrayList<GodClassCandidate> candidates;
    private String[] metrics = new String[] {"ATFD","DCC","DIT","TCC","LCOM","CAM","WMC","LOC","NOPA","NOAM","NOA","NOM"};

    public GodClassDetector(String[] sourceFilePaths, String[] encodings, String[] classPaths, String nnModelPath, String embeddingModelPath) throws IOException {
        super(sourceFilePaths,encodings,classPaths,nnModelPath);
        embeddingModel = new Word2VecModel(embeddingModelPath);
    }

    public void detect() throws IOException {
        GodClassParser parser = new GodClassParser(sourceFilePaths,encodings,classPaths);
        parser.parse();
        candidates = parser.getGodClassCandidates();

        GodClassModel godClassModel = new GodClassModel(nnModelPath);
        for(GodClassCandidate candidate : candidates){
            HashMap<String,Object> features = candidate.getFeatures();
            INDArray[] input = preprocess(features);
            INDArray[] output = godClassModel.predict(input);
            double[][] dOutput = output[0].toDoubleMatrix();
            if(dOutput[0][0] > 0.5) {
                candidate.addInfo("Probability", "" + dOutput[0][0]);
            }
        }
    }

    private INDArray[] preprocess(HashMap<String,Object> features){
        float[][][] input1 = new float[1][][];
        float[][] input2 = new float[1][metrics.length];

        for(int i=0;i<metrics.length;i++){
            input2[0][i] = ((Double)features.get(metrics[i])).floatValue();
        }

        ArrayList<String> attributes = (ArrayList<String>)features.get("Attributes");
        ArrayList<String> methods = (ArrayList<String>)features.get("Methods");
        attributes.addAll(methods);
        input1[0] = calVec(attributes);

        return new INDArray[] {Nd4j.create(input1),Nd4j.create(input2)};
    }

    private float[][] calVec(ArrayList<String> elements){
        float[][] vec = new float[50][200];

        for(int i=(50-elements.size()<0?0:50-elements.size()),j=0;i<50;i++,j++){
            String text = DataPreprocess.tokenize(elements.get(j));
            String[] words = text.split(" ");
            float[] sum = new float[200];
            for(String word : words){
                float[] v = embeddingModel.getWordVector(word);
                if(v == null) continue;
                for(int k=0;k<200;k++) sum[k] += v[k];
            }
            for(int k=0;k<200;k++) sum[k] /= words.length;
            vec[i] = sum;
        }
        return vec;
    }

    public void displayResult(){
        for(GodClassCandidate candidate : candidates){
            HashMap<String,String> info = candidate.getInfo();
            String probability = info.get("Probability");
            if(probability == null) continue;
            System.out.println(info.get("Path")+"||"+info.get("ClassName")+"||"+probability);
        }
    }

}
