package puzzle.characterOption;

import basemod.ReflectionHacks;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.screens.charSelect.CharacterOption;
import com.megacrit.cardcrawl.screens.mainMenu.MainMenuPanelButton;
import puzzle.util.TextureLoader;

public class AbstractOption extends CharacterOption {

    public int stageNumber;

    public AbstractOption(int stageNumber) {
        this(stageNumber, false);
    }

    public AbstractOption(int stageNumber, boolean isLocked) {
        super(CardCrawlGame.characterManager.recreateCharacter(AbstractPlayer.PlayerClass.IRONCLAD));
        this.stageNumber = stageNumber;
        this.locked = isLocked;
        if(!isLocked) {
            ReflectionHacks.setPrivate(this, CharacterOption.class, "buttonImg", TextureLoader.getTexture("puzzleResources/images/ui/charButton/stage_" + stageNumber + ".png"));
        }
    }
}