package me.liam.queue;

public abstract class Action {

    final public static long ACTION_DEFAULT_DELAY = 0L;

    public long duration = getDuration();

    public abstract void run();

    public long getDuration() {
        return ACTION_DEFAULT_DELAY;
    }
}
