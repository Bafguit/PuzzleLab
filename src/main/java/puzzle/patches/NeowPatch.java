//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package puzzle.patches;

import basemod.ReflectionHacks;
import basemod.interfaces.PostInitializeSubscriber;
import basemod.patches.com.megacrit.cardcrawl.helpers.TopPanel.TopPanelHelper;
import basemod.patches.com.megacrit.cardcrawl.ui.panels.TopPanel.TopPanelPatches;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.characters.AnimatedNpc;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.GenericEventDialog;
import com.megacrit.cardcrawl.events.RoomEventDialog;
import com.megacrit.cardcrawl.events.city.Colosseum;
import com.megacrit.cardcrawl.events.exordium.DeadAdventurer;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.MonsterHelper;
import com.megacrit.cardcrawl.localization.EventStrings;
import com.megacrit.cardcrawl.monsters.city.GremlinLeader;
import com.megacrit.cardcrawl.neow.NeowEvent;
import com.megacrit.cardcrawl.neow.NeowReward;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;

import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.screens.charSelect.CharacterOption;
import com.megacrit.cardcrawl.screens.charSelect.CharacterSelectScreen;
import com.megacrit.cardcrawl.ui.panels.TopPanel;
import com.megacrit.cardcrawl.vfx.InfiniteSpeechBubble;
import javassist.CannotCompileException;
import javassist.CtBehavior;
import javassist.expr.ExprEditor;
import javassist.expr.MethodCall;
import org.omg.CORBA.PUBLIC_MEMBER;
import puzzle.PuzzleLab;
import puzzle.characterOption.CampaignSelectScreen;
import puzzle.puzzles.StageLoader;
import savestate.SaveState;
import savestate.SaveStateMod;

public class NeowPatch {

    @SpireEnum
    public static NeowReward.NeowRewardType PUZZLE;

    public NeowPatch() {
    }

    @SpirePatch(
            clz = TopPanel.class,
            method = "updateMapButtonLogic"
    )
    public static class UpdateMapPatch {
        public UpdateMapPatch() {
        }

        public static SpireReturn Prefix(TopPanel _instance) {
            return SpireReturn.Return((Object)null);
        }
    }

    @SpirePatch(
            clz = TopPanel.class,
            method = "renderDungeonInfo",
            paramtypez = {SpriteBatch.class}
    )
    public static class RenderFloorPatch {
        public RenderFloorPatch() {
        }

        public static SpireReturn Prefix(TopPanel _instance, SpriteBatch sb) {
            float floorX = ReflectionHacks.getPrivate(_instance, TopPanel.class, "floorX");
            float ICON_Y = ReflectionHacks.getPrivate(_instance, TopPanel.class, "ICON_Y");
            float ICON_W = ReflectionHacks.getPrivate(_instance, TopPanel.class, "ICON_W");
            float INFO_TEXT_Y = ReflectionHacks.getPrivate(_instance, TopPanel.class, "INFO_TEXT_Y");
            if (AbstractDungeon.isAscensionMode) {
                sb.draw(ImageMaster.TP_ASCENSION, floorX + 106.0F * Settings.scale, ICON_Y, ICON_W, ICON_W);
                if (AbstractDungeon.ascensionLevel == 20) {
                    FontHelper.renderFontLeftTopAligned(sb, FontHelper.topPanelInfoFont, Integer.toString(AbstractDungeon.ascensionLevel), floorX + 166.0F * Settings.scale, INFO_TEXT_Y, Settings.GOLD_COLOR);
                } else {
                    FontHelper.renderFontLeftTopAligned(sb, FontHelper.topPanelInfoFont, Integer.toString(AbstractDungeon.ascensionLevel), floorX + 166.0F * Settings.scale, INFO_TEXT_Y, Settings.RED_TEXT_COLOR);
                }
            }
            if (_instance.ascensionHb != null) {
                _instance.ascensionHb.render(sb);
            }
            return SpireReturn.Return((Object)null);
        }
    }

    @SpirePatch(
            clz = TopPanel.class,
            method = "renderTopRightIcons"
    )
    public static class RenderMapPatch {
        public RenderMapPatch() {
        }

        public static ExprEditor Instrument() {
            return new ExprEditor() {
                public void edit(MethodCall m) throws CannotCompileException {
                    if (m.getClassName().equals("com.megacrit.cardcrawl.ui.panels.TopPanel") && m.getMethodName().equals("renderMapIcon")) {
                        m.replace("");
                    }

                }
            };
        }
    }

    @SpirePatch(
            clz = NeowReward.class,
            method = SpirePatch.CONSTRUCTOR,
            paramtypez = {boolean.class}
    )
    public static class NewRewardPatch {
        public NewRewardPatch() {
        }

        public static SpireReturn Prefix(NeowReward _instance, boolean firstMini) {
            ReflectionHacks.setPrivate(_instance, NeowReward.class, "activated", false);
            ReflectionHacks.setPrivate(_instance, NeowReward.class, "cursed", false);
            ReflectionHacks.setPrivate(_instance, NeowReward.class, "hp_bonus", 0);
            _instance.drawback = NeowReward.NeowRewardDrawback.NONE;
            _instance.optionLabel = StartTest.OPTIONS[0];
            _instance.type = PUZZLE;
            return SpireReturn.Return((Object)null);
        }
    }

    public static class SwapReward extends NeowReward {
        public SwapReward() {
            super(true);
        }
    }

    @SpirePatch(
            clz = NeowReward.class,
            method = "activate"
    )
    public static class NeowRewardActivatePatch {
        public NeowRewardActivatePatch() {
        }

        public static SpireReturn Prefix(NeowReward _instance) {
            if(_instance.type == NeowPatch.PUZZLE) {
                ReflectionHacks.setPrivate(_instance, NeowReward.class, "activated", true);
                AbstractDungeon.getCurrRoom().dispose();
                AbstractDungeon.getCurrRoom().clearEvent();
                AbstractDungeon.effectList.clear();
                AbstractDungeon.getCurrRoom().monsters = MonsterHelper.getEncounter("Colosseum Slavers");
                AbstractDungeon.getCurrRoom().rewards.clear();
                AbstractDungeon.getCurrRoom().rewardAllowed = false;
                AbstractDungeon.getCurrRoom().smoked = false;
                AbstractDungeon.player.isEscaping = false;
                AbstractDungeon.getCurrRoom().phase = AbstractRoom.RoomPhase.COMBAT;
                AbstractDungeon.getCurrRoom().monsters.init();
                AbstractDungeon.overlayMenu.endTurnButton.enable();
                //AbstractDungeon.player.preBattlePrep();
                GenericEventDialog.hide();
                AbstractDungeon.rs = AbstractDungeon.RenderScene.NORMAL;
                if(CardCrawlGame.mainMenuScreen.charSelectScreen instanceof CampaignSelectScreen) {
                    StageLoader.loadStage(((CampaignSelectScreen) CardCrawlGame.mainMenuScreen.charSelectScreen).currentStage);
                }
                AbstractDungeon.player.update();
                CardCrawlGame.fadeIn(1.5F);
                return SpireReturn.Return((Object) null);
            }

            return SpireReturn.Continue();
        }
    }

    @SpirePatch(
            clz = NeowEvent.class,
            method = "render",
            paramtypez = {SpriteBatch.class}
    )
    public static class NeowRenderPatch {
        public NeowRenderPatch() {
        }

        @SpireInsertPatch(
                locator = NeowPatch.NeowRenderPatch.RenderLocator.class
        )

        public static SpireReturn Insert(NeowEvent _instance, SpriteBatch sb) {
            AnimatedNpc npc = (AnimatedNpc) ReflectionHacks.getPrivate(_instance, NeowEvent.class, "npc");
            if(npc != null) {
                npc.render(sb);
            }
            return SpireReturn.Return((Object)null);
        }

        private static class RenderLocator extends SpireInsertLocator {
            private RenderLocator() {
            }

            public int[] Locate(CtBehavior ctBehavior) throws Exception {
                Matcher finalMatcher = new Matcher.MethodCallMatcher(AnimatedNpc.class, "render");
                return LineFinder.findInOrder(ctBehavior, finalMatcher);
            }
        }
    }

    @SpirePatch(
            clz = NeowEvent.class,
            method = "buttonEffect"
    )
    public static class NeowButtonPatch {
        public NeowButtonPatch() {
        }

        public static SpireReturn Prefix(NeowEvent _instance) {
            int sn = (int)ReflectionHacks.getPrivate(_instance, NeowEvent.class, "screenNum");
            if(sn == 900) {
                ArrayList<NeowReward> rewards = (ArrayList)ReflectionHacks.getPrivate(_instance, NeowEvent.class, "rewards");
                rewards.get(0).activate();
                sn = 99;
                return SpireReturn.Return((Object) null);
            }

            return SpireReturn.Continue();
        }
    }

    @SpirePatch(
            clz = NeowEvent.class,
            method = "blessing"
    )
    public static class BlessingPatch {
        public BlessingPatch() {
        }

        public static SpireReturn Prefix(NeowEvent _instance) {

            Method method = null;
            try {
                method = _instance.getClass().getDeclaredMethod("dismissBubble");
            } catch (NoSuchMethodException | SecurityException e) {
                e.printStackTrace();
            }

            method.setAccessible(true);

            try {
                method.invoke(_instance);
            } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
                e.printStackTrace();
            }

            final float DIALOG_X = (float)ReflectionHacks.getPrivate(_instance, NeowEvent.class, "DIALOG_X");
            final float DIALOG_Y = (float)ReflectionHacks.getPrivate(_instance, NeowEvent.class, "DIALOG_Y");
            String tempText;
            if(PuzzleLab.getCurMod().equals(PuzzleLab.PuzzleModType.MAKER)) {
                tempText = "Puzzle Maker";
            } else {
                tempText = StartTest.TEXT[StartTest.getCurNum()];
            }
            AbstractDungeon.effectList.add(new InfiniteSpeechBubble(DIALOG_X, DIALOG_Y, tempText));

            ArrayList<NeowReward> rewards = (ArrayList)ReflectionHacks.getPrivate(_instance, NeowEvent.class, "rewards");
            rewards.add(new NeowPatch.SwapReward());

            _instance.roomEventText.clearRemainingOptions();
            _instance.roomEventText.updateDialogOption(0, rewards.get(0).optionLabel);

            ReflectionHacks.setPrivate(_instance, NeowEvent.class, "screenNum", 900);

            return SpireReturn.Return((Object)null);
        }
    }

    @SpirePatch(
            clz = NeowEvent.class,
            method = "miniBlessing"
    )
    public static class MiniBlessingPatch {
        public MiniBlessingPatch() {
        }

        public static SpireReturn Prefix(NeowEvent _instance) {

            Method method = null;
            try {
                method = _instance.getClass().getDeclaredMethod("dismissBubble");
            } catch (NoSuchMethodException | SecurityException e) {
                e.printStackTrace();
            }

            method.setAccessible(true);

            try {
                method.invoke(_instance);
            } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
                e.printStackTrace();
            }

            final float DIALOG_X = (float)ReflectionHacks.getPrivate(_instance, NeowEvent.class, "DIALOG_X");
            final float DIALOG_Y = (float)ReflectionHacks.getPrivate(_instance, NeowEvent.class, "DIALOG_Y");
            AbstractDungeon.effectList.add(new InfiniteSpeechBubble(DIALOG_X, DIALOG_Y, StartTest.TEXT[StartTest.getCurNum()]));

            ArrayList<NeowReward> rewards = (ArrayList)ReflectionHacks.getPrivate(_instance, NeowEvent.class, "rewards");
            rewards.add(new NeowPatch.SwapReward());

            _instance.roomEventText.clearRemainingOptions();
            _instance.roomEventText.updateDialogOption(0, StartTest.OPTIONS[0]);

            ReflectionHacks.setPrivate(_instance, NeowEvent.class, "screenNum", 900);

            return SpireReturn.Return((Object)null);
        }
    }
}
