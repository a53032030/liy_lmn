package com.lmn.view.my.activity.modify;

import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.lmn.MainDataManager;
import com.lmn.R;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import lmn.com.lmnlibrary.base.BaseActivity;
import lmn.com.lmnlibrary.utils.RepeatUtils;

@Route(path = "/modify/activity")
public class ModifypswActivity extends BaseActivity implements ModifypswContract.View {


    @BindView(R.id.et_oldpsw)
    EditText etOldpsw;
    @BindView(R.id.clean_oldpsw)
    ImageView cleanOldpsw;
    @BindView(R.id.iv_show_oldpwd)
    ImageView ivShowOldpwd;
    @BindView(R.id.et_newpsw)
    EditText etNewpsw;
    @BindView(R.id.clean_newpsw)
    ImageView cleanNewpsw;
    @BindView(R.id.iv_show_newpwd)
    ImageView ivShowNewpwd;
    @BindView(R.id.btn_sure)
    Button btnSure;
    private boolean flag_old = false;
    private boolean flag_new = false;
    @Inject
    ModifypswPresenter modifypswPresenter;

    @Override
    public int getLayoutId() {
        return R.layout.activity_modifypsw;
    }

    @Override
    public void initview() {
        DaggerModifypswComponent
                .builder()
                .appComponent(getAppComponent())
                .modifypswPresenterModule(new ModifypswPresenterModule(MainDataManager.getInstance(mDataManager), this))
                .build()
                .inject(this);
    }

    @Override
    public void initData() {

    }

    @Override
    public void setListener() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        unbinder = ButterKnife.bind(this);
    }

    @OnClick({R.id.clean_oldpsw, R.id.iv_show_oldpwd, R.id.clean_newpsw, R.id.iv_show_newpwd, R.id.btn_sure})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.clean_oldpsw:
                etOldpsw.setText("");
                break;
            case R.id.iv_show_oldpwd:
                if (flag_old == true) {
                    etOldpsw.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    ivShowOldpwd.setImageResource(R.drawable.pass_gone);
                    flag_old = false;
                } else {
                    etOldpsw.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    ivShowOldpwd.setImageResource(R.drawable.pass_visuable);
                    flag_old = true;
                }
                String pwd = etOldpsw.getText().toString();
                if (!TextUtils.isEmpty(pwd))
                    etOldpsw.setSelection(pwd.length());
                break;
            case R.id.clean_newpsw:
                etNewpsw.setText("");
                break;
            case R.id.iv_show_newpwd:
                if (flag_new == true) {
                    etNewpsw.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    ivShowNewpwd.setImageResource(R.drawable.pass_gone);
                    flag_new = false;
                } else {
                    etNewpsw.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    ivShowNewpwd.setImageResource(R.drawable.pass_visuable);
                    flag_new = true;
                }
                String pwd_new = etNewpsw.getText().toString();
                if (!TextUtils.isEmpty(pwd_new))
                    etNewpsw.setSelection(pwd_new.length());
                break;
            case R.id.btn_sure:
                if (RepeatUtils.isFastDoubleClick()) {
                    if (etOldpsw.getText().toString().equals("")) {
                        showShortToast("请输入密码");
                        return;
                    }
                    if (etNewpsw.getText().toString().equals("")) {
                        showShortToast("请输入新密码");
                        return;
                    }
                    if (etOldpsw.getText().toString().getBytes().length<6||etOldpsw.getText().toString().getBytes().length>10) {
                        showShortToast("请输入6-10位旧密码");
                        return;
                    }
                    if (etNewpsw.getText().toString().getBytes().length<6||etNewpsw.getText().toString().getBytes().length>10) {
                        showShortToast("请输入6-10位新密码");
                        return;
                    }
                    modifypswPresenter.modify(mDataManager.getSPMapData().get("phone"),etOldpsw.getText().toString(),etNewpsw.getText().toString());
                }else {
                    showShortToast("请不要重复点击");
                }

                break;
        }
    }

    @Override
    public void success() {
        showShortToast("修改成功");
        finish();
    }

    @Override
    public void showProgressDialogView() {
        showProgressDialog("提交中");
    }

    @Override
    public void hiddenProgressDialogView() {
        hiddenProgressDialog();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        modifypswPresenter.destory();
    }
}
