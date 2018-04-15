package drusp.shimi.finalproject;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import de.codecrafters.tableview.TableView;
import de.codecrafters.tableview.toolkit.SimpleTableDataAdapter;
import de.codecrafters.tableview.toolkit.SimpleTableHeaderAdapter;
import drusp.shimi.finalproject.report.ReportAllDog;

public class ReportAllDogActivity extends AppCompatActivity {

    private static String[] spaceProbeHeaders = {"สถานะ", "วันที่", "สายพันธุ์", "อำเภอ", "เบอร์โทร"};

    private TextView textViewReportAllDogTotal;

    private Integer iLength = 0;

    private String[][] arrayAllDog = new String[10][5];
    private String sCountDataSnapshot = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_all_dog);

        textViewReportAllDogTotal = (TextView) findViewById(R.id.tv_report_all_dog_total);

        Button buttonShowReportAllDog = (Button) findViewById(R.id.btn_report_all_dog_title);

        final TableView<String[]> tableView = (TableView<String[]>) findViewById(R.id.table_view_report_all_dog);

        tableView.setHeaderBackgroundColor(Color.parseColor("#ff6e40"));
        tableView.setHeaderAdapter(new SimpleTableHeaderAdapter(this, spaceProbeHeaders));
        tableView.setColumnCount(5);

        buttonShowReportAllDog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                iLength = 0;

                DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference().child("main_poster");

                databaseRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        final Long lngCountDataSnapshot = dataSnapshot.getChildrenCount();
                        sCountDataSnapshot = String.valueOf(lngCountDataSnapshot);

                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            ReportAllDog reportAllDog = snapshot.getValue(ReportAllDog.class);
                            assert reportAllDog != null;
                            String sStatus = reportAllDog.status;
                            String sDate = reportAllDog.date;
                            String sBreed = reportAllDog.breed;
                            String sDistrict = reportAllDog.district;
                            String sPhone = reportAllDog.phone_number;

                            arrayAllDog[iLength][0] = sStatus;
                            arrayAllDog[iLength][1] = sDate;
                            arrayAllDog[iLength][2] = sBreed;
                            arrayAllDog[iLength][3] = sDistrict;
                            arrayAllDog[iLength][4] = sPhone;
                            iLength = iLength + 1;
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });
                textViewReportAllDogTotal.setText(sCountDataSnapshot);
                tableView.setDataAdapter(new SimpleTableDataAdapter(ReportAllDogActivity.this, arrayAllDog));
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

}
