package puzzle.abstracts;

import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.screens.DungeonTransitionScreen;
import com.megacrit.cardcrawl.screens.charSelect.CharacterSelectScreen;
import puzzle.puzzles.StageLoader;

public class PuzzleTransitionScreen extends DungeonTransitionScreen {
    public PuzzleTransitionScreen() {
        super("Puzzle");
        if(CardCrawlGame.mainMenuScreen.charSelectScreen != null) {
            CharacterSelectScreen cs = CardCrawlGame.mainMenuScreen.charSelectScreen;
            if (cs instanceof CampaignSelectScreen && StageLoader.puzzleType != null) {
                if (StageLoader.puzzleType.equals(StageLoader.PuzzleType.CAMPAIGN)) {
                    this.levelName = StageLoader.stageTitle;
                    this.levelNum = "Stage " + ((CampaignSelectScreen) cs).currentStage;
                } else if (StageLoader.puzzleType.equals(StageLoader.PuzzleType.CUSTOM)) {
                    this.levelName = StageLoader.stageTitle;
                    this.levelNum = "Custom Stage";
                } else {
                    this.levelName = "New Puzzle";
                    this.levelNum = "Puzzle Maker";
                }

            } else {
                this.levelName = "No Level Name";
                this.levelNum = "No Level Number";
            }
            AbstractDungeon.name = this.levelName;
            AbstractDungeon.levelNum = this.levelNum;
        }
    }
}
