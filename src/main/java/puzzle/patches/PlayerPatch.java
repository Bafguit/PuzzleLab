package puzzle.patches;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.core.AbstractCreature;
import javassist.CannotCompileException;
import javassist.expr.ExprEditor;
import javassist.expr.MethodCall;
import savestate.SaveState;

public class PlayerPatch {
    public PlayerPatch() {
    }

    @SpirePatch(
            clz = SaveState.class,
            method = "encode"
    )

    public static class StateEncodePatch {
        public StateEncodePatch() {
        }

        @SpireInstrumentPatch
        public static ExprEditor Instrument() {
            return new ExprEditor() {
                public void edit(MethodCall m) throws CannotCompileException {
                    if (m.getClassName().equals("savestate.MapRoomNodeState") && m.getMethodName().equals("encode")) {
                        m.replace("saveStateJson.addProperty(\"player_class\", com.megacrit.cardcrawl.dungeons.AbstractDungeon.player.chosenClass.toString()); saveStateJson.addProperty(\"stage_title\", \"Holding\"); saveStateJson.addProperty(\"stage_author\", \"FastCat\"); $_ = $proceed($$); ");
                    }
                }
            };
        }
    }

    @SpirePatch(
            clz = SaveState.class,
            method = SpirePatch.CONSTRUCTOR,
            paramtypez = {String.class}
    )

//    public static class SaveStatePatch {
//        public SaveStatePatch() {
//        }
//
//        public static SpireReturn Postfix(SaveState _instance, String jsonString) {
//            CharacterSelectScreen cs = CardCrawlGame.mainMenuScreen.charSelectScreen;
//            if(cs instanceof CampaignSelectScreen) {
//                if (((CampaignSelectScreen) cs).puzzleType == CampaignSelectScreen.PuzzleType.CAMPAIGN || ((CampaignSelectScreen) cs).puzzleType == CampaignSelectScreen.PuzzleType.CUSTOM) {
//                    JsonObject psd = (new JsonParser()).parse(jsonString).getAsJsonObject();
//                    String pc = psd.get("player_class").getAsString();
//                    String title = psd.get("stage_title").getAsString();
//                    String author = psd.get("stage_author").getAsString();
//                    AbstractPlayer.PlayerClass playerClass = AbstractPlayer.PlayerClass.valueOf(pc);
//                    StageLoader.loadStageInfo(playerClass, title, author);
//                }
//            }
//            return SpireReturn.Continue();
//        }
//    }

    @SpirePatch(
            clz = AbstractCreature.class,
            method = "renderHealth",
            paramtypez = {SpriteBatch.class}
    )
    public static class PlayerRenderPatch {
        public PlayerRenderPatch() {
        }

        public static ExprEditor Instrument() {
            return new ExprEditor() {
                public void edit(MethodCall m) throws CannotCompileException {
                    if (m.getClassName().equals("com.megacrit.cardcrawl.core.AbstractCreature") && m.getMethodName().equals("renderHealthBg")) {
                        m.replace("if(com.megacrit.cardcrawl.core.CardCrawlGame.mainMenuScreen.charSelectScreen instanceof puzzle.abstracts.CampaignSelectScreen && this.isPlayer) { y = this.hb.cY - this.hb.height / 2.0F; } $_ = $proceed($$);");
                    }

                }
            };
        }
    }
/*
    @SpirePatch(
            clz = AbstractCreature.class,
            method = "updateHbPopInAnimation"
    )
    public static class UpdateHbPopPatch {
        public UpdateHbPopPatch() {
        }

        public static SpireReturn Prefix(AbstractCreature _instance) {
            if(CardCrawlGame.mainMenuScreen.charSelectScreen instanceof CampaignSelectScreen && _instance.isPlayer) {
                ReflectionHacks.setPrivate(_instance, AbstractCreature.class, "hbShowTimer", 0.0F);
                return SpireReturn.Return((Object)null);
            }
            return SpireReturn.Continue();
        }
    }
*/
}
