package detector;

import candidate.FeatureEnvyCandidate;
import com.csvreader.CsvWriter;
import RemoteModel.IRemoteModel;
import model.Word2VecModel;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;
import parser.FeatureEnvyParser;
import utils.Config;
import utils.DataPreprocess;
import utils.JdbcUtil;

import java.io.IOException;
import java.nio.charset.Charset;
import java.rmi.NotBoundException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.HashMap;

public class FeatureEnvyDetector extends AbstractDetector{
    //private Word2VecModel embeddingModel;
    private ArrayList<FeatureEnvyCandidate> candidates;
    private IRemoteModel remoteMath ;
/*    public FeatureEnvyDetector(String[] sourceFilePaths, String[] encodings,String[] classPaths,String nnModelPath,String embeddingModelPath,String hostIP) throws IOException, NotBoundException {
        super(sourceFilePaths,encodings,classPaths,nnModelPath);
        embeddingModel = new Word2VecModel(embeddingModelPath);
        Registry registry = LocateRegistry.getRegistry("localhost");
        remoteMath = (IRemoteModel)registry.lookup("Compute");

    }*/
public FeatureEnvyDetector(String[] sourceFilePaths, String[] encodings,String[] classPaths,String hostIP) throws IOException, NotBoundException {
    super(sourceFilePaths,encodings,classPaths);
    Registry registry = LocateRegistry.getRegistry(hostIP);
    remoteMath = (IRemoteModel)registry.lookup("Compute");

}

    public void detect() throws IOException {
        FeatureEnvyParser parser = new FeatureEnvyParser(sourceFilePaths,encodings,classPaths);
        parser.parse();
        candidates = parser.getFeatureEnvyCandidates();

        //FeatureEnvyModel featureEnvyModel = new FeatureEnvyModel(nnModelPath);

        /*for(FeatureEnvyCandidate candidate : candidates){
            HashMap<String,Object> features = candidate.getFeatures();
            String methodName = (String)features.get("methodName");
            String sourceClassName = (String)features.get("sourceClassName");
            Double sourceDistance = (Double)features.get("sourceDistance");
            ArrayList<String> targetClassNames = (ArrayList<String>)features.get("targetClassNames");
            ArrayList<Double> targetDistances = (ArrayList<Double>)features.get("targetDistances");

            //INDArray[] input = preprocess(methodName,sourceClassName,sourceDistance,targetClassNames,targetDistances);
            //INDArray[] output = featureEnvyModel.predict(input);
            System.out.println("begin remote");
            INDArray[] output = remoteMath.Feature_predict(methodName,sourceClassName,sourceDistance,targetClassNames,targetDistances);
            System.out.println("remote finished");
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
        }*/
        ArrayList<String> res=remoteMath.Feature_detect(candidates);
        System.out.println(res);
    }

    public void displayResult(){
        for(FeatureEnvyCandidate candidate : candidates){
            HashMap<String,String> info = candidate.getInfo();
            String recommend = info.get("recommend");
            if(recommend == null) continue;
            System.out.println(info.get("Path")+"||"+info.get("SourceClass")+"||"+info.get("Method")+"||"+recommend);
        }
    }
    public void displayText(String path){

        try{
            CsvWriter csvwriter = new CsvWriter(path,',', Charset.forName("UTF-8"));
            String[] csvHeaders = {"Class Path","SourceClass","MethodName","RecommendClass"};
            csvwriter.writeRecord(csvHeaders);
            for(FeatureEnvyCandidate candidate : candidates){
                HashMap<String,String> info = candidate.getInfo();
                String recommend = info.get("recommend");
                if(recommend == null) continue;
                String[] csvContent = {info.get("Path"),info.get("SourceClass"),info.get("Method"),recommend};
                csvwriter.writeRecord(csvContent);

        }
            csvwriter.close();
            System.out.println("------write finished---------");

        }catch (IOException e){
            e.printStackTrace();
        }

    }

    public void saveToDataBase(){

        JdbcUtil conn = null;
        conn.getConn();
        for(FeatureEnvyCandidate candidate:candidates){
            HashMap<String,String> info = candidate.getInfo();
            String recommend = info.get("recommend");
            if(recommend == null) continue;
            conn.add1(info.get("Path"),info.get("SourceClass"),info.get("Method"),recommend);
        }
        conn.close();
    }

}
