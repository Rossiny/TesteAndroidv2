package com.example.rossinyamaral.bank.loginScreen;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.rossinyamaral.bank.local.BankCache;
import com.example.rossinyamaral.bank.BaseActivity;
import com.example.rossinyamaral.bank.R;
import com.example.rossinyamaral.bank.ViewsUtils;
import com.example.rossinyamaral.bank.model.UserAccountModel;


interface LoginActivityInput {
    void displayLoginData(UserAccountModel viewModel);

    void displayLoginError(String message);

    void displayPasswordError();
}


public class LoginActivity extends BaseActivity implements LoginActivityInput {

    public static String TAG = LoginActivity.class.getSimpleName();
    LoginInteractorInput output;
    LoginRouter router;

    private EditText userEditText;
    private EditText passwordEditText;
    private Button loginButton;

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
                LoginRequest loginRequest = new LoginRequest();
                loginRequest.user = userEditText.getText().toString();
                loginRequest.password = passwordEditText.getText().toString();
                fetchUserData(loginRequest);
            }
        });
    }

    public void fetchUserData(LoginRequest loginRequest) {
        ViewsUtils.loading(LoginActivity.this);
        output.fetchLoginData(loginRequest);
    }

    private void fillScreenWithLastUser() {
        String lastLogin = BankCache.getLastLogin();
        if (!TextUtils.isEmpty(lastLogin)) {
            userEditText.setText(lastLogin);
        }
    }

    private void clearPasswordField() {
        passwordEditText.setText("");
    }


    @Override
    public void displayLoginData(UserAccountModel viewModel) {
        Log.e(TAG, "displayLoginData() called with: viewModel = [" + viewModel + "]");
        router.onLoginClick(viewModel);
        clearPasswordField();
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
