package com.amrita.gpms;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class Stu_login extends AppCompatActivity {
private EditText username,password;
private Button login;
private  boolean is=true;
private ProgressDialog progress;
public static String user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stu_login);
        username=(EditText)findViewById(R.id.et_username1);
        password=(EditText)findViewById(R.id.et_password1);
        login=(Button)findViewById(R.id.bt_login1);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                close();
                showdialog();
                Login();
            }
        });
    }
    private void Login()
    {
        String url="http://reddysaisumanth014.000webhostapp.com/login.php";
        RequestQueue requestQueue=Volley.newRequestQueue(Stu_login.this);
        StringRequest stringRequest=new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                    canceldialog();
                if(response.equals("success"))
                {   user=username.getText().toString().trim().toUpperCase();
                    username.setText(null);
                    password.setText(null);
                  //  Toast.makeText(getApplicationContext(),"Login successful!",Toast.LENGTH_SHORT).show();
                    openIntent();
                }
                else{
                    //Toast.makeText(getApplicationContext(),"Invalid username or password!",Toast.LENGTH_LONG).show();
                    Snackbar.make(findViewById(R.id.stu_login_lay),"Invalid username or password", Snackbar.LENGTH_LONG).setActionTextColor(getResources().getColor(android.R.color.holo_red_light)).show();

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                canceldialog();
                //Toast.makeText(getApplicationContext(),"error:"+error.toString(),Toast.LENGTH_LONG).show();
                Snackbar.make(findViewById(R.id.stu_login_lay),"Error:"+error, Snackbar.LENGTH_LONG).setActionTextColor(getResources().getColor(android.R.color.holo_red_light)).show();

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params=new HashMap<>();
                params.put("Username",username.getText().toString().trim());
                params.put("Password",password.getText().toString().trim());
                return params;
            }
        };
      /*  int socketTimeout = 50000; // 30 seconds. You can change it
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);

        stringRequest.setRetryPolicy(policy);*/
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(5000,2,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);
    }
    private void openIntent()
    {
        Intent intent;
        intent=new Intent(this,student_tab.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
    private  void  showdialog()
    {    progress = new ProgressDialog(this);

        progress.setTitle("Loading");
        progress.setMessage("Authenticating...");
        progress.setCancelable(false); // disable dismiss by tapping outside of the dialog
        progress.show();
    }
    private  void  canceldialog()
    {
        progress.dismiss();
    }

    @Override
    public void onBackPressed() {
        Intent intent;
        intent=new Intent(this,MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
    private void  close()
    {
        View view=this.getCurrentFocus();
        InputMethodManager i=(InputMethodManager)this.getSystemService(Context.INPUT_METHOD_SERVICE);
        i.hideSoftInputFromWindow(view.getWindowToken(),0);
    }
}
