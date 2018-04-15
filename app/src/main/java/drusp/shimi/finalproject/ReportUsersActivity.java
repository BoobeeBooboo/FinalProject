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
import drusp.shimi.finalproject.report.ReportUsers;

public class ReportUsersActivity extends AppCompatActivity {

    private static String[] spaceProbeHeaders = {"ชื่อผู้ใช้", "อีเมล", "รหัสผ่าน", "วันที่สมัคร"};

    private TextView textViewReportUsersTotal;

    private Integer iLength = 0;

    private String[][] arrayUser = new String[2][4];
    private String sCountDataSnapshot = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_users);

        textViewReportUsersTotal = (TextView) findViewById(R.id.tv_report_users_total);

        Button buttonShowReportUsers = (Button) findViewById(R.id.btn_report_users_title);

        final TableView<String[]> tableView = (TableView<String[]>) findViewById(R.id.table_view_report_users);

        tableView.setHeaderBackgroundColor(Color.parseColor("#ff6e40"));
        tableView.setHeaderAdapter(new SimpleTableHeaderAdapter(this, spaceProbeHeaders));
        tableView.setColumnCount(4);

        buttonShowReportUsers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                iLength = 0;

                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("users");

                databaseReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        final Long lngCountDataSnapshot = dataSnapshot.getChildrenCount();
                        sCountDataSnapshot = String.valueOf(lngCountDataSnapshot);

                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            ReportUsers reportUsers = snapshot.getValue(ReportUsers.class);
                            assert reportUsers != null;
                            String sUsername = reportUsers.username;
                            String sEmail = reportUsers.email;
                            String sPassword = reportUsers.password;
                            String sSignUpDate = reportUsers.sign_up_date;

                            arrayUser[iLength][0] = sUsername;
                            arrayUser[iLength][1] = sEmail;
                            arrayUser[iLength][2] = sPassword;
                            arrayUser[iLength][3] = sSignUpDate;
                            iLength = iLength + 1;
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });
                textViewReportUsersTotal.setText(sCountDataSnapshot);
                tableView.setDataAdapter(new SimpleTableDataAdapter(ReportUsersActivity.this, arrayUser));
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
