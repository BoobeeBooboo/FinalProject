package drusp.shimi.finalproject;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import drusp.shimi.finalproject.model.MainPoster;

public class MainActivity extends AppCompatActivity {

    private RecyclerView viewDog;

    private DatabaseReference databaseMainPoster;
    private DatabaseReference databaseUser;

    private FirebaseAuth auth;
    private FirebaseAuth.AuthStateListener authStateListener;

    private String userId;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.btm_nav_main);

        viewDog = (RecyclerView) findViewById(R.id.recycler_main_list);

        auth = FirebaseAuth.getInstance();

        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user == null) {
                    toScreen();
                } else {
                    userId = auth.getCurrentUser().getUid();
                }
            }
        };

        databaseMainPoster = FirebaseDatabase.getInstance().getReference().child("main_poster");
        databaseUser = FirebaseDatabase.getInstance().getReference().child("users");

        databaseUser.keepSynced(true);
        databaseMainPoster.keepSynced(true);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
//        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        viewDog.setHasFixedSize(true);
        viewDog.setLayoutManager(layoutManager);

        BottomNavigationViewHelper.disableShiftMode(bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
//                    case R.id.menuMain:
//                        break;
                    case R.id.menuMaps:
                        toMapsActivity();
                        break;
                    case R.id.menuPost:
                        showDialogPost();
                        break;
                    case R.id.menuUser:
                        toUserActivity();
                        break;
                    case R.id.menuLogOut:
                        showAlertDialogLogOut();
//                        showDialogReport();
                        break;
                }
                return false;
            }
        });
    }

    @Override
    public void onBackPressed() {
        showAlertDialogLogOut();
    }

    private void toMapsActivity() {
        Intent toMapsIntent = new Intent(getApplicationContext(), MapsActivity.class);
        startActivity(toMapsIntent);
    }

    private void toScreen() {
        Intent toScreenIntent = new Intent(getApplicationContext(), ScreenActivity.class);
        toScreenIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(toScreenIntent);
    }

    private void toUserActivity() {
        Intent toUserIntent = new Intent(getApplicationContext(), UserActivity.class);
        startActivity(toUserIntent);
    }

    private void toLostDogActivity() {
        Intent toLostDogInputIntent = new Intent(getApplicationContext(), LostDogInputActivity.class);
        startActivity(toLostDogInputIntent);
    }

    private void toFoundDogActivity() {
        Intent toFoundDogInputIntent = new Intent(getApplicationContext(), FoundDogInputActivity.class);
        startActivity(toFoundDogInputIntent);
    }

    private void toAdoptDogActivity() {
        Intent toAdoptDogInputIntent = new Intent(getApplicationContext(), AdoptDogInputActivity.class);
        startActivity(toAdoptDogInputIntent);
    }

    @Override
    protected void onStart() {
        super.onStart();
        auth.addAuthStateListener(authStateListener);
        checkUserExist();

        FirebaseRecyclerAdapter<MainPoster, BlogViewHolder> firebaseRecyclerAdapter =
                new FirebaseRecyclerAdapter<MainPoster, BlogViewHolder>(
                        MainPoster.class,
                        R.layout.layout_main_poster,
                        BlogViewHolder.class,
                        databaseMainPoster
                ) {
                    @Override
                    protected void populateViewHolder(final BlogViewHolder viewHolder, MainPoster model, int position) {
                        final String postKey = getRef(position).getKey();
                        final String sStatus = model.getStatus();

                        viewHolder.setDogImageUrl(getApplicationContext(), model.getDog_image_url());
                        viewHolder.setStatus(model.getStatus());
                        viewHolder.setStatusDate(model.getStatus());
                        viewHolder.setDate(model.getDate());
                        viewHolder.setPhoneNumber(model.getPhone_number());

                        viewHolder.view.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                switch (sStatus) {
                                    case "หาย":
                                        Intent toLostDogPosterIntent = new Intent(MainActivity.this, LostDogPosterActivity.class);
                                        toLostDogPosterIntent.putExtra("postKey", postKey);
                                        startActivity(toLostDogPosterIntent);
                                        break;
                                    case "พบ":
                                        Intent toFoundDogPosterIntent = new Intent(MainActivity.this, FoundDogPosterActivity.class);
                                        toFoundDogPosterIntent.putExtra("postKey", postKey);
                                        startActivity(toFoundDogPosterIntent);
                                        break;
                                    case "หาบ้าน":
                                        Intent toAdoptDogPosterIntent = new Intent(MainActivity.this, AdoptDogPosterActivity.class);
                                        toAdoptDogPosterIntent.putExtra("postKey", postKey);
                                        startActivity(toAdoptDogPosterIntent);
                                        break;
                                }
                            }
                        });
                    }
                };
        viewDog.setAdapter(firebaseRecyclerAdapter);
    }

    private void showAlertDialogLogOut() {
        new AlertDialog.Builder(MainActivity.this, R.style.MyDialogTheme)
                .setIcon(R.drawable.app_icon)
                .setTitle(R.string.main_alert_title)
                .setMessage(R.string.main_alert_msg)
                .setPositiveButton(R.string.main_alert_done, null)
                .setNegativeButton(R.string.main_alert_cancel, null)
                .setPositiveButton(R.string.main_alert_done, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        logout();
                    }
                })
                .setNegativeButton(R.string.main_alert_cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();
    }

    private void logout() {
        auth.signOut();
    }

    private void checkUserExist() {
        databaseUser.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (!dataSnapshot.hasChild(userId)) {
                    Intent toScreenIntent = new Intent(MainActivity.this, ScreenActivity.class);
                    startActivity(toScreenIntent);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    private void showDialogPost() {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.layout_dialog_status);
        dialog.findViewById(R.id.btn_dialog_status_lost)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        toLostDogActivity();
                    }
                });
        dialog.findViewById(R.id.btn_dialog_status_found)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        toFoundDogActivity();
                    }
                });
        dialog.findViewById(R.id.btn_dialog_status_adopt)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        toAdoptDogActivity();
                    }
                });
        dialog.show();
    }

    private void toReportUsersActivity() {
        Intent toReportUsersIntent = new Intent(getApplicationContext(), ReportUsersActivity.class);
        startActivity(toReportUsersIntent);
    }

    private void toReportAdoptActivity() {
        Intent toReportAdoptIntent = new Intent(getApplicationContext(), ReportAdoptDogActivity.class);
        startActivity(toReportAdoptIntent);
    }

    private void toReportAllDogActivity() {
        Intent toReportAllDogIntent = new Intent(getApplicationContext(), ReportAllDogActivity.class);
        startActivity(toReportAllDogIntent);
    }

    private void toReportLostDogActivity() {
        Intent toReportLostDogIntent = new Intent(getApplicationContext(), ReportLostDogActivity.class);
        startActivity(toReportLostDogIntent);
    }

    private void toReportFoundDogActivity() {
        Intent toReportFoundDogIntent = new Intent(getApplicationContext(), ReportFoundDogActivity.class);
        startActivity(toReportFoundDogIntent);
    }

    private void toReportDateActivity() {
        Intent toReportDateIntent = new Intent(getApplicationContext(), ReportDateActivity.class);
        startActivity(toReportDateIntent);
    }

    private void showDialogReport() {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.layout_report);
        dialog.findViewById(R.id.btn_report_users)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        toReportUsersActivity();
                        toReportDateActivity();
                    }
                });
        dialog.findViewById(R.id.btn_report_all)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        toReportAllDogActivity();
                    }
                });
        dialog.findViewById(R.id.btn_report_lost)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        toReportLostDogActivity();
                    }
                });
        dialog.findViewById(R.id.btn_report_found)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        toReportFoundDogActivity();
                    }
                });
        dialog.findViewById(R.id.btn_report_adopt)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        toReportAdoptActivity();
                    }
                });
        dialog.show();
    }

    public static class BlogViewHolder extends RecyclerView.ViewHolder {
        View view;
        FirebaseAuth auth;

        public BlogViewHolder(View itemView) {
            super(itemView);
            view = itemView;
            auth = FirebaseAuth.getInstance();
        }

        void setStatus(String status) {
            TextView postStatus = (TextView) view.findViewById(R.id.tv_main_poster_status);

            switch (status) {
                case "หาย":
                    postStatus.setText(status);
                    postStatus.setTextColor(Color.parseColor("#ff6e40"));
                    break;
                case "พบ":
                    postStatus.setText(status);
                    postStatus.setTextColor(Color.parseColor("#4fc3f7"));
                    break;
                case "หาบ้าน":
                    postStatus.setText(status);
                    postStatus.setTextColor(Color.parseColor("#00e676"));
                    break;
            }
        }

        void setStatusDate(String status) {
            TextView postStatusDate = (TextView) view.findViewById(R.id.tv_main_poster_status_date);
            postStatusDate.setText(status);
        }

        void setDate(String date) {
            TextView postDate = (TextView) view.findViewById(R.id.tv_main_poster_date);
            postDate.setText(date);
        }

        void setPhoneNumber(String phone_number) {
            TextView postPhone = (TextView) view.findViewById(R.id.tv_main_poster_phone);
            postPhone.setText(phone_number);
        }

        void setDogImageUrl(Context context, String dog_image_url) {
            ImageView postImage = (ImageView) view.findViewById(R.id.iv_main_poster_dog_image);
            Picasso.with(context).load(dog_image_url).into(postImage);
        }
    }

}