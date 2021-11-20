package com.example.track;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class Conexao extends SQLiteOpenHelper {
    private static final String nome = "DB_CORDENADAS";
    private static final int versao = 1;

    public Conexao(Context context){
        super(context, nome, null, versao);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase){
        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS cordenadas_ll(latitude Float, data String, longitude Float)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i , int il){
        String sql = "";
        switch (i){
            case 1:
                sql = "ALTER TABLE cordenadas_ll ADD COLUMN data String;";
                sqLiteDatabase.execSQL(sql);
        }
    }
}
