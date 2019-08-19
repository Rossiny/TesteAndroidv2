package com.example.rossinyamaral.bank.loginScreen;

import android.content.Intent;
import android.support.annotation.NonNull;

import com.example.rossinyamaral.bank.model.UserAccountModel;
import com.example.rossinyamaral.bank.statementsScreen.StatementsActivity;

import java.lang.ref.WeakReference;


interface LoginRouterInput {
    Intent navigateToStatementsScreen();

    void passDataToNextScene(Intent intent, UserAccountModel model);

    void onLoginClick(UserAccountModel model);
}

public class LoginRouter implements LoginRouterInput { //, AdapterView.OnItemClickListener {

    public static String TAG = LoginRouter.class.getSimpleName();
    WeakReference<LoginActivity> activity;


    @NonNull
    @Override
    public Intent navigateToStatementsScreen() {
        return new Intent(activity.get(), StatementsActivity.class);
    }

    @Override
    public void passDataToNextScene(Intent intent, UserAccountModel model) {
         intent.putExtra("userAccount", model);
    }

    @Override
    public void onLoginClick(UserAccountModel model) {
        if (model != null) {
            Intent intent = navigateToStatementsScreen();
            passDataToNextScene(intent, model);
            activity.get().startActivity(intent);
        }
    }
}
