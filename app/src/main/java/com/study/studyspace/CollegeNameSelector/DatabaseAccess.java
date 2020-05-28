package com.study.studyspace.CollegeNameSelector;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.study.studyspace.Colleges;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

public class DatabaseAccess {
    private SQLiteOpenHelper openHelper;
    private SQLiteDatabase db;
    private static DatabaseAccess instance;
    private static final String DB_TABLE = "table_image";
    private static final String KEY_NAME = "image_name";
    private static final String KEY_IMAGE = "image_data";
    private static final String CREATE_TABLE_IMAGE = "CREATE TABLE IF NOT EXISTS " + DB_TABLE + "("+
            KEY_NAME + " TEXT," +
            KEY_IMAGE + " BLOB);";

    Cursor c=null;

    private  DatabaseAccess(Context context){
        this.openHelper= new DatabaseOpenHelper(context);
    }
    public static DatabaseAccess getInstance(Context context)
    {
        if(instance==null)
        {
                instance = new DatabaseAccess(context);
        }
        return  instance;
    }

    public void open()
    {
        this.db = openHelper.getWritableDatabase();
        this.db.execSQL(CREATE_TABLE_IMAGE);
    }
    public void close(){
        if(db!=null)
        {
            this.db.close();
        }
    }
    public  String getAddress()
    {
        c=db.rawQuery("Select College from Colleges where ID = 1",new String[]{});
        StringBuffer buffer = new StringBuffer();
        while (c.moveToNext())
        {
            String college = c.getString(0);
            buffer.append( ""+college);
        }
        return  buffer.toString();
    }
    public void saveImage(String url , Bitmap map)
    {
        byte[] image = getBytes(map);
        ContentValues cv = new  ContentValues();
        cv.put(KEY_NAME,    url);
        cv.put(KEY_IMAGE,   image);
        db.insert( DB_TABLE, null, cv );
        Log.i("gzzz","data added");
    }
    public Bitmap getImageFromDB(String url)
    {
        c=db.rawQuery("Select image_data from "+DB_TABLE+" where image_name="+url,new String[]{});
        byte[] image=c.getBlob(1);
        return getImage(image);
    }
    public List<Colleges> getColleges()
    {
        c=db.rawQuery("Select ID,College from Colleges ",new String[]{});
        List<Colleges> result = new ArrayList<>();

        if(c.moveToFirst())
        {
            do{
                Colleges colleges= new Colleges(c.getInt(c.getColumnIndex("ID")),c.getString(c.getColumnIndex("College")));

                result.add(colleges);
            }while (c.moveToNext());
        }
        return result;
    }
    public static byte[] getBytes(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, stream);
        return stream.toByteArray();
    }
    public static Bitmap getImage(byte[] image) {
        return BitmapFactory.decodeByteArray(image, 0, image.length);
    }
}
