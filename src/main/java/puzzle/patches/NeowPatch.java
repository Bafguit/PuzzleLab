//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package puzzle.patches;

import basemod.BaseMod;
import basemod.ReflectionHacks;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.characters.AnimatedNpc;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.GenericEventDialog;
import com.megacrit.cardcrawl.helpers.*;
import com.megacrit.cardcrawl.helpers.controller.CInputActionSet;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import com.megacrit.cardcrawl.monsters.MonsterGroup;
import com.megacrit.cardcrawl.neow.NeowEvent;
import com.megacrit.cardcrawl.neow.NeowReward;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;

import com.megacrit.cardcrawl.rewards.RewardItem;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.rooms.MonsterRoomBoss;
import com.megacrit.cardcrawl.rooms.TreasureRoomBoss;
import com.megacrit.cardcrawl.rooms.VictoryRoom;
import com.megacrit.cardcrawl.screens.DungeonTransitionScreen;
import com.megacrit.cardcrawl.screens.VictoryScreen;
import com.megacrit.cardcrawl.screens.charSelect.CharacterSelectScreen;
import com.megacrit.cardcrawl.ui.buttons.ProceedButton;
import com.megacrit.cardcrawl.ui.panels.TopPanel;
import com.megacrit.cardcrawl.vfx.InfiniteSpeechBubble;
import javassist.CannotCompileException;
import javassist.CtBehavior;
import javassist.expr.ExprEditor;
import javassist.expr.MethodCall;
import puzzle.PuzzleLab;
import puzzle.abstracts.CampaignSelectScreen;
import puzzle.abstracts.FinishScreen;
import puzzle.puzzles.StageLoader;
import savestate.SaveStateMod;

import static basemod.ReflectionHacks.*;

public class NeowPatch {

    @SpireEnum
    public static NeowReward.NeowRewardType PUZZLE;

    public NeowPatch() {
    }

    @SpirePatch(
            clz = ProceedButton.class,
            method = "update"
    )

    public static class UpdateProceedPatch {
        public UpdateProceedPatch() {
        }

        public static SpireReturn Prefix(ProceedButton _instance) {
            if (!(boolean) getPrivate(_instance, ProceedButton.class, "isHidden")) {
                setPrivate(_instance, ProceedButton.class, "wavyTimer", (float) getPrivate(_instance, ProceedButton.class, "wavyTimer") + Gdx.graphics.getDeltaTime() * 3.0F);
                float current_x = (float) ReflectionHacks.getPrivate(_instance, ProceedButton.class, "current_x");
                Hitbox hb = (Hitbox) getPrivate(_instance, ProceedButton.class, "hb");
                if (current_x - (float) getPrivate(_instance, ProceedButton.class, "SHOW_X") < (float) getPrivate(_instance, ProceedButton.class, "CLICKABLE_DIST")) {
                    ((Hitbox) getPrivate(_instance, ProceedButton.class, "hb")).update();
                }

                _instance.isHovered = hb.hovered;
                if (hb.hovered && InputHelper.justClickedLeft) {
                    CardCrawlGame.sound.play("UI_CLICK_1");
                    ((Hitbox) getPrivate(_instance, ProceedButton.class, "hb")).clickStarted = true;
                }

                hb = (Hitbox) getPrivate(_instance, ProceedButton.class, "hb");
                if (hb.justHovered && AbstractDungeon.screen == AbstractDungeon.CurrentScreen.COMBAT_REWARD) {
                    Iterator var1 = AbstractDungeon.combatRewardScreen.rewards.iterator();

                    while (var1.hasNext()) {
                        RewardItem i = (RewardItem) var1.next();
                        i.flash();
                    }
                }

                if (hb.clicked || CInputActionSet.proceed.isJustPressed()) {
                    ((Hitbox) getPrivate(_instance, ProceedButton.class, "hb")).clicked = false;
                    System.out.println("## Screen Check");
                    _instance.hide();
                    AbstractDungeon.victoryScreen = new FinishScreen((MonsterGroup) null);
                }

                float target_x = (float) getPrivate(_instance, ProceedButton.class, "target_x");
                if (current_x != target_x) {
                    setPrivate(_instance, ProceedButton.class, "current_x", MathUtils.lerp((float) ReflectionHacks.getPrivate(_instance, ProceedButton.class, "current_x"), target_x, Gdx.graphics.getDeltaTime() * 9.0F));
                    current_x = (float) ReflectionHacks.getPrivate(_instance, ProceedButton.class, "current_x");
                    if (Math.abs(current_x - target_x) < Settings.UI_SNAP_THRESHOLD) {
                        setPrivate(_instance, ProceedButton.class, "current_x", target_x);
                    }
                }
                return SpireReturn.Return((Object) null);
            }
            return SpireReturn.Continue();
        }

    }

    @SpirePatch(
            clz = DungeonTransitionScreen.class,
            method = "setAreaName"
    )
    public static class AreaNamePatch {
        public AreaNamePatch() {
        }

        public static SpireReturn Prefix(DungeonTransitionScreen _instance) {
            CharacterSelectScreen cs = CardCrawlGame.mainMenuScreen.charSelectScreen;
            if(cs instanceof CampaignSelectScreen && StageLoader.stageClass != null) {
                if(((CampaignSelectScreen) cs).puzzleType == CampaignSelectScreen.PuzzleType.CAMPAIGN) {
                    _instance.levelName = StageLoader.stageTitle;
                    _instance.levelNum = "Stage " + ((CampaignSelectScreen) cs).currentStage;
                } else if(((CampaignSelectScreen) cs).puzzleType == CampaignSelectScreen.PuzzleType.CUSTOM) {
                    _instance.levelName = StageLoader.stageTitle;
                    _instance.levelNum = "Custom Stage";
                } else {
                    _instance.levelName = "New Puzzle";
                    _instance.levelNum = "Puzzle Maker";
                }

            } else {
                _instance.levelName = "No Level Name";
                _instance.levelNum = "No Level Number";
            }
            AbstractDungeon.name = _instance.levelName;
            AbstractDungeon.levelNum = _instance.levelNum;
            return SpireReturn.Return((Object)null);
        }
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
            float floorX = getPrivate(_instance, TopPanel.class, "floorX");
            float ICON_Y = getPrivate(_instance, TopPanel.class, "ICON_Y");
            float ICON_W = getPrivate(_instance, TopPanel.class, "ICON_W");
            float INFO_TEXT_Y = getPrivate(_instance, TopPanel.class, "INFO_TEXT_Y");
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
            setPrivate(_instance, NeowReward.class, "activated", false);
            setPrivate(_instance, NeowReward.class, "cursed", false);
            setPrivate(_instance, NeowReward.class, "hp_bonus", 0);
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
                setPrivate(_instance, NeowReward.class, "activated", true);
                AbstractDungeon.getCurrRoom().dispose();
                AbstractDungeon.getCurrRoom().clearEvent();
                AbstractDungeon.effectList.clear();
                AbstractDungeon.getCurrRoom().monsters = MonsterHelper.getEncounter("Colosseum Slavers");
                AbstractDungeon.getCurrRoom().smoked = false;
                AbstractDungeon.player.isEscaping = false;
                AbstractDungeon.getCurrRoom().phase = AbstractRoom.RoomPhase.COMBAT;
                AbstractDungeon.getCurrRoom().monsters.init();
                AbstractDungeon.overlayMenu.endTurnButton.enable();
                GenericEventDialog.hide();
                AbstractDungeon.rs = AbstractDungeon.RenderScene.NORMAL;
                AbstractDungeon.player.preBattlePrep();
                if(CardCrawlGame.mainMenuScreen.charSelectScreen instanceof CampaignSelectScreen) {
                    if(((CampaignSelectScreen) CardCrawlGame.mainMenuScreen.charSelectScreen).puzzleType == CampaignSelectScreen.PuzzleType.CAMPAIGN) {
                        StageLoader.loadStage(((CampaignSelectScreen) CardCrawlGame.mainMenuScreen.charSelectScreen).currentStage);
                    }
                }
                AbstractDungeon.getCurrRoom().rewards.clear();
                AbstractDungeon.getCurrRoom().rewardAllowed = false;
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
            AnimatedNpc npc = (AnimatedNpc) getPrivate(_instance, NeowEvent.class, "npc");
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
            int sn = (int) getPrivate(_instance, NeowEvent.class, "screenNum");
            if(sn == 900) {
                ArrayList<NeowReward> rewards = (ArrayList) getPrivate(_instance, NeowEvent.class, "rewards");
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

            final float DIALOG_X = (float) getPrivate(_instance, NeowEvent.class, "DIALOG_X");
            final float DIALOG_Y = (float) getPrivate(_instance, NeowEvent.class, "DIALOG_Y");
            String tempText;
            if(StageLoader.puzzleType.equals(StageLoader.PuzzleType.CAMPAIGN) || StageLoader.puzzleType.equals(StageLoader.PuzzleType.CUSTOM)) {
                tempText = StageLoader.stageTitle + " by " + StageLoader.stageAuthor;
            } else {
                tempText = "Make your own puzzle!";
            }
            AbstractDungeon.effectList.add(new InfiniteSpeechBubble(DIALOG_X, DIALOG_Y, tempText));

            ArrayList<NeowReward> rewards = (ArrayList) getPrivate(_instance, NeowEvent.class, "rewards");
            rewards.add(new NeowPatch.SwapReward());

            _instance.roomEventText.clearRemainingOptions();
            _instance.roomEventText.updateDialogOption(0, rewards.get(0).optionLabel);

            setPrivate(_instance, NeowEvent.class, "screenNum", 900);

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

            final float DIALOG_X = (float) getPrivate(_instance, NeowEvent.class, "DIALOG_X");
            final float DIALOG_Y = (float) getPrivate(_instance, NeowEvent.class, "DIALOG_Y");
            AbstractDungeon.effectList.add(new InfiniteSpeechBubble(DIALOG_X, DIALOG_Y, StartTest.TEXT[StartTest.getCurNum()]));

            ArrayList<NeowReward> rewards = (ArrayList) getPrivate(_instance, NeowEvent.class, "rewards");
            rewards.add(new NeowPatch.SwapReward());

            _instance.roomEventText.clearRemainingOptions();
            _instance.roomEventText.updateDialogOption(0, StartTest.OPTIONS[0]);

            setPrivate(_instance, NeowEvent.class, "screenNum", 900);

            return SpireReturn.Return((Object)null);
        }
    }
}
