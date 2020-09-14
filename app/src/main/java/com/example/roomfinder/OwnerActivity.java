package com.example.roomfinder;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import org.w3c.dom.Text;

import java.io.File;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.security.acl.Owner;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class OwnerActivity extends AppCompatActivity {

    private ProgressBar progressBarSubmit;

    private TextInputEditText ownerName,
            number,
            location,
            roomNumber;

    private RadioGroup type,
            preference,
            internet,
            kitchen,
            parking;

    private RadioButton rbType,
            rbPreference,
            rbInternet,
            rbKitchen,
            rbParking;

    private Button btnBrowseImage,
            btnSubmit,
            btnReset;

    private TextView textImage;

    private String imageName;

    private Uri filepath;
    private Bitmap bitmap;

    private String textName,
            textNumber,
            textLocation,
            textRoomNumber;

    private String textType,
            textPreference,
            textInternet,
            textKitchen,
            textParking;

    String currentDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());

    private static final int IMAGE_REQUEST_CODE = 125;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_owner);

        Toolbar toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        getValuesFromLayout();

        btnSubmit.setOnClickListener(new View.OnClickListener() {@Override
        public void onClick(View view) {
            storeInputs();
        }
        });

        btnReset.setOnClickListener(new View.OnClickListener() {@Override
        public void onClick(View view) {
            resetInputs();
        }
        });
    }

    public void resetInputs() {
        ownerName.setText("");
        number.setText("");
        location.setText("");
        roomNumber.setText("");
        textImage.setText(this.getString(R.string.selectRoomImage));
        textImage.setTextColor(Color.BLACK);
        type.clearCheck();
        preference.clearCheck();
        internet.clearCheck();
        kitchen.clearCheck();
        parking.clearCheck();
    }

    public void getValuesFromLayout() {
        btnSubmit = (Button) findViewById(R.id.btnSubmit);
        btnReset = (Button) findViewById(R.id.btnReset);

        textImage = (TextView) findViewById(R.id.textImage);
        ownerName = (TextInputEditText) findViewById(R.id.textinput_ownername);
        number = (TextInputEditText) findViewById(R.id.textinput_ownernumber);
        location = (TextInputEditText) findViewById(R.id.textinput_ownerlocation);
        roomNumber = (TextInputEditText) findViewById(R.id.textinput_roomnumber);

        type = (RadioGroup) findViewById(R.id.type);
        preference = (RadioGroup) findViewById(R.id.preference);
        internet = (RadioGroup) findViewById(R.id.internet);
        kitchen = (RadioGroup) findViewById(R.id.kitchen);
        parking = (RadioGroup) findViewById(R.id.parking);
        //floor = (RadioGroup) findViewById(R.id.floor);
        //Progressbar
        //progressBarSubmit = (ProgressBar) findViewById(R.id.progressSubmit);
    }

    public void storeInputs() {
        textName = ownerName.getText().toString();
        textNumber = number.getText().toString();
        textLocation = location.getText().toString();
        textRoomNumber = roomNumber.getText().toString();

        //Getting the string value of selected radio buttons
        textType = getRadioGroupCheckedValue(type);
        textPreference = getRadioGroupCheckedValue(preference);
        textInternet = getRadioGroupCheckedValue(internet);
        textKitchen = getRadioGroupCheckedValue(kitchen);
        textParking = getRadioGroupCheckedValue(parking);

        //Research garni part
        if (textName.equals("")) {
            ownerName.setError(this.getString(R.string.nameEmpty));
        } else if (textNumber.equals("")) {
            number.setError(this.getString(R.string.numberEmpty));
        } else if (textLocation.equals("")) {
            location.setError(this.getString(R.string.locationEmpty));
        } else if (textRoomNumber.equals("")) {
            roomNumber.setError(this.getString(R.string.roomNumberEmpty));
        } else if (textType.equals("") || textPreference.equals("") || textInternet.equals("") || textKitchen.equals("") || textParking.equals("")) {
            Toast.makeText(this, this.getString(R.string.optionsEmpty), Toast.LENGTH_SHORT).show();
        } else if (filepath.equals("") || filepath.equals(null)) {

            textImage.setTextColor(Color.RED);
            Toast.makeText(this, this.getString(R.string.imageNotSelected), Toast.LENGTH_SHORT).show();
        } else {

            //SqliteDBHelper dbHelper = new SqliteDBHelper(this);
            Room room = new Room(textName, textNumber, textLocation, textType, textParking, textPreference, textInternet, bitmap, currentDate, textKitchen, textRoomNumber);
            //dbHelper.insertRoom(room);
            DataSubmitAsyncTask dataSubmitAsyncTask = new DataSubmitAsyncTask(this, room);
            dataSubmitAsyncTask.execute();
            resetInputs();
        }
    }

    public String getRadioGroupCheckedValue(RadioGroup radioGroup) {
        RadioButton radioButton = null;
        int selectedId = radioGroup.getCheckedRadioButtonId();
        radioButton = (RadioButton) findViewById(selectedId);
        String text = (String) radioButton.getText();
        return text;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.owner_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public void onClickBtnBrowse(View view) {
        Dexter.withActivity(OwnerActivity.this).withPermission(Manifest.permission.READ_EXTERNAL_STORAGE).withListener(new PermissionListener() {@Override
        public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");
            startActivityForResult(Intent.createChooser(intent, OwnerActivity.this.getString(R.string.selectRoomImage)), IMAGE_REQUEST_CODE);
        }

            @Override
            public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {

            }

            @Override
            public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                permissionToken.continuePermissionRequest();
            }
        }).check();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        try {
            super.onActivityResult(requestCode, resultCode, data);
            if (requestCode == IMAGE_REQUEST_CODE && resultCode == RESULT_OK && data != null && data.getData() != null) {

                filepath = data.getData();
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filepath);

                Toast.makeText(this, this.getString(R.string.imageUploded), Toast.LENGTH_SHORT).show();
                textImage.setTextColor(Color.parseColor("#DC46ED"));
                //String filename=path.substring(path.lastIndexOf("/")+1);
                //textImage.setText("Image name:" + getFileName());
                textImage.setText(this.getString(R.string.imageUploded));

            } else {
                textImage.setTextColor(Color.RED);
                Toast.makeText(this, this.getString(R.string.imageNotSelected), Toast.LENGTH_SHORT).show();
            }
        }
        catch(Exception e) {
            Log.d("IMAGE", e.getMessage());
        }

    }

    private static class DataSubmitAsyncTask extends AsyncTask < Void,
            Integer,
            String > {

        private WeakReference < OwnerActivity > activityWeakReference;
        private Room room;
        private ProgressDialog progressDialog;

        DataSubmitAsyncTask(OwnerActivity ownerActivity, Room room) {
            activityWeakReference = new WeakReference < OwnerActivity > (ownerActivity);
            this.room = room;
            progressDialog = new ProgressDialog(ownerActivity);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            OwnerActivity ownerActivity = activityWeakReference.get();

            if (ownerActivity == null || ownerActivity.isFinishing()) {
                return;
            }

            progressDialog.setTitle(ownerActivity.getString(R.string.submitNotification));
            progressDialog.setMessage(ownerActivity.getString(R.string.uploading));
            progressDialog.show();

        }

        @Override
        protected String doInBackground(Void...params) {

            OwnerActivity ownerActivity = activityWeakReference.get();

            if (ownerActivity == null || ownerActivity.isFinishing()) {
                return "Error";
            }
            SqliteDBHelper dbHelper = new SqliteDBHelper(ownerActivity);
            dbHelper.insertRoom(room);

            try {
                Thread.sleep(1000);
            } catch(Exception e) {
                e.printStackTrace();
            }
            return ownerActivity.getString(R.string.submitSuccess);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            OwnerActivity ownerActivity = activityWeakReference.get();

            if (ownerActivity == null || ownerActivity.isFinishing()) {
                return;
            }
            if (progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
            Toast.makeText(ownerActivity, s, Toast.LENGTH_SHORT).show();
        }

    }

}