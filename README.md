# PlayerHeads

Minecraft Resource Pack and Plugin API to create chaticons from the players skinhead.


---
## Features
- **Player Heads:** Generate an 8×8 pixel representation of a player's head using their skin.
- **Multiple Skin Sources:** Choose between different skin retrieval services:
    - **MOJANG** (default)
    - **CRAFATAR**
    - **MCHEADS**
- **Multiple Display Options:** Display player heads in chat messages, action bars, bossbars, and more.
- **PlaceholderAPI Integration:** Provides placeholders to easily insert head icons into your server’s messages.

---

### Unicode Characters

This works by coloring a set of unicodes which are set in the Resource Pack under a custom font called "playerhead".  
It is then arranged into a grid of 8x8 of pixels using negative space.

- `\uF001`: Pixel 1 (1st Row)
- `\uF002`: Pixel 2 (2nd Row)
- `\uF003`: Pixel 3 (3rd Row)
- `\uF004`: Pixel 4 (4th Row)
- `\uF005`: Pixel 5 (5th Row)
- `\uF006`: Pixel 6 (6th Row)
- `\uF007`: Pixel 7 (7th Row)
- `\uF008`: Pixel 8 (8th Row)
- `\uF101`: Negative space (Moves back 1px)
- `\uF102`: Negative space (Moves back 2px)

---
### Configuration
After installation, a `config.yml` is created in your server’s plugin folder with the following default options:

```yml
# Which skin source to use (MOJANG, CRAFATAR or MCHEADS). Default is MOJANG.
skin-source: MOJANG

# Whether the resource pack will be automatically downloaded and applied for every player.
auto-download-pack: true

# Should the server be treated as running in online mode.
online-mode: true

# Whether to display the player head with its hat overlay.
enable-skin-overlay: true

# Should join messages include the player head.
enable-join-messages: true

# Should leave messages include the player head.
enable-leave-messages: true

# Should chat messages include the player head.
enable-chat-messages: true

# Should death messages include the player head.
enable-death-messages: true

# Delay (in seconds) before join messages are broadcast.
# This helps ensure that textures have loaded. Default is 3.
join-messages-delay-seconds: 3

```
---

## PlaceholderAPI Integration

If PlaceholderAPI is installed on your server, PlayerHeads automatically registers a hook that provides the following placeholders:

- **`%playerheads%` or `%playerheadds_self%`**  
  Returns the head of the player using the placeholder.  
  **Usage:** In chat plugins or configuration files, simply use `%playerheads%` to show the head of the player who requested it.

- **`%playerheads_other:<player>%`**  
  Returns the head of the specified player.  
  **Example:** `%playerheads_other:Notch%` will display Notch’s head.

If a player is not found or is offline when using `%playerheadds_self%`, an appropriate message will be returned.

---

## Adding the Dependency

For developers who wish to use the PlayerHeads API in their own projects, add the following dependency:

### Maven
```xml
    <repositories>
        <repository>
            <id>jitpack.io</id>
            <url>https://jitpack.io</url>
        </repository>
    </repositories>

    <dependencies>
      <dependency>
        <groupId>com.github.GoldenSBG</groupId>
        <artifactId>PlayerHeads</artifactId>
        <version>v1.0.0</version>
      </dependency>
    </dependencies>
```

---
## API Usage
In your plugin’s main class, initialize the API in the `onEnable()` method:
``` java
@Override
public void onEnable() {
    PlayerHeadsAPI.initialize(this);
    // ... additional initialization code
}
```

### Retrieving a Player Head
The API now uses caching internally. The default skin source is automatically selected from your configuration (`skin-source`), but you can also specify your own source if desired.
#### Examples
``` java
// Get a player's head (with skin overlay) as a Component (cached for 5 minutes)
Component headComponents = PlayerHeadsAPI.getInstance().getHead(player);

// Get a player's head without the overlay:
Component headComponentsNoOverlay = PlayerHeadsAPI.getInstance().getHead(player, false);

// Get a player's head as a legacy formatted String:
String headString = PlayerHeadsAPI.getInstance().getHeadAsString(player);
```
***Note: The API caches each player’s head for 5 minutes, reducing the need for repeated asynchronous skin fetches.***

---

## License and use
This resource pack is available under the Creative Commons Attribution 4.0 International License (see LICENSE.txt). You are free to use, modify, and distribute this project as long as you include proper attribution.

