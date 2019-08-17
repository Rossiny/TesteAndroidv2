package com.example.rossinyamaral.bank.loginScreen;

import com.example.rossinyamaral.bank.ApiCallback;
import com.example.rossinyamaral.bank.BankCache;
import com.example.rossinyamaral.bank.BankStorage;
import com.example.rossinyamaral.bank.ErrorResponse;
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

    public void fetchLoginData(LoginRequest request) {
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
                        saveLastLogin(model);
                        loginResponse.userAccountModel = model;
                        output.presentLoginData(loginResponse);
                    }

                    @Override
                    public void onError(ErrorResponse error) {
                        output.presentLoginError(error.message);
                    }
                });

    }

    private void saveLastLogin(UserAccountModel model) {
        BankCache.setLastLogin(model.getName());
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
