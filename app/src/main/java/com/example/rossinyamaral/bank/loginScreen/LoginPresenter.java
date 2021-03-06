package com.example.rossinyamaral.bank.loginScreen;

import java.lang.ref.WeakReference;

interface LoginPresenterInput {
    void presentLoginData(LoginResponse response);
    void presentLoginError(String message);
    void presentPasswordError();
}


public class LoginPresenter implements LoginPresenterInput {

    public static String TAG = LoginPresenter.class.getSimpleName();

    //weak var output: HomePresenterOutput!
    public WeakReference<LoginActivityInput> output;


    @Override
    public void presentLoginData(LoginResponse response) {
        // Log.e(TAG, "presentLoginData() called with: response = [" + response + "]");
        //Do your decoration or filtering here
        if (response != null) {
            LoginViewModel loginViewModel = new LoginViewModel();
            loginViewModel.name = response.userAccountModel.getName();
            loginViewModel.bankAccount = response.userAccountModel.getBankAccount();
            loginViewModel.agency = response.userAccountModel.getAgency();
            loginViewModel.balance = response.userAccountModel.getBalance();
            output.get().displayLoginData(response.userAccountModel);
        }
    }

    @Override
    public void presentLoginError(String message) {
        output.get().displayLoginError(message);
    }

    @Override
    public void presentPasswordError() {
        output.get().displayPasswordError();
    }
}
