package puzzle.puzzles;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.screens.charSelect.CharacterSelectScreen;
import puzzle.abstracts.CampaignSelectScreen;
import savestate.SaveState;
import savestate.SaveStateMod;

import java.io.*;

public class StageLoader {

    public static AbstractPlayer.PlayerClass stageClass;
    public static PuzzleType puzzleType = PuzzleType.NONE;
    public static String stageTitle;
    public static String stageAuthor;
    public static int stageNum = 0;

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
        if(stageNumber == 0) {
            stageNumber = 1;
        }
        JsonParser jsonParser = new JsonParser();
        FileHandle fileHandle = Gdx.files.internal("puzzleResources/puzzles/stage_" + stageNumber + ".json");
        Object object = jsonParser.parse(fileHandle.reader());
        JsonObject jsonObject = (JsonObject) object;
        SaveStateMod.saveState = new SaveState(jsonObject.toString());
        SaveStateMod.saveState.loadState();
    }

    public static void loadStageInfo(int stageNumber) {
        if(stageNumber == 0) {
            stageNumber = 1;
        }
        stageNum = stageNumber;
        loadStageInfo("puzzleResources/puzzles/stage_" + stageNum + ".json");
    }

    public static void loadStageInfo(String filePath) {
        JsonParser jsonParser = new JsonParser();
        FileHandle fileHandle = Gdx.files.internal(filePath);
        Object object = jsonParser.parse(fileHandle.reader());
        JsonObject psd = (JsonObject) object;
        String pc = psd.get("player_class").getAsString();
        String title = psd.get("stage_title").getAsString();
        String author = psd.get("stage_author").getAsString();
        AbstractPlayer.PlayerClass playerClass = AbstractPlayer.PlayerClass.valueOf(pc);
        stageClass = playerClass;
        stageTitle = title;
        stageAuthor = author;
        CharacterSelectScreen cs = CardCrawlGame.mainMenuScreen.charSelectScreen;
        if(cs instanceof CampaignSelectScreen) {
            if(((CampaignSelectScreen) cs).puzzleType == CampaignSelectScreen.PuzzleType.CAMPAIGN) {
                puzzleType = PuzzleType.CAMPAIGN;
            } else if(((CampaignSelectScreen) cs).puzzleType == CampaignSelectScreen.PuzzleType.CUSTOM) {
                puzzleType = PuzzleType.CUSTOM;
            } else {
                puzzleType = PuzzleType.MAKER;
            }

        }
    }

    public static void resetInfo() {
        stageClass = null;
        stageTitle = null;
        stageAuthor = null;
    }

    public static void emptyLoad() { }

    public static void emptyLoad2() { }

    public static enum PuzzleType {
        CAMPAIGN, CUSTOM, MAKER, NONE;
    }
}
