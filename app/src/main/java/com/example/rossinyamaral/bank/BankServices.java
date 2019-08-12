package com.example.rossinyamaral.bank;

import com.example.rossinyamaral.bank.model.StatementModel;
import com.example.rossinyamaral.bank.model.UserAccountModel;

import java.util.List;

public interface BankServices {

    void login(String user, String password, ApiCallback<UserAccountModel> callback);

    void getStatements(int userId, ApiCallback<List<StatementModel>> callback);
}
