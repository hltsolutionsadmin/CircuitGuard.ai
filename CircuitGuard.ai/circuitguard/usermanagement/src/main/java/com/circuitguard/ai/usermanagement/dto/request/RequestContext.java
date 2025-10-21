package com.circuitguard.ai.usermanagement.dto.request;

public class RequestContext {

    private static final ThreadLocal<Long> userIdThreadLocal = new ThreadLocal<>();
    private static final ThreadLocal<Long> apiKeyIdThreadLocal = new ThreadLocal<>();

    public static void setUserId(Long userId) {
        userIdThreadLocal.set(userId);
    }

    public static Long getUserId() {
        return userIdThreadLocal.get();
    }

    public static void setApiKeyId(Long apiKeyId) {
        apiKeyIdThreadLocal.set(apiKeyId);
    }

    public static Long getApiKeyId() {
        return apiKeyIdThreadLocal.get();
    }

    public static void clear() {
        userIdThreadLocal.remove();
        apiKeyIdThreadLocal.remove();
    }
}
