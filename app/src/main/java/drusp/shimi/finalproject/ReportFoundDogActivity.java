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
import drusp.shimi.finalproject.report.ReportFoundDog;

public class ReportFoundDogActivity extends AppCompatActivity {

    private static String[] spaceProbeHeaders = {"วันที่พบ", "สายพันธุ์", "อำเภอ", "เบอร์โทร"};

    private TextView textViewReportFoundDogTotal;

    private Integer iLength = 0;

    private String[][] arrayFound = new String[6][4];
    private String sCountDataSnapshot = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_found_dog);

        textViewReportFoundDogTotal = (TextView) findViewById(R.id.tv_report_found_dog_total);

        Button buttonShowReportFoundDog = (Button) findViewById(R.id.btn_report_found_dog_title);

        final TableView<String[]> tableView = (TableView<String[]>) findViewById(R.id.table_view_report_found_dog);

        tableView.setHeaderBackgroundColor(Color.parseColor("#ff6e40"));
        tableView.setHeaderAdapter(new SimpleTableHeaderAdapter(this, spaceProbeHeaders));
        tableView.setColumnCount(4);

        buttonShowReportFoundDog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                iLength = 0;

                DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference().child("found_dog_poster");

                databaseRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        final Long lngCountDataSnapshot = dataSnapshot.getChildrenCount();
                        sCountDataSnapshot = String.valueOf(lngCountDataSnapshot);

                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            ReportFoundDog reportFoundDog = snapshot.getValue(ReportFoundDog.class);
                            assert reportFoundDog != null;
                            String sFoundDate = reportFoundDog.found_date;
                            String sBreed = reportFoundDog.breed;
                            String sDistrict = reportFoundDog.district;
                            String sPhone = reportFoundDog.phone_number;

                            arrayFound[iLength][0] = sFoundDate;
                            arrayFound[iLength][1] = sBreed;
                            arrayFound[iLength][2] = sDistrict;
                            arrayFound[iLength][3] = sPhone;
                            iLength = iLength + 1;
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });
                textViewReportFoundDogTotal.setText(sCountDataSnapshot);
                tableView.setDataAdapter(new SimpleTableDataAdapter(ReportFoundDogActivity.this, arrayFound));
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

}
