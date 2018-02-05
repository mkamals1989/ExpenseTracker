package com.example.kamal.test;

import android.app.DatePickerDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

import static com.example.kamal.test.Constants.DATEFORMATTER;
import static com.example.kamal.test.Constants.categoryType;

public class AddExpense extends AppCompatActivity implements View.OnClickListener {

    private int type;
    private TextView header_txt, titleTv, dateTv, amountTv;
    private Spinner spinner;
    private Button save, delete;
    private DbHelper db;
    private ExpenseModel receivedExpenseModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_expense);
        db = new DbHelper(this);

        initialiseViews();
        getBundleData();
    }

    private void getBundleData() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            type = bundle.getInt("INTENT_TYPE", 0);
            if (type == 1) {
                delete.setVisibility(View.GONE);
            } else if (type == 2) {
                header_txt.setText("View Expense");
                receivedExpenseModel = getIntent().getParcelableExtra("DATA");
                if (receivedExpenseModel != null) {
                    titleTv.setText(receivedExpenseModel.getTitle());
                    dateTv.setText(receivedExpenseModel.getDate());
                    amountTv.setText(receivedExpenseModel.getAmount());

                    for (int i = 0; i < categoryType.length; i++) {
                        if (categoryType[i].equalsIgnoreCase(receivedExpenseModel.getCategory()))
                            spinner.setSelection(i);
                    }
                }
            }
        }
    }

    private void initialiseViews() {
        header_txt = (TextView) findViewById(R.id.header_txt);
        titleTv = (TextView) findViewById(R.id.title);
        amountTv = (TextView) findViewById(R.id.amount);
        dateTv = (TextView) findViewById(R.id.date);
        spinner = (Spinner) findViewById(R.id.spinner);
        save = (Button) findViewById(R.id.save);
        delete = (Button) findViewById(R.id.delete);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, categoryType);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        dateTv.setOnClickListener(this);
        save.setOnClickListener(this);
        delete.setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.date:
                openDatePicker();
                break;

            case R.id.save:
                validateAndSaveData();
                break;

            case R.id.delete:
                deleteData();
                break;

            default:
                break;
        }
    }

    private void deleteData() {
        int status = db.deleteRecord(receivedExpenseModel);
        if (status != 0) {
            Toast.makeText(getApplicationContext(), "Deleted", Toast.LENGTH_SHORT).show();
            finish();
        } else
            Toast.makeText(this, "Something went wrong.", Toast.LENGTH_SHORT).show();

    }

    private void validateAndSaveData() {
        String title = titleTv.getText().toString();
        String amount = amountTv.getText().toString();
        String date = dateTv.getText().toString();
        String category = spinner.getSelectedItem().toString();

        if (title.isEmpty()) {
            Toast.makeText(this, "Enter Title", Toast.LENGTH_SHORT).show();
            return;
        }

        if (amount.isEmpty()) {
            Toast.makeText(this, "Enter Amount", Toast.LENGTH_SHORT).show();
            return;
        }

        if (date.isEmpty()) {
            Toast.makeText(this, "Select Date", Toast.LENGTH_SHORT).show();
            return;
        }

        if (category.isEmpty()) {
            Toast.makeText(this, "Select Category", Toast.LENGTH_SHORT).show();
            return;
        }

        if (category.equalsIgnoreCase("Select Category")) {
            Toast.makeText(this, "Select any category", Toast.LENGTH_SHORT).show();
            return;
        }

        switch (type) {
            case 1: // For Creating new expense
                ExpenseModel expenseModel = new ExpenseModel();
                expenseModel.setTitle(title);
                expenseModel.setAmount(amount);
                expenseModel.setDate(date);
                expenseModel.setCategory(category);
                boolean result = db.insertData(expenseModel);
                if (result) {
                    Toast.makeText(this, "Expense Saved.", Toast.LENGTH_SHORT).show();
                    finish();
                } else
                    Toast.makeText(this, "Something went wrong.", Toast.LENGTH_SHORT).show();
                break;

            case 2: // For updating the existing expense
                receivedExpenseModel.setTitle(title);
                receivedExpenseModel.setAmount(amount);
                receivedExpenseModel.setDate(date);
                receivedExpenseModel.setCategory(category);
                int status = db.updateData(receivedExpenseModel);
                if (status != -1) {
                    Toast.makeText(this, "Expense Saved.", Toast.LENGTH_SHORT).show();
                    finish();
                } else
                    Toast.makeText(this, "Something went wrong.", Toast.LENGTH_SHORT).show();
                break;

        }
    }

    private void openDatePicker() {
        Calendar newCalendar = Calendar.getInstance();
        DatePickerDialog dateDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                dateTv.setText(DATEFORMATTER.format(newDate.getTime()));
            }

        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
        dateDialog.show();
    }
}
