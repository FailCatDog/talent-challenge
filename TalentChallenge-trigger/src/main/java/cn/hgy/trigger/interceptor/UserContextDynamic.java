package cn.hgy.trigger.interceptor;

/**
 * @Author: 黄光宇
 * @CreateTime: 2025-09-28
 * @Description: 用户上下文，用于传递用户id
 */
public class UserContextDynamic {
    private static final ThreadLocal<String> USER_CONTEXT_DYNAMIC = new ThreadLocal<>();

    public static void setUserContextDynamic(String userId) {
        USER_CONTEXT_DYNAMIC.set(userId);
    }

    public static String getUserContextDynamic() {
        return USER_CONTEXT_DYNAMIC.get();
    }

    public static void removeUserContextDynamic() {
        USER_CONTEXT_DYNAMIC.remove();
    }
}
