
   ![演示图1](https://github.com/Liam6666/FragmentationX/blob/master/screenshot/%E6%88%AA%E5%9B%BE1.png)
   ![演示图2](https://github.com/Liam6666/FragmentationX/blob/master/screenshot/%E6%88%AA%E5%9B%BE2.png)
   ![演示图3](https://github.com/Liam6666/FragmentationX/blob/master/screenshot/%E6%88%AA%E5%9B%BE3.png)
   
    **FragmentationX**

> 这个库与 [YoKeyword大神的Fragmentation](https://github.com/YoKeyword/Fragmentation) 的非常相似，用法也大致相同，我之前一直使用，遗憾的是作者忙于工作很久没有更新了，无奈只能自己动手做一个。
借鉴[YoKeyword大神的Fragmentation](https://github.com/YoKeyword/Fragmentation)的同时呢，我也加入了自己的一些想法，扩展了易用性，代码结构简单，通俗易懂。


> 致力于提升Android原生应用体验，主打单Activity + 多Fragment页面构架模式，降低耦合，提升流畅，纵享尽丝滑。
> 

 1. 丰富API

> 封装了多个易用API，满足绝大多数的使用场景。

 2. 自定义转场动画

> 一键设置全局动画 + 自定义设置动画。定制个性动画。

 3. 滑动关闭

> 加入SwipebackLayout，动动手指即可滑动页面。

 4. 无限嵌套
 
 > 支持无限嵌套，无需再担心逻辑错乱了。
 
 5. 自定义处理Back键事件

> 自动处理了back事件，也支持重写onBackPressed自定义回退事件。

``` 
How to use:

1.Add mave to project build.gradle
allprojects {
    repositories {
        maven { url 'https://jitpack.io' }
    }
}

2.Add library
implementation 'com.github.Liam6666:FragmentationX:xxx'


3.
public class WeChatActivity extends SupportActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wechat_root);
        if (findFragmentByClass(RootFragment.class) == null){
            loadRootFragment(R.id.container,RootFragment.newInstance());
        }
    }
}
```

> SupportActivity API Document:
> 1.setDefaultAnimation 设置全局默认动画
> 2.findFragmentByClass 查找Fragment对象
> 3.postDataToFragments 发送一条消息，接收对象是当前入栈的所有fragment
> 4.loadRootFragment 添加第一个fragment
> 5.loadMultipleRootFragments 添加多个可切换显示的fragment
> 6.showHideAllFragment 显示某一个fragment，并隐藏当前栈内的其他fragment
> 7.start 启动的一个新的fragment
> 8.startWithPop 启动一个新的fragment，并关闭当前界面
> 9.startWithPopTo 启动一个新的fragment，并关闭当前界面到栈内的某一个位置
> 10.pop 从栈底弹出一个fragment
> 11.popTo 弹出多个fragment到栈内某一个位置


> SupportFragment API Document
> 1.findFragmentByClass 查找Fragment对象
> 2.findChildFragmentByClass 查找子Fragment对象
> 3.dispatcherOnBackPressed 分发Back键事件
> 4.onBackPressed 消费Back键事件
> 5.onCreateCustomerAnimation 重写完成自定义动画
> 6.onLazyInit 懒加载
> 7.onEnterAnimEnd 当入场动画结束时调用一次
> 8.onSupportPause 当页面不可见时调用一次
> 9.onSupportResume 当页面从不可见中恢复时调用一次
> 10.onSwipeDrag 当触摸边缘开始滑动时调用
> 11.onResult 同Activity的onResult
> 12.onPostedData 从postDataToFragments接受到消息
> 13.setResult 同Activity的setResult
> 14.getExtraTransaction 自定义事物
> 15.loadRootFragment 添加子fragment
> 16.loadMultipleRootFragments 添加多个可切换的子fragment
> 17.showHideAllFragment 显示隐藏栈内的fragment
> 18.start 启动一个新的同级fragment
> 19.startForResult 同Activity的startForResult
> 20.startWithPop 启动一个新的fragment，并关闭当前界面
> 21.startWithPopTo 启动一个新的fragment，并关闭当前界面到栈内的某一个位置
> 22.pop 从栈底弹出一个fragment
> 23.popTo 弹出多个fragment到栈内某一个位置
> 24.popChild 从栈底弹出一个子fragment
> 25.popChildTo 弹出多个子fragment到栈内某一个位置
> 26.popAllChild 弹出所有的子fragment
