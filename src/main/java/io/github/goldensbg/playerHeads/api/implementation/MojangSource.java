package io.github.goldensbg.playerHeads.api.implementation;

import io.github.goldensbg.playerHeads.api.SkinSource;
import io.github.goldensbg.playerHeads.api.SkinSourceEnum;
import net.kyori.adventure.text.Component;
import net.md_5.bungee.api.chat.BaseComponent;
import org.bukkit.OfflinePlayer;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Base64;

public class MojangSource extends SkinSource {


    public MojangSource(boolean useUUIDWhenRetrieve) {
        super(SkinSourceEnum.MOJANG, true, useUUIDWhenRetrieve);
    }

    public MojangSource() {
        super(SkinSourceEnum.MOJANG, true);
    }

    @Override
    public Component getHead(OfflinePlayer player, boolean overlay) {

        if (useUUIDWhenRetrieve()) {
            return toBaseComponent(getPixelColorsFromSkin(getPlayerSkinFromMojang(player.getUniqueId().toString()), overlay));
        } else {
            return toBaseComponent(getPixelColorsFromSkin(getPlayerSkinFromMojang(getUUIDFromName(player)), overlay));
        }

    }

    public String getUUIDFromName(OfflinePlayer offlinePlayer) {
        try {
            URL url = new URL("https://api.mojang.com/users/profiles/minecraft/" + offlinePlayer.getName());
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }

                JSONObject jsonObject = new JSONObject(response.toString());
                return jsonObject.getString("id");
                /*
                reader.close();
                String jsonResponse = response.toString();
                JSONObject jsonObject = new JSONObject(jsonResponse);
                return jsonObject.getString("id");
*/
            }
        } catch (IOException | JSONException e) {
            e.printStackTrace();
            return "";
        }

    }

    private String getPlayerSkinFromMojang(String uuid) {
        try {
            URL url = new URL("https://sessionserver.mojang.com/session/minecraft/profile/" + uuid);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                /*reader.close();
                String jsonResponse = response.toString();*/
                JSONObject jsonObject = new JSONObject(response.toString());
                JSONArray propertiesArray = jsonObject.getJSONArray("properties");

                for (int i = 0; i < propertiesArray.length(); i++) {
                    JSONObject property = propertiesArray.getJSONObject(i);
                    if (property.getString("name").equals("textures")) {
                        String value = property.getString("value");
                        byte[] decodedBytes = Base64.getDecoder().decode(value);
                        String decodedValue = new String(decodedBytes);
                        JSONObject textureJson = new JSONObject(decodedValue);
                        return textureJson.getJSONObject("textures").getJSONObject("SKIN").getString("url");
                    }
                }
            }
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
        return "Unable to retrieve player skin URL."; //TODO Add error handling
    }


}