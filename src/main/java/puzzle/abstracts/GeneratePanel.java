package puzzle.abstracts;

import com.badlogic.gdx.Gdx;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.ui.panels.SeedPanel;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;

public class GeneratePanel extends SeedPanel {
    public boolean isCopied = false;

    public GeneratePanel() {

    }

    public void show() {
        super.show();
        textField = "Link URL";
    }

    public void confirm() {
        String myString = textField;
        StringSelection stringSelection = new StringSelection(myString);
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboard.setContents(stringSelection, null);
        this.isCopied = true;
    }

    public void close() {
        super.close();
        if (Settings.isControllerMode) {
            Gdx.input.setCursorPosition(10, Settings.HEIGHT / 2);
        }
        Settings.isTrial = false;
        Settings.isDailyRun = false;
        Settings.isEndless = false;
        CardCrawlGame.trial = null;
        CardCrawlGame.startOver();
    }
}
