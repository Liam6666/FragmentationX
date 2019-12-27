package me.liam.queue;

public abstract class Action {

    final public static int TYPE_LOAD = 1;

    final public static int TYPE_POP = 2;

    public abstract long run();

    public int actionType(){
        return TYPE_LOAD;
    }
}
