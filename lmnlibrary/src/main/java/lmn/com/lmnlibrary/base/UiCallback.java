package lmn.com.lmnlibrary.base;

import android.os.Bundle;

/**
 * Created by wanglei on 2016/12/1.
 */

public interface UiCallback {
    //初始化数据
    void initData(Bundle savedInstanceState);
    //设置事件监听
    void setListener();
    //设置布局资源文件id
    int getLayoutId();
    //是否使用eventbus
    boolean useEventBus();
    //初始化databinding
    void initDatabinding();


}
