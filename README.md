
    这个库与 YoKeyword/Fragmentation 的非常相似，用法也大致相同，我之前一直使用，遗憾的是作者忙于工作很久没有更新了，无奈只能自己动手做一个。
    借鉴YoKeyword/Fragmentation的同时呢，我也加入了自己的一些想法，扩展了易用性，代码结构简单，通俗易懂。
    
    FragmentationX，是我为这个库起的新名字，因为适配了Android X，所以干脆就叫FragmentationX吧！
    
    这是一个文档草稿，后续放出Release 版本之后我会认真写一份API的文档，大家可以在Issues里提出意见或建议，一起让这个库变得更加优秀。
    
    下面是目前有的api，后续还会加入更多易用的api，如有什么想法，可以在Issues里提给我，我会非常感激。
    
    ------------------------ In Support Activity APIS -----------------------------
    
    public void setDefaultAnimation(FragmentAnimation animation);

    public FragmentAnimation getDefaultAnimation();

    public <T extends SupportFragment> T findFragmentByClass(Class cls);

    public void postDataToFragments(int code, Bundle data);

    public void loadRootFragment(int containerId, SupportFragment to, FragmentAnimation anim, boolean playEnterAnim, boolean addToBackStack);

    public void loadRootFragment(int containerId, SupportFragment to);

    public void loadMultipleRootFragments(int containerId, int showPosition, SupportFragment... fragments);

    public void showHideAllFragment(SupportFragment show);

    public void start(SupportFragment to);

    public void start(SupportFragment to, boolean addToBackStack);

    public void start(SupportFragment from, SupportFragment to);

    public void start(SupportFragment from, SupportFragment to,boolean addToBackStack);

    public void startWithPop(SupportFragment from, SupportFragment to);

    public void pop();

    public void popTo(Class cls);

    public void popTo(Class cls, boolean includeTarget);
        
    ------------------------ In Support Fragment APIS -----------------------------
    
    public boolean dispatcherOnBackPressed();

    public boolean onBackPressed();

    public FragmentAnimation onCreateCustomerAnimation();

    public void onEnterAnimEnd();

    public void onSupportPause();

    public void onSupportResume();

    public void onSwipeDrag(SupportFragment beforeOne,int state, float scrollPercent);

    public void onResult(int requestCode, int resultCode, Bundle data);

    public void onNotification(int code, Bundle data);

    public void setResult(int resultCode, Bundle data);

    public void loadRootFragment(int containerId, SupportFragment to, FragmentAnimation anim, boolean playEnterAnim, boolean addToBackStack);

    public void loadRootFragment(int containerId, SupportFragment to);

    public void loadMultipleRootFragments(int containerId, int showPosition, SupportFragment... fragments);

    public void showHideAllFragment(SupportFragment show);

    public void start(SupportFragment to);

    public void start(SupportFragment to, boolean addToBackStack);

    public void startForResult(SupportFragment to, int requestCode);

    public void startWithPop(SupportFragment to);

    public void pop();

    public void popTo(Class cls);

    public void popTo(Class cls, boolean includeTarget);

    public void popChild();

    public void popChildTo(Class cls);

    public void popChildTo(Class cls, boolean includeTarget);
    
    ------------------------ In Extra Transaction APIS -----------------------------
    
    public abstract ExtraTransaction setCustomerAnimations(int enterAnim,int exitAnim);

    public abstract ExtraTransaction setCustomerAnimations(int enterAnim,int exitAnim,int popEnterAnim,int popExitAnim);

    public abstract ExtraTransaction setResult(int resultCode, Bundle data);

    public abstract ExtraTransaction displayEnterAnim(boolean displayEnterAnim);

    public abstract ExtraTransaction setTag(String tag);

    public abstract ExtraTransaction addBackStack(boolean addBackStack);

    public abstract ExtraTransaction show(SupportFragment... show);

    public abstract ExtraTransaction hide(SupportFragment... hide);

    public abstract ExtraTransaction runOnExecute(Runnable run);

    public abstract void loadRootFragment(int containerId, SupportFragment to);

    public abstract void start(SupportFragment to);

    public abstract void startWithPop(SupportFragment to);

    public abstract void startWithPopTo(SupportFragment to, Class popToCls);

    public abstract void startWithPopTo(SupportFragment to, Class popToCls,boolean includeTarget);

    public abstract void startForResult(SupportFragment to,int requestCode);

    public abstract void pop();

    public abstract void popChild();

    public abstract void popTo(Class popToCls);

    public abstract void popTo(Class popToCls,boolean includeTarget);

    public abstract void popChildTo(Class popToCls);

    public abstract void popChildTo(Class popToCls,boolean includeTarget);

    public abstract void remove(SupportFragment remove);

    public abstract void remove(SupportFragment remove, boolean anim);

 
    ------------------------ In Develop -----------------------------
    
    debugMode
    
    and more functions in develop, welcome make 'star' and follow up me
    
    Thanks for https://github.com/YoKeyword/Fragmentation [DEPRECATED] 
