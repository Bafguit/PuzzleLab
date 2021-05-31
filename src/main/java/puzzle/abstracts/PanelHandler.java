package puzzle.abstracts;

import basemod.TopPanelItem;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import puzzle.util.TextureLoader;

public class PanelHandler {
    public static final TitlePanel titlePanel = new TitlePanel();
    public static final GeneratePanel generatePanel = new GeneratePanel();
    public static final LinkPanel linkPanel = new LinkPanel();

    public PanelHandler() {

    }

    public static class PuzzleLinkTopPanel extends TopPanelItem {
        public static final String ID = "puzzle:Link";

        public PuzzleLinkTopPanel() {
            super(TextureLoader.getTexture("puzzleResources/images/save.png"), ID);
        }

        public void update() {
            super.update();
            PanelHandler.titlePanel.update();
            PanelHandler.generatePanel.update();
            PanelHandler.linkPanel.update();
        }

        public void render(SpriteBatch sb) {
            super.render(sb);
            PanelHandler.titlePanel.render(sb);
            PanelHandler.generatePanel.render(sb);
            PanelHandler.linkPanel.render(sb);
        }

        protected void onClick() {
            if(AbstractDungeon.getCurrRoom().phase.equals(AbstractRoom.RoomPhase.COMBAT)) {
                PanelHandler.titlePanel.show();
            }
        }
    }
}
