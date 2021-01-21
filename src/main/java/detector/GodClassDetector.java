package detector;

import RemoteModel.IRemoteModel;
import candidate.GodClassCandidate;
import com.csvreader.CsvWriter;
import com.sun.xml.internal.fastinfoset.algorithm.FloatEncodingAlgorithm;
import model.GodClassModel;
import model.Word2VecModel;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;
import parser.GodClassParser;
import utils.Config;
import utils.DataPreprocess;
import utils.JdbcUtil;
import utils.Utils;

import java.io.IOException;
import java.nio.charset.Charset;
import java.rmi.NotBoundException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

public class GodClassDetector extends AbstractDetector {
    //private Word2VecModel embeddingModel;
    private ArrayList<GodClassCandidate> candidates;
    private IRemoteModel remoteMath ;
    private String[] metrics = new String[] {"ATFD","DCC","DIT","TCC","LCOM","CAM","WMC","LOC","NOPA","NOAM","NOA","NOM"};
/*
    public GodClassDetector(String[] sourceFilePaths, String[] encodings, String[] classPaths, String nnModelPath, String embeddingModelPath) throws IOException {
        super(sourceFilePaths,encodings,classPaths);
        embeddingModel = new Word2VecModel(embeddingModelPath);
    }*/
    public GodClassDetector(String[] sourceFilePaths, String[] encodings, String[] classPaths, String hostIp) throws IOException, NotBoundException {
        super(sourceFilePaths,encodings,classPaths);
        Registry registry = LocateRegistry.getRegistry(hostIp);
        remoteMath = (IRemoteModel)registry.lookup("Compute");

    }

    public void detect() throws IOException {
        GodClassParser parser = new GodClassParser(sourceFilePaths,encodings,classPaths);
        parser.parse();
        candidates = parser.getGodClassCandidates();

        //GodClassModel godClassModel = new GodClassModel("D:\\\\TSE\\\\Resources\\\\HW\\\\FeatureEnvyModel.h5");
        /*for(GodClassCandidate candidate : candidates){
            HashMap<String,Object> features = candidate.getFeatures();
            //INDArray[] input = preprocess(features);
            //INDArray[] output = godClassModel.predict(input);
            INDArray[] output = remoteMath.GodClass_predict(features);
            double[][] dOutput = output[0].toDoubleMatrix();
            if(dOutput[0][0] > Config.getGodClassThreshold()) {
                candidate.addInfo("Probability", "" + dOutput[0][0]);
            }
        }*/
        ArrayList<String> res=remoteMath.GodClass_detect(candidates);
        System.out.println(res);
    }



    public void displayResult(){
        for(GodClassCandidate candidate : candidates){
            HashMap<String,String> info = candidate.getInfo();
            String probability = info.get("Probability");
            if(probability == null) continue;
            System.out.println(info.get("Path")+"||"+info.get("ClassName")+"||"+probability);
        }
    }
    public void displayText(String path){
        try{
            CsvWriter csvwriter = new CsvWriter(path,',', Charset.forName("UTF-8"));
            String[] csvHeaders = {"Class Path","ClassName","Probability"};
            csvwriter.writeRecord(csvHeaders);
            for(GodClassCandidate candidate : candidates){
                HashMap<String,String> info = candidate.getInfo();
                String probability = info.get("Probability");
                if(probability == null) continue;
                String[] csvContent = {info.get("Path"),info.get("ClassName"),probability};
                csvwriter.writeRecord(csvContent);

            }
            csvwriter.close();
            System.out.println("------write finished---------");

        }catch (IOException e){
            e.printStackTrace();
        }

    }
    public void saveToDataBase() {
        JdbcUtil conn = null;
        conn.getConn();
        for(GodClassCandidate candidate : candidates){
            HashMap<String,String> info = candidate.getInfo();
            String probability = info.get("Probability");
            if(probability == null) continue;
            conn.add2(info.get("Path"),info.get("ClassName"),probability);
        }
        conn.close();
    }

}
