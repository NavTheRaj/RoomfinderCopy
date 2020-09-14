package com.example.roomfinder;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

public class SqliteDBHelper extends SQLiteOpenHelper {
    Context context;
    private ByteArrayOutputStream byteArrayOutputStream;
    private byte[] imageinBytes;
    private static final String DB_NAME = "roomfinder"; // the name of our database
    SQLiteDatabase db;
    private static final int DB_VERSION = 7; // the version of the database
    String tblSQL = "CREATE TABLE ROOM (_id INTEGER PRIMARY KEY AUTOINCREMENT," +
            "NAME TEXT," +
            "PNUMBER TEXT," +
            "LOCATION TEXT," +
            "TYPE TEXT," +
            "PARKING TEXT," +
            "PREFERENCE TEXT," +
            "INTERNET TEXT," +
            "IMAGE BLOB," +
            "DATE TEXT," +
            "KITCHEN TEXT," +
            "ROOM TEXT" +
            ")";



    public SqliteDBHelper(Context context) {

        super(context, DB_NAME, null, DB_VERSION);
        this.context = context;

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            db.execSQL(tblSQL);
            Log.d("TABLESUCCESS", "TABLE CREATED SUCCESSFULLY");
        }catch(Exception e){
            Log.d("TABLEERROR", e.getMessage());
        }
    }


    //When the version of database in changed or upgraded this function works out
    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS FRIEND");
        onCreate(db);

    }


    //Function to insert a row of room information in the database
    public void insertRoom(Room room){
        try {
           SQLiteDatabase db = this.getWritableDatabase();

           //Converts the image into bytes array.
           Bitmap imageToStoreBitmap = room.getImage();
           byteArrayOutputStream = new ByteArrayOutputStream();
           imageToStoreBitmap.compress(Bitmap.CompressFormat.JPEG,100,byteArrayOutputStream);

           imageinBytes = byteArrayOutputStream.toByteArray();

           //Conversion ends here

           ContentValues cv = new ContentValues();
           cv.put("NAME", room.getOwnerName());
           cv.put("PNUMBER", room.getOwnerNumber());
           cv.put("LOCATION", room.getLocation());
           cv.put("TYPE", room.getType());
           cv.put("PARKING", room.getParking());
           cv.put("PREFERENCE", room.getPreference());
           cv.put("INTERNET", room.getInternet());
           cv.put("IMAGE", imageinBytes); //store the image in database in the form of bytes array
           cv.put("DATE", room.getPostDate());
           cv.put("KITCHEN", room.getKitchen());
           cv.put("ROOM", room.getRoom());

           long queryRunsCheck = db.insert("ROOM", null, cv);
           if(queryRunsCheck!=-1) {
               Toast.makeText(context,context.getString(R.string.dataSuccess),Toast.LENGTH_SHORT).show();
               db.close();
           }else{
               Toast.makeText(context,context.getString(R.string.failedData),Toast.LENGTH_SHORT).show();
           }

       }
       catch(Exception e){
           Log.d("INSERTERROR", e.getMessage());
       }
    }


    //Function to get all the room data in the form of arraylist
    public ArrayList<Room> getRoomsList(){
        ArrayList<Room> rooms=new ArrayList<>();
        Room d;
        try {
            SQLiteDatabase db = getReadableDatabase();
            Cursor c = db.rawQuery("SELECT * FROM ROOM", null);
            if (c.moveToFirst()) {// moves the current cursor to first record if exists
                do {

                    //converting byte array into bitmap.
                    byte[] imageBytes = c.getBlob(8);
                    Bitmap objectBitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);


                    d = new Room();
                    d.set_id(c.getInt(0));
                    d.setOwnerName(c.getString(1));
                    d.setOwnerNumber(c.getString(2));
                    d.setLocation(c.getString(3));
                    d.setType(c.getString(4));
                    d.setParking(c.getString(5));
                    d.setPreference(c.getString(6));
                    d.setInternet(c.getString(7));
                    d.setImage(objectBitmap);
                    d.setPostDate(c.getString(9));
                    d.setKitchen(c.getString(10));
                    d.setRoom(c.getString(11));

                    rooms.add(d);
                } while (c.moveToNext());
            }else{
                Toast.makeText(context,R.string.noRooms,Toast.LENGTH_SHORT).show();
            }

            db.close();
        }catch(Exception e){
            Log.d("GETERROR", e.getMessage());
        }
        return rooms;
    }



    public Room getRoom(int id){
        Room d=new Room();
        try {
            SQLiteDatabase db = getReadableDatabase();
            Cursor c = db.rawQuery("SELECT * FROM ROOM WHERE _id=" + id, null);
            if (c.moveToFirst()) {
                byte[] imageBytes = c.getBlob(8);
                Bitmap objectBitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
                d.set_id(c.getInt(0));
                d.setOwnerName(c.getString(1));
                d.setOwnerNumber(c.getString(2));
                d.setLocation(c.getString(3));
                d.setType(c.getString(4));
                d.setParking(c.getString(5));
                d.setPreference(c.getString(6));
                d.setInternet(c.getString(7));
                d.setImage(objectBitmap);
                d.setPostDate(c.getString(9));
                d.setKitchen(c.getString(10));
                d.setRoom(c.getString(11));
            }
            db.close();
        }catch(Exception e){
            Log.d("GETROOMDETAILDEBUG", e.getMessage());
        }
        return  d;
    }



    //Function to update the room in database
    public void updateRoom(Room room){
        Room d=new Room();
        try {
            SQLiteDatabase db = getReadableDatabase();
            ContentValues cv=new ContentValues();

            //Converts the image into bytes array.
            Bitmap imageToStoreBitmap = room.getImage();
            byteArrayOutputStream = new ByteArrayOutputStream();
            imageToStoreBitmap.compress(Bitmap.CompressFormat.JPEG,100,byteArrayOutputStream);

            imageinBytes = byteArrayOutputStream.toByteArray();

            cv.put("NAME", room.getOwnerName());
            cv.put("PNUMBER", room.getOwnerNumber());
            cv.put("LOCATION", room.getLocation());
            cv.put("TYPE", room.getType());
            cv.put("PARKING", room.getParking());
            cv.put("PREFERENCE", room.getPreference());
            cv.put("INTERNET", room.getInternet());
            cv.put("IMAGE", imageinBytes); //store the image in database in the form of bytes array
            cv.put("DATE", room.getPostDate());
            cv.put("KITCHEN", room.getKitchen());
            cv.put("ROOM", room.getRoom());

            db.update("FRIEND",cv,"_id=?",new String[]{room.get_id()+""});

            db.close();
        }catch(Exception e){
            Log.d("GETROOMDETAILDEBUG", e.getMessage());
        }
    }



    //Delete the room from database
    public void deleteRoom(int id){

        try {
            SQLiteDatabase db = getReadableDatabase();
            Cursor c = db.rawQuery("DELETE * FROM ROOM WHERE _id=" + id, null);
        }catch(Exception e){
            e.printStackTrace();
        }

    }






    //Database manager for mobile view of sqlite database

    public ArrayList<Cursor> getData(String Query){
        //get writable database
        SQLiteDatabase sqlDB = this.getWritableDatabase();
        String[] columns = new String[] { "message" };
        //an array list of cursor to save two cursors one has results from the query
        //other cursor stores error message if any errors are triggered
        ArrayList<Cursor> alc = new ArrayList<Cursor>(2);
        MatrixCursor Cursor2= new MatrixCursor(columns);
        alc.add(null);
        alc.add(null);

        try{
            String maxQuery = Query ;
            //execute the query results will be save in Cursor c
            Cursor c = sqlDB.rawQuery(maxQuery, null);

            //add value to cursor2
            Cursor2.addRow(new Object[] { "Success" });

            alc.set(1,Cursor2);
            if (null != c && c.getCount() > 0) {

                alc.set(0,c);
                c.moveToFirst();

                return alc ;
            }
            return alc;
        } catch(SQLException sqlEx){
            Log.d("printing exception", sqlEx.getMessage());
            //if any exceptions are triggered save the error message to cursor an return the arraylist
            Cursor2.addRow(new Object[] { ""+sqlEx.getMessage() });
            alc.set(1,Cursor2);
            return alc;
        } catch(Exception ex){
            Log.d("printing exception", ex.getMessage());

            //if any exceptions are triggered save the error message to cursor an return the arraylist
            Cursor2.addRow(new Object[] { ""+ex.getMessage() });
            alc.set(1,Cursor2);
            return alc;
        }
    }



}
