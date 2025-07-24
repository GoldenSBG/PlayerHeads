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

public class McHeadsSource extends SkinSource {


    public McHeadsSource(boolean useUUIDWhenRetrieve) {
        super(SkinSourceEnum.MCHEADS, true, useUUIDWhenRetrieve);
    }

    public McHeadsSource() {
        super(SkinSourceEnum.MCHEADS, true);
    }

    @Override
    public Component getHead(OfflinePlayer player, boolean overlay) {

        String nameOrUUID = useUUIDWhenRetrieve() ? player.getUniqueId().toString() : player.getName();

        String[] colors = new String[64];
        try {
            String url = "https://mc-heads.net/avatar/" + nameOrUUID + "/8";
            if (!overlay) { url += "/nohelm";}

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
        return toBaseComponent(colors);

    }


}