package org.jconfdominicana.security.vaadin;

import jakarta.enterprise.context.ApplicationScoped;
import org.jconfdominicana.model.Profile;
import org.jconfdominicana.model.common.Tenant;
import org.jconfdominicana.model.common.User;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author me@fredpena.dev
 * @created 04/07/2024  - 13:51
 */

@ApplicationScoped
public class CacheService {

    public static final String KEY_PROFILE = "profile-%s";
    public static final String KEY_USER = "user-%s";
    public static final String KEY_TENANT = "tenant-%s";

    private final Map<String, Object> map = new ConcurrentHashMap<>();

    public void putProfile(String key, Profile value) {
        map.put(KEY_PROFILE.formatted(key), value);
    }

    public void putUser(String key, User value) {
        map.put(KEY_USER.formatted(key), value);
    }

    public void putTenant(String key, Tenant value) {
        map.put(KEY_TENANT.formatted(key), value);
    }

    private Object get(String key) {
        if (map.containsKey(key)) {
            return map.get(key);
        }
        return null;
    }

    public Profile getProfile(String key) {
        return (Profile) get(KEY_PROFILE.formatted(key));
    }

    public User getUser(String key) {
        return (User) get(KEY_USER.formatted(key));
    }

    public Tenant getTenant(String key) {
        return (Tenant) get(KEY_TENANT.formatted(key));
    }

    public void clear(String key) {
        map.remove(KEY_PROFILE.formatted(key));
        map.remove(KEY_USER.formatted(key));
        map.remove(KEY_TENANT.formatted(key));
    }


}
