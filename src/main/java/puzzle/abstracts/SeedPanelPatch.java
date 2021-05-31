package puzzle.abstracts;

import basemod.ReflectionHacks;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.controller.CInputActionSet;
import com.megacrit.cardcrawl.helpers.input.ScrollInputProcessor;
import com.megacrit.cardcrawl.screens.mainMenu.MenuButton;
import com.megacrit.cardcrawl.screens.mainMenu.MenuPanelScreen;
import com.megacrit.cardcrawl.ui.panels.SeedPanel;

public class SeedPanelPatch {
    public SeedPanelPatch() {

    }


    @SpirePatch(
            clz = SeedPanel.class,
            method = "close"
    )
    public static class SeedClosePatch {
        public SeedClosePatch() {
        }

        public static SpireReturn Prefix(SeedPanel _instance) {
            _instance.yesHb.move(-1000.0F, -1000.0F);
            _instance.noHb.move(-1000.0F, -1000.0F);
            _instance.shown = false;
            ReflectionHacks.setPrivate(_instance, SeedPanel.class, "animTimer", 0.25F);
            Gdx.input.setInputProcessor(new ScrollInputProcessor());
            return SpireReturn.Return((Object) null);
        }
    }

    @SpirePatch(
            clz = SeedPanel.class,
            method = "render",
            paramtypez = {SpriteBatch.class}
    )
    public static class SeedRenderPatch {
        public SeedRenderPatch() {
        }

        public static SpireReturn Prefix(SeedPanel _instance, SpriteBatch sb) {
            Color screenColor = (Color) ReflectionHacks.getPrivate(_instance, SeedPanel.class, "screenColor");
            Color uiColor = (Color) ReflectionHacks.getPrivate(_instance, SeedPanel.class, "uiColor");
            sb.setColor(screenColor);
            sb.draw(ImageMaster.WHITE_SQUARE_IMG, 0.0F, 0.0F, (float)Settings.WIDTH, (float)Settings.HEIGHT);
            sb.setColor(uiColor);
            sb.draw(ImageMaster.OPTION_CONFIRM, (float)Settings.WIDTH / 2.0F - 180.0F, Settings.OPTION_Y - 207.0F, 180.0F, 207.0F, 360.0F, 414.0F, Settings.scale, Settings.scale, 0.0F, 0, 0, 360, 414, false, false);
            sb.draw(ImageMaster.RENAME_BOX, (float)Settings.WIDTH / 2.0F - 160.0F, Settings.OPTION_Y - 160.0F, 160.0F, 160.0F, 320.0F, 320.0F, Settings.scale * 1.1F, Settings.scale * 1.1F, 0.0F, 0, 0, 320, 320, false, false);
            FontHelper.renderSmartText(sb, FontHelper.cardTitleFont, _instance.textField, (float)Settings.WIDTH / 2.0F - 120.0F * Settings.scale, Settings.OPTION_Y + 4.0F * Settings.scale, 100000.0F, 0.0F, uiColor, 0.82F);
            if(_instance instanceof TitlePanel) {
                if (!_instance.isFull()) {
                    float tmpAlpha = (MathUtils.cosDeg((float)(System.currentTimeMillis() / 3L % 360L)) + 1.25F) / 3.0F * uiColor.a;
                    FontHelper.renderSmartText(sb, FontHelper.cardTitleFont, "_", (float)Settings.WIDTH / 2.0F - 122.0F * Settings.scale + FontHelper.getSmartWidth(FontHelper.cardTitleFont, _instance.textField, 1000000.0F, 0.0F, 0.82F), Settings.OPTION_Y + 4.0F * Settings.scale, 100000.0F, 0.0F, new Color(1.0F, 1.0F, 1.0F, tmpAlpha), 0.82F);
                }

                Color c = Settings.GOLD_COLOR.cpy();
                c.a = uiColor.a;
                FontHelper.renderFontCentered(sb, FontHelper.cardTitleFont, "Title of the Puzzle", (float)Settings.WIDTH / 2.0F, Settings.OPTION_Y + 126.0F * Settings.scale, c);
                if (_instance.yesHb.clickStarted) {
                    sb.setColor(new Color(1.0F, 1.0F, 1.0F, uiColor.a * 0.9F));
                    sb.draw(ImageMaster.OPTION_YES, (float)Settings.WIDTH / 2.0F - 86.5F - 100.0F * Settings.scale, Settings.OPTION_Y - 37.0F - 120.0F * Settings.scale, 86.5F, 37.0F, 173.0F, 74.0F, Settings.scale, Settings.scale, 0.0F, 0, 0, 173, 74, false, false);
                    sb.setColor(new Color(uiColor));
                } else {
                    sb.draw(ImageMaster.OPTION_YES, (float)Settings.WIDTH / 2.0F - 86.5F - 100.0F * Settings.scale, Settings.OPTION_Y - 37.0F - 120.0F * Settings.scale, 86.5F, 37.0F, 173.0F, 74.0F, Settings.scale, Settings.scale, 0.0F, 0, 0, 173, 74, false, false);
                }

                if (!_instance.yesHb.clickStarted && _instance.yesHb.hovered) {
                    sb.setColor(new Color(1.0F, 1.0F, 1.0F, uiColor.a * 0.25F));
                    sb.setBlendFunction(770, 1);
                    sb.draw(ImageMaster.OPTION_YES, (float)Settings.WIDTH / 2.0F - 86.5F - 100.0F * Settings.scale, Settings.OPTION_Y - 37.0F - 120.0F * Settings.scale, 86.5F, 37.0F, 173.0F, 74.0F, Settings.scale, Settings.scale, 0.0F, 0, 0, 173, 74, false, false);
                    sb.setBlendFunction(770, 771);
                    sb.setColor(uiColor);
                }

                if (_instance.yesHb.clickStarted) {
                    c = Color.LIGHT_GRAY.cpy();
                } else if (_instance.yesHb.hovered) {
                    c = Settings.CREAM_COLOR.cpy();
                } else {
                    c = Settings.GOLD_COLOR.cpy();
                }

                c.a = uiColor.a;
                FontHelper.renderFontCentered(sb, FontHelper.cardTitleFont, _instance.TEXT[2], (float)Settings.WIDTH / 2.0F - 110.0F * Settings.scale, Settings.OPTION_Y - 118.0F * Settings.scale, c, 0.82F);
                sb.draw(ImageMaster.OPTION_NO, (float) Settings.WIDTH / 2.0F - 80.5F + 106.0F * Settings.scale, Settings.OPTION_Y - 37.0F - 120.0F * Settings.scale, 80.5F, 37.0F, 161.0F, 74.0F, Settings.scale, Settings.scale, 0.0F, 0, 0, 161, 74, false, false);
                if (!_instance.noHb.clickStarted && _instance.noHb.hovered) {
                    sb.setColor(new Color(1.0F, 1.0F, 1.0F, uiColor.a * 0.25F));
                    sb.setBlendFunction(770, 1);
                    sb.draw(ImageMaster.OPTION_NO, (float)Settings.WIDTH / 2.0F - 80.5F + 106.0F * Settings.scale, Settings.OPTION_Y - 37.0F - 120.0F * Settings.scale, 80.5F, 37.0F, 161.0F, 74.0F, Settings.scale, Settings.scale, 0.0F, 0, 0, 161, 74, false, false);
                    sb.setBlendFunction(770, 771);
                    sb.setColor(uiColor);
                }

                if (_instance.noHb.clickStarted) {
                    c = Color.LIGHT_GRAY.cpy();
                } else if (_instance.noHb.hovered) {
                    c = Settings.CREAM_COLOR.cpy();
                } else {
                    c = Settings.GOLD_COLOR.cpy();
                }

                c.a = uiColor.a;
                FontHelper.renderFontCentered(sb, FontHelper.cardTitleFont, _instance.TEXT[3], (float)Settings.WIDTH / 2.0F + 110.0F * Settings.scale, Settings.OPTION_Y - 118.0F * Settings.scale, c, 0.82F);
                if (_instance.shown) {
                    if (Settings.isControllerMode) {
                        sb.draw(CInputActionSet.proceed.getKeyImg(), 770.0F * Settings.scale - 32.0F, Settings.OPTION_Y - 32.0F - 140.0F * Settings.scale, 32.0F, 32.0F, 64.0F, 64.0F, Settings.scale, Settings.scale, 0.0F, 0, 0, 64, 64, false, false);
                        sb.draw(CInputActionSet.cancel.getKeyImg(), 1150.0F * Settings.scale - 32.0F, Settings.OPTION_Y - 32.0F - 140.0F * Settings.scale, 32.0F, 32.0F, 64.0F, 64.0F, Settings.scale, Settings.scale, 0.0F, 0, 0, 64, 64, false, false);
                    }

                    _instance.yesHb.render(sb);
                    _instance.noHb.render(sb);
                }
                return SpireReturn.Return((Object) null);
            } else if(_instance instanceof GeneratePanel) {
                if (!_instance.isFull()) {
                    float tmpAlpha = (MathUtils.cosDeg((float)(System.currentTimeMillis() / 3L % 360L)) + 1.25F) / 3.0F * uiColor.a;
                    FontHelper.renderSmartText(sb, FontHelper.cardTitleFont, "_", (float)Settings.WIDTH / 2.0F - 122.0F * Settings.scale + FontHelper.getSmartWidth(FontHelper.cardTitleFont, _instance.textField, 1000000.0F, 0.0F, 0.82F), Settings.OPTION_Y + 4.0F * Settings.scale, 100000.0F, 0.0F, new Color(1.0F, 1.0F, 1.0F, tmpAlpha), 0.82F);
                }

                Color c = Settings.GOLD_COLOR.cpy();
                c.a = uiColor.a;
                FontHelper.renderFontCentered(sb, FontHelper.cardTitleFont, "Link Generated!", (float)Settings.WIDTH / 2.0F, Settings.OPTION_Y + 126.0F * Settings.scale, c);
                if (_instance.yesHb.clickStarted) {
                    sb.setColor(new Color(1.0F, 1.0F, 1.0F, uiColor.a * 0.9F));
                    sb.draw(ImageMaster.OPTION_YES, (float)Settings.WIDTH / 2.0F - 86.5F - 100.0F * Settings.scale, Settings.OPTION_Y - 37.0F - 120.0F * Settings.scale, 86.5F, 37.0F, 173.0F, 74.0F, Settings.scale, Settings.scale, 0.0F, 0, 0, 173, 74, false, false);
                    sb.setColor(new Color(uiColor));
                } else {
                    sb.draw(ImageMaster.OPTION_YES, (float)Settings.WIDTH / 2.0F - 86.5F - 100.0F * Settings.scale, Settings.OPTION_Y - 37.0F - 120.0F * Settings.scale, 86.5F, 37.0F, 173.0F, 74.0F, Settings.scale, Settings.scale, 0.0F, 0, 0, 173, 74, false, false);
                }

                if (!_instance.yesHb.clickStarted && _instance.yesHb.hovered) {
                    sb.setColor(new Color(1.0F, 1.0F, 1.0F, uiColor.a * 0.25F));
                    sb.setBlendFunction(770, 1);
                    sb.draw(ImageMaster.OPTION_YES, (float)Settings.WIDTH / 2.0F - 86.5F - 100.0F * Settings.scale, Settings.OPTION_Y - 37.0F - 120.0F * Settings.scale, 86.5F, 37.0F, 173.0F, 74.0F, Settings.scale, Settings.scale, 0.0F, 0, 0, 173, 74, false, false);
                    sb.setBlendFunction(770, 771);
                    sb.setColor(uiColor);
                }

                if (_instance.yesHb.clickStarted) {
                    c = Color.LIGHT_GRAY.cpy();
                } else if (_instance.yesHb.hovered) {
                    c = Settings.CREAM_COLOR.cpy();
                } else {
                    c = Settings.GOLD_COLOR.cpy();
                }

                c.a = uiColor.a;
                FontHelper.renderFontCentered(sb, FontHelper.cardTitleFont, "Copy", (float)Settings.WIDTH / 2.0F - 110.0F * Settings.scale, Settings.OPTION_Y - 118.0F * Settings.scale, c, 0.82F);
                sb.draw(ImageMaster.OPTION_NO, (float) Settings.WIDTH / 2.0F - 80.5F + 106.0F * Settings.scale, Settings.OPTION_Y - 37.0F - 120.0F * Settings.scale, 80.5F, 37.0F, 161.0F, 74.0F, Settings.scale, Settings.scale, 0.0F, 0, 0, 161, 74, false, false);
                if (!_instance.noHb.clickStarted && _instance.noHb.hovered) {
                    sb.setColor(new Color(1.0F, 1.0F, 1.0F, uiColor.a * 0.25F));
                    sb.setBlendFunction(770, 1);
                    sb.draw(ImageMaster.OPTION_NO, (float)Settings.WIDTH / 2.0F - 80.5F + 106.0F * Settings.scale, Settings.OPTION_Y - 37.0F - 120.0F * Settings.scale, 80.5F, 37.0F, 161.0F, 74.0F, Settings.scale, Settings.scale, 0.0F, 0, 0, 161, 74, false, false);
                    sb.setBlendFunction(770, 771);
                    sb.setColor(uiColor);
                }

                if (_instance.noHb.clickStarted) {
                    c = Color.LIGHT_GRAY.cpy();
                } else if (_instance.noHb.hovered) {
                    c = Settings.CREAM_COLOR.cpy();
                } else {
                    c = Settings.GOLD_COLOR.cpy();
                }

                c.a = uiColor.a;
                FontHelper.renderFontCentered(sb, FontHelper.cardTitleFont, "End", (float)Settings.WIDTH / 2.0F + 110.0F * Settings.scale, Settings.OPTION_Y - 118.0F * Settings.scale, c, 0.82F);
                if (_instance.shown) {
                    if (Settings.isControllerMode) {
                        sb.draw(CInputActionSet.proceed.getKeyImg(), 770.0F * Settings.scale - 32.0F, Settings.OPTION_Y - 32.0F - 140.0F * Settings.scale, 32.0F, 32.0F, 64.0F, 64.0F, Settings.scale, Settings.scale, 0.0F, 0, 0, 64, 64, false, false);
                        sb.draw(CInputActionSet.cancel.getKeyImg(), 1150.0F * Settings.scale - 32.0F, Settings.OPTION_Y - 32.0F - 140.0F * Settings.scale, 32.0F, 32.0F, 64.0F, 64.0F, Settings.scale, Settings.scale, 0.0F, 0, 0, 64, 64, false, false);
                    }

                    _instance.yesHb.render(sb);
                    _instance.noHb.render(sb);
                    if (((GeneratePanel) _instance).isCopied) {
                        FontHelper.renderFontCentered(sb, FontHelper.panelNameFont, "Copied Successfully.", (float)Settings.WIDTH / 2.0F, 100.0F * Settings.scale, uiColor);
                    }
                }
                return SpireReturn.Return((Object) null);
            }
            return SpireReturn.Continue();
        }
    }
}
