package com.example.rossinyamaral.bank.statementsScreen;

import com.example.rossinyamaral.bank.ApiCallback;
import com.example.rossinyamaral.bank.BankApplication;
import com.example.rossinyamaral.bank.BankServicesImpl;
import com.example.rossinyamaral.bank.ErrorResponse;
import com.example.rossinyamaral.bank.model.StatementModel;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

interface StatementsInteractorInput {
    void fetchStatementsData(StatementsRequest request);
    String getFormattedAccount(String account, String agency);
    String getFormattedMoney(double value);
    String getFormattedDate(String dateString);
}


public class StatementsInteractor implements StatementsInteractorInput {

    public static String TAG = StatementsInteractor.class.getSimpleName();
    public StatementsPresenterInput output;
    private StatementsWorkerInput aStatementsWorkerInput;

    private Locale LOCALE = new Locale("PT", "br");

    private StatementsWorkerInput getStatementsWorkerInput() {
        if (aStatementsWorkerInput == null) return new StatementsWorker();
        return aStatementsWorkerInput;
    }

    public void setStatementsWorkerInput(StatementsWorkerInput aStatementsWorkerInput) {
        this.aStatementsWorkerInput = aStatementsWorkerInput;
    }

    @Override
    public void fetchStatementsData(StatementsRequest request) {
        aStatementsWorkerInput = getStatementsWorkerInput();
        if (request != null) {
            aStatementsWorkerInput.getStatements(request.userId,
                    new ApiCallback<List<StatementModel>>() {
                        @Override
                        public void onSuccess(List<StatementModel> object) {

                            StatementsResponse StatementsResponse = new StatementsResponse();
                            StatementsResponse.statements = object;
                            output.presentStatementsData(StatementsResponse);
                        }

                        @Override
                        public void onError(ErrorResponse error) {
                            output.presentError(error.message);
                        }
                    });
        }
    }

    @Override
    public String getFormattedAccount(String account, String agency) {
        return String.format("%s / %s", account, getFormattedAgency(agency));
    }

    private String getFormattedAgency(String agency) {
        if (agency != null && agency.length() == 9) {
            StringBuilder builder = new StringBuilder(agency);
            builder.insert(8, "-");
            builder.insert(2, ".");
            agency = builder.toString();
        }
        return agency;
    }

    @Override
    public String getFormattedMoney(double value) {
        NumberFormat numberFormat = NumberFormat.getCurrencyInstance(LOCALE);
        return numberFormat.format(value);
    }

    @Override
    public String getFormattedDate(String dateString) {
        try {
            SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd", LOCALE);
            SimpleDateFormat format2 = new SimpleDateFormat("dd/MM/yyyy", LOCALE);
            Date date = format1.parse(dateString);
            return format2.format(date);
        } catch (Exception e) {
            return "";
        }
    }
}
