package puzzle.puzzles;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import savestate.SaveState;
import savestate.SaveStateMod;

import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class StageLoader {

    private static final Gson GSON = new Gson();
    private static final Type TYPE = new TypeToken<List<StageObject>>() {}.getType();

    public static List<StageObject> load(File file) throws FileNotFoundException {
        JsonReader reader = new JsonReader(new FileReader(file));
        List<StageObject> data = GSON.fromJson(reader, TYPE);
        System.out.println("무야호!");
        return data;
    }

    public static void save(File file) throws IOException {
        if(SaveStateMod.saveState != null) {
            try {
                FileWriter fileWriter = new FileWriter(file);
                fileWriter.write(SaveStateMod.saveState.encode());
                fileWriter.flush();
                fileWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void loadStage(int stageNumber) {
        JsonParser jsonParser = new JsonParser();
        FileHandle fileHandle = Gdx.files.internal("puzzleResources/puzzles/stage_" + stageNumber + ".json");
        Object object = jsonParser.parse(fileHandle.reader());
        JsonObject jsonObject = (JsonObject) object;
        SaveStateMod.saveState = new SaveState(jsonObject.toString());
        SaveStateMod.saveState.loadState();
    }
    /*
    public static void main(String[] args) {

    }*/
}
