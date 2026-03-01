package ru.cu.custom.scope;

public class JobActionContextHolder {
    private static final ThreadLocal<JobActionContext> jobContextHolder = new ThreadLocal<>();

    private JobActionContextHolder() {
    }

    public static void setJobActionContext(JobActionContext context) {
        jobContextHolder.set(context);
    }

    public static void resetJobContext() {
        jobContextHolder.remove();
    }

    public static JobActionContext getJobContext() {
        return jobContextHolder.get();
    }
}
