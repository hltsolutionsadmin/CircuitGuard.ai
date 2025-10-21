package com.skillrat.commonservice.dto;

/**
 * Thread-local context holder to store request-scoped data like apiKeyId, userId, tenantKey, and tenantDbName.
 */
public class RequestContext {

    private static final ThreadLocal<Long> apiKeyId = new ThreadLocal<>();
    private static final ThreadLocal<Long> userId = new ThreadLocal<>();
    private static final ThreadLocal<String> tenantKey = new ThreadLocal<>();
    private static final ThreadLocal<String> tenantDbName = new ThreadLocal<>();

    public static void setApiKeyId(Long id) {
        apiKeyId.set(id);
    }

    public static Long getApiKeyId() {
        return apiKeyId.get();
    }

    public static void setUserId(Long id) {
        userId.set(id);
    }

    public static Long getUserId() {
        return userId.get();
    }

    public static void setTenantKey(String key) {
        tenantKey.set(key);
    }

    public static String getTenantKey() {
        return tenantKey.get();
    }

    public static void setTenantDbName(String name) {
        tenantDbName.set(name);
    }

    public static String getTenantDbName() {
        return tenantDbName.get();
    }

    public static boolean isSandboxRequest() {
        return apiKeyId.get() != null;
    }

    public static void clear() {
        apiKeyId.remove();
        userId.remove();
        tenantKey.remove();
        tenantDbName.remove();
    }
}
