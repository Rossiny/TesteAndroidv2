package com.example.rossinyamaral.bank.statementsScreen;


import java.lang.ref.WeakReference;

interface StatementsPresenterInput {
    void presentStatementsData(StatementsResponse response);
    void presentError(String error);
}


public class StatementsPresenter implements StatementsPresenterInput {

    public static String TAG = StatementsPresenter.class.getSimpleName();

    public WeakReference<StatementsActivityInput> output;


    @Override
    public void presentStatementsData(StatementsResponse response) {
        // Log.e(TAG, "presentStatementsData() called with: response = [" + response + "]");
        //Do your decoration or filtering here
        if (response != null) {
            StatementsViewModel viewModel = new StatementsViewModel();
            viewModel.statements = response.statements;
            output.get().displayStatementsData(viewModel);
        }
    }

    @Override
    public void presentError(String error) {
        output.get().displayError(error);
    }
}
