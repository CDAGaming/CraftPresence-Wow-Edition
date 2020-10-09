package com.gitlab.cdagaming.craftpresence.utils.discord.rpc.entities;

import com.gitlab.cdagaming.craftpresence.utils.StringUtils;

/**
 * Constants representing various Discord client party privacy levels,
 * such as Public or Private
 */
public enum PartyPrivacy {
    /**
     * Constant for the "Private" Discord RPC Party privacy level.
     */
    Private(0),

    /**
     * Constant for the "Public" Discord RPC Party privacy level.
     */
    Public(1);

    private final String displayName;

    private final int partyIndex;

    PartyPrivacy(final int partyIndex) {
        this.partyIndex = partyIndex;
        this.displayName = StringUtils.formatWord(name());
    }

    PartyPrivacy(final int partyIndex, final String displayName) {
        this.partyIndex = partyIndex;
        this.displayName = displayName;
    }

    /**
     * Gets a {@link PartyPrivacy} matching the specified display name.
     * <p>
     * This is only internally implemented.
     *
     * @param partyIndex The party index to get from.
     * @return The PartyPrivacy corresponding to the display name, or
     * {@link PartyPrivacy#Public} if none match.
     */
    public static PartyPrivacy from(int partyIndex) {
        for (PartyPrivacy value : values()) {
            if (value.getPartyIndex() == partyIndex) {
                return value;
            }
        }
        return Public;
    }

    /**
     * Gets a {@link PartyPrivacy} matching the specified display name.
     * <p>
     * This is only internally implemented.
     *
     * @param displayName The display name to get from.
     * @return The PartyPrivacy corresponding to the display name, or
     * {@link PartyPrivacy#Public} if none match.
     */
    public static PartyPrivacy from(String displayName) {
        for (PartyPrivacy value : values()) {
            if (value.getDisplayName() != null && value.getDisplayName().equals(displayName)) {
                return value;
            }
        }
        return Public;
    }

    /**
     * Gets a {@link PartyPrivacy} matching the specified display name.
     * <p>
     * This is only internally implemented.
     *
     * @param displayName The display name to get from.
     * @return The PartyPrivacy corresponding to the display name, or
     * {@link PartyPrivacy#Public} if none match.
     */
    public static PartyPrivacy from(int partyIndex, String displayName) {
        for (PartyPrivacy value : values()) {
            if (value.getDisplayName() != null && value.getDisplayName().equals(displayName) && value.getPartyIndex() == partyIndex) {
                return value;
            }
        }
        return Public;
    }

    public int getPartyIndex() {
        return partyIndex;
    }

    public String getDisplayName() {
        return displayName;
    }
}
