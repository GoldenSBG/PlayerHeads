package io.github.goldensbg.playerHeads.api;

import lombok.Getter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.OfflinePlayer;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class SkinSource {

    @Getter
    private final SkinSourceEnum skinSource;

    private final boolean hasUsernameSupport;

    private final boolean useUUIDWhenRetrieve;

    public SkinSource(SkinSourceEnum skinSource, boolean hasUsernameSupport, boolean useUUIDWhenRetrieve) {
        this.skinSource = skinSource;
        this.hasUsernameSupport = hasUsernameSupport;
        this.useUUIDWhenRetrieve = useUUIDWhenRetrieve;
    }

    public SkinSource(SkinSourceEnum skinSource, boolean hasUsernameSupport) {
        this(skinSource, hasUsernameSupport, true);
    }

    abstract public Component getHead(OfflinePlayer player, boolean overlay);

    public Component toBaseComponent(String[] hexColors) {
        if (hexColors == null || hexColors.length < 64) {
            throw new IllegalArgumentException("Hex colors must have at least 64 elements.");
        }

        List<Component> parts = new ArrayList<>(64);

        for (int i = 0; i < 64; i++) {
            int row = i / 8;
            int col = i % 8;
            char unicodeChar = (char) ('\uF000' + (i % 8) + 1);
            String text;

            if (i == 7 || i == 15 || i == 23 || i == 31 || i == 39 || i == 47 || i == 55) {
                text = unicodeChar + Character.toString('\uF101');
            } else if (i == 63) {
                text = Character.toString(unicodeChar);
            } else {
                text = unicodeChar + Character.toString('\uF102');
            }

            TextColor color = TextColor.fromHexString(hexColors[i]);
            Component pixel = Component.text(text).color(color);
            parts.add(pixel);
        }

        // Optional: Font setzen (wenn du ein eigenes Font-Pack nutzt)
        // Du kannst dies weglassen oder nur setzen, wenn du spezielle Minecraft-Fonts brauchst.
        Component full = Component.join(Component.empty(), parts);
        return full;

        /*TextComponent[][] components = new TextComponent[8][8];

        for (int i = 0; i < 64; i++) {
            int row = i / 8;
            int col = i % 8;
            char unicodeChar = (char) ('\uF000' + (i % 8) + 1);
            TextComponent component = new TextComponent();

            if (i == 7 || i == 15 || i == 23 || i == 31 || i == 39 || i == 47 || i == 55) {
                component.setText(unicodeChar + Character.toString('\uF101'));
            } else if (i == 63) {
                component.setText(Character.toString(unicodeChar));
            } else {
                component.setText(unicodeChar + Character.toString('\uF102'));
            }

            component.setColor(ChatColor.of(hexColors[i]));
            components[row][col] = component;
        }

        TextComponent defaultFont = new TextComponent();
        defaultFont.setText("");
        defaultFont.setFont("minecraft:default");

        BaseComponent[] baseComponents = new ComponentBuilder()
                .append(Arrays.stream(components)
                        .flatMap(Arrays::stream)
                        .toArray(TextComponent[]::new))
                .append(defaultFont)
                .create();

        return baseComponents;*/

    }

    public String[] getPixelColorsFromSkin(String playerSkinUrl, boolean overlay) {
        String[] colors = new String[64];
        try {
            BufferedImage skinImage = ImageIO.read(new URL(playerSkinUrl));

            if (skinImage.getHeight() < 64) {
                overlay = false;
            }

            int faceStartX = 8, faceStartY = 8;
            int faceWidth = 8, faceHeight = 8;
            int overlayStartX = 40, overlayStartY = 8;

            BufferedImage faceImage = skinImage.getSubimage(faceStartX, faceStartY, faceWidth, faceHeight);

            BufferedImage overlayImage = null;
            if (overlay) {
                overlayImage = skinImage.getSubimage(overlayStartX, overlayStartY, faceWidth, faceHeight);
            }

            int index = 0;
            for (int x = 0; x < faceHeight; x++) {
                for (int y = 0; y < faceWidth; y++) {
                    int rgbFace = faceImage.getRGB(x, y);
                    if (overlay && overlayImage != null) {
                        int rgbOverlay = overlayImage.getRGB(x, y);
                        if ((rgbOverlay >> 24) != 0x00) {
                            colors[index++] = String.format("#%06X", (rgbOverlay & 0xFFFFFF));
                            continue;
                        }
                    }
                    colors[index++] = String.format("#%06X", (rgbFace & 0xFFFFFF));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return colors;
    }

    public boolean hasUsernameSupport() {
        return hasUsernameSupport;
    }

    public boolean useUUIDWhenRetrieve() {
        return useUUIDWhenRetrieve;
    }
}
