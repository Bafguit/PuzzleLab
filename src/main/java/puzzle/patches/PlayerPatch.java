package puzzle.patches;

import basemod.ReflectionHacks;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.characters.AnimatedNpc;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.neow.NeowEvent;
import com.megacrit.cardcrawl.neow.NeowReward;
import javassist.CannotCompileException;
import javassist.CtBehavior;
import javassist.expr.ExprEditor;
import javassist.expr.MethodCall;
import puzzle.characterOption.CampaignSelectScreen;

public class PlayerPatch {
    public PlayerPatch() {
    }

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
                        m.replace("if(com.megacrit.cardcrawl.core.CardCrawlGame.mainMenuScreen.charSelectScreen instanceof puzzle.characterOption.CampaignSelectScreen) { if(((puzzle.characterOption.CampaignSelectScreen) com.megacrit.cardcrawl.core.CardCrawlGame.mainMenuScreen.charSelectScreen).puzzleType == puzzle.characterOption.CampaignSelectScreen.PuzzleType.CAMPAIGN && this.isPlayer) { y = this.hb.cY - this.hb.height / 2.0F;}} $_ = $proceed($$);");
                    }

                }
            };
        }
    }

    @SpirePatch(
            clz = AbstractCreature.class,
            method = "updateHbPopInAnimation"
    )
    public static class UpdateHbPopPatch {
        public UpdateHbPopPatch() {
        }

        public static SpireReturn Prefix(AbstractCreature _instance) {
            if(CardCrawlGame.mainMenuScreen.charSelectScreen instanceof CampaignSelectScreen) {
                if(((CampaignSelectScreen) CardCrawlGame.mainMenuScreen.charSelectScreen).puzzleType == CampaignSelectScreen.PuzzleType.CAMPAIGN && _instance.isPlayer) {
                    ReflectionHacks.setPrivate(_instance, AbstractCreature.class, "hbShowTimer", 0.0F);
                    return SpireReturn.Return((Object)null);
                }
            }
            return SpireReturn.Continue();
        }
    }

}
