package drusp.shimi.finalproject;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ScreenActivity extends AppCompatActivity implements View.OnClickListener {

    private FirebaseAuth auth;

    private DatabaseReference databaseUser;

    private Button buttonToApp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screen);

        buttonToApp = (Button) findViewById(R.id.btn_screen_submit);

        auth = FirebaseAuth.getInstance();

        databaseUser = FirebaseDatabase.getInstance().getReference().child("users");

        buttonToApp.setOnClickListener(this);
    }

    private void checkUserExist() {
        if (auth.getCurrentUser() != null) {
            final String userId = auth.getCurrentUser().getUid();

            databaseUser.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.hasChild(userId)) {
                        Intent toMain = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(toMain);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            });
        } else if (auth.getCurrentUser() == null) {
            Intent toLog = new Intent(getApplicationContext(), LogInActivity.class);
            startActivity(toLog);
        }
    }

    @Override
    public void onClick(View view) {
        if (view == buttonToApp) {
            checkUserExist();
        }
    }

    @Override
    public void onBackPressed() {
        finish();
    }

}
