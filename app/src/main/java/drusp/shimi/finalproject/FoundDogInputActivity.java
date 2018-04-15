package drusp.shimi.finalproject;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Calendar;

public class FoundDogInputActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int PICK_IMAGE_REQUEST = 200;

    private String mSelectedBreed;
    private String mSelectedGender;
    private String mSelectedLocation1;
    private String mSelectedDistrict;
    private String mSelectedCollar;

    private ImageView imageViewDogPhoto;

    private EditText editTextLocation2;
    private EditText editTextPhone;
    private EditText editTextFacebook;
    private EditText editTextLine;
    private EditText editTextBackupPhone;

    private Button buttonChoosePhoto;
    private Button buttonSelectBreed;
    private Button buttonSelectGender;
    private Button buttonSelectlocation1;
    private Button buttonSelectDistrict;
    private Button buttonSelectFoundDate;
    private Button buttonSelectCollar;

    private FloatingActionButton fabSubmit;

    private DatePickerDialog datePickerDialog;

    private Uri uri;

    private Bitmap bm;

    private String encodeTobase64(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, 0, baos);
        byte[] b = baos.toByteArray();
        String imageEncoded = Base64.encodeToString(b, Base64.DEFAULT);
        Log.d("Image Log:", imageEncoded);
        return imageEncoded;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_found_dog_input);

        buttonChoosePhoto = (Button) findViewById(R.id.btn_found_dog_input_choose);
        buttonSelectBreed = (Button) findViewById(R.id.btn_found_dog_input_breed);
        buttonSelectGender = (Button) findViewById(R.id.btn_found_dog_input_gender);
        buttonSelectlocation1 = (Button) findViewById(R.id.btn_found_dog_input_location1);
        buttonSelectDistrict = (Button) findViewById(R.id.btn_found_dog_input_district);
        buttonSelectFoundDate = (Button) findViewById(R.id.btn_found_dog_input_found_date);
        buttonSelectCollar = (Button) findViewById(R.id.btn_found_dog_input_collar);

        imageViewDogPhoto = (ImageView) findViewById(R.id.iv_found_dog_input_photo);

        editTextLocation2 = (EditText) findViewById(R.id.et_found_dog_input_location2);
        editTextPhone = (EditText) findViewById(R.id.et_found_dog_input_phone);
        editTextFacebook = (EditText) findViewById(R.id.et_found_dog_input_facebook);
        editTextLine = (EditText) findViewById(R.id.et_found_dog_input_line);
        editTextBackupPhone = (EditText) findViewById(R.id.et_found_dog_input_backup_phone);

        fabSubmit = (FloatingActionButton) findViewById(R.id.fab_found_dog_input_submit);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_found_dog_input);

        buttonChoosePhoto.setOnClickListener(this);
        buttonSelectBreed.setOnClickListener(this);
        buttonSelectGender.setOnClickListener(this);
        buttonSelectlocation1.setOnClickListener(this);
        buttonSelectDistrict.setOnClickListener(this);
        buttonSelectFoundDate.setOnClickListener(this);
        buttonSelectCollar.setOnClickListener(this);

        fabSubmit.setOnClickListener(this);

        buttonSelectFoundDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR); // current year
                int mMonth = c.get(Calendar.MONTH); // current month
                int mDay = c.get(Calendar.DAY_OF_MONTH); // current day

                datePickerDialog = new DatePickerDialog(FoundDogInputActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @SuppressLint("SetTextI18n")
                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                int month = monthOfYear + 1;
                                String formattedMonth = "" + month;
                                String formattedDayOfMonth = "" + dayOfMonth;

                                if (month < 10) {
                                    formattedMonth = "0" + month;
                                }
                                if (dayOfMonth < 10) {
                                    formattedDayOfMonth = "0" + dayOfMonth;
                                }
                                buttonSelectFoundDate.setText(formattedDayOfMonth + "/"
                                        + formattedMonth + "/" + year);
                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
                buttonSelectFoundDate.setTextColor(Color.parseColor("#000000"));
            }
        });
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_white_arrow));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void showAlertDialogSubmit() {
        new AlertDialog.Builder(FoundDogInputActivity.this, R.style.MyDialogTheme)
                .setIcon(R.drawable.app_icon)
                .setTitle(R.string.found_dog_input_alert_title)
                .setMessage(R.string.found_dog_input_alert_msg)
                .setPositiveButton(R.string.found_dog_input_alert_done, null)
                .setNegativeButton(R.string.found_dog_input_alert_cancel, null)
                .setPositiveButton(R.string.found_dog_input_alert_done, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        sendData();
                    }
                })
                .setNegativeButton(R.string.found_dog_input_alert_cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();
    }

    public void showDialogBreed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(FoundDogInputActivity.this, R.style.MyDialogTheme);
        builder.setTitle(R.string.found_dog_input_alert_breed);

        final String[] items = getResources().getStringArray(R.array.breedTH);

        builder.setSingleChoiceItems(items, 0,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mSelectedBreed = items[which];
                    }
                });
        String positiveText = getString(R.string.found_dog_input_alert_done);
        builder.setPositiveButton(positiveText,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        buttonSelectBreed.setText(mSelectedBreed);
                        buttonSelectBreed.setTextColor(Color.parseColor("#000000"));
                        dialog.dismiss();
                    }
                });
        String negativeText = getString(R.string.found_dog_input_alert_cancel);
        builder.setNegativeButton(negativeText,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        AlertDialog dialog = builder.create();
        // display dialog
        dialog.show();
    }

    public void showDialogGender() {
        AlertDialog.Builder builder = new AlertDialog.Builder(FoundDogInputActivity.this, R.style.MyDialogTheme);
        builder.setTitle(R.string.found_dog_input_alert_gender);

        final String[] items = getResources().getStringArray(R.array.gender);

        builder.setSingleChoiceItems(items, 0,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mSelectedGender = items[which];

                    }
                });
        String positiveText = getString(R.string.found_dog_input_alert_done);
        builder.setPositiveButton(positiveText,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        buttonSelectGender.setText(mSelectedGender);
                        buttonSelectGender.setTextColor(Color.parseColor("#000000"));
                        dialog.dismiss();
                    }
                });
        String negativeText = getString(R.string.found_dog_input_alert_cancel);
        builder.setNegativeButton(negativeText,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        AlertDialog dialog = builder.create();
        // display dialog
        dialog.show();
    }

    public void showDialogLocation1() {
        AlertDialog.Builder builder = new AlertDialog.Builder(FoundDogInputActivity.this, R.style.MyDialogTheme);
        builder.setTitle(R.string.found_dog_input_alert_location1);

        final String[] items = getResources().getStringArray(R.array.location);

        builder.setSingleChoiceItems(items, 0,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mSelectedLocation1 = items[which];

                    }
                });
        String positiveText = getString(R.string.found_dog_input_alert_done);
        builder.setPositiveButton(positiveText,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        buttonSelectlocation1.setText(mSelectedLocation1);
                        buttonSelectlocation1.setTextColor(Color.parseColor("#000000"));
                        dialog.dismiss();
                    }
                });
        String negativeText = getString(R.string.found_dog_input_alert_cancel);
        builder.setNegativeButton(negativeText,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        AlertDialog dialog = builder.create();
        // display dialog
        dialog.show();
    }

    public void showDialogDistrict() {
        AlertDialog.Builder builder = new AlertDialog.Builder(FoundDogInputActivity.this, R.style.MyDialogTheme);
        builder.setTitle(R.string.found_dog_input_alert_district);

        final String[] items = getResources().getStringArray(R.array.district);

        builder.setSingleChoiceItems(items, 0,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mSelectedDistrict = items[which];

                    }
                });
        String positiveText = getString(R.string.found_dog_input_alert_done);
        builder.setPositiveButton(positiveText,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        buttonSelectDistrict.setText(mSelectedDistrict);
                        buttonSelectDistrict.setTextColor(Color.parseColor("#000000"));
                        dialog.dismiss();
                    }
                });
        String negativeText = getString(R.string.found_dog_input_alert_cancel);
        builder.setNegativeButton(negativeText,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        AlertDialog dialog = builder.create();
        // display dialog
        dialog.show();
    }

    public void showDialogCollar() {
        AlertDialog.Builder builder = new AlertDialog.Builder(FoundDogInputActivity.this, R.style.MyDialogTheme);
        builder.setTitle(R.string.found_dog_input_alert_collar);

        final String[] items = getResources().getStringArray(R.array.collar);

        builder.setSingleChoiceItems(items, 0,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mSelectedCollar = items[which];

                    }
                });
        String positiveText = getString(R.string.found_dog_input_alert_done);
        builder.setPositiveButton(positiveText,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        buttonSelectCollar.setText(mSelectedCollar);
                        buttonSelectCollar.setTextColor(Color.parseColor("#000000"));
                        dialog.dismiss();
                    }
                });
        String negativeText = getString(R.string.found_dog_input_alert_cancel);
        builder.setNegativeButton(negativeText,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        AlertDialog dialog = builder.create();
        // display dialog
        dialog.show();
    }

    @SuppressLint("SetTextI18n")
    private void showAlertDialogError(int iCase) {
        String msg = null;
        switch (iCase) {
            case 1:
                msg = getString(R.string.found_dog_input_alert_case1);
                buttonChoosePhoto.requestFocus();
                break;
            case 2:
                msg = getString(R.string.found_dog_input_alert_case2);
                String xBreed = buttonSelectBreed.getText().toString().trim();
                buttonSelectBreed.setText("*" + " " + xBreed);
                buttonSelectBreed.setTextColor(Color.parseColor("#d50000"));
                buttonSelectBreed.setFocusable(true);
                buttonSelectBreed.setFocusableInTouchMode(true);///add this line
                buttonSelectBreed.requestFocus();
                break;
            case 3:
                msg = getString(R.string.found_dog_input_alert_case3);
                buttonSelectBreed.setTextColor(Color.parseColor("#d50000"));
                buttonSelectBreed.setFocusable(true);
                buttonSelectBreed.setFocusableInTouchMode(true);///add this line
                buttonSelectBreed.requestFocus();
                break;
            case 4:
                msg = getString(R.string.found_dog_input_alert_case4);
                String xGender = buttonSelectGender.getText().toString().trim();
                buttonSelectGender.setText("*" + " " + xGender);
                buttonSelectGender.setTextColor(Color.parseColor("#d50000"));
                buttonSelectGender.setFocusable(true);
                buttonSelectGender.setFocusableInTouchMode(true);///add this line
                buttonSelectGender.requestFocus();
                break;
            case 5:
                msg = getString(R.string.found_dog_input_alert_case5);
                buttonSelectGender.setTextColor(Color.parseColor("#d50000"));
                buttonSelectGender.setFocusable(true);
                buttonSelectGender.setFocusableInTouchMode(true);///add this line
                buttonSelectGender.requestFocus();
                break;
            case 6:
                msg = getString(R.string.found_dog_input_alert_case6);
                String xLocation1 = buttonSelectlocation1.getText().toString().trim();
                buttonSelectlocation1.setText("*" + " " + xLocation1);
                buttonSelectlocation1.setTextColor(Color.parseColor("#d50000"));
                buttonSelectlocation1.setFocusable(true);
                buttonSelectlocation1.setFocusableInTouchMode(true);///add this line
                buttonSelectlocation1.requestFocus();
                break;
            case 7:
                msg = getString(R.string.found_dog_input_alert_case7);
                buttonSelectlocation1.setTextColor(Color.parseColor("#d50000"));
                buttonSelectlocation1.setFocusable(true);
                buttonSelectlocation1.setFocusableInTouchMode(true);///add this line
                buttonSelectlocation1.requestFocus();
                break;
            case 8:
                msg = getString(R.string.found_dog_input_alert_case8);
                editTextLocation2.setTextColor(Color.parseColor("#000000"));
                editTextLocation2.requestFocus();
                break;
            case 9:
                msg = getString(R.string.found_dog_input_alert_case9);
                String xDistrict = buttonSelectDistrict.getText().toString().trim();
                buttonSelectDistrict.setText("*" + " " + xDistrict);
                buttonSelectDistrict.setTextColor(Color.parseColor("#d50000"));
                buttonSelectDistrict.setFocusable(true);
                buttonSelectDistrict.setFocusableInTouchMode(true);///add this line
                buttonSelectDistrict.requestFocus();
                break;
            case 10:
                msg = getString(R.string.found_dog_input_alert_case10);
                buttonSelectDistrict.setTextColor(Color.parseColor("#d50000"));
                buttonSelectDistrict.setFocusable(true);
                buttonSelectDistrict.setFocusableInTouchMode(true);///add this line
                buttonSelectDistrict.requestFocus();
                break;
            case 11:
                msg = getString(R.string.found_dog_input_alert_case11);
                String xFound_date = buttonSelectFoundDate.getText().toString().trim();
                buttonSelectFoundDate.setText("*" + " " + xFound_date);
                buttonSelectFoundDate.setTextColor(Color.parseColor("#d50000"));
                buttonSelectFoundDate.setFocusable(true);
                buttonSelectFoundDate.setFocusableInTouchMode(true);///add this line
                buttonSelectFoundDate.requestFocus();
                break;
            case 12:
                msg = getString(R.string.found_dog_input_alert_case12);
                buttonSelectFoundDate.setTextColor(Color.parseColor("#d50000"));
                buttonSelectFoundDate.setFocusable(true);
                buttonSelectFoundDate.setFocusableInTouchMode(true);///add this line
                buttonSelectFoundDate.requestFocus();
                break;
            case 13:
                msg = getString(R.string.found_dog_input_alert_case13);
                editTextPhone.setTextColor(Color.parseColor("#000000"));
                editTextPhone.requestFocus();
                break;
            case 14:
                msg = getString(R.string.found_dog_input_alert_case14);
                String xCollar = buttonSelectCollar.getText().toString().trim();
                buttonSelectCollar.setText("*" + " " + xCollar);
                buttonSelectCollar.setTextColor(Color.parseColor("#d50000"));
                buttonSelectCollar.setFocusable(true);
                buttonSelectCollar.setFocusableInTouchMode(true);///add this line
                buttonSelectCollar.requestFocus();
                break;
            case 15:
                msg = getString(R.string.found_dog_input_alert_case15);
                buttonSelectCollar.setTextColor(Color.parseColor("#d50000"));
                buttonSelectCollar.setFocusable(true);
                buttonSelectCollar.setFocusableInTouchMode(true);///add this line
                buttonSelectCollar.requestFocus();
                break;
            case 16:
                msg = getString(R.string.found_dog_input_alert_case16);
                editTextLocation2.setTextColor(Color.parseColor("#000000"));
                editTextLocation2.requestFocus();
                break;
            case 17:
                msg = getString(R.string.found_dog_input_alert_case17);
                editTextPhone.setTextColor(Color.parseColor("#000000"));
                editTextPhone.requestFocus();
                break;
        }

        new AlertDialog.Builder(FoundDogInputActivity.this, R.style.MyDialogTheme)
                .setIcon(R.drawable.app_icon)
                .setTitle(R.string.found_dog_input_alert_title)
                .setMessage(msg)
                .setPositiveButton(R.string.found_dog_input_alert_done, null)
                .setPositiveButton(R.string.found_dog_input_alert_done, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();
    }

    private void showFileChooser() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(Intent.createChooser(intent, "เลือกรูปภาพ"), PICK_IMAGE_REQUEST);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            uri = data.getData();
            try {
                bm = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                imageViewDogPhoto.setImageBitmap(bm);
                encodeTobase64(bm);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void submit() {
        final String breed = buttonSelectBreed.getText().toString().trim();
        final String gender = buttonSelectGender.getText().toString().trim();
        final String location1 = buttonSelectlocation1.getText().toString().trim();
        final String location2 = editTextLocation2.getText().toString().trim();
        final String district = buttonSelectDistrict.getText().toString().trim();
        final String found_date = buttonSelectFoundDate.getText().toString().trim();
        final String phone = editTextPhone.getText().toString().trim();
        final String collar = buttonSelectCollar.getText().toString().trim();
        final String sBreed = getString(R.string.found_dog_input_breed);
        final String sBreed2 = getString(R.string.found_dog_input_breed2);
        final String sGender = getString(R.string.found_dog_input_gender);
        final String sGender2 = getString(R.string.found_dog_input_gender2);
        final String sLocation1 = getString(R.string.found_dog_input_location1);
        final String sLocation1_2 = getString(R.string.found_dog_input_location1_2);
        final String sDistrict = getString(R.string.found_dog_input_district);
        final String sDistrict2 = getString(R.string.found_dog_input_district2);
        final String sFoundDate = getString(R.string.found_dog_input_found_date);
        final String sFoundDate2 = getString(R.string.found_dog_input_found_date2);
        final String sCollar = getString(R.string.found_dog_input_collar);
        final String sCollar2 = getString(R.string.found_dog_input_collar2);

        Integer iCase;
        if (imageViewDogPhoto.getDrawable() != null && !breed.equals(sBreed) && !breed.equals(sBreed2) && !gender.equals(sGender)
                && !gender.equals(sGender2) && !location1.equals(sLocation1) && !location1.equals(sLocation1_2) && !TextUtils.isEmpty(location2)
                && !district.equals(sDistrict) && !district.equals(sDistrict2) && !found_date.equals(sFoundDate)
                && !found_date.equals(sFoundDate2) && !TextUtils.isEmpty(phone) && !collar.equals(sCollar) && !collar.equals(sCollar2)) {
            if (location2.length() < 5 || (location2.length() > 13)) {
                iCase = 16;
                showAlertDialogError(iCase);
            } else if (location2.length() >= 5 || (location2.length() <= 13)) {
                if (phone.length() < 9 || (phone.length() > 10)) {
                    iCase = 17;
                    showAlertDialogError(iCase);
                } else if (phone.length() >= 9 || (phone.length() <= 10)) {
                    showAlertDialogSubmit();
                }
            }
        } else if (imageViewDogPhoto.getDrawable() == null || breed.equals(sBreed) || breed.equals(sBreed2) || gender.equals(sGender)
                || gender.equals(sGender2) || location1.equals(sLocation1) || location1.equals(sLocation1_2) || location2.isEmpty()
                || district.equals(sDistrict) || district.equals(sDistrict2) || found_date.equals(sFoundDate)
                || found_date.equals(sFoundDate2) || phone.isEmpty() || collar.equals(sCollar) || collar.equals(sCollar2)) {
            if (imageViewDogPhoto.getDrawable() == null) {
                iCase = 1;
                showAlertDialogError(iCase);
            } else if (breed.equals(sBreed)) {
                iCase = 2;
                showAlertDialogError(iCase);
            } else if (breed.equals(sBreed2)) {
                iCase = 3;
                showAlertDialogError(iCase);
            } else if (gender.equals(sGender)) {
                iCase = 4;
                showAlertDialogError(iCase);
            } else if (gender.equals(sGender2)) {
                iCase = 5;
                showAlertDialogError(iCase);
            } else if (location1.equals(sLocation1)) {
                iCase = 6;
                showAlertDialogError(iCase);
            } else if (location1.equals(sLocation1_2)) {
                iCase = 7;
                showAlertDialogError(iCase);
            } else if (location2.isEmpty()) {
                iCase = 8;
                showAlertDialogError(iCase);
            } else if (district.equals(sDistrict)) {
                iCase = 9;
                showAlertDialogError(iCase);
            } else if (district.equals(sDistrict2)) {
                iCase = 10;
                showAlertDialogError(iCase);
            } else if (found_date.equals(sFoundDate)) {
                iCase = 11;
                showAlertDialogError(iCase);
            } else if (found_date.equals(sFoundDate2)) {
                iCase = 12;
                showAlertDialogError(iCase);
            } else if (phone.isEmpty()) {
                iCase = 13;
                showAlertDialogError(iCase);
            } else if (collar.equals(sCollar)) {
                iCase = 14;
                showAlertDialogError(iCase);
            } else if (collar.equals(sCollar2)) {
                iCase = 15;
                showAlertDialogError(iCase);
            }
        }
    }

    private void sendData() {
        final String sUriFDogPhoto = uri.toString();

        SharedPreferences sharedPref = getSharedPreferences("foundDogInput", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("foundDogInputDogPhoto", encodeTobase64(bm));
        editor.putString("foundDogInpuDogFilepath", sUriFDogPhoto);
        editor.putString("foundDogInputLocation1", buttonSelectlocation1.getText().toString().trim());
        editor.putString("foundDogInputLocation2", editTextLocation2.getText().toString().trim());
        editor.putString("foundDogInputBreed", buttonSelectBreed.getText().toString().trim());
        editor.putString("foundDogInputDistrict", buttonSelectDistrict.getText().toString().trim());
        editor.putString("foundDogInputGender", buttonSelectGender.getText().toString().trim());
        editor.putString("foundDogInputCollar", buttonSelectCollar.getText().toString().trim());
        editor.putString("foundDogInputFoundDate", buttonSelectFoundDate.getText().toString().trim());
        editor.putString("foundDogInputPhone", editTextPhone.getText().toString().trim());
        editor.putString("foundDogInputBackupPhone", editTextBackupPhone.getText().toString().trim());
        editor.putString("foundDogInputFacebook", editTextFacebook.getText().toString().trim());
        editor.putString("foundDogInputLine", editTextLine.getText().toString().trim());
        editor.apply();
        Intent i = new Intent(getApplicationContext(), FoundDogLocationActivity.class);
        startActivity(i);
    }

    @Override
    public void onClick(View view) {
        if (view == buttonChoosePhoto) {
            showFileChooser();
        } else if (view == fabSubmit) {
            submit();
        } else if (view == buttonSelectBreed) {
            showDialogBreed();
        } else if (view == buttonSelectGender) {
            showDialogGender();
        } else if (view == buttonSelectlocation1) {
            showDialogLocation1();
        } else if (view == buttonSelectDistrict) {
            showDialogDistrict();
        } else if (view == buttonSelectCollar) {
            showDialogCollar();
        }
    }

}