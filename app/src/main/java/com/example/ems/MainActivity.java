package com.example.ems;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    EditText id, name, email, phone;
    Button addEmployee, search, update, clear, showALL, delete, next;
    TextView errorMsg;
    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.main_layout);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        id = findViewById(R.id.editbox_empID);
        name = findViewById(R.id.editbox_empNAME);
        email = findViewById(R.id.editbox_empEMAIL);
        phone = findViewById(R.id.editbox_empPhone);
        addEmployee = findViewById(R.id.button_addRECORD);
        search = findViewById(R.id.button_search);
        update = findViewById(R.id.button_update);
        showALL = findViewById(R.id.button_showALL);
        delete = findViewById(R.id.button_delete);
        clear = findViewById(R.id.button_clear);
        errorMsg = findViewById(R.id.text_resultFAILbox);
        next = findViewById(R.id.button_next);

        db = openOrCreateDatabase("records.db", Context.MODE_PRIVATE, null);
        db.execSQL("create table if not exists employee(id text, name text, email text, phone text)");

        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                id.setText("");
                name.setText("");
                email.setText("");
                phone.setText("");
                errorMsg.setText("");
            }
        });

        addEmployee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String EmployeeID = id.getText().toString();
                String EmployeeName = name.getText().toString();
                String EmployeeEmail = email.getText().toString();
                String EmployeePhone = phone.getText().toString();

                if (TextUtils.isEmpty(EmployeeID) || TextUtils.isEmpty(EmployeeName) || TextUtils.isEmpty(EmployeeEmail) || TextUtils.isEmpty(EmployeePhone)) {
                    errorMsg.setText("Empty Fields");
                }
                else {
                    db.execSQL("insert into employee values('"+EmployeeID+"','"+EmployeeName+"','"+EmployeeEmail+"','"+EmployeePhone+"')");
                    errorMsg.setText("");
                    id.setText("");
                    name.setText("");
                    email.setText("");
                    phone.setText("");
                    Toast.makeText(MainActivity.this, "Success", Toast.LENGTH_SHORT).show();
                }
            }
        });

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String EmployeeID = id.getText().toString();
                Cursor c = db.rawQuery("select name, email, phone from employee where id='"+EmployeeID+"'",null);
                if(c.moveToNext()){
                    errorMsg.setText("");
                    name.setText(c.getString(0));
                    email.setText(c.getString(1));
                    phone.setText(c.getString(2));
                    Toast.makeText(MainActivity.this, "Success", Toast.LENGTH_SHORT).show();
                }
                else {
                    errorMsg.setText("No such entry");
                }
            }
        });

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String EmployeeID = id.getText().toString();
                String EmployeeEmail = email.getText().toString();
                String EmployeePhone = phone.getText().toString();

                if (TextUtils.isEmpty(EmployeeID)){
                    errorMsg.setText("Employee ID is empty");
                }
                else {
                    errorMsg.setText("");
                    if (!TextUtils.isEmpty(EmployeeEmail)){
                        db.execSQL("update employee set email='"+EmployeeEmail+"' where id='"+EmployeeID+"'");
                        Toast.makeText(MainActivity.this, "Success", Toast.LENGTH_SHORT).show();
                    }
                    if (!TextUtils.isEmpty(EmployeePhone)){
                        db.execSQL("update employee set phone='"+EmployeePhone+"' where id='"+EmployeeID+"'");
                        Toast.makeText(MainActivity.this, "Success", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        showALL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Cursor c = db.rawQuery("select id,name,email,phone from employee",null);
                StringBuffer br=new StringBuffer();
                while (c.moveToNext()){
                    br.append("ID: "+c.getString(0)+"\n");
                    br.append("Name: "+c.getString(1)+"\n");
                    br.append("eMAIL: "+c.getString(2)+"\n");
                    br.append("Phone: "+c.getString(3)+"\n\n");
                }
                showData("Employee Records",br.toString());
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String EmployeeID = id.getText().toString();
                if(TextUtils.isEmpty(EmployeeID)){
                    errorMsg.setText("Employee ID needed");
                }
                else {
                    errorMsg.setText("");
                    db.execSQL("delete from employee where id='"+EmployeeID+"'");
                    Toast.makeText(MainActivity.this, "Deleted", Toast.LENGTH_SHORT).show();
                }
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent nxt = new Intent(MainActivity.this, CourseActivity.class);
                startActivity(nxt);
            }
        });
    }
    public void showData(String title, String msg){
        AlertDialog.Builder ad = new AlertDialog.Builder(this);
        ad.setTitle(title);
        ad.setMessage(msg);
        ad.show();
    }
}