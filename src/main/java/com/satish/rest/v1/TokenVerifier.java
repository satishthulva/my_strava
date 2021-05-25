package com.satish.rest.v1;

import com.satish.accesscontrol.Actor;

import java.util.concurrent.ConcurrentHashMap;

/**
 * In-memory based token store. Does not survive server restarts. Does not work
 * when we have multiple servers for the application. This is just used as proof of
 * concept. Proper implementation in production environment will be backed by a secure
 * persistent store.
 *
 * @author satish.thulva
 **/
public class TokenVerifier {
    // Existence of an entry in this map for a token means that it's valid
    private final ConcurrentHashMap<String, Actor> tokenToUserIdMap = new ConcurrentHashMap<>();
    // Reverse lookUp based on userId to facilitate token expiry
    private final ConcurrentHashMap<Long, String> userIdToTokenMap = new ConcurrentHashMap<>();

    public void addToken(Actor actor, String token) {
        userIdToTokenMap.put(actor.getId(), token);
        tokenToUserIdMap.put(token, actor);
    }

    public void expireToken(Long userId) {
        String cookie = userIdToTokenMap.remove(userId);
        if (cookie != null) {
            tokenToUserIdMap.remove(cookie);
        }
    }

    public Actor getUserIfValid(String token) {
        return tokenToUserIdMap.get(token);
    }
}
