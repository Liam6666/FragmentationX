    ------------------------ In Support Activity APIs -----------------------------
        
    public void setDefaultAnimation(FragmentAnimation animation);

    public FragmentAnimation getDefaultAnimation();

    public <T extends SupportFragment> T findFragmentByClass(Class cls);

    public void loadRootFragment(int containerId, SupportFragment to, FragmentAnimation anim, boolean playEnterAnim);

    public void loadRootFragment(int containerId, SupportFragment to);

    public void loadMultipleRootFragments(int containerId, int showPosition, SupportFragment... fragments);

    public void showHideAllFragment(SupportFragment show);

    public void start(SupportFragment to);

    public void start(SupportFragment from, SupportFragment to);

    public void startWithPop(SupportFragment from, SupportFragment to);

    public void pop();

    public void popTo(Class cls);

    public void popTo(Class cls, boolean includeTarget);
    
------------------------ In Support Fragment APIs -----------------------------
    
    public boolean dispatcherOnBackPressed();

    public boolean onBackPressed();

    public boolean onBackPressedChild();

    public FragmentAnimation onCreateCustomerAnimation();

    public void onEnterAnimEnd();

    public void loadRootFragment(int containerId, SupportFragment to, FragmentAnimation anim, boolean playEnterAnim);

    public void loadRootFragment(int containerId, SupportFragment to);

    public void loadMultipleRootFragments(int containerId, int showPosition, SupportFragment... fragments);

    public void showHideAllFragment(SupportFragment show);

    public void start(SupportFragment to);

    public void startWithPop(SupportFragment to);

    public void pop();

    public void popTo(Class cls);

    public void popTo(Class cls, boolean includeTarget);

    public void popChild();

    public void popChildTo(Class cls);

    public void popChildTo(Class cls, boolean includeTarget);
 
 
    ------------------------ In Develop -----------------------------
    
    attachSwipebackLayout
    
    startForResult
    
    setResule
    
    debugMode
    
    onVisibleChange
    
    and more functions in develop, welcome make 'star' and follow up me
    
    Thanks for https://github.com/YoKeyword/Fragmentation [DEPRECATED] 
