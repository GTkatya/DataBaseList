package com.example.databaselist;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    Button bAdd, bDel, bClear, bRead, bUpd;
    EditText eID,eName, eEmail;
    DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bAdd = findViewById(R.id.bAdd);
        bAdd.setOnClickListener(this::onClick);

        bRead = findViewById(R.id.bRead);
        bRead.setOnClickListener(this::onClick);

        bClear = findViewById(R.id.bClear);
        bClear.setOnClickListener(this::onClick);

        bUpd = findViewById(R.id.bUpd);
        bUpd.setOnClickListener(this::onClick);

        bDel = findViewById(R.id.bDel);
        bDel.setOnClickListener(this::onClick);

        eID = findViewById(R.id.eID);
        eName = findViewById(R.id.eName);
        eEmail = findViewById(R.id.eEmail);

        dbHelper = new DBHelper(this);
    }

    @Override
    public void onClick(View v) {
        String ID = eID.getText().toString();
        String name = eName.getText().toString();
        String email = eEmail.getText().toString();

        SQLiteDatabase database = dbHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues(); //added new line in table

        switch (v.getId()){
            case R.id.bAdd:
                contentValues.put(DBHelper.KEY_NAME, name);
                contentValues.put(DBHelper.KEY_MAIL, email);
                database.insert(DBHelper.TABLE_PERSONS, null, contentValues);
                break;
            case R.id.bRead:
                Cursor cursor = database.query(DBHelper.TABLE_PERSONS, null,
                        null, null, null, null, null);
                if(cursor.moveToFirst())
                {
                    int idIndex = cursor.getColumnIndex(DBHelper.KEY_ID);
                    int nameIndex = cursor.getColumnIndex(DBHelper.KEY_NAME);
                    int emailIndex = cursor.getColumnIndex(DBHelper.KEY_MAIL);
                    do{
                        Log.d("mLog", "ID =" + cursor.getInt(idIndex)+
                                ", name = " + cursor.getString(nameIndex) +
                                ", email = " + cursor.getString(emailIndex));
                    } while (cursor.moveToNext());
                } else Log.d("mLog", "0 rows");
                cursor.close();
                break;
            case R.id.bClear:
                database.delete(DBHelper.TABLE_PERSONS, null, null);
                break;
            case R.id.bDel:
                if (ID.equalsIgnoreCase(" "))
                {
                    break;
                }
                int delCount = database.delete(DBHelper.TABLE_PERSONS, DBHelper.KEY_ID +
                        "=" + ID, null);
                Log.d("mLog", "was deleted line = " + delCount);
            case R.id.bUpd:
                if (ID.equalsIgnoreCase(" "))
                {
                    break;
                }
                contentValues.put(DBHelper.KEY_MAIL, email);
                contentValues.put(DBHelper.KEY_NAME, name);
                int updCount = database.update(DBHelper.TABLE_PERSONS, contentValues, DBHelper.KEY_ID + "= ?", new String[] {ID});
                Log.d("mLog", "Updated lines = " + updCount);

        }
        dbHelper.close(); //close our BD
    }
}