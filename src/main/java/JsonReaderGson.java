import com.google.gson.Gson;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
//使用 Gson 读取 JSON 文件

public class JsonReaderGson {
    private static TrainingSystem trainingSystem;

    public static TrainingSystem getTrainingSystem() {
        if (trainingSystem == null) {
            trainingSystem = loadTrainingSystem();
        }
        return trainingSystem;
    }

    private static TrainingSystem loadTrainingSystem() {
        // 加载 TrainingSystem 实例
        Gson gson = new Gson();
        try {
            return gson.fromJson(new FileReader("src/main/resources/data.json"), TrainingSystem.class);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public static void setTrainingSystem(TrainingSystem trainingSystem) {
        //将trainingSystem保存在data.json
        Gson gson = new Gson();
        FileWriter writer;
        try {
            writer = new FileWriter("src/main/resources/data.json");
            gson.toJson(trainingSystem, writer);
            writer.flush();
            writer.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

}

