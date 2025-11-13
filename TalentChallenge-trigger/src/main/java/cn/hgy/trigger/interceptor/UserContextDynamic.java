package cn.hgy.trigger.interceptor;

/**
 * @Author: 黄光宇
 * @CreateTime: 2025-09-28
 * @Description: 用户上下文，用于传递用户id
 */
public class UserContextDynamic {
    private static final ThreadLocal<UserContext> USER_CONTEXT_DYNAMIC = new ThreadLocal<>();

    public static void setUserContext(String userId, Integer userType) {
        USER_CONTEXT_DYNAMIC.set(new UserContext(userId, userType));
    }

    public static void setUserContextDynamic(String userId) {
        setUserContext(userId, null);
    }

    public static String getUserContextDynamic() {
        UserContext context = USER_CONTEXT_DYNAMIC.get();
        return context == null ? null : context.userId;
    }

    public static Integer getUserType() {
        UserContext context = USER_CONTEXT_DYNAMIC.get();
        return context == null ? null : context.userType;
    }

    public static void removeUserContextDynamic() {
        USER_CONTEXT_DYNAMIC.remove();
    }

    private static final class UserContext {
        private final String userId;
        private final Integer userType;

        private UserContext(String userId, Integer userType) {
            this.userId = userId;
            this.userType = userType;
        }
    }
}
