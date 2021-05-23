//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package puzzle.patches;

import basemod.ReflectionHacks;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.characters.AnimatedNpc;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.GenericEventDialog;
import com.megacrit.cardcrawl.events.RoomEventDialog;
import com.megacrit.cardcrawl.events.city.Colosseum;
import com.megacrit.cardcrawl.events.exordium.DeadAdventurer;
import com.megacrit.cardcrawl.helpers.MonsterHelper;
import com.megacrit.cardcrawl.localization.EventStrings;
import com.megacrit.cardcrawl.monsters.city.GremlinLeader;
import com.megacrit.cardcrawl.neow.NeowEvent;
import com.megacrit.cardcrawl.neow.NeowReward;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;

import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.vfx.InfiniteSpeechBubble;
import javassist.CannotCompileException;
import javassist.CtBehavior;
import javassist.expr.ExprEditor;
import javassist.expr.MethodCall;
import org.omg.CORBA.PUBLIC_MEMBER;

public class NeowPatch {

    @SpireEnum
    public static NeowReward.NeowRewardType PUZZLE;

    public NeowPatch() {
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
                System.out.println("### Neow Reward Activate");
                ReflectionHacks.setPrivate(_instance, NeowReward.class, "activated", true);
                AbstractDungeon.getCurrRoom().dispose();
                AbstractDungeon.getCurrRoom().clearEvent();
                AbstractDungeon.effectList.clear();
                AbstractDungeon.getCurrRoom().monsters = MonsterHelper.getEncounter("Colosseum Slavers");
                AbstractDungeon.getCurrRoom().rewards.clear();
                AbstractDungeon.getCurrRoom().rewardAllowed = false;
                AbstractDungeon.getCurrRoom().event.hasFocus = false;
                AbstractDungeon.getCurrRoom().smoked = false;
                AbstractDungeon.player.isEscaping = false;
                AbstractDungeon.getCurrRoom().phase = AbstractRoom.RoomPhase.COMBAT;
                AbstractDungeon.getCurrRoom().monsters.init();
                AbstractRoom.waitTimer = 0.1F;
                AbstractDungeon.player.preBattlePrep();
                GenericEventDialog.hide();
                CardCrawlGame.fadeIn(1.5F);
                AbstractDungeon.rs = AbstractDungeon.RenderScene.NORMAL;
                AbstractDungeon.getCurrRoom().event.combatTime = true;
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
            AbstractDungeon.effectList.add(new InfiniteSpeechBubble(DIALOG_X, DIALOG_Y, StartTest.TEXT[StartTest.getCurNum()]));

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
