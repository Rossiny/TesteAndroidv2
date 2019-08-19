package com.example.rossinyamaral.bank.statementsScreen;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rossinyamaral.bank.BankApplication;
import com.example.rossinyamaral.bank.BaseActivity;
import com.example.rossinyamaral.bank.R;
import com.example.rossinyamaral.bank.ViewsUtils;
import com.example.rossinyamaral.bank.model.StatementModel;
import com.example.rossinyamaral.bank.model.UserAccountModel;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;


interface StatementsActivityInput {
    void displayStatementsData(StatementsViewModel viewModel);

    void displayError(String error);
}


public class StatementsActivity extends BaseActivity implements StatementsActivityInput {

    public static String TAG = StatementsActivity.class.getSimpleName();
    public StatementsInteractorInput output;
    public StatementsRouter router;

    private TextView nameTextView;
    private TextView accountTextView;
    private TextView balanceTextView;
    private ImageView logoutImageView;

    private RecyclerView recyclerView;
    private StatementListAdapter adapter;

    private UserAccountModel userAccountModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //do the setup
        setContentView(R.layout.activity_statements);
        adjustBar(R.color.colorButton);

        StatementsConfigurator.INSTANCE.configure(this);
        userAccountModel = getIntent().getParcelableExtra("userAccount");

        checkUserData();
        bindViews();
        adjustRecyclerView();
        fillUserData();
        setListeners();
        fetchUsersStatements();
    }

    private void checkUserData() {
        if (userAccountModel == null) {
            Toast.makeText(this, "Ops! Ocorreu um erro...", Toast.LENGTH_LONG).show();
            finish();
        }
    }

    private void bindViews() {
        nameTextView = findViewById(R.id.nameEditText);
        accountTextView = findViewById(R.id.accountTextView);
        balanceTextView = findViewById(R.id.balanceTextView);
        logoutImageView = findViewById(R.id.logoutImageView);
        recyclerView = findViewById(R.id.recyclerView);
    }

    private void adjustRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new StatementListAdapter(new ArrayList<StatementModel>());
        recyclerView.setAdapter(adapter);
    }

    private void fillUserData() {
        nameTextView.setText(userAccountModel.getName());
        accountTextView.setText(output.getFormattedAccount(userAccountModel.getBankAccount(),
                userAccountModel.getAgency()));
        balanceTextView.setText(output.getFormattedMoney(userAccountModel.getBalance()));
    }

    private void setListeners() {
        logoutImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void fetchUsersStatements() {
        StatementsRequest aStatementsRequest = new StatementsRequest();
        aStatementsRequest.userId = userAccountModel.getUserId();
        output.fetchStatementsData(aStatementsRequest);
    }

    @Override
    public void displayStatementsData(StatementsViewModel viewModel) {
        setStatementsLisView(viewModel.statements);
    }

    @Override
    public void displayError(String error) {
        ViewsUtils.dismissLoading();
        ViewsUtils.alert(this, error, null);
    }

    private void setStatementsLisView(List<StatementModel> statements) {
        adapter.setStatements(statements);
        adapter.notifyDataSetChanged();
        Log.d(TAG, "Configured RecyclerView");
    }


    private class StatementListAdapter extends RecyclerView.Adapter<StatementListAdapter.ViewHolder> {

        private static final int RECENT_ITEM = 0;
        private static final int STATEMENT_ITEM = 1;

        private LayoutInflater layoutInflater;

        private List<StatementModel> statements;

        StatementListAdapter(List<StatementModel> statements) {
            this.layoutInflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
            this.statements = statements;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

            int resource = viewType == RECENT_ITEM ? R.layout.item_recents : R.layout.item_statement;
            View layoutView = layoutInflater.inflate(resource, parent, false);
            return new ViewHolder(layoutView);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
            int pos = position - 1;
            if (position > 0) {
                viewHolder.titleTextView.setText(statements.get(pos).getTitle());
                viewHolder.descTextView.setText(statements.get(pos).getDesc());
                viewHolder.dateTextView.setText(output.getFormattedDate(statements.get(pos).getDate()));
                viewHolder.valueTextView.setText(output.getFormattedMoney(statements.get(pos).getValue()));
            }
        }

        @Override
        public int getItemCount() {
            return statements != null ? statements.size() + 1 : 0;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public int getItemViewType(int position) {
            return position == 0 ? RECENT_ITEM : STATEMENT_ITEM;
        }

        private void setStatements(List<StatementModel> statements) {
            this.statements = statements;
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            TextView titleTextView;
            TextView descTextView;
            TextView dateTextView;
            TextView valueTextView;

            ViewHolder(View itemView) {
                super(itemView);
                titleTextView = itemView.findViewById(R.id.titleTextView);
                descTextView = itemView.findViewById(R.id.descTextView);
                dateTextView = itemView.findViewById(R.id.dateTextView);
                valueTextView = itemView.findViewById(R.id.valueTextView);
            }
        }
    }
}
