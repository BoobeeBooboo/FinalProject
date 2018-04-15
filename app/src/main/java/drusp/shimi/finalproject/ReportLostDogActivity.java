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
import drusp.shimi.finalproject.report.ReportLostDog;

public class ReportLostDogActivity extends AppCompatActivity {

    private static String[] spaceProbeHeaders = {"ชื่อสุนัข", "สายพันธุ์", "วันที่หาย", "อำเภอ", "เบอร์โทร"};

    private TextView textViewReportLostDogTotal;

    private Integer iLength = 0;

    private String[][] arrayLost = new String[6][5];
    private String sCountDataSnapshot = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_lost_dog);

        textViewReportLostDogTotal = (TextView) findViewById(R.id.tv_report_lost_dog_total);

        Button buttonShowReportLostDog = (Button) findViewById(R.id.btn_report_lost_dog_title);

        final TableView<String[]> tableView = (TableView<String[]>) findViewById(R.id.table_view_report_lost_dog);

        tableView.setHeaderBackgroundColor(Color.parseColor("#ff6e40"));
        tableView.setHeaderAdapter(new SimpleTableHeaderAdapter(this, spaceProbeHeaders));
        tableView.setColumnCount(5);

        buttonShowReportLostDog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                iLength = 0;

                DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference().child("lost_dog_poster");

                databaseRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        final Long lngCountDataSnapshot = dataSnapshot.getChildrenCount();
                        sCountDataSnapshot = String.valueOf(lngCountDataSnapshot);

                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            ReportLostDog reportLostDog = snapshot.getValue(ReportLostDog.class);
                            assert reportLostDog != null;
                            String sDogName = reportLostDog.dog_name;
                            String sBreed = reportLostDog.breed;
                            String sLostDate = reportLostDog.lost_date;
                            String sDistrict = reportLostDog.district;
                            String sPhone = reportLostDog.phone_number;

                            arrayLost[iLength][0] = sDogName;
                            arrayLost[iLength][1] = sBreed;
                            arrayLost[iLength][2] = sLostDate;
                            arrayLost[iLength][3] = sDistrict;
                            arrayLost[iLength][4] = sPhone;

                            iLength = iLength + 1;
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });
                textViewReportLostDogTotal.setText(sCountDataSnapshot);
                tableView.setDataAdapter(new SimpleTableDataAdapter(ReportLostDogActivity.this, arrayLost));
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

}
