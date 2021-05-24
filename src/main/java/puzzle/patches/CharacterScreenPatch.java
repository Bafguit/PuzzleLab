package puzzle.patches;

import basemod.ReflectionHacks;
import com.evacipated.cardcrawl.modthespire.lib.SpireEnum;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.google.gson.Gson;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.daily.DailyScreen;
import com.megacrit.cardcrawl.helpers.GameDictionary;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.neow.NeowReward;
import com.megacrit.cardcrawl.screens.custom.CustomModeScreen;
import com.megacrit.cardcrawl.screens.mainMenu.MainMenuPanelButton;
import com.megacrit.cardcrawl.screens.mainMenu.MainMenuScreen;
import com.megacrit.cardcrawl.screens.mainMenu.MenuButton;
import com.megacrit.cardcrawl.screens.mainMenu.MenuPanelScreen;
import com.megacrit.cardcrawl.ui.panels.TopPanel;
import savestate.SaveStateMod;

import java.awt.*;
import java.util.Map;

public class CharacterScreenPatch {

    @SpireEnum
    public static MainMenuPanelButton.PanelClickResult PUZZLE_CAMPAIGN;
    @SpireEnum
    public static MainMenuPanelButton.PanelClickResult PUZZLE_CUSTOM;
    @SpireEnum
    public static MainMenuPanelButton.PanelClickResult PUZZLE_MAKER;
    @SpireEnum
    public static MenuPanelScreen.PanelScreen PUZZLE;
    @SpireEnum
    public static MenuButton.ClickResult PUZZLE_LAB;

    public CharacterScreenPatch() {
    }

    @SpirePatch(
            clz = MainMenuPanelButton.class,
            method = "setLabel"
    )
    public static class SetLabelPatch {
        public SetLabelPatch() {
        }

        public static SpireReturn Postfix(MainMenuPanelButton _instance) {
            MainMenuPanelButton.PanelClickResult result = (MainMenuPanelButton.PanelClickResult) ReflectionHacks.getPrivate(_instance, MainMenuPanelButton.class, "result");
            if(result == PUZZLE_CAMPAIGN) {
                ReflectionHacks.setPrivate(_instance, MainMenuPanelButton.class, "header", "Campaign");
                ReflectionHacks.setPrivate(_instance, MainMenuPanelButton.class, "description", "Welcome to Doctor D's Puzzle Lab!");
                ReflectionHacks.setPrivate(_instance, MainMenuPanelButton.class, "portraitImg", ImageMaster.P_STANDARD);
            } else if(result == PUZZLE_CUSTOM) {
                ReflectionHacks.setPrivate(_instance, MainMenuPanelButton.class, "header", "Custom");
                ReflectionHacks.setPrivate(_instance, MainMenuPanelButton.class, "description", "Load a custom puzzle.");
                ReflectionHacks.setPrivate(_instance, MainMenuPanelButton.class, "panelImg", ImageMaster.MENU_PANEL_BG_BLUE);
                ReflectionHacks.setPrivate(_instance, MainMenuPanelButton.class, "portraitImg", ImageMaster.P_DAILY);
            } else if(result == PUZZLE_MAKER) {
                ReflectionHacks.setPrivate(_instance, MainMenuPanelButton.class, "header", "Maker");
                ReflectionHacks.setPrivate(_instance, MainMenuPanelButton.class, "description", "Create your own puzzle.");
                ReflectionHacks.setPrivate(_instance, MainMenuPanelButton.class, "panelImg", ImageMaster.MENU_PANEL_BG_RED);
                ReflectionHacks.setPrivate(_instance, MainMenuPanelButton.class, "portraitImg", ImageMaster.P_LOOP);
            }
            return SpireReturn.Continue();
        }
    }

    @SpirePatch(
            clz = MainMenuPanelButton.class,
            method = "buttonEffect"
    )
    public static class ButtonEffectPatch {
        public ButtonEffectPatch() {
        }

        public static SpireReturn Postfix(MainMenuPanelButton _instance) {
            MainMenuPanelButton.PanelClickResult result = (MainMenuPanelButton.PanelClickResult) ReflectionHacks.getPrivate(_instance, MainMenuPanelButton.class, "result");
            if(result == PUZZLE_CAMPAIGN) {

            } else if(result == PUZZLE_CUSTOM) {

            } else if(result == PUZZLE_MAKER) {

            }
            return SpireReturn.Continue();
        }
    }

    @SpirePatch(
            clz = MenuPanelScreen.class,
            method = "initializePanels"
    )
    public static class InitializePanelsPatch {
        public InitializePanelsPatch() {
        }

        public static SpireReturn Postfix(MenuPanelScreen _instance) {
            MenuPanelScreen.PanelScreen result = (MenuPanelScreen.PanelScreen) ReflectionHacks.getPrivate(_instance, MenuPanelScreen.class, "screen");
            float PANEL_Y = (float) ReflectionHacks.getPrivate(_instance, MenuPanelScreen.class, "PANEL_Y");
            if(result == PUZZLE) {
                _instance.panels.add(new MainMenuPanelButton(PUZZLE_CAMPAIGN, MainMenuPanelButton.PanelColor.BLUE, (float) Settings.WIDTH / 2.0F - 450.0F * Settings.scale, PANEL_Y));
                _instance.panels.add(new MainMenuPanelButton(PUZZLE_CUSTOM, MainMenuPanelButton.PanelColor.BEIGE, (float)Settings.WIDTH / 2.0F, PANEL_Y));
                _instance.panels.add(new MainMenuPanelButton(PUZZLE_MAKER, MainMenuPanelButton.PanelColor.RED, (float)Settings.WIDTH / 2.0F + 450.0F * Settings.scale, PANEL_Y));
            }
            return SpireReturn.Continue();
        }
    }

    @SpirePatch(
            clz = MainMenuScreen.class,
            method = "setMainMenuButtons"
    )
    public static class SetMainMenuPatch {
        public SetMainMenuPatch() {
        }

        public static SpireReturn Postfix(MainMenuScreen _instance) {
            if (CardCrawlGame.characterManager.anySaveFileExists()) {
                int index = _instance.buttons.size();
                _instance.buttons.remove(--index);
                _instance.buttons.remove(--index);
            } else {
                int index = _instance.buttons.size();
                _instance.buttons.remove(--index);
            }

            int index = _instance.buttons.size();
            _instance.buttons.add(new MenuButton(PUZZLE_LAB, index));

            return SpireReturn.Continue();
        }
    }

    @SpirePatch(
            clz = MenuButton.class,
            method = "setLabel"
    )
    public static class SetMenuLabelPatch {
        public SetMenuLabelPatch() {
        }

        public static SpireReturn Postfix(MenuButton _instance) {
            if(_instance.result == PUZZLE_LAB) {
                ReflectionHacks.setPrivate(_instance, MenuButton.class, "label", "Puzzle Lab");
            }
            return SpireReturn.Continue();
        }
    }

    @SpirePatch(
            clz = MenuButton.class,
            method = "buttonEffect"
    )
    public static class MenuButtonEffectPatch {
        public MenuButtonEffectPatch() {
        }

        public static SpireReturn Postfix(MenuButton _instance) {
            if(_instance.result == PUZZLE_LAB) {
                CardCrawlGame.mainMenuScreen.panelScreen.open(PUZZLE);
            }
            return SpireReturn.Continue();
        }
    }
}
