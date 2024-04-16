package org.parknich.theme.item.items;

import net.minecraft.ChatFormatting;
import org.parknich.theme.ParkTheme;
import org.parknich.theme.Panel;
import org.rusherhack.client.api.feature.module.IModule;
import org.rusherhack.client.api.render.IRenderer2D;
import org.rusherhack.client.api.render.RenderContext;
import org.rusherhack.client.api.setting.ColorSetting;
import org.rusherhack.core.setting.Setting;

import java.awt.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ColorItem extends ExtendableItem{
    public ColorItem(ExtendableItem parent, IModule module, Panel panel, Setting<?> settingValue) {
        super(parent, module, panel, settingValue);
        colorMode = ((ColorSetting) setting).getRainbowMode();

        // size
        this.svPickerWidth = getWidth() - 14;
        this.svPickerHeight = getWidth() - 14;

        this.hPickerWidth = 6;
        this.hPickerHeight = getWidth() - 14;

        this.aPickerWidth = getWidth() - 14;
        this.aPickerHeight = 6;

        // cursors
        Color color = ((ColorSetting) setting).getValue();
        float[] hsv = Color.RGBtoHSB(color.getRed(), color.getBlue(), color.getGreen(), null);
        hCursorY = hsv[0] * (hPickerHeight - 3F);
        hCursorX = hPickerWidth / 2;
        svCursorX = hsv[1] * (svPickerWidth - 3F);
        svCursorY = (1.0F - hsv[2]) * (svPickerHeight - 3F);
        aCursorX = (color.getAlpha() / 255F) * (aPickerWidth - 3F);
        aCursorY = aPickerHeight / 2;
        prevHeight = renderHeight = getHeight(false);


        small = !((ColorSetting) setting).isAlphaAllowed();

    }

    ColorSetting.RainbowMode colorMode;
    Pattern pattern = Pattern.compile("(?<=ColorItem\\[).+(?=])");
    private final double svPickerWidth;
    private final double hPickerWidth;
    private final double aPickerWidth;
    private final double hCursorX;
    boolean changed = false;
    double h = 0;
    double rainbow = 0;
    private boolean isOpenPicker;
    private int speed;
    // positions
    private double svPickerX;
    private double svPickerY;
    private final double svPickerHeight;
    private double hPickerX;
    private double hPickerY;
    private final double hPickerHeight;
    private double aPickerX;
    private double aPickerY;
    private final double aPickerHeight;
    // cursor
    private double svCursorX, svCursorY;
    private double hCursorY;
    private double aCursorX;
    private double renderHeight;
    private double prevHeight;
    private final double aCursorY;
    private boolean svChanging, hChanging, aChanging, sChanging, small = false;
    double posYRainbow;


    @Override
    public void render(RenderContext context, double mouseX, double mouseY) {
        super.render(context, mouseX, mouseY);
        possibleHeightUpdate();
        double x = parent.getX() + 1.5;
        renderer.drawRectangle(x ,getY(), getWidth(), getHeight(), ParkTheme.theme.getColorSetting().getValueRGB());

        if(isHovering(mouseX, mouseY)) {
            renderer.drawRectangle(x, getY(), getWidth(), getHeight(), new Color(0, 0, 0, 70).getRGB());
        }

        fontRenderer.drawText(fontRenderer.trimStringToWidth(setting.getDisplayName(), getWidth()), x + 1, getY() + 2, ParkTheme.theme.fontColor.getValueRGB(), getWidth(), 1);
        double rectScale = getHeight();
        double rectX = x + getWidth() - rectScale - 15;
        double rectY = (int) getCenter(getY(), getHeight(), rectScale) + 3;

        renderer.drawRectangle(rectX - 1.3F, rectY - 1.3F, 25, 8.5F, Color.BLACK.getRGB());
        renderer.drawRectangle(rectX - 0.7F, rectY - 0.7F, 24, 7.5F, Color.WHITE.getRGB());
        renderer.drawRectangle(rectX - 0.7F, rectY - 0.7F, 24, 7.5F, ((ColorSetting) setting).getValueRGB());



        if (open || getHeight() > 11) {
            Color c = ((ColorSetting) this.setting).getValue();
            double pickerX = x + 1.5F;
            double pickerY = this.getY() + this.getHeight();
            // sv
            h = ((hPickerHeight - 3) - hCursorY) / (hPickerHeight - 3); //0-80
            double r;
            this.svPickerX = pickerX + 1;
            this.svPickerY = pickerY + 1;
            r = 1.0F / svPickerHeight;
            for (int i = 0; i < svPickerHeight; i++) {
                double v0 = r * (svPickerHeight - i);
                double v1 = r * (svPickerHeight - (i + 1));
                int left = Color.HSBtoRGB((float) h, 0F, (float) v0);
                int right = Color.HSBtoRGB((float) h, 1.0F, (float) v1);
                renderer.drawGradientRectangle(svPickerX, svPickerY + i, svPickerX + svPickerWidth, svPickerY + svPickerHeight, svPickerWidth, 1.1, left, right);
            }

            // sv cursor
            double svCx = svCursorX + svPickerX;
            double svCy = svCursorY + svPickerY;
            renderCursor(svCx, svCy, renderer);
            if (svChanging) {
                svCursorX = mouseX - svPickerX;
                if (svCursorX < 0)
                    svCursorX = 0;
                if (svCursorX > svPickerWidth)
                    svCursorX = svPickerWidth;
                if (svCursorX + 3 > svPickerWidth)
                    svCursorX = svPickerWidth - 3.F;

                svCursorY = mouseY - svPickerY;
                if (svCursorY < 0)
                    svCursorY = 0;
                if (svCursorY > svPickerHeight)
                    svCursorY = svPickerHeight;
                if (svCursorY + 3 > svPickerHeight)
                    svCursorY = svPickerHeight - 3.F;

                changed = true;
            }

            // h

            this.hPickerX = svPickerX + svPickerWidth + 4;
            this.hPickerY = svPickerY - 0.3F;
            r = 1.0F / hPickerHeight;
            for (int i = 0; i < hPickerHeight; i++) {
                double h0 = r * (hPickerHeight - i);
                double h1 = r * (hPickerHeight - (i + 1));
                int top = Color.HSBtoRGB((float) h0, 1.0F, 1.0F);
                int bottom = Color.HSBtoRGB((float) h1, 1.0F, 1.0F);
                renderer.drawGradientRectangle(hPickerX, hPickerY + i, hPickerX + hPickerWidth, hPickerY + hPickerHeight, hPickerWidth, 1, top, bottom);
            }
            // h cursor
            renderCursorNoWith(hCursorX + hPickerX, hCursorY + hPickerY, renderer);

            if (hChanging) {
                hCursorY = mouseY - hPickerY;
                if (hCursorY < 0)
                    hCursorY = 0;
                if (hCursorY > hPickerHeight)
                    hCursorY = hPickerHeight;
                if (hCursorY + 3 > hPickerHeight)
                    hCursorY = hPickerHeight - 3;

                changed = true;
            }

            if (!small) {
                // alpha
                this.aPickerX = svPickerX;
                this.aPickerY = svPickerY + svPickerHeight + 3;
                // rect
                double aRectScale = aPickerHeight / 2;
                double rectCount = (aPickerWidth / aRectScale) - 1;
                for (int i = 0; i < rectCount; i++) {
                    double aRectX1 = aPickerX + i * aRectScale;
                    double aRectX2 = aRectScale; //(i + 1) * aRectScale;
                    if (aRectX2 > aPickerX + aPickerWidth)
                        aRectX2 = aPickerX + aPickerWidth;

                    renderer.drawRectangle(aRectX1, aPickerY, aRectX2, aRectScale, i % 2 == 0 ? new Color(255, 255, 255).getRGB() : new Color(204, 204, 204).getRGB());
                    renderer.drawRectangle(aRectX1, aPickerY + aRectScale, aRectX2, aRectScale, (i + 1) % 2 == 0 ? new Color(255, 255, 255).getRGB() : new Color(204, 204, 204).getRGB());
                }
                int right = toRGBA(c.getRed(), c.getGreen(), c.getBlue(), 1);
                int left = toRGBA(c.getRed(), c.getGreen(), c.getBlue(), 255);
                renderer.drawGradientRectangle(aPickerX, aPickerY, aPickerWidth + aPickerX, aPickerHeight + aPickerY,
                        aPickerWidth, aPickerHeight, right, left);
                // cursor
                double aCx = aCursorX + aPickerX;
                double aCy = aCursorY + aPickerY;
                renderCursorNoHeight(aCx, aCy, renderer);
                if (aChanging) {
                    aCursorX = mouseX - aPickerX;
                    if (aCursorX < 0)
                        aCursorX = 0;
                    if (aCursorX > aPickerWidth)
                        aCursorX = aPickerWidth;
                    if (aCursorX + 3 > aPickerWidth)
                        aCursorX = aPickerWidth - 3;
                    changed = true;
                }
            }

            if (changed) {
                double s = svCursorX / (svPickerWidth - 3F);
                double v = ((svPickerHeight - 3F) - svCursorY) / (svPickerHeight - 3F);
                double a = aCursorX / ((aPickerWidth - 3F));
                Color color = new Color(Color.HSBtoRGB((float) h, (float) s, (float) v));
                setting.setValue(new Color(color.getRed(), color.getGreen(), color.getBlue(), Math.max(0, Math.min(255, (int) (a * 255)))));

                changed = false;
            }
            posYRainbow = !small ? aPickerY + 9 : svPickerY + svPickerHeight + 1;
            //renderer.drawRectangle(x, posYRainbow, getWidth() - 1, getHeight(), isHovering(mouseX, mouseY, x, posYRainbow, x + getWidth(), posYRainbow + getHeight()) ? new Color(0, 0, 0, 70).getRGB() : ExamplePlugin.theme.getColorSetting().getValueRGB());
            //fontRenderer.drawText(fontRenderer.trimStringToWidth("ColorMode: " + colorMode.name(), getWidth()), x + 3.5, posYRainbow, ExamplePlugin.theme.fontColor.getValueRGB(), getWidth(), getHeight());
            if (sChanging) {
                setSettingFromX(mouseX);
            }
        }


        if (isHovering(
                mouseX,
                mouseY,
                x,
                getY(),
                x + getWidth(),
                getY() + getHeight()
        )) {
            String description =
                    ChatFormatting.RED + "Red " + ((ColorSetting) setting).getValue().getRed() +
                            ChatFormatting.GREEN + " Green " + ((ColorSetting) setting).getValue().getGreen() +
                            ChatFormatting.BLUE + " Blue " + ((ColorSetting) setting).getValue().getBlue() +
                            (!small ? ChatFormatting.WHITE + " Alpha " + ((ColorSetting) setting).getValue().getAlpha() : "") +
                            "\n" +
                            ChatFormatting.WHITE +
                            (setting.getDescription().isEmpty() ?
                                    "A Color setting." + ChatFormatting.GREEN + " Name" + ChatFormatting.RESET + " «" + setting.getDisplayName() + "»."
                                    : setting.getDescription());

            drawDesc(renderer, mouseX + 8, mouseY + 8, description);
        }

    }
    public static int toRGBA(int r, int g, int b, int a) {
        return (r << 16) + (g << 8) + (b) + (a << 24);
    }

    @Override
    public boolean keyTyped(int keyCode, int scanCode, int modifiers) {
        if (isCopy(keyCode) && isHovering(mouseX, mouseY, getX(), getY(), getX() + getWidth(), getY() + getHeight())) {

            setClipboardString("ColorItem[" +
                    ((ColorSetting) setting).getValue().getRed() + "/" +
                    ((ColorSetting) setting).getGreen() + "/" +
                    ((ColorSetting) setting).getBlue() + "/" +
                    ((ColorSetting) setting).getAlpha() +
                    "]");
        }
        if (isPaste(keyCode) && isHovering(mouseX, mouseY, getX(), getY(), getX() + getWidth(), getY() + getHeight()) && getClipboardString().startsWith("ColorItem[")) {
            Matcher matcher = pattern.matcher(getClipboardString());

            while (matcher.find()) {
                String[] split = matcher.group(0).split("/");
                Color color = new Color(Integer.parseInt(split[0]), Integer.parseInt(split[1]), Integer.parseInt(split[2]), Integer.parseInt(split[3]));
                setting.setValue(color);
                float[] hsv = Color.RGBtoHSB(color.getRed(), color.getBlue(), color.getGreen(), null);
                hCursorY = hsv[0] * (hPickerHeight - 3F);
                svCursorX = hsv[1] * (svPickerWidth - 3F);
                svCursorY = (1.0F - hsv[2]) * (svPickerHeight - 3F);
                aCursorX = (color.getAlpha() / 255F) * (aPickerWidth - 3F);
                changed = true;
            }
        }
        if (isCTRLR(keyCode) && isHovering(mouseX, mouseY, getX(), getY(), getX() + getWidth(), getY() + getHeight())) {
            Color color = ((ColorSetting) setting).getDefaultValue();
            setting.setValue(color);
            float[] hsv = Color.RGBtoHSB(color.getRed(), color.getBlue(), color.getGreen(), null);
            hCursorY = hsv[0] * (hPickerHeight - 3F);
            svCursorX = hsv[1] * (svPickerWidth - 3F);
            svCursorY = (1.0F - hsv[2]) * (svPickerHeight - 3F);
            aCursorX = (color.getAlpha() / 255F) * (aPickerWidth - 3F);
            changed = true;
        }
        return super.keyTyped(keyCode, scanCode, modifiers);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (open) {
            if (button == 0) {
                if (isHovering(mouseX, mouseY, svPickerX, svPickerY, svPickerX + svPickerWidth, svPickerY + svPickerHeight)) {
                    svChanging = true;
                }
                if (isHovering(mouseX, mouseY, hPickerX, hPickerY, hPickerX + hPickerWidth, hPickerY + hPickerHeight)) {
                    hChanging = true;
                }
                if (isHovering(mouseX, mouseY, aPickerX, aPickerY, aPickerX + aPickerWidth, aPickerY + aPickerHeight) && !small) {
                    aChanging = true;
                }
            }
        }

        if (isHovering(mouseX, mouseY) && !setting.isHidden() && (button == 0 || button == 1)) {
            open = !open;
        }

        return super.mouseClicked(mouseX, mouseY, button);
    }
    private void setSettingFromX(double mouseX) {
        double percent = (mouseX - getX()) / (getWidth());
        int speed = 1 + (int) (254 * percent);
        if (speed >= 1 && speed <= 255) {
            this.speed = speed;
        }
    }
    public double getCenter(double a, double b, double c) {
        return a + (b - c) / 2;
    }
    private void renderCursor(double x, double y, IRenderer2D renderer) {
        renderer.drawRectangle(x, y, 3, 3, new Color(20, 20, 20).getRGB());
        renderer.drawRectangle(x + 0.5, y + 0.5, 1.8, 1.8, new Color(250, 250, 250).getRGB());
    }

    private void renderCursorNoWith(double x, double y, IRenderer2D renderer) {
        renderer.drawRectangle(x - 5, y, 10, 3, new Color(20, 20, 20).getRGB());
        renderer.drawRectangle(x - 4, y + 1, 8, 1, new Color(250, 250, 250).getRGB());
    }

    private void renderCursorNoHeight(double x, double y, IRenderer2D renderer) {
        renderer.drawRectangle(x, y - 5, 3, 10, new Color(20, 20, 20).getRGB());
        renderer.drawRectangle(x + 1, y - 4, 1, 8, new Color(250, 250, 250).getRGB());
    }
    @Override
    public void mouseReleased(double mouseX, double mouseY, int button) {
        if (open) {
            svChanging = false;
            hChanging = false;
            aChanging = false;
            sChanging = false;
        }
        super.mouseReleased(mouseX, mouseY, button);
    }

    @Override
    public double getHeight(boolean total) {
        if(total) return renderHeight;
        return super.getHeight(false);
    }
    protected void possibleHeightUpdate() {
        double temp;
        if (open) {
            double l = !small ? 114F : 102.5F;
            temp = l - getHeight(false);
        } else {
            temp = super.getHeight();
        }
        if (this.renderHeight == temp) {
            return;
        }
        prevHeight = this.renderHeight;
        this.renderHeight = temp;
    }
}
