package com.satish.web;

import com.satish.accesscontrol.Actor;

import java.util.concurrent.ConcurrentHashMap;

/**
 * In-memory cookie store for web requests authentication. This helps to support browser based access
 * without REST calls. This is not used for REST calls. REST calls work based on auth-bearer token.
 *
 * @author satish.thulva
 **/
public class CookieVerifier {
    // Existence of an entry in this map for a cookie means that it's valid
    private final ConcurrentHashMap<String, Actor> cookieToUserIdMap = new ConcurrentHashMap<>();
    // Reverse lookUp based on userId to facilitate logout
    private final ConcurrentHashMap<Long, String> userIdToCookieMap = new ConcurrentHashMap<>();

    public void addCookie(Actor actor, String cookie) {
        userIdToCookieMap.put(actor.getId(), cookie);
        cookieToUserIdMap.put(cookie, actor);
    }

    public void expireCookie(Long userId) {
        String cookie = userIdToCookieMap.remove(userId);
        if (cookie != null) {
            cookieToUserIdMap.remove(cookie);
        }
    }

    public Actor getUserIfValid(String cookie) {
        return cookieToUserIdMap.get(cookie);
    }

}
