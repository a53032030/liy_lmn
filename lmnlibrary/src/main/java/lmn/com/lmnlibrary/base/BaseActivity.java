package lmn.com.lmnlibrary.base;


import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.IntentFilter;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.shashank.sony.fancytoastlib.FancyToast;

import butterknife.Unbinder;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import lmn.com.lmnlibrary.AppComponent;
import lmn.com.lmnlibrary.GlobalAppComponent;
import lmn.com.lmnlibrary.event.BusFactory;
import lmn.com.lmnlibrary.manager.DataManager;
import lmn.com.lmnlibrary.receiver.NetWorkChangeBroadcastReceiver;
import lmn.com.lmnlibrary.utils.DialogUtil;
import lmn.com.lmnlibrary.utils.KnifeUtil;

/**
 * Created by admin on 2017/3/12.
 */

public abstract class BaseActivity<B extends ViewDataBinding> extends AppCompatActivity implements UiCallback{
    protected DataManager mDataManager;
    protected Context mContext;
    protected Dialog loadingDialog;
    protected Unbinder unbinder;
    protected B viewDataBinding;
    private NetWorkChangeBroadcastReceiver receiver;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDataManager = getAppComponent().getDataManager();
        mContext = getAppComponent().getContext();
        registerNetChangeReceiver();
        if (getLayoutId() > 0) {
//            setContentView(getLayoutId());
            initDatabinding();
            unbinder = KnifeUtil.bind(this);
        }
        initData(savedInstanceState);
        setListener();
    }
    @Override
    public void initDatabinding() {
        viewDataBinding = DataBindingUtil.setContentView(this,getLayoutId());
    }
    private void registerNetChangeReceiver() {
        receiver = new NetWorkChangeBroadcastReceiver(this);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(receiver , intentFilter);
    }

    protected AppComponent getAppComponent() {
        return GlobalAppComponent.getAppComponent();
    }

    protected void addFragment(int containerViewId, Fragment fragment , String tag) {
        final FragmentTransaction fragmentTransaction = this.getSupportFragmentManager().beginTransaction();

        fragmentTransaction.add(containerViewId, fragment , tag);
        fragmentTransaction.commit();
    }

    protected void showShortToast(String message){
        Toast.makeText(getApplicationContext(),message,Toast.LENGTH_SHORT).show();
    }

    protected void showSuccessToast(String message){
        FancyToast.makeText(this,message,FancyToast.LENGTH_LONG,FancyToast.SUCCESS,true);
    }
    protected void showDefaultToast(String message){
        FancyToast.makeText(this,message,FancyToast.LENGTH_LONG,FancyToast.SUCCESS,true);
    }
    protected void showErrorToast(String message){
        FancyToast.makeText(this,message,FancyToast.LENGTH_LONG,FancyToast.DEFAULT,true);
    }

    protected void showProgressDialog(){
        this.showProgressDialog(null,null);
    }

    protected void showProgressDialog(String msg){
        this.showProgressDialog(msg , null);
    }

    protected void showProgressDialog(DialogInterface.OnCancelListener listener){
        this.showProgressDialog(null ,listener);
    }

    protected void showProgressDialog(String msg , DialogInterface.OnCancelListener listener){
        if(loadingDialog == null){
            loadingDialog = DialogUtil.createLoadingDialog(this, msg, listener);
        }else if(!loadingDialog.isShowing()){
            loadingDialog.show();
        }

    }

    protected void hiddenProgressDialog(){
        if(loadingDialog != null && loadingDialog.isShowing()){
            loadingDialog.dismiss();
        }
    }


    private CompositeDisposable disposables;

    /**
     * 添加观察者
     * @param disposable d
     */
    public void addDisposable(Disposable disposable){
        if(disposables == null){
            disposables = new CompositeDisposable();
        }
        disposables.add(disposable);

    }

    /**
     * 注销观察者，防止泄露
     */
    public void clearDisposable(){
        if(disposables != null){
            disposables.clear();
            disposables = null;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(loadingDialog != null && loadingDialog.isShowing()){
            loadingDialog.dismiss();
            loadingDialog = null;
        }
        clearDisposable();
        if(unbinder != null){
            unbinder.unbind();
        }

        if(null != receiver){
            receiver.onDestroy();
            unregisterReceiver(receiver);
            receiver = null;
        }
    }
    @Override
    protected void onStart() {
        super.onStart();
        if (useEventBus()) {
            BusFactory.getBus().register(this);
        }
    }
    @Override
    public boolean useEventBus() {
        return false;
    }
    @Override
    protected void onStop() {
        super.onStop();
        BusFactory.getBus().unregister(this);
    }
}
