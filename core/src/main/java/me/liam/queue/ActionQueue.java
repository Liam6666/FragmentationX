package me.liam.queue;

import android.os.Handler;
import android.os.Looper;

import java.util.LinkedList;
import java.util.Queue;

public class ActionQueue {

    private Queue<Action> actionQueue = new LinkedList<>();

    private Handler handler;

    public ActionQueue(Handler handler){
        this.handler = handler;
    }

    public void enqueue(final Action action){
        if (action.actionType() == Action.TYPE_LOAD){
            if (Thread.currentThread() == Looper.getMainLooper().getThread()){
                action.run();
            }else {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        action.run();
                    }
                });
            }
            return;
        }
        if (!actionQueue.isEmpty()) return;
        actionQueue.add(action);
        handlerAction();
    }


    private void handlerAction(){
        if (actionQueue.size() == 1){
            handler.post(new Runnable() {
                @Override
                public void run() {
                    Action a = actionQueue.peek();
                    if (a != null){
                        long duration = a.run();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                actionQueue.clear();
                            }
                        },duration);
                    }else {
                        actionQueue.clear();
                    }
                }
            });
        }
    }
}
