package com.gitlab.cdagaming.craftpresence.handler.discord.rpc;

import com.gitlab.cdagaming.craftpresence.handler.StringHandler;
import com.sun.jna.Structure;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Struct binding for a RichPresence
 */
public class DiscordRichPresence extends Structure {
    private static final List<String> FIELD_ORDER = Collections.unmodifiableList(Arrays.asList(
            "state",
            "details",
            "startTimestamp",
            "endTimestamp",
            "largeImageKey",
            "largeImageText",
            "smallImageKey",
            "smallImageText",
            "partyId",
            "partySize",
            "partyMax",
            "matchSecret",
            "joinSecret",
            "spectateSecret",
            "instance"
    ));

    /**
     * The user's current party status.
     * <br>Example: "Looking to Play", "Playing Solo", "In a Group"
     *
     * <p><b>Maximum: 128 characters</b>
     */
    public String state;

    /**
     * What the player is currently doing.
     * <br>Example: "Competitive - Captain's Mode", "In Queue", "Unranked PvP"
     *
     * <p><b>Maximum: 128 characters</b>
     */
    public String details;

    /**
     * Unix timestamp (seconds) for the start of the game.
     * <br>Example: 1507665886
     */
    public long startTimestamp;

    /**
     * Unix timestamp (seconds) for the start of the game.
     * <br>Example: 1507665886
     */
    public long endTimestamp;

    /**
     * Name of the uploaded image for the large profile artwork.
     * <br>Example: "default"
     *
     * <p><b>Maximum: 32 characters</b>
     */
    public String largeImageKey;

    /**
     * Tooltip for the largeImageKey.
     * <br>Example: "Blade's Edge Arena", "Numbani", "Danger Zone"
     *
     * <p><b>Maximum: 128 characters</b>
     */
    public String largeImageText;

    /**
     * Name of the uploaded image for the small profile artwork.
     * <br>Example: "rogue"
     *
     * <p><b>Maximum: 32 characters</b>
     */
    public String smallImageKey;

    /**
     * Tooltip for the smallImageKey.
     * <br>Example: "Rogue - Level 100"
     *
     * <p><b>Maximum: 128 characters</b>
     */
    public String smallImageText;

    /**
     * ID of the player's party, lobby, or group.
     * <br>Example: "ae488379-351d-4a4f-ad32-2b9b01c91657"
     *
     * <p><b>Maximum: 128 characters</b>
     */
    public String partyId;

    /**
     * Current size of the player's party, lobby, or group.
     * <br>Example: 1
     */
    public int partySize;

    /**
     * Maximum size of the player's party, lobby, or group.
     * <br>Example: 5
     */
    public int partyMax;

    /**
     * Unique hashed string for Spectate and Join.
     * Required to enable match interactive buttons in the user's presence.
     * <br>Example: "MmhuZToxMjMxMjM6cWl3amR3MWlqZA=="
     *
     * <p><b>Maximum: 128 characters</b>
     */
    public String matchSecret;

    /**
     * Unique hashed string for Spectate button.
     * This will enable the "Spectate" button on the user's presence if whitelisted.
     * <br>Example: "MTIzNDV8MTIzNDV8MTMyNDU0"
     *
     * <p><b>Maximum: 128 characters</b>
     */
    public String joinSecret;

    /**
     * Unique hashed string for chat invitations and Ask to Join.
     * This will enable the "Ask to Join" button on the user's presence if whitelisted.
     * <br>Example: "MTI4NzM0OjFpMmhuZToxMjMxMjM="
     *
     * <p><b>Maximum: 128 characters</b>
     */
    public String spectateSecret;

    /**
     * Marks the matchSecret as a game session with a specific beginning and end.
     * Boolean value of 0 or 1.
     * <br>Example: 1
     */
    public byte instance;

    public DiscordRichPresence(final String state, final String details, final long startTimestamp, final long endTimestamp, final String largeImageKey, final String largeImageText, final String smallImageKey, final String smallImageText, final String partyId, final int partySize, final int partyMax, final String matchSecret, final String joinSecret, final String spectateSecret, final byte instance) {
        this.state = !StringHandler.isNullOrEmpty(state) ? new String(state.getBytes(StandardCharsets.UTF_8)).replaceAll("\\s+", " ") : state;
        this.details = !StringHandler.isNullOrEmpty(details) ? new String(details.getBytes(StandardCharsets.UTF_8)).replaceAll("\\s+", " ") : details;
        this.startTimestamp = startTimestamp;
        this.endTimestamp = endTimestamp;
        this.largeImageKey = !StringHandler.isNullOrEmpty(largeImageKey) ? new String(largeImageKey.getBytes(StandardCharsets.UTF_8)).replaceAll("\\s+", " ") : largeImageKey;
        this.largeImageText = !StringHandler.isNullOrEmpty(largeImageText) ? new String(largeImageText.getBytes(StandardCharsets.UTF_8)).replaceAll("\\s+", " ") : largeImageText;
        this.smallImageKey = !StringHandler.isNullOrEmpty(smallImageKey) ? new String(smallImageKey.getBytes(StandardCharsets.UTF_8)).replaceAll("\\s+", " ") : smallImageKey;
        this.smallImageText = !StringHandler.isNullOrEmpty(smallImageText) ? new String(smallImageText.getBytes(StandardCharsets.UTF_8)).replaceAll("\\s+", " ") : smallImageText;
        this.partyId = !StringHandler.isNullOrEmpty(partyId) ? new String(partyId.getBytes(StandardCharsets.UTF_8)).replaceAll("\\s+", " ") : partyId;
        this.partySize = partySize;
        this.partyMax = partyMax;
        this.matchSecret = !StringHandler.isNullOrEmpty(matchSecret) ? new String(matchSecret.getBytes(StandardCharsets.UTF_8)).replaceAll("\\s+", " ") : matchSecret;
        this.joinSecret = !StringHandler.isNullOrEmpty(joinSecret) ? new String(joinSecret.getBytes(StandardCharsets.UTF_8)).replaceAll("\\s+", " ") : joinSecret;
        this.spectateSecret = !StringHandler.isNullOrEmpty(spectateSecret) ? new String(spectateSecret.getBytes(StandardCharsets.UTF_8)).replaceAll("\\s+", " ") : spectateSecret;
        this.instance = instance;
    }

    @Override
    protected List<String> getFieldOrder() {
        return FIELD_ORDER;
    }
}
