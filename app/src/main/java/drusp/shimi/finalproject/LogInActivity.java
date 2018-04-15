package drusp.shimi.finalproject;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LogInActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText editTextEmail;
    private EditText editTextPassword;

    private Button buttonLogIn;
    private Button buttonToSignUp;

    private FirebaseAuth auth;

    private DatabaseReference databaseUser;

    private ProgressDialog progress;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        auth = FirebaseAuth.getInstance();

        databaseUser = FirebaseDatabase.getInstance().getReference().child("users");

        progress = new ProgressDialog(this);

        editTextEmail = (EditText) findViewById(R.id.et_log_in_email);
        editTextPassword = (EditText) findViewById(R.id.et_log_in_password);

        buttonLogIn = (Button) findViewById(R.id.btn_log_in_submit);
        buttonToSignUp = (Button) findViewById(R.id.btn_log_in_to_sign_up);

        buttonLogIn.setOnClickListener(this);
        buttonToSignUp.setOnClickListener(this);
    }

    private void toSignUp() {
        Intent toSignUpIntent = new Intent(getApplicationContext(), SignUpActivity.class);
        startActivity(toSignUpIntent);
    }

    private void showAlertDialogError(int i) {
        String msg = null;
        switch (i) {
            case 1:
                msg = getString(R.string.log_in_alert_case1);
                editTextEmailFocus();
                break;
            case 2:
                msg = getString(R.string.log_in_alert_case2);
                editTextEmailFocus();
                break;
            case 3:
                msg = getString(R.string.log_in_alert_case3);
                editTextPasswordFocus();
                break;
            case 4:
                msg = getString(R.string.log_in_alert_case4);
                editTextEmailFocus();
                break;
            case 5:
                msg = getString(R.string.log_in_alert_case5);
                editTextEmailFocus();
                break;
            case 6:
                msg = getString(R.string.log_in_alert_case6);
                editTextPasswordFocus();
                break;
        }

        new AlertDialog.Builder(LogInActivity.this, R.style.MyDialogTheme)
                .setIcon(R.drawable.app_icon)
                .setTitle(R.string.log_in_alert_title)
                .setMessage(msg)
                .setPositiveButton(R.string.log_in_alert_done, null)
                .setPositiveButton(R.string.log_in_alert_done, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();
    }

    private void checkLogin() {
        final String email = editTextEmail.getText().toString().trim();
        final String password = editTextPassword.getText().toString().trim();

        if (!email.isEmpty() && !password.isEmpty()) {
            if (email.length() < 10) {
                int i = 1;
                showAlertDialogError(i);
            } else if (email.length() >= 10) {
                if (!email.contains("@")) {
                    int i = 2;
                    showAlertDialogError(i);
                } else if (email.contains("@")) {
                    if ((password.length()) < 6 || (password.length() > 20)) {
                        int i = 3;
                        showAlertDialogError(i);
                    } else if ((password.length()) >= 6 || (password.length() <= 20)) {
                        String sProgress = getString(R.string.log_in_progress);
                        progress.setMessage(sProgress);
                        progress.show();
                        auth.signInWithEmailAndPassword(email, password)
                                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        progress.dismiss();

                                        if (task.isSuccessful()) {
                                            checkUser();
                                        } else {
                                            int i = 4;
                                            showAlertDialogError(i);
                                        }
                                    }
                                });
                    }
                }
            }
        } else if (email.isEmpty() || password.isEmpty()) {
            if (email.isEmpty()) {
                int i = 5;
                showAlertDialogError(i);
            } else if (password.isEmpty()) {
                int i = 6;
                showAlertDialogError(i);
            }
        }
    }

    private void checkUser() {
        if (auth.getCurrentUser() != null) {
            final String userId = auth.getCurrentUser().getUid();

            databaseUser.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.hasChild(userId)) {
                        Intent toMainIntent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(toMainIntent);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            });
        }
    }

    @Override
    public void onClick(View view) {
        if (view == buttonLogIn) {
            checkLogin();
        } else if (view == buttonToSignUp) {
            toSignUp();
        }
    }

    private void editTextEmailFocus() {
        editTextEmail.setBackgroundResource(R.drawable.border_focus);
        editTextEmail.requestFocus();
        editTextPassword.setBackgroundResource(R.drawable.border_non_focus);
    }

    private void editTextPasswordFocus() {
        editTextEmail.setBackgroundResource(R.drawable.border_non_focus);
        editTextPassword.setBackgroundResource(R.drawable.border_focus);
        editTextPassword.requestFocus();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

}
