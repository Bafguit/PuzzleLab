package puzzle.patches;

import basemod.ReflectionHacks;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.characters.AnimatedNpc;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.neow.NeowEvent;
import com.megacrit.cardcrawl.screens.charSelect.CharacterOption;
import com.megacrit.cardcrawl.screens.charSelect.CharacterSelectScreen;
import com.megacrit.cardcrawl.screens.mainMenu.MainMenuPanelButton;
import com.megacrit.cardcrawl.screens.mainMenu.MainMenuScreen;
import com.megacrit.cardcrawl.screens.mainMenu.MenuButton;
import com.megacrit.cardcrawl.screens.mainMenu.MenuPanelScreen;
import javassist.CtBehavior;
import puzzle.PuzzleLab;
import puzzle.characterOption.CampaignSelectScreen;
import sun.security.provider.ConfigFile;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

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
            clz = CharacterSelectScreen.class,
            method = "positionButtons"
    )
    public static class PositionButtonPatch {
        public PositionButtonPatch() {
        }

        public static SpireReturn Prefix(CharacterSelectScreen _instance) {
            int count = 0;
            if(PuzzleLab.getCurMod() == PuzzleLab.PuzzleModType.CAMPAIGN) {
                float offsetX = (float)Settings.WIDTH / 2.0F - 550.0F * Settings.scale;
                for(int i = 0; i < 4; ++i) {
                    for(int j = 0; j < 6; j++) {
                        if (Settings.isMobile) {
                            ((CharacterOption) _instance.options.get(count++)).hb.move(offsetX + (float) j * 220.0F * Settings.scale, 230.0F * Settings.yScale + (float) i * 220.0F * Settings.scale);
                        } else {
                            ((CharacterOption) _instance.options.get(count++)).hb.move(offsetX + (float) j * 220.0F * Settings.scale, 190.0F * Settings.yScale + (float) i * 220.0F * Settings.scale);
                        }
                    }
                }
                return SpireReturn.Return((Object)null);
            } else {
                return SpireReturn.Continue();
            }
        }
    }

    @SpirePatch(
            clz = CharacterSelectScreen.class,
            method = "initialize"
    )
    public static class CharInitializePatch {
        public CharInitializePatch() {
        }

        public void positionButton(CharacterSelectScreen _instance) {

        }

        @SpireInsertPatch(
                locator = CharInitializePatch.RenderLocator.class
        )

        public static SpireReturn Insert(CharacterSelectScreen _instance) {
            if(_instance instanceof CampaignSelectScreen) {
                if(((CampaignSelectScreen) _instance).puzzleType == CampaignSelectScreen.PuzzleType.CAMPAIGN) {
                    _instance.options.clear();
                    for (int i = 0; i < 24; i++) {
                        AbstractPlayer p = CardCrawlGame.characterManager.recreateCharacter(AbstractPlayer.PlayerClass.THE_SILENT);
                        _instance.options.add(p.getCharacterSelectOption());
                    }
                }
            }
                return SpireReturn.Continue();
        }

        private static class RenderLocator extends SpireInsertLocator {
            private RenderLocator() {
            }

            public int[] Locate(CtBehavior ctBehavior) throws Exception {
                Matcher finalMatcher = new Matcher.MethodCallMatcher(CharacterSelectScreen.class, "positionButtons");
                return LineFinder.findInOrder(ctBehavior, finalMatcher);
            }
        }
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
                if(CardCrawlGame.mainMenuScreen.charSelectScreen instanceof CampaignSelectScreen) {
                    ((CampaignSelectScreen) CardCrawlGame.mainMenuScreen.charSelectScreen).puzzleType = CampaignSelectScreen.PuzzleType.CAMPAIGN;
                }
                CardCrawlGame.mainMenuScreen.charSelectScreen.open(false);
            } else if(result == PUZZLE_CUSTOM) {
                if(CardCrawlGame.mainMenuScreen.charSelectScreen instanceof CampaignSelectScreen) {
                    ((CampaignSelectScreen) CardCrawlGame.mainMenuScreen.charSelectScreen).puzzleType = CampaignSelectScreen.PuzzleType.CUSTOM;
                }
            } else if(result == PUZZLE_MAKER) {
                if(CardCrawlGame.mainMenuScreen.charSelectScreen instanceof CampaignSelectScreen) {
                    ((CampaignSelectScreen) CardCrawlGame.mainMenuScreen.charSelectScreen).puzzleType = CampaignSelectScreen.PuzzleType.MAKER;
                }
                CardCrawlGame.mainMenuScreen.charSelectScreen.open(false);
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
            method = SpirePatch.CONSTRUCTOR,
            paramtypez = {boolean.class}
    )
    public static class MainMenuPatch {
        public MainMenuPatch() {
        }

        public static SpireReturn Postfix(MainMenuScreen _instance) {
            _instance.charSelectScreen = (CharacterSelectScreen) new CampaignSelectScreen();
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
