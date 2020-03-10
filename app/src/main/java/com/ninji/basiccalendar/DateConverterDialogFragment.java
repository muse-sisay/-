package com.ninji.basiccalendar;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.ninji.basiccalendar.model.DayAndDates;

import java.util.Calendar;

public class DateConverterDialogFragment extends DialogFragment {

    private View mView;
    private EditText mDayEdit;
    private EditText mMonthEdit;
    private TextInputLayout mInputLayoutDate;
    private TextInputLayout mInputLayoutMonth;
    private TextInputLayout mInputLayoutYear;
    private EditText mYearEdit;
    private TextView mConversionResult;


    private TextView mCalanderConverted;
    private TextView mCalander;

    private boolean isEthiopian;

    @Nullable
//    @Override
//    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        View v = LayoutInflater.from(getActivity())
//                .inflate(R.layout.dialog_date, null);
//
//        return v;
//    }




        @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        mView = LayoutInflater.from(getActivity())
                .inflate(R.layout.dialog_date, null);

        isEthiopian = true ;
        bindView();

        AlertDialog dialog = new AlertDialog.Builder(getActivity())
                .setView(mView)
                .setTitle(null)
                .setPositiveButton(null, null)
                .create()
                ;
        dialog.getWindow().setBackgroundDrawable( new ColorDrawable(Color.TRANSPARENT));
        return dialog;
    }

    private void bindView() {

        mCalander = mView.findViewById(R.id.title_calendar_one);
        mCalanderConverted = mView.findViewById(R.id.title_calendar_two);
        // input for day
        mDayEdit = mView.findViewById(R.id.edit_day);
        mInputLayoutDate = mView.findViewById(R.id.input_layout_day);

        // input for month
        mMonthEdit = mView.findViewById(R.id.edit_month);
        mInputLayoutMonth = mView.findViewById(R.id.input_layout_month);

        //input for year
        mYearEdit = mView.findViewById(R.id.edit_year);
        mInputLayoutYear = mView.findViewById(R.id.input_layout_year);

        Button btnConvert = mView.findViewById(R.id.btn_convert);
        final Button btnChange = mView.findViewById(R.id.btn_change);
        mConversionResult = mView.findViewById(R.id.result_date);

        mDayEdit.addTextChangedListener(new TextChangeListener(mDayEdit));
        mMonthEdit.addTextChangedListener(new TextChangeListener(mMonthEdit));
        mYearEdit.addTextChangedListener(new TextChangeListener(mYearEdit));

        btnConvert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                convertDate();
            }
        });

        btnChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isEthiopian){
                    mCalander.setText(R.string.ethiopian_calander);
                    mCalanderConverted.setText(R.string.gregorian_calander);
                    btnChange.setText(R.string.to_ethiopian);
                    isEthiopian = false ;
                } else {
                    mCalander.setText(R.string.gregorian_calander);
                    mCalanderConverted.setText(R.string.ethiopian_calander);
                    btnChange.setText(R.string.to_gregorian);
                    isEthiopian = true ;
                }

            }
        });

    }

    private void convertDate() {
        if ( !validateDay() )
            return ;
        if ( !validateMonth())
            return ;
        if ( !validateYear())
            return ;
        if ( isEthiopian){
            converEthiopicDate();
        } else {
            convertGregorianDate();
        }

    }

    private void convertGregorianDate() {

        int day = Integer.parseInt(mDayEdit.getText().toString() );
        int month = Integer.parseInt(mMonthEdit.getText().toString() );
        int year = Integer.parseInt(mYearEdit.getText().toString() );

        int [] values = new EthiopicCalendar(year , month , day).ethiopicToGregorian();

        Calendar cal = Calendar.getInstance();
        cal.set(values[0] , values[1] -1 ,values[2]);

        String dayGreg = DayAndDates.DaysOfWeek.gregDays[ cal.get(Calendar.DAY_OF_WEEK )-1]  + " ";
        String monthEth = DayAndDates.Months.gMonths[values[1] -1 ] + " " ;
//        String date =  dayEth + monthEth + values[2]+ " " + values[0] ;
        String date =dayGreg + " " + monthEth + values[2]+ " " + values[0] ;
//            Toast.makeText(getContext(), cal.getTime().toString(), Toast.LENGTH_SHORT).show();
        mConversionResult.setText(date);
        mCalanderConverted.setVisibility(View.VISIBLE);
        mConversionResult.setVisibility(View.VISIBLE);
    }

    private void converEthiopicDate() {

        // depending on the calendar type do conversion
        int day = Integer.parseInt(mDayEdit.getText().toString() );
        int month = Integer.parseInt(mMonthEdit.getText().toString() );
        int year = Integer.parseInt(mYearEdit.getText().toString() );

        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_MONTH , day);
        cal.set(Calendar.MONTH , month -1);
        cal.set(Calendar.YEAR , year);

        EthiopicCalendar ethiopicCalendar = new EthiopicCalendar(cal);
        int [] values = ethiopicCalendar.gregorianToEthiopic() ;

        int dayOfWeek =  cal.get(Calendar.DAY_OF_WEEK) == 1 ?  7:  cal.get(Calendar.DAY_OF_WEEK) - 1;

        String dayEth = getString(DayAndDates.DaysOfWeek.ethDays[dayOfWeek-1] ) + " ";
        String monthEth = getString(DayAndDates.Months.ethMonths[values[1] - 1 ] )+ " " ;
        String date =  dayEth + monthEth + values[2]+ " " + values[0] ;
//            Toast.makeText(getContext(), cal.getTime().toString(), Toast.LENGTH_SHORT).show();
        mConversionResult.setText(date);
        mCalanderConverted.setVisibility(View.VISIBLE);
        mConversionResult.setVisibility(View.VISIBLE);
    }

    private boolean validateYear() {
        if ( mYearEdit.getText().toString().isEmpty() ){
            mInputLayoutYear.setError("Enter a Valid Day");
            return false;
        } else {
            mInputLayoutYear.setErrorEnabled(false);
        }

        return true ;
    }

    private boolean validateMonth() {
        if ( mMonthEdit.getText().toString().isEmpty() ){
            mInputLayoutMonth.setError("Enter a Valid Day");
            return false;
        } else {
            mInputLayoutMonth.setErrorEnabled(false);
        }

        return true ;
    }

    private boolean validateDay() {
        if ( mDayEdit.getText().toString().isEmpty() ){
            mInputLayoutDate.setError("Enter a Valid Day");
            return false;
        } else {
            mInputLayoutDate.setErrorEnabled(false);
        }

        return true ;
    }

    private class TextChangeListener implements TextWatcher {

       private View view ;

        private TextChangeListener(View view) {
            this.view = view;
        }


        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            switch ( view.getId()){
                case R.id.edit_day:
                    validateDay();
                    break ;
                case R.id.edit_month:
                    validateMonth();
                    break ;
                case R.id.edit_year:
                    validateYear();
                    break;
            }

        }
    }
}
