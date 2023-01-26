package com.oshnisoft.erp.btl.ui.bill;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.oshnisoft.erp.btl.App;
import com.oshnisoft.erp.btl.R;

import java.util.Calendar;


public class BillDatePickerFragment extends DialogFragment {

    private DatePicker datePicker;
    public BillDateDialogListener dateDialogListener;
    boolean isStart;

    public static BillDatePickerFragment newInstance() {
        return new BillDatePickerFragment();
    }

    Context context;

    public BillDateDialogListener getDateDialogListener() {
        return dateDialogListener;
    }

    public void setDateDialogListener(BillDateDialogListener dateDialogListener, boolean isStart) {
        this.dateDialogListener = dateDialogListener;
        this.isStart = isStart;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    public interface BillDateDialogListener {
        void setDate(String date);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        App.getComponent().inject(this);
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        View v = LayoutInflater.from(context).inflate(R.layout.fragment_dialog_date_picker, null);
        datePicker = (DatePicker) v.findViewById(R.id.dialog_date_date_picker);
        final AlertDialog builder = new AlertDialog.Builder(context).setView(v).setPositiveButton(android.R.string.ok, null).setNegativeButton(android.R.string.cancel, null).setCancelable(false).create();
        Calendar maxDate = Calendar.getInstance();
        Calendar minDate = Calendar.getInstance();
        minDate.add(Calendar.MONTH, -1);
        datePicker.setMaxDate(maxDate.getTimeInMillis());
        datePicker.setMinDate(minDate.getTimeInMillis());
        builder.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {

                Button buttonPositive = ((AlertDialog) dialogInterface).getButton(AlertDialog.BUTTON_POSITIVE);
                buttonPositive.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int year = datePicker.getYear();
                        int mon = datePicker.getMonth();
                        int day = datePicker.getDayOfMonth();
                        Calendar cal = Calendar.getInstance();
                        cal.set(year, mon, day);
                        String date = "" + year + "-" + monthConvert((mon + 1)) + "-" + monthConvert(day);
                        dateDialogListener.setDate(date);
                        dialogInterface.dismiss();

                    }
                });

                Button buttonNegative = ((AlertDialog) dialogInterface).getButton(AlertDialog.BUTTON_NEGATIVE);
                buttonNegative.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialogInterface.dismiss();
                    }
                });
            }
        });
        return builder;
    }

    public String monthConvert(int month) {
        String monthCal = String.valueOf(month);
        if (monthCal.length() == 1) {
            return "0" + monthCal;
        }
        return monthCal;
    }


}
