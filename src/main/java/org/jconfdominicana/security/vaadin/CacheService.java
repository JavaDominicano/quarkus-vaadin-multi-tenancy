package org.jconfdominicana.security.vaadin;

import io.quarkus.cache.Cache;
import io.quarkus.cache.CacheName;
import io.quarkus.cache.CaffeineCache;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import org.jconfdominicana.model.Profile;
import org.jconfdominicana.model.common.Tenant;
import org.jconfdominicana.model.common.User;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * @author me@fredpena.dev
 * @created 04/07/2024  - 13:51
 */

@ApplicationScoped
public class CacheService {

    @CacheName("tenant-cache")
    Cache cache;

    public void putProfile(String key, Profile value) {
        cache.as(CaffeineCache.class).put("profile-%s".formatted(key), CompletableFuture.completedFuture(value));
    }

    public void putUser(String key, User value) {
        cache.as(CaffeineCache.class).put("user-%s".formatted(key), CompletableFuture.completedFuture(value));
    }

    public void putTenant(String key, Tenant value) {
        cache.as(CaffeineCache.class).put("tenant-%s".formatted(key), CompletableFuture.completedFuture(value));
    }


//    public Profile getProfile(String key) throws ExecutionException, InterruptedException {
//        return (Profile) cache.as(CaffeineCache.class).getIfPresent("profile-%s".formatted(key)).get();
//    }
//
//    public User getUser(String key) throws ExecutionException, InterruptedException {
//        return (User) cache.as(CaffeineCache.class).getIfPresent("user-%s".formatted(key)).get();
//    }

    public Profile getProfile(String key) throws ExecutionException, InterruptedException {
        CompletableFuture<Object> present = cache.as(CaffeineCache.class).getIfPresent("profile-%s".formatted(key));
        if (present == null) {
            return null;
        }
        return (Profile) Uni.createFrom().completionStage(present).await().indefinitely();
    }

    public User getUser(String key) throws ExecutionException, InterruptedException {
        CompletableFuture<Object> present = cache.as(CaffeineCache.class).getIfPresent("user-%s".formatted(key));
        if (present == null) {
            return null;
        }
        return (User) Uni.createFrom().completionStage(present).await().indefinitely();
    }

    public Tenant getTenant(String key) throws ExecutionException, InterruptedException {
        CompletableFuture<Object> present = cache.as(CaffeineCache.class).getIfPresent("tenant-%s".formatted(key));
        if (present == null) {
            return null;
        }
        return (Tenant) Uni.createFrom().completionStage(present).await().indefinitely();
    }

}
