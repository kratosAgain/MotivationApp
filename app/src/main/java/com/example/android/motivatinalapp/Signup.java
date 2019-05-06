package com.example.android.motivatinalapp;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;


public class Signup extends Activity{
    DatabaseHelper db = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup_activity);
        db =new DatabaseHelper(this);
    }

    public void onSignup(View v)
    {
        if(v.getId()==R.id.button)
        {
            EditText name = (EditText)findViewById(R.id.nameid);

            EditText email = (EditText)findViewById(R.id.emailid);
            EditText username = (EditText)findViewById(R.id.usernameid);
            EditText password = (EditText)findViewById(R.id.passwordid);
            EditText cpassword = (EditText)findViewById(R.id.cpasswordid);

            String namestr =name.getText().toString();
            Log.d("NAME", namestr);
            String usernamestr =username.getText().toString();
            Log.d("userNAME", usernamestr);
            String emailstr =email.getText().toString();
            Log.d("emailstr", emailstr);
            String passstr =password.getText().toString();

            String cpassstr =cpassword.getText().toString();
            if(!passstr.equals(cpassstr))
            {
                Log.d("in IF", "in the if");
                Toast.makeText(this.getApplicationContext(), "Password do not match", Toast.LENGTH_SHORT).show();
            }
            else
            {
                //insert into database
                Contact c =new Contact();
                c.setNam(namestr);
                c.setEmail(emailstr);
                c.setPass(passstr);
                c.setUname(usernamestr);
                db.insertContact(c);
                currentUser.currentUserName = usernamestr;
                db.close();
            }
        }

    }

}
