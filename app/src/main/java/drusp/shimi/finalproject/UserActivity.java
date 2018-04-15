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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import drusp.shimi.finalproject.model.MainPoster;

public class UserActivity extends AppCompatActivity implements View.OnClickListener {

    private RecyclerView viewDog;

    private FirebaseAuth auth;
    private FirebaseAuth.AuthStateListener authStateListener;

    private DatabaseReference databaseReference;
    private DatabaseReference databaseDeleteUserPost;
    private DatabaseReference databaseDeleteMarker;

    private TextView textViewUsername;
    private TextView textViewEmail;
    private TextView textViewAlert;

    private Button buttonLost;
    private Button buttonFound;
    private Button buttonAdopt;

    private Query queryLost;
    private Query queryFound;
    private Query queryAdopt;
    private Query queryA;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        buttonLost = (Button) findViewById(R.id.btn_user_lost);
        buttonFound = (Button) findViewById(R.id.btn_user_found);
        buttonAdopt = (Button) findViewById(R.id.btn_user_adopt);

        textViewUsername = (TextView) findViewById(R.id.tv_user_username);
        textViewEmail = (TextView) findViewById(R.id.tv_user_email);
        textViewAlert = (TextView) findViewById(R.id.tv_user_alert);

        viewDog = (RecyclerView) findViewById(R.id.recycler_user_list);

        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.btm_nav_user);

        auth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = auth.getCurrentUser();

        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user == null) {
                    toScreen();
                } else {
                    String userId = auth.getCurrentUser().getUid();
                }
            }
        };

        assert currentUser != null;
        String sUid = currentUser.getUid();

        DatabaseReference databaseUser = FirebaseDatabase.getInstance().getReference().child("users").child(currentUser.getUid());
        databaseReference = FirebaseDatabase.getInstance().getReference().child("main_poster");
        databaseDeleteUserPost = FirebaseDatabase.getInstance().getReference().child("user_post").child(sUid);
        databaseDeleteMarker = FirebaseDatabase.getInstance().getReference().child("marker_location");

        queryLost = databaseDeleteUserPost.orderByChild("status").equalTo("หาย");
        queryFound = databaseDeleteUserPost.orderByChild("status").equalTo("พบ");
//        queryFound = databaseDeleteUserPost.orderByChild("timestamp_query").startAt("20170801").endAt("20171231");
        queryAdopt = databaseDeleteUserPost.orderByChild("status").equalTo("หาบ้าน");
//        queryA = databaseReference.orderByKey();

        databaseUser.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String sUsername = (String) dataSnapshot.child("username").getValue();
                String sEmail = (String) dataSnapshot.child("email").getValue();

                textViewUsername.setText(sUsername);
                textViewEmail.setText(sEmail);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        buttonLost.setOnClickListener(this);
        buttonFound.setOnClickListener(this);
        buttonAdopt.setOnClickListener(this);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);

        viewDog.setHasFixedSize(true);
        viewDog.setLayoutManager(layoutManager);

        BottomNavigationViewHelper.disableShiftMode(bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.menuMain:
                        toMainActivity();
                        break;
                    case R.id.menuMaps:
                        toMapsActivity();
                        break;
                    case R.id.menuPost:
                        showDialogPost();
                        break;
//                    case R.id.menuUser:
//                        break;
                    case R.id.menuLogOut:
                        showAlertDialogLogOut();
                        break;
                }
                return false;
            }
        });
    }

    private void showAlertDialogLogOut() {
        new AlertDialog.Builder(UserActivity.this, R.style.MyDialogTheme)
                .setIcon(R.drawable.app_icon)
                .setTitle(R.string.user_alert_title2)
                .setMessage(R.string.user_alert_msg2)
                .setPositiveButton(R.string.user_alert_done2, null)
                .setNegativeButton(R.string.user_alert_cancel2, null)
                .setPositiveButton(R.string.user_alert_done2, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        logout();
                    }
                })
                .setNegativeButton(R.string.user_alert_cancel2, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();
    }

    private void toScreen() {
        Intent toScreenIntent = new Intent(getApplicationContext(), ScreenActivity.class);
        toScreenIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(toScreenIntent);
    }

    private void toMainActivity() {
        Intent toMainIntent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(toMainIntent);
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

    private void searchLost() {
        FirebaseRecyclerAdapter<MainPoster, UserActivity.BlogViewHolder> firebaseRecyclerAdapter =
                new FirebaseRecyclerAdapter<MainPoster, UserActivity.BlogViewHolder>(
                        MainPoster.class,
                        R.layout.layout_search,
                        UserActivity.BlogViewHolder.class,
                        queryLost
                ) {
                    @Override
                    protected void populateViewHolder(BlogViewHolder viewHolder, MainPoster model, int position) {
                        final String postKey = getRef(position).getKey();

                        viewHolder.setDogImageUrl(getApplicationContext(), model.getDog_image_url());
                        viewHolder.setStatus(model.getStatus());
                        viewHolder.setStatusDate(model.getStatus());
                        viewHolder.setDate(model.getDate());

                        viewHolder.buttonDelete.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                new AlertDialog.Builder(UserActivity.this, R.style.MyDialogTheme)
                                        .setIcon(R.drawable.app_icon)
                                        .setTitle(R.string.user_alert_title)
                                        .setMessage(R.string.user_alert_msg)
                                        .setPositiveButton(R.string.user_alert_done, null)
                                        .setNegativeButton(R.string.user_alert_cancel, null)
                                        .setPositiveButton(R.string.user_alert_done, new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                databaseReference.child(postKey).addListenerForSingleValueEvent(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                                        databaseReference.child(postKey).removeValue();
                                                        databaseDeleteUserPost.child(postKey).removeValue();
                                                        String sMarkerID = (String) dataSnapshot.child("marker_id").getValue();
                                                        assert sMarkerID != null;
                                                        databaseDeleteMarker.child(sMarkerID).removeValue();
                                                    }

                                                    @Override
                                                    public void onCancelled(DatabaseError databaseError) {
                                                    }
                                                });
                                            }
                                        })
                                        .setNegativeButton(R.string.user_alert_cancel, new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.dismiss();
                                            }
                                        }).show();
                            }
                        });
                    }
                };
        viewDog.setAdapter(firebaseRecyclerAdapter);
    }

    private void searchFound() {
        FirebaseRecyclerAdapter<MainPoster, UserActivity.BlogViewHolder> firebaseRecyclerAdapter =
                new FirebaseRecyclerAdapter<MainPoster, UserActivity.BlogViewHolder>(
                        MainPoster.class,
                        R.layout.layout_search,
                        UserActivity.BlogViewHolder.class,
                        queryFound
                ) {
                    @Override
                    protected void populateViewHolder(BlogViewHolder viewHolder, MainPoster model, int position) {

                        final String postKey = getRef(position).getKey();

                        viewHolder.setDogImageUrl(getApplicationContext(), model.getDog_image_url());
                        viewHolder.setStatus(model.getStatus());
                        viewHolder.setStatusDate(model.getStatus());
                        viewHolder.setDate(model.getDate());

                        viewHolder.buttonDelete.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                new AlertDialog.Builder(UserActivity.this, R.style.MyDialogTheme)
                                        .setIcon(R.drawable.app_icon)
                                        .setTitle(R.string.user_alert_title)
                                        .setMessage(R.string.user_alert_msg)
                                        .setPositiveButton(R.string.user_alert_done, null)
                                        .setNegativeButton(R.string.user_alert_cancel, null)
                                        .setPositiveButton(R.string.user_alert_done, new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                databaseReference.child(postKey).addListenerForSingleValueEvent(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                                        databaseReference.child(postKey).removeValue();
                                                        databaseDeleteUserPost.child(postKey).removeValue();
                                                        String sMarkerID = (String) dataSnapshot.child("marker_id").getValue();
                                                        assert sMarkerID != null;
                                                        databaseDeleteMarker.child(sMarkerID).removeValue();
                                                    }

                                                    @Override
                                                    public void onCancelled(DatabaseError databaseError) {
                                                    }
                                                });
                                            }
                                        })
                                        .setNegativeButton(R.string.user_alert_cancel, new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.dismiss();
                                            }
                                        }).show();
                            }
                        });
                    }
                };
        viewDog.setAdapter(firebaseRecyclerAdapter);
    }

    private void searchAdopt() {
        FirebaseRecyclerAdapter<MainPoster, UserActivity.BlogViewHolder> firebaseRecyclerAdapter =
                new FirebaseRecyclerAdapter<MainPoster, UserActivity.BlogViewHolder>(
                        MainPoster.class,
                        R.layout.layout_search,
                        UserActivity.BlogViewHolder.class,
                        queryAdopt
                ) {
                    @Override
                    protected void populateViewHolder(BlogViewHolder viewHolder, MainPoster model, int position) {
                        final String postKey = getRef(position).getKey();

                        viewHolder.setDogImageUrl(getApplicationContext(), model.getDog_image_url());
                        viewHolder.setStatus(model.getStatus());
                        viewHolder.setStatusDate(model.getStatus());
                        viewHolder.setDate(model.getDate());

                        viewHolder.buttonDelete.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                new AlertDialog.Builder(UserActivity.this, R.style.MyDialogTheme)
                                        .setIcon(R.drawable.app_icon)
                                        .setTitle(R.string.user_alert_title)
                                        .setMessage(R.string.user_alert_msg)
                                        .setPositiveButton(R.string.user_alert_done, null)
                                        .setNegativeButton(R.string.user_alert_cancel, null)
                                        .setPositiveButton(R.string.user_alert_done, new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                databaseReference.child(postKey).addListenerForSingleValueEvent(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                                        databaseReference.child(postKey).removeValue();
                                                        databaseDeleteUserPost.child(postKey).removeValue();
                                                    }

                                                    @Override
                                                    public void onCancelled(DatabaseError databaseError) {
                                                    }
                                                });
                                            }
                                        })
                                        .setNegativeButton(R.string.user_alert_cancel, new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.dismiss();
                                            }
                                        }).show();
                            }
                        });
                    }
                };
        viewDog.setAdapter(firebaseRecyclerAdapter);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private void toMapsActivity() {
        Intent toMapsIntent = new Intent(getApplicationContext(), MapsActivity.class);
        startActivity(toMapsIntent);
    }

    private void logout() {
        auth.signOut();
    }

    @Override
    public void onClick(View view) {
        if (view == buttonLost) {
            textViewAlert.setText(R.string.user_alert_lost);
            viewDog.setAdapter(null);
            searchLost();
        } else if (view == buttonFound) {
            textViewAlert.setText(R.string.user_alert_found);
            viewDog.setAdapter(null);
            searchFound();
        } else if (view == buttonAdopt) {
            textViewAlert.setText(R.string.user_alert_adopt);
            viewDog.setAdapter(null);
            searchAdopt();
        }
    }

    public static class BlogViewHolder extends RecyclerView.ViewHolder {
        View view;
        FirebaseAuth auth;
        Button buttonDelete;

        public BlogViewHolder(View itemView) {
            super(itemView);
            view = itemView;
            auth = FirebaseAuth.getInstance();
            buttonDelete = (Button) view.findViewById(R.id.btn_search_delete);
        }

        void setStatus(String status) {
            TextView postStatus = (TextView) view.findViewById(R.id.tv_search_status);

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
            TextView postStatusDate = (TextView) view.findViewById(R.id.tv_search_status_date);
            postStatusDate.setText(status);
        }

        void setDate(String date) {
            TextView postDate = (TextView) view.findViewById(R.id.tv_search_date);
            postDate.setText(date);
        }

        void setDogImageUrl(Context context, String dogImageUrl) {
            ImageView postImage = (ImageView) view.findViewById(R.id.iv_search_dog_image);
            Picasso.with(context).load(dogImageUrl).into(postImage);
        }
    }

}
