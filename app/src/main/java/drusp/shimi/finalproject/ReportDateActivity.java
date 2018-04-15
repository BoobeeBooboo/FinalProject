package drusp.shimi.finalproject;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;

import de.codecrafters.tableview.TableView;
import de.codecrafters.tableview.toolkit.SimpleTableDataAdapter;
import de.codecrafters.tableview.toolkit.SimpleTableHeaderAdapter;

public class ReportDateActivity extends AppCompatActivity implements View.OnClickListener {

    private static String[] spaceProbeHeaders = {"วันที่", "สายพันธุ์", "อำเภอ", "สถานะ"};

    private String[][] arrayDate = new String[10][4];

    private Button buttonSelectDateStart;
    private Button buttonSelectDateEnd;

    private DatePickerDialog datePickerDialogReport;

    private TextView textViewReportDateTotal;
    private TextView textViewReportDateTotal2;

    private Integer iLength = 0;

    private String sCountDataSnapshot = null;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_date);

        buttonSelectDateStart = (Button) findViewById(R.id.btn_report_date_start);
        buttonSelectDateEnd = (Button) findViewById(R.id.btn_report_date_end);
        Button buttonSearch = (Button) findViewById(R.id.btn_report_date_search);

        textViewReportDateTotal = (TextView) findViewById(R.id.tv_report_date_total);
        textViewReportDateTotal2 = (TextView) findViewById(R.id.tv_report_date_total2);

        buttonSelectDateStart.setOnClickListener(this);
        buttonSelectDateEnd.setOnClickListener(this);

        final TableView<String[]> tableView = (TableView<String[]>) findViewById(R.id.table_view_report_date);
        final TableView<String[]> tableView2 = (TableView<String[]>) findViewById(R.id.table_view_report_date2);

        tableView.setHeaderBackgroundColor(Color.parseColor("#ff6e40"));
        tableView.setHeaderAdapter(new SimpleTableHeaderAdapter(this, spaceProbeHeaders));
        tableView.setHeaderVisible(true);
        tableView.setColumnCount(4);

        tableView2.setHeaderAdapter(new SimpleTableHeaderAdapter(this, spaceProbeHeaders));
        tableView2.setHeaderVisible(false);
        tableView2.setColumnCount(4);

        buttonSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                iLength = 0;

                final String sStart = buttonSelectDateStart.getText().toString().trim();
                final String sEnd = buttonSelectDateEnd.getText().toString().trim();

                if (sStart.equals("เริ่มวันที่") || sEnd.equals("สิ้นสุดวันที่")) {
                    Toast.makeText(getApplicationContext(), "เลือกวันที่", Toast.LENGTH_LONG).show();
                } else {
                    final String[] startParts = sStart.split("/");
                    final String[] endParts = sEnd.split("/");

                    final String sStartDay = startParts[0];
                    final String sStartMonth = startParts[1];
                    final String sStartYear = startParts[2];
                    final String sStartTimestamp = sStartYear + sStartMonth + sStartDay;
                    final Long longStartTimestamp = Long.parseLong(sStartTimestamp);

                    final String sEndDay = endParts[0];
                    final String sEndMonth = endParts[1];
                    final String sEndYear = endParts[2];
                    final String sEndTimestamp = sEndYear + sEndMonth + sEndDay;
                    final Long longEndTimestamp = Long.parseLong(sEndTimestamp);

                    Log.e("XXX", sStart + "," + sEnd);

                    DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference().child("main_poster");
                    Query query = databaseRef.orderByChild("timestamp_query").startAt(longStartTimestamp).endAt(longEndTimestamp);

                    query.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            final Long lngCountDataSnapshot = dataSnapshot.getChildrenCount();
                            sCountDataSnapshot = String.valueOf(lngCountDataSnapshot);

                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                final String sDate = (String) snapshot.child("date").getValue();
                                final String sBreed = (String) snapshot.child("breed").getValue();
                                final String sDistrict = (String) snapshot.child("district").getValue();
                                final String sStatus = (String) snapshot.child("status").getValue();

                                arrayDate[iLength][0] = sDate;
                                arrayDate[iLength][1] = sBreed;
                                arrayDate[iLength][2] = sDistrict;
                                arrayDate[iLength][3] = sStatus;

                                Log.e("count", snapshot.getKey() + "/" + sDate + "/" + sBreed + "/"
                                        + sBreed + "/" + sDistrict + "/" + sStatus);
                                iLength = iLength + 1;
                            }
                            Log.e("CountDataSnapshot", sCountDataSnapshot);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                        }
                    });
                    textViewReportDateTotal.setText(sCountDataSnapshot);
                    tableView.setDataAdapter(new SimpleTableDataAdapter(ReportDateActivity.this, arrayDate));
                }
            }
        });

//
//                    Query query = databaseRef.orderByChild("timestamp_query").startAt(longStartTimestamp).endAt(longEndTimestamp);
//
//                    query.addValueEventListener(new ValueEventListener() {
//                        @Override
//                        public void onDataChange(DataSnapshot dataSnapshot) {
//                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
//                                ReportDate reportDate = snapshot.getValue(ReportDate.class);
//
//                                final long maxNum = dataSnapshot.getChildrenCount();
//                                final int count = (int) maxNum;
////                                if(iCount.equals(0)) {
////                                    iCount = iCount + count;
////                                }
//                                assert reportDate != null;
//                                String sDate = reportDate.date;
//                                String sBreed = reportDate.breed;
//                                String sDistrict = reportDate.district;
//                                String sStatus = reportDate.status;
//                                Long longQuery = reportDate.timestamp_query;
//                                String sQuery = String.valueOf(longQuery);
////                                Log.e(snapshot.getKey(), sQuery);
//
////                                for(int i = 20170101;i <= longStartTimestamp; i++ ){
////                                    date[iLength][0] = sDate;
////                                    date[iLength][1] = sBreed;
////                                    date[iLength][2] = sDistrict;
////                                    date[iLength][3] = sStatus;
////                                    iLength = iLength + 1;
////
////                                    final long maxNum = dataSnapshot.getChildrenCount();
////                                    final int count = (int) maxNum;
////                                    final String sCount = String.valueOf(count);
////                                    textViewReportDateTotal.setText(sCount);
////                                }
//
////                                if (dbQuery.equals(dbStartTimestamp)) {
//
//
////                                new ReportDateActivity(count, 4);
//
////                                date[iLength][0] = sDate;
////                                date[iLength][1] = sBreed;
////                                date[iLength][2] = sDistrict;
////                                date[iLength][3] = sStatus;
////                                iLength = iLength + 1;
////
////                                final long maxNum = dataSnapshot.getChildrenCount();
////                                final int count = (int) maxNum;
////                                final String sCount = String.valueOf(count);
////                                textViewReportDateTotal.setText(sCount);
//
////                                } else if (dbQuery.equals(dbStartTimestamp + iLength)){
////                                    date2[iLength-1][0] = sDate;
////                                    date2[iLength-1][1] = sBreed;
////                                    date2[iLength-1][2] = sDistrict;
////                                    date2[iLength-1][3] = sStatus;
////                                    iLength = iLength + 1;
////
////                                    final long maxNum = dataSnapshot.getChildrenCount();
////                                    final int count = (int) maxNum;
////                                    final String sCount = String.valueOf(count);
////                                    textViewReportDateTotal2.setText(sCount);
////                                }
//                            }
//
//                        }
//
//                        @Override
//                        public void onCancelled(DatabaseError databaseError) {
//                        }
//                    });
//
////                    String sCount = String.valueOf(iCount);
////                    Log.e("Log.e ====>", sCount);
//                }
//
////                new ReportDateActivity(iCount,4);
//                tableView.setDataAdapter(new SimpleTableDataAdapter(ReportDateActivity.this, date));

//                iLength = 0;
//                iLoopSnapshot = 0;
//
//                final String sStart = buttonSelectDateStart.getText().toString().trim();
//                final String sEnd = buttonSelectDateEnd.getText().toString().trim();
//
//                if (sStart.equals("เริ่มวันที่") || sEnd.equals("สิ้นสุดวันที่")) {
//                    Toast.makeText(getApplicationContext(), "เลือกวันที่", Toast.LENGTH_LONG).show();
//                } else {
//                    final String[] startParts = sStart.split("/");
//                    final String[] endParts = sEnd.split("/");
//
//                    final String sStartDay = startParts[0];
//                    final String sStartMonth = startParts[1];
//                    final String sStartYear = startParts[2];
//                    final String sStartTimestamp = sStartYear + sStartMonth + sStartDay;
//                    final Long longStartTimestamp = Long.parseLong(sStartTimestamp);
//
//                    final String sEndDay = endParts[0];
//                    final String sEndMonth = endParts[1];
//                    final String sEndYear = endParts[2];
//                    final String sEndTimestamp = sEndYear + sEndMonth + sEndDay;
//                    final Long longEndTimestamp = Long.parseLong(sEndTimestamp);
//
//                    DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference().child("view").child("date").child("timestamp");
////                    DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference().child("main_poster");
////                    Query query = databaseRef.limitToFirst(10).startAt(longStartTimestamp).endAt(longEndTimestamp);
//
//                    databaseRef.addValueEventListener(new ValueEventListener() {
//                        @Override
//                        public void onDataChange(DataSnapshot dataSnapshot) {
//                            final Long lngCountDataSnapshot = dataSnapshot.getChildrenCount();
//                            final String sCountDataSnapshot = String.valueOf(lngCountDataSnapshot);
//                            final Integer intCountDataSnapshot = lngCountDataSnapshot.intValue();
//
//                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
//                                String sLoopSnapshot = String.valueOf(iLoopSnapshot);
//                                final String sSnapshotKey = snapshot.getKey();
//                                final Long lngCountSnapshotKey = snapshot.getChildrenCount();
//                                final String sCountSnapshotKey = String.valueOf(lngCountSnapshotKey);
//
//                                sDataSnapshot = new String[intCountDataSnapshot][1];
//                                sDataSnapshot[iLoopSnapshot][0] = sSnapshotKey;
//                                Log.d("Data", sLoopSnapshot + "/" + sDataSnapshot[iLoopSnapshot][0] + "/" + sCountSnapshotKey);
//
////                                for (DataSnapshot childSnapshot : snapshot.getChildren()) {
////                                    final String sChildSnapshot = childSnapshot.getKey();
////                                    Log.d(sLoopSnapshot, sSnapshotKey + "/" + sCountSnapshotKey + "/" + sChildSnapshot);
////                                }
//                                iLoopSnapshot = iLoopSnapshot + 1;
//                            }
//                            Log.d("ChildCount", sCountDataSnapshot);
//                            textViewReportDateTotal.setText(sCountDataSnapshot);
//                            tableView.setDataAdapter(new SimpleTableDataAdapter(ReportDateActivity.this, sDataSnapshot));
//                        }
//
//                        @Override
//                        public void onCancelled(DatabaseError databaseError) {
//                        }
//
//                    });
//
//                }

    }

    @Override
    public void onClick(View view) {
        if (view == buttonSelectDateStart) {
            selectStartDate();
        } else if (view == buttonSelectDateEnd) {
            selectEndDate();
        }
    }

    private void selectStartDate() {
        final Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR); // current year
        int mMonth = c.get(Calendar.MONTH); // current month
        int mDay = c.get(Calendar.DAY_OF_MONTH); // current day
        // date picker dialog
        datePickerDialogReport = new DatePickerDialog(ReportDateActivity.this,
                new DatePickerDialog.OnDateSetListener() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        // set day of month , month and year value in the edit text
                        int month = monthOfYear + 1;
                        String formattedMonth = "" + month;
                        String formattedDayOfMonth = "" + dayOfMonth;

                        if (month < 10) {
                            formattedMonth = "0" + month;
                        }
                        if (dayOfMonth < 10) {
                            formattedDayOfMonth = "0" + dayOfMonth;
                        }
                        buttonSelectDateStart.setText(formattedDayOfMonth + "/"
                                + formattedMonth + "/" + year);
                    }
                }, mYear, mMonth, mDay);
        datePickerDialogReport.show();
        buttonSelectDateStart.setTextColor(Color.parseColor("#000000"));
    }

    private void selectEndDate() {
        final Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR); // current year
        int mMonth = c.get(Calendar.MONTH); // current month
        int mDay = c.get(Calendar.DAY_OF_MONTH); // current day
        // date picker dialog
        datePickerDialogReport = new DatePickerDialog(ReportDateActivity.this,
                new DatePickerDialog.OnDateSetListener() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        // set day of month , month and year value in the edit text
                        int month = monthOfYear + 1;
                        String formattedMonth = "" + month;
                        String formattedDayOfMonth = "" + dayOfMonth;

                        if (month < 10) {
                            formattedMonth = "0" + month;
                        }
                        if (dayOfMonth < 10) {
                            formattedDayOfMonth = "0" + dayOfMonth;
                        }
                        buttonSelectDateEnd.setText(formattedDayOfMonth + "/"
                                + formattedMonth + "/" + year);
                    }
                }, mYear, mMonth, mDay);
        datePickerDialogReport.show();
        buttonSelectDateEnd.setTextColor(Color.parseColor("#000000"));
    }

}


