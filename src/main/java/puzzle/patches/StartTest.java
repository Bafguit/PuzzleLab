//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package puzzle.patches;

import basemod.interfaces.PostDungeonInitializeSubscriber;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.core.CardCrawlGame.GameMode;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.CardHelper;
import com.megacrit.cardcrawl.helpers.MonsterHelper;
import com.megacrit.cardcrawl.helpers.SeedHelper;
import com.megacrit.cardcrawl.helpers.TipTracker;
import com.megacrit.cardcrawl.localization.EventStrings;
import com.megacrit.cardcrawl.monsters.city.GremlinLeader;
import com.megacrit.cardcrawl.monsters.exordium.Lagavulin;
import com.megacrit.cardcrawl.neow.NeowEvent;
import com.megacrit.cardcrawl.neow.NeowReward;
import com.megacrit.cardcrawl.neow.NeowRoom;
import com.megacrit.cardcrawl.random.Random;
import com.megacrit.cardcrawl.screens.DungeonTransitionScreen;
import com.megacrit.cardcrawl.shop.ShopScreen;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.AbstractList;

public class StartTest {

    public static final EventStrings eventStrings = CardCrawlGame.languagePack.getEventString("BASIC");
    public static final String[] TEXT = eventStrings.DESCRIPTIONS;
    public static final String[] OPTIONS = eventStrings.OPTIONS;

    private static int curNum = 0;

    public static void restartRun() {
        CardCrawlGame.music.fadeAll();

        AbstractDungeon.closeCurrentScreen();

        CardCrawlGame.dungeonTransitionScreen = new DungeonTransitionScreen("Exordium");
        AbstractDungeon.reset();
        Settings.hasEmeraldKey = false;
        Settings.hasRubyKey = false;
        Settings.hasSapphireKey = false;
        ShopScreen.resetPurgeCost();
        CardCrawlGame.tips.initialize();
        CardCrawlGame.metricData.clearData();
        CardHelper.clear();
        TipTracker.refresh();
        System.gc();
        if (CardCrawlGame.chosenCharacter == null) {
            CardCrawlGame.chosenCharacter = AbstractDungeon.player.chosenClass;
        }

        if (!Settings.seedSet) {
            Long sTime = System.nanoTime();
            Random rng = new Random(sTime);
            Settings.seedSourceTimestamp = sTime;
            Settings.seed = SeedHelper.generateUnoffensiveSeed(rng);
            SeedHelper.cachedSeed = null;
        }
        AbstractDungeon.bossCount = 1;
        AbstractDungeon.generateSeeds();
        CardCrawlGame.mode = GameMode.CHAR_SELECT;
        AbstractDungeon.getCurrRoom().monsters = MonsterHelper.getEncounter(GremlinLeader.ID);
    }

    public static int getCurNum() {
        return curNum;
    }
/*
    @Override
    public void receivePostDungeonInitialize() {

        if (true) {

            NeowEvent neowEvent = new NeowEvent();
            Method method = null;
            try {
                method = neowEvent.getClass().getDeclaredMethod("blessing");
            } catch (NoSuchMethodException | SecurityException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }


            method.setAccessible(true);

            try {
                method.invoke(neowEvent);
            } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }


        }

    }*/
}
