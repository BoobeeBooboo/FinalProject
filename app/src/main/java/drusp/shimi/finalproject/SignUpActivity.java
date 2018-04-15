package drusp.shimi.finalproject;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText editTextUsername;
    private EditText editTextEmail;
    private EditText editTextPassword;

    private Button buttonSignUp;

    private FirebaseAuth auth;

    private DatabaseReference databaseRefUsers;

    private ProgressDialog progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        auth = FirebaseAuth.getInstance();

        databaseRefUsers = FirebaseDatabase.getInstance().getReference().child("users");

        progress = new ProgressDialog(this);

        editTextUsername = (EditText) findViewById(R.id.et_sign_up_username);
        editTextEmail = (EditText) findViewById(R.id.et_sign_up_email);
        editTextPassword = (EditText) findViewById(R.id.et_sign_up_password);

        buttonSignUp = (Button) findViewById(R.id.btn_sign_up_submit);

        buttonSignUp.setOnClickListener(this);

        showAlertDialogSignUp();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private void showAlertDialogSignUp() {
        new AlertDialog.Builder(SignUpActivity.this, R.style.MyDialogTheme)
                .setIcon(R.drawable.app_icon)
                .setTitle(R.string.sign_up_alert_title)
                .setMessage(R.string.sign_up_msg)
                .setPositiveButton(R.string.sign_up_alert_done, null)
                .setPositiveButton(R.string.sign_up_alert_done, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();
    }

    private void showAlertDialogError(int i) {
        String msg = null;
        switch (i) {
            case 1:
                msg = getString(R.string.sign_up_alert_case1);
                editTextUsernameFocus();
                break;
            case 2:
                msg = getString(R.string.sign_up_alert_case2);
                editTextEmailFocus();
                break;
            case 3:
                msg = getString(R.string.sign_up_alert_case3);
                editTextEmailFocus();
                break;
            case 4:
                msg = getString(R.string.sign_up_alert_case4);
                editTextPasswordFocus();
                break;
            case 5:
                msg = getString(R.string.sign_up_alert_case5);
                editTextUsernameFocus();
                break;
            case 6:
                msg = getString(R.string.sign_up_alert_case6);
                editTextUsernameFocus();
                break;
            case 7:
                msg = getString(R.string.sign_up_alert_case7);
                editTextEmailFocus();
                break;
            case 8:
                msg = getString(R.string.sign_up_alert_case8);
                editTextPasswordFocus();
                break;
        }

        new AlertDialog.Builder(SignUpActivity.this, R.style.MyDialogTheme)
                .setIcon(R.drawable.app_icon)
                .setTitle(R.string.sign_up_alert_title)
                .setMessage(msg)
                .setPositiveButton(R.string.sign_up_alert_done, null)
                .setPositiveButton(R.string.sign_up_alert_done, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();
    }

    private void startRegister() {
        final String username = editTextUsername.getText().toString().trim();
        final String email = editTextEmail.getText().toString().trim();
        final String password = editTextPassword.getText().toString().trim();

        if (!TextUtils.isEmpty(username) && !TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)) {
            if ((username.length() < 4) || (username.length() > 20)) {
                int i = 1;
                showAlertDialogError(i);
            } else if ((username.length() >= 4) || (username.length() <= 20)) {
                if (email.length() < 10) {
                    int i = 2;
                    showAlertDialogError(i);
                } else if (email.length() >= 10) {
                    if (!email.contains("@")) {
                        int i = 3;
                        showAlertDialogError(i);
                    } else if (email.contains("@")) {
                        if ((password.length()) < 6 || (password.length() > 20)) {
                            int i = 4;
                            showAlertDialogError(i);
                        } else if ((password.length()) >= 6 || (password.length() <= 20)) {
                            String sProgress = getString(R.string.sign_up_progress);
                            progress.setMessage(sProgress);
                            progress.show();

                            auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        String userId = auth.getCurrentUser().getUid();

                                        Calendar calendar = Calendar.getInstance();
                                        SimpleDateFormat mdformat = new SimpleDateFormat("dd/MM/yyyy");
                                        String strDate = mdformat.format(calendar.getTime());

                                        DatabaseReference registerUser = databaseRefUsers.child(userId);

                                        registerUser.child("username").setValue(username);
                                        registerUser.child("email").setValue(email);
                                        registerUser.child("password").setValue(password);
                                        registerUser.child("sign_up_date").setValue(strDate);

                                        progress.dismiss();

                                        Intent toMainIntent = new Intent(getApplicationContext(), MainActivity.class);
                                        startActivity(toMainIntent);
                                    } else {
                                        int i = 5;
                                        showAlertDialogError(i);
                                    }
                                }
                            });
                        }
                    }
                }
            }
        } else if (TextUtils.isEmpty(username) || TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
            if (TextUtils.isEmpty(username)) {
                int i = 6;
                showAlertDialogError(i);
            } else if (TextUtils.isEmpty(email)) {
                int i = 7;
                showAlertDialogError(i);
            } else if (TextUtils.isEmpty(password)) {
                int i = 8;
                showAlertDialogError(i);
            }
        }
    }

    @Override
    public void onClick(View v) {
        if (v == buttonSignUp) {
            startRegister();
        }
    }

    private void editTextUsernameFocus() {
        editTextUsername.setBackgroundResource(R.drawable.border_focus);
        editTextUsername.requestFocus();
        editTextEmail.setBackgroundResource(R.drawable.border_non_focus);
        editTextPassword.setBackgroundResource(R.drawable.border_non_focus);
    }

    private void editTextEmailFocus() {
        editTextUsername.setBackgroundResource(R.drawable.border_non_focus);
        editTextEmail.setBackgroundResource(R.drawable.border_focus);
        editTextEmail.requestFocus();
        editTextPassword.setBackgroundResource(R.drawable.border_non_focus);
    }

    private void editTextPasswordFocus() {
        editTextUsername.setBackgroundResource(R.drawable.border_non_focus);
        editTextEmail.setBackgroundResource(R.drawable.border_non_focus);
        editTextPassword.setBackgroundResource(R.drawable.border_focus);
        editTextPassword.requestFocus();
    }

}
