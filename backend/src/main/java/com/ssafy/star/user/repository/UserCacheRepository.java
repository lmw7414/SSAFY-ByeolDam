package com.ssafy.star.user.repository;

import com.ssafy.star.user.dto.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Repository;

import java.time.Duration;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class UserCacheRepository {

    private final RedisTemplate<String, User> userRedisTemplate;
    private final static Duration USER_CACHE_TTL = Duration.ofMillis(1800000); //30분

    public void setUser(User user) {
        String key = getKey(user.email());
        userRedisTemplate.opsForValue().set(key, user, USER_CACHE_TTL);
    }

    public Optional<User> getUser(String email) {
        String key = getKey(email);
        User user = userRedisTemplate.opsForValue().get(key);
        return Optional.ofNullable(user);
    }

    public void updateUser(User user) {
        String key = getKey(user.email());
        ValueOperations<String, User> valueOps = userRedisTemplate.opsForValue();
        valueOps.set(key, user);
    }

    public void deleteUser(String email) {
        String key = getKey(email);
        userRedisTemplate.delete(key);
    }


    private String getKey(String email) {
        return "USER:" + email;
    }
}
