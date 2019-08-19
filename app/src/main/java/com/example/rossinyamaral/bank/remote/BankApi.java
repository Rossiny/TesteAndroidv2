package com.example.rossinyamaral.bank.remote;

import com.example.rossinyamaral.bank.ApiCallback;
import com.example.rossinyamaral.bank.model.StatementModel;
import com.example.rossinyamaral.bank.model.UserAccountModel;

import java.util.List;

public class BankApi {

    private static BankServices services;

    public static void setService(BankServices services) {
        BankApi.services = services;
    }

    public static void login(String user, String password, ApiCallback<UserAccountModel> callback) {
        services.login(user, password, callback);
    }

    public static void getStatements(int userId, ApiCallback<List<StatementModel>> callback) {
        services.getStatements(userId, callback);
    }
}
