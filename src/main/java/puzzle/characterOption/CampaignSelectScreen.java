package puzzle.characterOption;

import com.megacrit.cardcrawl.screens.charSelect.CharacterSelectScreen;

public class CampaignSelectScreen extends CharacterSelectScreen {

    public PuzzleType puzzleType;
    public int currentStage;

    public CampaignSelectScreen(PuzzleType puzzleType) {
        super();
        this.puzzleType = puzzleType;
        this.currentStage = 0;
    }

    public CampaignSelectScreen() {
        this(PuzzleType.NONE);
    }

    public static enum PuzzleType {
        CAMPAIGN, CUSTOM, MAKER, NONE;
    }
}
