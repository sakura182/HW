package RemoteModel;

import candidate.FeatureEnvyCandidate;
import candidate.GodClassCandidate;
import org.nd4j.linalg.api.ndarray.INDArray;

import java.io.IOException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;

public interface IRemoteModel extends Remote {
    public ArrayList<String> Feature_detect(ArrayList<FeatureEnvyCandidate> candidates) throws RemoteException,IOException;
    public ArrayList<String> GodClass_detect(ArrayList<GodClassCandidate> candidates) throws RemoteException,IOException;
    public void setEmbeddingModelPath(String embeddingModelPath) throws RemoteException;
    public void setNnModelPath1(String nnModelPath)throws RemoteException;
    public void setNnModelPath2(String nnModelPath)throws RemoteException;
}
