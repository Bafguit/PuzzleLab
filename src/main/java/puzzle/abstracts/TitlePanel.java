package puzzle.abstracts;

import basemod.ReflectionHacks;
import basemod.patches.com.megacrit.cardcrawl.helpers.TopPanel.TopPanelHelper;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import java.awt.datatransfer.StringSelection;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;

import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.screens.mainMenu.MainMenuScreen;
import com.megacrit.cardcrawl.ui.panels.SeedPanel;
import puzzle.PuzzleLab;
import puzzle.puzzles.StageLoader;

public class TitlePanel extends SeedPanel {

    public TitlePanel() {
    }

    public void show() {
        super.show();
        textField = "New Title";
    }

    public void confirm() {
        StageLoader.stageTitle = textField;
        this.close();
        PanelHandler.generatePanel.show(MainMenuScreen.CurScreen.NONE);
    }
}
