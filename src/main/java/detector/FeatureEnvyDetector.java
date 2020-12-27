package detector;

import candidate.FeatureEnvyCandidate;
import model.FeatureEnvyModel;
import model.Word2VecModel;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;
import parser.FeatureEnvyParser;
import utils.Config;
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
            if(maxv > Config.getFeatureEnvyThreshold())
                candidate.addInfo("recommend",targetClassNames.get(pos));
        }
    }

    private INDArray[] preprocess(String methodName, String sourceClassName, Double sourceDistance, ArrayList<String> targetClassNames, ArrayList<Double> targetDistances){
        int targetsNumber = targetClassNames.size();
        int sequenceLength = Config.getFeatureEnvySequenceLength();
        float[][][] input1 = new float[targetsNumber][sequenceLength * 3][];
        float[][][] input2 = new float[targetsNumber][2][1];

        float[][] methodVec = word2vec(methodName);
        float[][] sourceClassVec = word2vec(sourceClassName);

        for(int i=0;i<targetsNumber;i++){
            String targetSimpleName = getSimpleName(targetClassNames.get(i));
            float[][] targetClassVec = word2vec(targetSimpleName);
            for(int j=0;j<sequenceLength;j++) input1[i][j] = methodVec[j];
            for(int j=sequenceLength;j<2*sequenceLength;j++) input1[i][j] = sourceClassVec[j-sequenceLength];
            for(int j=2*sequenceLength;j<3*sequenceLength;j++) input1[i][j] = targetClassVec[j-2*sequenceLength];
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
        words = DataPreprocess.fixWordsSize(words,Config.getFeatureEnvySequenceLength(),"*");
        float[][] vec = new float[words.size()][];
        for(int i=0;i<words.size();i++){
            vec[i] = embeddingModel.getWordVector(words.get(i));
            if(vec[i] == null) vec[i] = new float[Config.getFeatureEnvyEmbeddingDimension()];
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
