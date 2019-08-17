package com.example.rossinyamaral.bank.loginScreen;

import com.example.rossinyamaral.bank.ApiCallback;
import com.example.rossinyamaral.bank.BankApi;
import com.example.rossinyamaral.bank.ErrorResponse;
import com.example.rossinyamaral.bank.model.UserAccountModel;

interface LoginWorkerInput {
    //Define needed interfaces
    void getUserAccount(String user, String password,
                                    ApiCallback<UserAccountModel> callback);
}

public class LoginWorker implements LoginWorkerInput {

    @Override
    public void getUserAccount(String user, String password,
                                           final ApiCallback<UserAccountModel> callback) {
        BankApi.login(user, password,
                new ApiCallback<UserAccountModel>() {
                    @Override
                    public void onSuccess(UserAccountModel model) {
                        callback.onSuccess(model);
                    }

                    @Override
                    public void onError(ErrorResponse error) {
                        callback.onError(error);
                    }
                });
    }
}
