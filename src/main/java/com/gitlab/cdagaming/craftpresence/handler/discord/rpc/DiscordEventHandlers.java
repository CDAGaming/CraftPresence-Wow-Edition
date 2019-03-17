package com.gitlab.cdagaming.craftpresence.handler.discord.rpc;

import com.gitlab.cdagaming.craftpresence.handler.StringHandler;
import com.sun.jna.Callback;
import com.sun.jna.Structure;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Struct containing handlers for RPC events
 * <br>Provided handlers can be null.
 */
public class DiscordEventHandlers extends Structure {
    private static final List<String> FIELD_ORDER = Collections.unmodifiableList(Arrays.asList(
            "ready",
            "disconnected",
            "errored",
            "joinGame",
            "spectateGame",
            "joinRequest"
    ));
    /**
     * Called when the RPC connection has been established
     */
    public OnReady ready;
    /**
     * Called when the RPC connection has been severed
     */
    public OnStatus disconnected;
    /**
     * Called when an internal error is caught within the SDK
     */
    public OnStatus errored;
    /**
     * Called when the logged in user joined a game
     */
    public OnGameUpdate joinGame;
    /**
     * Called when the logged in user joined to spectate a game
     */
    public OnGameUpdate spectateGame;
    /**
     * Called when another discord user wants to join the game of the logged in user
     */
    public OnJoinRequest joinRequest;

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof DiscordEventHandlers))
            return false;
        DiscordEventHandlers that = (DiscordEventHandlers) o;
        return ready.equals(that.ready)
                && disconnected.equals(that.disconnected)
                && errored.equals(that.errored)
                && joinGame.equals(that.joinGame)
                && spectateGame.equals(that.spectateGame)
                && joinRequest.equals(that.joinRequest);
    }

    @Override
    public int hashCode() {
        return StringHandler.generateHash(ready, disconnected, errored, joinGame, spectateGame, joinRequest);
    }

    @Override
    protected List<String> getFieldOrder() {
        return FIELD_ORDER;
    }

    /**
     * Handler function for the ready event
     */
    public interface OnReady extends Callback {
        void accept(DiscordUser user);
    }

    /**
     * Handler function for the exceptional events (error, disconnect)
     */
    public interface OnStatus extends Callback {
        void accept(int errorCode, String message);
    }

    /**
     * Handler function for game update events (joinGame, spectateGame)
     */
    public interface OnGameUpdate extends Callback {
        void accept(String secret);
    }

    /**
     * Handler function for user join requests
     */
    public interface OnJoinRequest extends Callback {
        void accept(DiscordUser request);
    }
}
