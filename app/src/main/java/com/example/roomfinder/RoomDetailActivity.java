package com.example.roomfinder;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class RoomDetailActivity extends AppCompatActivity {
    public static final String EXTRA_ROOMID = "roomId";
    int roomId = 0;
    String intentRoomId;
    ImageView imageViewCall,
            imageViewRoom;
    TextView textInternet,
            textKitchen,
            textParking,
            textType,
            textPreferWithRooms,
            textLocation,
            textCall;
    File imagePath;
    SqliteDBHelper dbHelper;
    private String phoneNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SqliteDBHelper dbHelper = new SqliteDBHelper(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room_detail);

        setToolBarAndActionBar(); // Sets the toolbar and action bar in the layout
        getIncomingIntent(); //Receives the intent and its values
        setLayoutValues(); //Sets the layout values
    }

    private void setToolBarAndActionBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    private void getIncomingIntent() {
        if (getIntent().hasExtra(EXTRA_ROOMID)) {
            roomId = getIntent().getExtras().getInt(EXTRA_ROOMID);
            //            Toast.makeText(this, roomId, Toast.LENGTH_SHORT).show();
        } else {

            Toast.makeText(this, this.getString(R.string.noDescription), Toast.LENGTH_SHORT).show();
        }

    }

    private void setLayoutValues() {
        dbHelper = new SqliteDBHelper(this);
        final Room room = dbHelper.getRoom(roomId);

        phoneNumber = room.getOwnerNumber();

        imageViewRoom = (ImageView) findViewById(R.id.room_image);
        imageViewRoom.setImageBitmap(room.getImage());

        textInternet = (TextView) findViewById(R.id.textInternet);
        textInternet.setText(room.getInternet());

        textKitchen = (TextView) findViewById(R.id.textKitchen);
        textKitchen.setText(room.getKitchen());

        textType = (TextView) findViewById(R.id.textType);
        textType.setText(room.getType());

        textParking = (TextView) findViewById(R.id.textParking);
        textParking.setText(room.getParking());

        textLocation = (TextView) findViewById(R.id.textLocation);
        textLocation.setText(room.getLocation() + " | " + this.getString(R.string.postedOn) + "" + room.getPostDate());

        textPreferWithRooms = (TextView) findViewById(R.id.textPreferWithRooms);
        textPreferWithRooms.setText(room.getPreference() + " " + this.getString(R.string.preferred) + " | " + room.getRoom() + " " + this.getString(R.string.rooms) + " | " + this.getString(R.string.postedBy) + ":" + room.getOwnerName());

        imageViewCall = (ImageView) findViewById(R.id.textCall);

        imageViewCall.setOnClickListener(new View.OnClickListener() {@Override
        public void onClick(View view) {
            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse("tel:" + room.getOwnerNumber()));
            startActivity(intent);
        }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.description_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.share:
                Bitmap bitmap = takeScreenshot(); //takes the screenshot of current layout
                saveBitmap(bitmap); //Converts the screenshot into bitmap
                shareImage(); //finally create the intent to share the image through various sharing apps as whatsapp,drive ...
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    public Bitmap takeScreenshot() {
        View rootView = findViewById(android.R.id.content).getRootView();
        rootView.setDrawingCacheEnabled(true);
        return rootView.getDrawingCache();
    }

    public void saveBitmap(Bitmap bitmap) {
        imagePath = new File(Environment.getExternalStorageDirectory() + "/room_info.png");
        FileOutputStream fos;
        try {
            fos = new FileOutputStream(imagePath);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.flush();
            fos.close();
        } catch(FileNotFoundException e) {
            Log.e("GREC", e.getMessage(), e);
        } catch(IOException e) {
            Log.e("GREC", e.getMessage(), e);
        }
    }

    private void shareImage() {
        Uri uri = Uri.fromFile(imagePath);
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("image/*");
        String shareBody = this.getString(R.string.shareMessage) + phoneNumber;
        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, this.getString(R.string.roomInfo));
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
        sharingIntent.putExtra(Intent.EXTRA_STREAM, uri);

        startActivity(Intent.createChooser(sharingIntent, this.getString(R.string.shareVia)));
    }
}