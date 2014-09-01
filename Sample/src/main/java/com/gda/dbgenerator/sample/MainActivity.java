package com.gda.dbgenerator.sample;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import java.util.List;

public class MainActivity extends Activity {

  private EditText etId, etFname, etLname, etAge;

  private Button btInsert, btQuery;

  private TextView tvResult;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    etId = (EditText) findViewById(R.id.id);
    etFname = (EditText) findViewById(R.id.firstname);
    etLname = (EditText) findViewById(R.id.lastname);
    etAge = (EditText) findViewById(R.id.age);

    tvResult = (TextView) findViewById(R.id.result);

    btInsert = (Button) findViewById(R.id.insert);
    btQuery = (Button) findViewById(R.id.query);

    btInsert.setOnClickListener(new View.OnClickListener() {

      @Override public void onClick(View view) {
        Contact contact = new Contact();

        try {
          contact.setId(Integer.parseInt(etId.getText().toString()));
          contact.setFirstName(etFname.getText().toString());
          contact.setLastName(etLname.getText().toString());
          contact.setAge(Integer.parseInt(etAge.getText().toString()));
          ContactDAO.getInstance(getApplicationContext()).insert(contact);
        } catch (Exception e) {
          tvResult.setText(e.getMessage());
        }
      }
    });

    btQuery.setOnClickListener(new View.OnClickListener() {

      @Override public void onClick(View view) {

        ContactDAO.getInstance(getApplicationContext())
            .queryAll(ContactTable.COLUMN_ID, new DAOListener<List<Contact>>() {

              @Override public void onFinish(List<Contact> contacts) {
                tvResult.setText(contacts.toString());
              }
            });
      }
    });
  }
}
