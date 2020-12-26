package detector;

import candidate.FeatureEnvyCandidate;
import model.FeatureEnvyModel;
import model.Word2VecModel;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;
import parser.FeatureEnvyParser;
import utils.DataPreprocess;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class FeatureEnvyDetector extends AbstractDetector{
    private Word2VecModel embeddingModel;
    private ArrayList<FeatureEnvyCandidate> candidates;

    public FeatureEnvyDetector(String[] sourceFilePaths, String[] encodings,String[] classPaths,String nnModelPath,String embeddingModelPath) throws IOException {
        super(sourceFilePaths,encodings,classPaths,nnModelPath);
        embeddingModel = new Word2VecModel(embeddingModelPath);
    }

    public void detect() throws IOException {
        FeatureEnvyParser parser = new FeatureEnvyParser(sourceFilePaths,encodings,classPaths);
        parser.parse();
        candidates = parser.getFeatureEnvyCandidates();

        FeatureEnvyModel featureEnvyModel = new FeatureEnvyModel(nnModelPath);
        for(FeatureEnvyCandidate candidate : candidates){
            HashMap<String,Object> features = candidate.getFeatures();
            String methodName = (String)features.get("methodName");
            String sourceClassName = (String)features.get("sourceClassName");
            Double sourceDistance = (Double)features.get("sourceDistance");
            ArrayList<String> targetClassNames = (ArrayList<String>)features.get("targetClassNames");
            ArrayList<Double> targetDistances = (ArrayList<Double>)features.get("targetDistances");

            INDArray[] input = preprocess(methodName,sourceClassName,sourceDistance,targetClassNames,targetDistances);
            INDArray[] output = featureEnvyModel.predict(input);
            int pos = 0;
            double maxv = 0;
            double[][] dOutput = output[0].toDoubleMatrix();
            for(int i=0;i<dOutput.length;i++) {
                double p = dOutput[i][1];
                if(p > maxv){
                    maxv = p;
                    pos= i;
                }
            }
            if(maxv > 0.6)
                candidate.addInfo("recommend",targetClassNames.get(pos));
        }
    }

    private INDArray[] preprocess(String methodName, String sourceClassName, Double sourceDistance, ArrayList<String> targetClassNames, ArrayList<Double> targetDistances){
        int targetsNumber = targetClassNames.size();
        float[][][] input1 = new float[targetsNumber][15][];
        float[][][] input2 = new float[targetsNumber][2][1];

        float[][] methodVec = word2vec(methodName);
        float[][] sourceClassVec = word2vec(sourceClassName);

        for(int i=0;i<targetsNumber;i++){
            String targetSimpleName = getSimpleName(targetClassNames.get(i));
            float[][] targetClassVec = word2vec(targetSimpleName);
            for(int j=0;j<5;j++) input1[i][j] = methodVec[j];
            for(int j=5;j<10;j++) input1[i][j] = sourceClassVec[j-5];
            for(int j=10;j<15;j++) input1[i][j] = targetClassVec[j-10];
            input2[i][0][0] = (float)sourceDistance.doubleValue();
            input2[i][1][0] = (float)targetDistances.get(i).doubleValue();
        }
        return new INDArray[] {Nd4j.create(input1),Nd4j.create(input2)};
    }

    private String getSimpleName(String qualifiedName){
        String[] temp = qualifiedName.split("\\.");
        return temp[temp.length-1];
    }

    private float[][] word2vec(String name){
        ArrayList<String> words = DataPreprocess.splitWord(name);
        words = DataPreprocess.fixWordsSize(words,5,"*");
        float[][] vec = new float[words.size()][];
        for(int i=0;i<words.size();i++){
            vec[i] = embeddingModel.getWordVector(words.get(i));
            if(vec[i] == null) vec[i] = new float[200];
        }

        return vec;
    }

    public void displayResult(){
        for(FeatureEnvyCandidate candidate : candidates){
            HashMap<String,String> info = candidate.getInfo();
            String recommend = info.get("recommend");
            if(recommend == null) continue;
            System.out.println(info.get("Path")+"||"+info.get("SourceClass")+"||"+info.get("Method")+"||"+recommend);
        }
    }

}
