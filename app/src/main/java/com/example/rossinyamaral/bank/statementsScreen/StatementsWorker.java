package com.example.rossinyamaral.bank.statementsScreen;

import com.example.rossinyamaral.bank.ApiCallback;
import com.example.rossinyamaral.bank.remote.BankApi;
import com.example.rossinyamaral.bank.remote.ErrorResponse;
import com.example.rossinyamaral.bank.model.StatementModel;

import java.util.List;

interface StatementsWorkerInput {
    //Define needed interfaces
    void getStatements(int userId, ApiCallback<List<StatementModel>> callback);
}

public class StatementsWorker implements StatementsWorkerInput {

    @Override
    public void getStatements(int userId, final ApiCallback<List<StatementModel>> callback) {
        BankApi.getStatements(userId,
                new ApiCallback<List<StatementModel>>() {
                    @Override
                    public void onSuccess(List<StatementModel> list) {
                        callback.onSuccess(list);
                    }

                    @Override
                    public void onError(ErrorResponse errorResponse) {
                        callback.onError(errorResponse);
                    }
                });
    }
}
