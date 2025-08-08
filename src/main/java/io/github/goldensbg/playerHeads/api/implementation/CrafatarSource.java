package io.github.goldensbg.playerHeads.api.implementation;

import io.github.goldensbg.playerHeads.api.SkinSource;
import io.github.goldensbg.playerHeads.api.SkinSourceEnum;
import net.kyori.adventure.text.Component;
import net.md_5.bungee.api.chat.BaseComponent;
import org.bukkit.OfflinePlayer;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

public class CrafatarSource extends SkinSource {

    public CrafatarSource(boolean useUUIDWhenRetrieve) {
        super(SkinSourceEnum.MCHEADS, false, useUUIDWhenRetrieve);
    }

    public CrafatarSource() {
        super(SkinSourceEnum.MCHEADS, false);
    }

    @Override
    public Component getHead(OfflinePlayer player, boolean overlay) {

        if (!hasUsernameSupport() && !useUUIDWhenRetrieve()) {
            throw new UnsupportedOperationException("CrafatarSource does not support username to retrieve player heads");
        }

        String[] colors = new String[64];
        try {
            String url = "https://crafatar.com/avatars/" + player.getUniqueId() + "?size=8";
            if (overlay) { url += "&overlay";}

            BufferedImage skinImage = ImageIO.read(new URL(url));
            int faceWidth = 8, faceHeight = 8;

            int index = 0;
            for (int x = 0; x < faceHeight; x++) {
                for (int y = 0; y < faceWidth; y++) {
                    colors[index++] = String.format("#%06X", (skinImage.getRGB(x, y) & 0xFFFFFF));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return toComponent(colors);

    }

}