package com.example.track;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.ContactsContract;

import java.text.DateFormat;
import java.util.Date;

public class CordenadaDao {
    private Conexao con;
    private SQLiteDatabase db;

    public CordenadaDao(Context context){
        con = new Conexao(context);
        db = con.getWritableDatabase();
    }

    public long insert(Cordenadas_ll cordenadas_ll){
        ContentValues contentValues = new ContentValues();
        contentValues.put("data", cordenadas_ll.getData());
        contentValues.put("latitude", cordenadas_ll.getLatitude());
        contentValues.put("longitude", cordenadas_ll.getLongitude());

        return db.insert("cordenadas_ll",null, contentValues);
    }

    public Cursor find(){
        return db.rawQuery("SELECT * FROM cordenadas_ll",null);
    }

    public void deleteAll (){
        db.execSQL("Delete from cordenadas_ll");
        db.close();
    }

}
