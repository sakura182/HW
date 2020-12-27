package utils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class Config {
    private static Properties properties = null;

    public static void init() throws IOException {
        FileInputStream fileInputStream = new FileInputStream("./src/config.properties");
        properties = new Properties();
        properties.load(fileInputStream);
        fileInputStream.close();
    }

    public static int getFeatureEnvyEmbeddingDimension(){
        return Integer.valueOf(properties.getProperty("FeatureEnvyEmbeddingDimension")).intValue();
    }

    public static int getGodClassEmbeddingDimension(){
        return Integer.valueOf(properties.getProperty("GodClassEmbeddingDimension")).intValue();
    }

    public static int getFeatureEnvySequenceLength(){
        return Integer.valueOf(properties.getProperty("FeatureEnvySequenceLength")).intValue();
    }

    public static int getGodClassSequenceLength(){
        return Integer.valueOf(properties.getProperty("GodClassSequenceLength")).intValue();
    }

    public static double getFeatureEnvyThreshold(){
        return Double.valueOf(properties.getProperty("FeatureEnvyThreshold")).doubleValue();
    }

    public static double getGodClassThreshold(){
        return Double.valueOf(properties.getProperty("GodClassThreshold")).doubleValue();
    }

}
