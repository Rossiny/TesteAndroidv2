package com.example.rossinyamaral.bank.loginScreen;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.rossinyamaral.bank.BankApplication;
import com.example.rossinyamaral.bank.BankCache;
import com.example.rossinyamaral.bank.BaseActivity;
import com.example.rossinyamaral.bank.R;
import com.example.rossinyamaral.bank.ViewsUtils;
import com.example.rossinyamaral.bank.model.UserAccountModel;

import java.util.regex.Pattern;


interface LoginActivityInput {
    void displayLoginData(UserAccountModel viewModel);

    void displayLoginError(String message);

    void displayPasswordError();
}


public class LoginActivity extends BaseActivity implements LoginActivityInput {

    public static String TAG = LoginActivity.class.getSimpleName();
    LoginInteractorInput output;
    LoginRouter router;

    EditText userEditText;
    EditText passwordEditText;
    Button loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        LoginConfigurator.INSTANCE.configure(this);

        adjustBar(android.R.color.transparent);
        bindViews();
        setListeners();
        fillScreenWithLastUser();
    }

    private void bindViews() {
        userEditText = findViewById(R.id.userEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        loginButton = findViewById(R.id.loginButton);
    }

    private void setListeners() {
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickLogin();
            }
        });
    }

    private void clickLogin() {
        ViewsUtils.loading(LoginActivity.this);
        LoginRequest aLoginRequest = new LoginRequest();
        //populate the request
        aLoginRequest.user = userEditText.getText().toString();
        aLoginRequest.password = passwordEditText.getText().toString();
        output.fetchLoginData(aLoginRequest);
    }

    private void fillScreenWithLastUser() {
        String lastLogin = BankCache.getLastLogin();
//        String lastPassword = BankApplication.getInstance().getLastPassword();
        if (!TextUtils.isEmpty(lastLogin)) {
            userEditText.setText(lastLogin);
//            if (!TextUtils.isEmpty(lastPassword)) {
//                passwordEditText.setText(lastPassword);
//            }
        }
    }


    @Override
    public void displayLoginData(UserAccountModel viewModel) {
        Log.e(TAG, "displayLoginData() called with: viewModel = [" + viewModel + "]");
        // Deal with the data
        router.onLoginClick(viewModel);
        ViewsUtils.dismissLoading();
    }

    @Override
    public void displayLoginError(String message) {
        ViewsUtils.dismissLoading();
        ViewsUtils.alert(this, message, null);
    }

    @Override
    public void displayPasswordError() {
        displayLoginError(getString(R.string.password_error_message));
    }
}
