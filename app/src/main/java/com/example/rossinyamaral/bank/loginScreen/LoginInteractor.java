package com.example.rossinyamaral.bank.loginScreen;

import com.example.rossinyamaral.bank.ApiCallback;
import com.example.rossinyamaral.bank.local.BankCache;
import com.example.rossinyamaral.bank.remote.ErrorResponse;
import com.example.rossinyamaral.bank.model.UserAccountModel;

import java.util.regex.Pattern;

interface LoginInteractorInput {

    void fetchLoginData(LoginRequest request);
}

public class LoginInteractor implements LoginInteractorInput {

    public static String TAG = LoginInteractor.class.getSimpleName();
    public LoginPresenterInput output;
    private LoginWorkerInput aLoginWorkerInput;

    private LoginWorkerInput getLoginWorkerInput() {
        if (aLoginWorkerInput == null) return new LoginWorker();
        return aLoginWorkerInput;
    }

    public void setLoginWorkerInput(LoginWorkerInput aLoginWorkerInput) {
        this.aLoginWorkerInput = aLoginWorkerInput;
    }

    public void fetchLoginData(final LoginRequest request) {
        if (!isLoginPasswordValid(request.password)) {
            output.presentPasswordError();
            return;
        }

        aLoginWorkerInput = getLoginWorkerInput();
        final LoginResponse loginResponse = new LoginResponse();
        // Call the workers
        aLoginWorkerInput.getUserAccount(request.user, request.password,
                new ApiCallback<UserAccountModel>() {
                    @Override
                    public void onSuccess(UserAccountModel model) {
                        saveLastLogin(request.user);
                        loginResponse.userAccountModel = model;
                        output.presentLoginData(loginResponse);
                    }

                    @Override
                    public void onError(ErrorResponse error) {
                        output.presentLoginError(error.message);
                    }
                });

    }

    private void saveLastLogin(String login) {
        BankCache.setLastLogin(login);
    }

    private boolean isLoginPasswordValid(String password) {
        return hasUppercaseLetter(password) &&
                hasAlphanumericCharacter(password) &&
                hasSpecialCharacter(password);
    }

    private boolean hasUppercaseLetter(String password) {
        return Pattern.compile("[A-Z]").matcher(password).find();
    }

    private boolean hasSpecialCharacter(String password) {
        return Pattern.compile("[^a-zA-Z0-9]").matcher(password).find();
    }

    private boolean hasAlphanumericCharacter(String password) {
        return Pattern.compile("[a-z0-9]").matcher(password).find();
    }
}
