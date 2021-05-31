package puzzle.abstracts;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.controller.CInputActionSet;
import com.megacrit.cardcrawl.monsters.MonsterGroup;
import com.megacrit.cardcrawl.screens.DungeonTransitionScreen;
import com.megacrit.cardcrawl.screens.VictoryScreen;
import com.megacrit.cardcrawl.ui.buttons.ProceedButton;
import com.megacrit.cardcrawl.ui.buttons.ReturnToMenuButton;

import javax.smartcardio.Card;

public class FinishScreen extends VictoryScreen {
    public FinishScreen(MonsterGroup m) {
        super(m);
    }

    public void update() {
        this.returnButton.update();
        if (this.returnButton.hb.clicked || this.returnButton.show && CInputActionSet.select.isJustPressed()) {
            CInputActionSet.topPanel.unpress();
            if (Settings.isControllerMode) {
                Gdx.input.setCursorPosition(10, Settings.HEIGHT / 2);
            }

            this.returnButton.hb.clicked = false;
            this.returnButton.hide();
            Settings.isTrial = false;
            Settings.isDailyRun = false;
            Settings.isEndless = false;
            CardCrawlGame.trial = null;
            CardCrawlGame.startOver();
        }
    }
}
