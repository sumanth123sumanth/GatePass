package com.amrita.gpms;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.amrita.gpms.Stu_login.user;


public class Tab1_request extends Fragment implements DatePickerDialog.OnDateSetListener {
 private EditText wid,outdate,indate,reason;
   private Button submit,outdate_pick,indate_pick;
    private ProgressDialog progress,progress1;
   private String w,r,sysdate;
   private Date Indate,Outdate,date,tdate;
   private static boolean limit_exe=false;
   private String s,a;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tab1_request, container, false);
        return rootView;
    }
    @Override
    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
        Calendar c=Calendar.getInstance();
        c.set(i,i1,i2);
    }
    private void showoutdate()
    {
        DatePickerFragment date = new DatePickerFragment();
        /**
         * Set Up Current Date Into dialog
         */
        Calendar calender = Calendar.getInstance();
        Bundle args = new Bundle();
        args.putInt("year", calender.get(Calendar.YEAR));
        args.putInt("month", calender.get(Calendar.MONTH));
        args.putInt("day", calender.get(Calendar.DAY_OF_MONTH));
        date.setArguments(args);
        /**
         * Set Call back to capture selected date
         */
        date.setCallBack(ondate);
        date.show(getFragmentManager(), "Date Picker");
    }

    DatePickerDialog.OnDateSetListener ondate = new DatePickerDialog.OnDateSetListener() {

        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {

            outdate.setText(String.valueOf(year) + "-" + String.valueOf(monthOfYear+1)
                    + "-" + String.valueOf(dayOfMonth));
        }
    };
    private void showindate()
    {

        DatePickerFragment date = new DatePickerFragment();
        /**
         * Set Up Current Date Into dialog
         */
        Calendar calender = Calendar.getInstance();
        Bundle args = new Bundle();
        args.putInt("year", calender.get(Calendar.YEAR));
        args.putInt("month", calender.get(Calendar.MONTH));
        args.putInt("day", calender.get(Calendar.DAY_OF_MONTH));
        date.setArguments(args);
        /**
         * Set Call back to capture selected date
         */
        date.setCallBack(ondate2);
        date.show(getFragmentManager(), "Date Picker2");
    }
    DatePickerDialog.OnDateSetListener ondate2 = new DatePickerDialog.OnDateSetListener() {

        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {

            indate.setText(String.valueOf(year) + "-" + String.valueOf(monthOfYear+1)
                    + "-" + String.valueOf(dayOfMonth));
        }
    };

    public void onViewCreated(View view, Bundle savedInstanceState) {
        wid=(EditText)view.findViewById(R.id.et_wid);
        outdate=(EditText)view.findViewById(R.id.et_outdate);
        indate=(EditText)view.findViewById(R.id.et_indate);
        reason=(EditText)view.findViewById(R.id.et_reason);
        submit=(Button)view.findViewById(R.id.bt_submit);
        outdate_pick=(Button)view.findViewById(R.id.bt_outdate);
        outdate_pick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showoutdate();
            }
        });
        indate_pick=(Button)view.findViewById(R.id.bt_indatepick);
        indate_pick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showindate();
            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {
                w=wid.getText().toString().trim();
                r=reason.getText().toString().trim();
                SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
                try {
                     Indate=sdf.parse(indate.getText().toString().trim());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                try {
                     Outdate=sdf.parse(outdate.getText().toString().trim());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                 date=new Date();
                 sysdate=sdf.format(date);
                try {
                     tdate=sdf.parse(sysdate);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                if(((!w.isEmpty())&&(!outdate.getText().toString().trim().isEmpty()))&&((!indate.getText().toString().trim().isEmpty())&&!r.isEmpty())) {
                    // Toast.makeText(getActivity().getApplicationContext(),"hello",Toast.LENGTH_SHORT).show();
                    if (((Indate.compareTo(Outdate) >= 0) && (Indate.compareTo(tdate) >= 0)) && (Outdate.compareTo(tdate) >= 0)) {
                        showdialog1();
                        check_limit();
                    }
                    else
                    {
                        AlertDialog alertDialog1 = new AlertDialog.Builder(
                            getActivity()).create();
                        alertDialog1.setTitle("Error");
                        alertDialog1.setMessage("         Invalid dates!!!");
                        alertDialog1.setIcon(R.drawable.error);
                        alertDialog1.show();
                    }
                }
                else
                {
                    Toast.makeText(getActivity().getApplicationContext(),"Please fill all the details",Toast.LENGTH_LONG).show();
                }
            }
        });
    }
private void Submit()
{
    String url="http://reddysaisumanth014.000webhostapp.com/Req_submit.php";
    RequestQueue requestQueue= Volley.newRequestQueue(this.getActivity());
    StringRequest stringRequest=new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
        @Override
        public void onResponse(String response) {

            if(response.equals("success"))
            {   clear();
                canceldialog();
                //Toast.makeText(getActivity().getApplicationContext(),"Request Submitted Sucessfully!",Toast.LENGTH_SHORT).show();
                AlertDialog alertDialog = new AlertDialog.Builder(
                        getActivity()).create();
                alertDialog.setTitle("Successful");
                alertDialog.setMessage("Request submitted successfully");
                alertDialog.setIcon(R.drawable.tick);
                alertDialog.show();

            }
            else{clear();
                 canceldialog();
                Toast.makeText(getActivity().getApplicationContext(),"Error:Request not submitted",Toast.LENGTH_LONG).show();
            }
        }
    },
            new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    canceldialog();
                    Toast.makeText(getActivity().getApplicationContext(),"error:"+error.toString(),Toast.LENGTH_LONG).show();
                }
            }
    ){
        @Override
        protected Map<String, String> getParams() throws AuthFailureError {
            Map<String,String> params=new HashMap<>();
            params.put("Regno",user);
            params.put("Wid",wid.getText().toString().trim());
            params.put("Outdate",outdate.getText().toString().trim());
            params.put("Indate",indate.getText().toString().trim());
            params.put("Reason",reason.getText().toString().trim());
            params.put("Approval","NOT_APPROVED");
            return params;
        }
    };
    stringRequest.setRetryPolicy(new DefaultRetryPolicy(5000,3,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    requestQueue.add(stringRequest);
}
   private  void  showdialog()
    {    progress = new ProgressDialog(this.getActivity());

        progress.setTitle("Loading");
        progress.setMessage("Submitting Request...");
        progress.setCancelable(false); // disable dismiss by tapping outside of the dialog
        progress.show();
    }
    private  void  canceldialog()
    {
        progress.dismiss();
    }
    private  void  showdialog1()
    {    progress1 = new ProgressDialog(this.getActivity());

        progress1.setTitle("Loading");
        progress1.setMessage("Verifying...");
        progress1.setCancelable(false); // disable dismiss by tapping outside of the dialog
        progress1.show();
    }
    private void canceldialog1()
    {
        progress1.dismiss();
    }
    private  void clear()
    {
        wid.setText(null);
        outdate.setText(null);
        indate.setText(null);
        reason.setText(null);
    }
    private void check_limit()
    {

        Calendar c=Calendar.getInstance();
        c.setTime(Outdate);
        int year=c.get(Calendar.YEAR);
        int month=c.get(Calendar.MONTH)+1;
        Date d=new Date();
         s=year+"-"+month+"-"+"01";
         a=year+"-"+month+"-"+"31";
       // Toast.makeText(getContext(),"s:"+s,Toast.LENGTH_SHORT).show();
        //Toast.makeText(getContext(),"a:"+a,Toast.LENGTH_SHORT).show();
        String url1="http://reddysaisumanth014.000webhostapp.com/check_limit.php";
        final RequestQueue requestQueue1= Volley.newRequestQueue(this.getActivity());
        StringRequest stringRequest2=new StringRequest(Request.Method.POST, url1, new Response.Listener<String>() {
            @Override
            public void onResponse(String response1) {canceldialog1();
                try {

                    JSONArray gparray = new JSONArray(response1);
                   if(gparray.length()>=2) {
                       limit_exe = true;
                       clear();
                       AlertDialog alertDialog1 = new AlertDialog.Builder(
                               getActivity()).create();
                       alertDialog1.setTitle("Submission failed");
                       alertDialog1.setMessage("         Oops!! Gatepass limit exceeded");
                       alertDialog1.setIcon(R.drawable.error);
                       alertDialog1.show();
                   }
                   else {
                       limit_exe = false;
                       showdialog();
                       Submit();
                   }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        canceldialog1();
                        Toast.makeText(getActivity().getApplicationContext(),"error:"+error.toString(),Toast.LENGTH_LONG).show();
                    }
                }
        ){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params1=new HashMap<String, String>();
                params1.put("Regno",user);
                params1.put("Low",s);
                params1.put("Up",a);
                return params1;
            }
        };
      // stringRequest2.setRetryPolicy(new DefaultRetryPolicy(5000,3,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue1.add(stringRequest2);
    }


}
