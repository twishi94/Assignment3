package com.example.cozoo.memo;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.view.View.OnClickListener;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.View;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    EditText ed1,ed2,ed3,edRollNo,edName,edSem;
    Button b1,b2,b3,b4,b5,b6,addb,deleteb,updateb,viewb,viewAllb;
    TextView te1,te2,te3;
    SQLiteDatabase db;
    public static final String Prefrence = "Prefname";
    String data;
    private String file = "Data";
    SharedPreferences sharedPref;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Intialising controls

        ed1 = (EditText) findViewById(R.id.edit1);
        ed2 = (EditText) findViewById(R.id.edit2);
        ed3 = (EditText) findViewById(R.id.edit3);
        edRollNo = (EditText) findViewById(R.id.Roll);
        edName = (EditText) findViewById(R.id.name);
        edSem = (EditText) findViewById(R.id.sem);

        te1 = (TextView) findViewById(R.id.text1);
        te2 = (TextView) findViewById(R.id.text2);
        te3 = (TextView) findViewById(R.id.text3);

        b1 = (Button) findViewById(R.id.button1);
        b2 = (Button) findViewById(R.id.button2);
        b3 = (Button) findViewById(R.id.button3);
        b4 = (Button) findViewById(R.id.button4);
        b5 = (Button) findViewById(R.id.button5);
        b6 = (Button) findViewById(R.id.button6);
        addb = (Button) findViewById(R.id.adb);
        deleteb = (Button) findViewById(R.id.delb);
        updateb = (Button) findViewById(R.id.upb);
        viewb = (Button) findViewById(R.id.vb);
        viewAllb = (Button) findViewById(R.id.vab);

        sharedPref = getSharedPreferences(Prefrence, 0);

        //Registering event handlers
        addb.setOnClickListener(this);
        deleteb.setOnClickListener(this);
        updateb.setOnClickListener(this);
        viewb.setOnClickListener(this);
        viewAllb.setOnClickListener(this);

        //Creating database and table
        db = openOrCreateDatabase("StudentDB", getApplicationContext().MODE_PRIVATE, null);
        db.execSQL("CREATE TABLE IF NOT EXISTS student(rollno VARCHAR NOT NULL,name VARCHAR,sem VARCHAR,PRIMARY KEY(rollno));");

        //Shared Preference
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = ed1.getText().toString();
                sharedPref = getSharedPreferences(Prefrence, 0);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString("UserName", name);
                editor.commit();
                Toast.makeText(MainActivity.this, "Text Saved", Toast.LENGTH_LONG).show();
            }
        });

        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sharedPref = getSharedPreferences(Prefrence, 0);
                String user = sharedPref.getString("UserName", "NA");
                te1.setText(user);

            }
        });

        //Internal Storage
        b3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                data = ed2.getText().toString();
                try {
                    FileOutputStream fileOut = openFileOutput(file, MODE_PRIVATE);
                    fileOut.write(data.getBytes());
                    fileOut.close();
                    Toast.makeText(getBaseContext(), "Saved", Toast.LENGTH_SHORT).show();

                } catch (Exception e) {

                    e.printStackTrace();
                }


            }
        });

        b4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    FileInputStream fileIn = openFileInput(file);
                    int i;
                    String temp = "";

                    while ((i = fileIn.read()) != -1) {
                        temp = temp + Character.toString((char) i);
                    }
                    te2.setText(temp);
                    Toast.makeText(MainActivity.this, "File read", Toast.LENGTH_LONG).show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    //SQLite
    public void onClick(View view)
    {
        if(view == addb)
        {
            if(edRollNo.getText().toString().trim().length()==0||
                    edName.getText().toString().trim().length()==0||
                    edSem.getText().toString().trim().length()==0)
            {
                showMessage("Error", "Please enter all values");
                return;
            }
            db.execSQL("INSERT INTO student VALUES('"+edRollNo.getText()+"','"+edName.getText()+"','"+edSem.getText()+"');");
            showMessage("Success", "Record added");
            clearText();
        }

        if(view == deleteb)
        {
            if(edRollNo.getText().toString().trim().length()==0)
            {
                showMessage("Error", "Please enter the Rollno");
                return;
            }
            Cursor c = db.rawQuery("SELECT * FROM student WHERE rollno='"+edRollNo.getText()+"'",null);
            if(c.moveToFirst())
            {
                db.execSQL("DELETE FROM student WHERE rollno='"+edRollNo.getText()+"'");
                showMessage("Sucess","Record deleted");
            }
            else
            {
                showMessage("Error","Invalid rollno");
            }
            clearText();
        }

        if(view == updateb)
        {
            if(edRollNo.getText().toString().trim().length()==0)
            {
                showMessage("Error", "Please enter the Rollno");
                return;
            }
            Cursor c=db.rawQuery("SELECT * FROM student WHERE rollno='"+edRollNo.getText()+"'", null);
            if(c.moveToFirst())
            {
                db.execSQL("UPDATE student SET name='"+edName.getText()+"',sem='"+edSem.getText()+
                        "' WHERE rollno='"+edRollNo.getText()+"'");
                showMessage("Success", "Record Modified");
            }
            else
            {
                showMessage("Error", "Invalid Rollno");
            }
            clearText();
        }

        if(view == viewb)
        {
            if(edRollNo.getText().toString().trim().length()==0)
            {
                showMessage("Error", "Please enter the Rollno");
                return;
            }
            Cursor c=db.rawQuery("SELECT * FROM student WHERE rollno='"+edRollNo.getText()+"'", null);
            if(c.moveToFirst())
            {
                edName.setText(c.getString(1));
                edSem.setText(c.getString(2));
            }
            else
            {
                showMessage("Error", "Invalid Rollno");
                clearText();
            }
        }

        if(view == viewAllb){
            Cursor c=db.rawQuery("SELECT * FROM student", null);
            if(c.getCount()==0)
            {
                showMessage("Error", "No records found");
                return;
            }
            StringBuffer buffer=new StringBuffer();
            while(c.moveToNext())
            {
                buffer.append("Rollno: "+c.getString(0)+"\n");
                buffer.append("Name: "+c.getString(1)+"\n");
                buffer.append("Sem: "+c.getString(2)+"\n\n");
            }
            showMessage("Student Details", buffer.toString());
        }
    }
    public void showMessage(String title,String message)
    {
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.show();
    }
    public void clearText()
    {
        edRollNo.setText("");
        edName.setText("");
        edSem.setText("");
        edRollNo.requestFocus();
    }

    public void writeToExt(View view)  //write to external storage
    {

        FileOutputStream fileOutputStream = null;
        String state = Environment.getExternalStorageState();
        if(isExternalStorageWritable())
        {

            File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS),"twishi.txt");
            String message = ed3.getText().toString();
            Toast.makeText(getApplicationContext(),message,Toast.LENGTH_LONG).show();

            try {
                fileOutputStream = openFileOutput("twishi.txt",MODE_PRIVATE);
                fileOutputStream.write(message.getBytes());
                fileOutputStream.close();
                //ed3.setText("");
            } catch (FileNotFoundException e) {
                Toast.makeText(getApplicationContext(),"5",Toast.LENGTH_LONG).show();
                e.printStackTrace();
            } catch (IOException e) {
                Toast.makeText(getApplicationContext(),"6",Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }


        }
        else
        {
            Toast.makeText(getApplicationContext(),"No SD Card",Toast.LENGTH_LONG).show();
        }


    }

    public void readFromEx(View view) //read from external storage
    {
        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS),"twishi.txt");
        String message;
        try {
            FileInputStream fileInputStream = openFileInput("twishi.txt");
            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            StringBuffer stringBuffer = new StringBuffer();
            while((message=bufferedReader.readLine()) != null)
            {
                stringBuffer.append(message+"\n");
            }
            te3.setText(stringBuffer.toString());
        }catch(FileNotFoundException e){
            Toast.makeText(getApplicationContext(),"5",Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
        catch(IOException e){
            Toast.makeText(getApplicationContext(),"6",Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }


    }

    public boolean isExternalStorageWritable(){
        String state = Environment.getExternalStorageState();
        if(Environment.MEDIA_MOUNTED.equals(state)){
            return true;
        }
        return false;
    }

}
