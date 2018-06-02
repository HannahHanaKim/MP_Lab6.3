package com.example.hannahkim.lab6_3;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    TextView textName, textNum;
    EditText editName, editNum;
    Button addBtn, deleteBtn;
    ListView listView;

    SQLiteDatabase db;
    MySQLiteOpenHelper helper;

    String[] students;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textName = (TextView)findViewById(R.id.textName);
        textNum = (TextView)findViewById(R.id.textNum);
        editName = (EditText)findViewById(R.id.editName);
        editNum = (EditText)findViewById(R.id.editNum);
        addBtn = (Button)findViewById(R.id.addBtn);
        deleteBtn = (Button)findViewById(R.id.deleteBtn);
        listView = (ListView)findViewById(R.id.listView);

        helper = new MySQLiteOpenHelper(MainActivity.this, "student.db", null, 1);

        View.OnClickListener buttonListener = new View.OnClickListener() {
            public void onClick(View view) {

                switch(view.getId()) {
                    case R.id.addBtn:
                        String name = editName.getText().toString();
                        String studentNo = editNum.getText().toString();

                        if(name.equals("") || studentNo.equals(""))
                            Toast.makeText(getApplicationContext(), "모든 항목을 입력해주세요.", Toast.LENGTH_LONG).show();
                        else {
                            insert(name, studentNo);
                            invalidate();
                        }
                        break;

                    case R.id.deleteBtn: //when delete button is clicked
                        String delName = editName.getText().toString();
                        String delNum = editNum.getText().toString();
                        if(delName.equals(""))
                            Toast.makeText(getApplicationContext(), "이름을 입력해주세요.", Toast.LENGTH_LONG).show();
                        else
                            delete(delName);
                        invalidate();
                        break;

                    default:
                        break;
                }
            }
        };

        addBtn.setOnClickListener(buttonListener);
        deleteBtn.setOnClickListener(buttonListener);
    }

    public void insert(String name, String studentNo) { //add data into DB
        db = helper.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("name", name);
        values.put("studentNo", studentNo);
        db.insert("student", null, values);
    }

    public void delete(String name) { //delete with name
        db = helper.getWritableDatabase();
        db.delete("student", "name=?", new String[] {name});

    }

    public void select() { //show all the data
        db = helper.getReadableDatabase();

        Cursor c = db.query("student", null, null, null, null, null, null);

        students = new String[c.getCount()];
        int count = 0;

        while(c.moveToNext()) {
            students[count] = c.getString(c.getColumnIndex("name"))
                    + " " + c.getString(c.getColumnIndex("studentNo"));
            count++;
        }
        c.close();
    }

    private void invalidate() {
        select();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, students);
        listView.setAdapter(adapter);
    }
}
