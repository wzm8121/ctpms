package com.mzw.ctpmsbackend.context;

import com.mzw.ctpmsbackend.entity.AdminAuditLog;

public class AuditLogContext {
    private static final ThreadLocal<AdminAuditLog> holder = new ThreadLocal<>();

    private static AdminAuditLog getOrInit() {
        AdminAuditLog log = holder.get();
        if (log == null) {
            log = new AdminAuditLog();
            holder.set(log);
        }
        return log;
    }

    public static void init() {
        holder.set(new AdminAuditLog());
    }

    public static AdminAuditLog get() {
        return holder.get();
    }

    public static void setResult(int result) {
        getOrInit().setResult(result);
    }

    public static void setReason(String reason) {
        getOrInit().setReason(reason);
    }

    public static void setTargetId(Integer id) {
        if (id != null) {
            getOrInit().setTargetId(id);
        }
    }

    public static void clear() {
        holder.remove();
    }
}

