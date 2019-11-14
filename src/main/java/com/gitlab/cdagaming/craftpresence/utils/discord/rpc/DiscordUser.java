package com.gitlab.cdagaming.craftpresence.utils.discord.rpc;

import com.gitlab.cdagaming.craftpresence.utils.StringUtils;
import com.sun.jna.Structure;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Struct binding for a discord join request.
 */
public class DiscordUser extends Structure {
    private static final List<String> FIELD_ORDER = Collections.unmodifiableList(Arrays.asList(
            "userId",
            "username",
            "discriminator",
            "avatar"
    ));
    /**
     * The userId for the user that requests to join
     */
    public String userId;
    /**
     * The username of the user that requests to join
     */
    public String username;
    /**
     * The discriminator of the user that requests to join
     */
    public String discriminator;
    /**
     * The avatar of the user that requests to join
     */
    public String avatar;

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof DiscordUser))
            return false;
        DiscordUser that = (DiscordUser) o;
        return userId.equals(that.userId)
                && username.equals(that.username)
                && discriminator.equals(that.discriminator)
                && avatar.equals(that.avatar);
    }

    @Override
    public int hashCode() {
        return StringUtils.generateHash(userId, username, discriminator, avatar);
    }

    @Override
    protected List<String> getFieldOrder() {
        return FIELD_ORDER;
    }
}
