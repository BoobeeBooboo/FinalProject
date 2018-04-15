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
import drusp.shimi.finalproject.report.ReportAdoptDog;

public class ReportAdoptDogActivity extends AppCompatActivity {
    private static String[] spaceProbeHeaders = {"ชื่อสุนัข", "สายพันธุ์", "วันที่ประกาศ", "เบอร์โทร", "เหตุผลที่ให้หาบ้านใหม่"};

    private TextView textViewReportAdoptDogTotal;

    private Integer iLength = 0;

    private String[][] arrayAdopt = new String[6][5];
    private String sCountDataSnapshot = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_adopt_dog);

        textViewReportAdoptDogTotal = (TextView) findViewById(R.id.tv_report_adopt_dog_total);

        Button buttonShowReportLostDog = (Button) findViewById(R.id.btn_report_adopt_dog_title);

        final TableView<String[]> tableView = (TableView<String[]>) findViewById(R.id.table_view_report_adopt_dog);

        tableView.setHeaderBackgroundColor(Color.parseColor("#ff6e40"));
        tableView.setHeaderAdapter(new SimpleTableHeaderAdapter(this, spaceProbeHeaders));
        tableView.setColumnCount(5);

        buttonShowReportLostDog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                iLength = 0;

                DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference().child("adopt_dog_poster");

                databaseRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        final Long lngCountDataSnapshot = dataSnapshot.getChildrenCount();
                        sCountDataSnapshot = String.valueOf(lngCountDataSnapshot);

                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            ReportAdoptDog reportAdoptDog = snapshot.getValue(ReportAdoptDog.class);
                            assert reportAdoptDog != null;
                            String sDogName = reportAdoptDog.dog_name;
                            String sBreed = reportAdoptDog.breed;
                            String sAdoptDate = reportAdoptDog.adopt_date;
                            String sPhone = reportAdoptDog.phone_number;
                            String sReason = reportAdoptDog.reason;

                            arrayAdopt[iLength][0] = sDogName;
                            arrayAdopt[iLength][1] = sBreed;
                            arrayAdopt[iLength][2] = sAdoptDate;
                            arrayAdopt[iLength][3] = sPhone;
                            arrayAdopt[iLength][4] = sReason;

                            iLength = iLength + 1;
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });
                textViewReportAdoptDogTotal.setText(sCountDataSnapshot);
                tableView.setDataAdapter(new SimpleTableDataAdapter(ReportAdoptDogActivity.this, arrayAdopt));
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

}
