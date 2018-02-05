package com.example.kamal.test;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.ArrayList;

/**
 * Created by KAMAL on 2/5/2018.
 */

public class ExpenseListing extends AppCompatActivity {

    private FloatingActionButton fab;
    private RecyclerView list;
    private DataAdapter dataAdapter;
    private DbHelper db;
    private ArrayList<ExpenseModel> expenseModels;
    private ProgressDialog dialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listing);
        db = new DbHelper(this);

        initialiseViews();
    }

    private void initialiseViews() {
        dialog = new ProgressDialog(this);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        list = (RecyclerView) findViewById(R.id.list);
        list.setLayoutManager(new LinearLayoutManager(this));
        dataAdapter = new DataAdapter(this, new ArrayList<ExpenseModel>(), new DataAdapter.CallBack() {
            @Override
            public void onClick(int pos) {
                Intent detailIntent = new Intent(ExpenseListing.this, AddExpense.class);
                detailIntent.putExtra("INTENT_TYPE", 2);
                detailIntent.putExtra("DATA", expenseModels.get(pos));
                startActivity(detailIntent);
            }
        });
        list.setAdapter(dataAdapter);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent openIntent = new Intent(ExpenseListing.this, AddExpense.class);
                openIntent.putExtra("INTENT_TYPE", 1);
                startActivity(openIntent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        new GetDataFromDB().execute();

    }

    public void onViewStatsClicked(View view) {
        Intent openIntent = new Intent(ExpenseListing.this, ViewStats.class);
        startActivity(openIntent);
    }

    private class GetDataFromDB extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (expenseModels != null && expenseModels.size() > 0)
                if (dataAdapter != null)
                    dataAdapter.refreshData(expenseModels);
            if (dialog.isShowing())
                dialog.dismiss();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog.setMessage("Loading Data...");
            dialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            expenseModels = db.getAllRecords();
            return null;
        }
    }
}
