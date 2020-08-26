package topapp.id.dpac;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import toppapp.id.dpac.R;

/**
 * Created by topapp.id on 10/02/18.
 */

public class LoginActivity extends AbsRunTimePermission {
    private static final int REQUEST_PERMISSION = 10;
    ProgressDialog pDialog;
    EditText etusername, etpassword;
    TextView tvregister;
    Button login_button;

    Koneksi koneksi = new Koneksi();
    String service_api = koneksi.getUrl();

    ConnectivityManager conMgr;

    private String url = service_api + "login_calon.php";
    private static final String TAG = LoginActivity.class.getSimpleName();
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";

    public static final String TAG_ID = "kode_user";
    public static final String TAG_NAMA = "nama";
    public static final String TAG_LEVEL = "level";
    public static final String TAG_DAPIL = "dapil";
    public static final String TAG_DAERAH= "daerah";
    public static final String TAG_IDDAERAH= "id_daerah";

    int success;
    String tag_json_obj = "json_obj_req";

    SharedPreferences sharedPreferences;
    Boolean session = false;
    String kode_user, nama, level;
    public static final String my_shared_preferences = "my_shared_preferences";
    public static final String session_status = "session_status";


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //request permission here
        requestAppPermissions(new String[]{
                        Manifest.permission.CAMERA,
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION},
                R.string.msg,REQUEST_PERMISSION);

        conMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        {
            if (conMgr.getActiveNetworkInfo() != null
                    && conMgr.getActiveNetworkInfo().isAvailable()
                    && conMgr.getActiveNetworkInfo().isConnected()) {

            } else {
                Toast.makeText(getApplicationContext(), "Koneksi internet anda belum dihidupkan", Toast.LENGTH_LONG).show();
            }
        }

        etusername = findViewById(R.id.username);
        etpassword = findViewById(R.id.password);
        login_button = findViewById(R.id.login_button);
        tvregister = findViewById(R.id.tvregister);

        // cek session login, jika TRUE direct ke Halaman Utama
        sharedPreferences = getSharedPreferences(my_shared_preferences, Context.MODE_PRIVATE);
        session = sharedPreferences.getBoolean(session_status, false);

        kode_user = sharedPreferences.getString(TAG_ID, null);
        nama = sharedPreferences.getString(TAG_NAMA, null);
        level = sharedPreferences.getString(TAG_LEVEL, null);


        if(session){
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            intent.putExtra(TAG_ID, kode_user);
            intent.putExtra(TAG_NAMA, nama);
            intent.putExtra(TAG_LEVEL, level);
            finish();
            startActivity(intent);
        }

        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = etusername.getText().toString();
                String password = etpassword.getText().toString();

                // cek apabila ada kolom yang kosong
                if (username.trim().length()>0 && password.trim().length()>0){
                    if (conMgr.getActiveNetworkInfo() != null
                            && conMgr.getActiveNetworkInfo().isAvailable()
                            && conMgr.getActiveNetworkInfo().isConnected()) {
                        checkLogin(username, password);
                    } else {
                        Toast.makeText(getApplicationContext() ,"Koneksi internet anda belum dihidupkan", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext() ,"Kolom username dan password tidak boleh kosong", Toast.LENGTH_LONG).show();
                }
            }
        });

        tvregister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog alertDialog = new AlertDialog.Builder(
                        LoginActivity.this).create();

                // Setting Dialog Title
                alertDialog.setTitle(R.string.license_title);

                // Setting Dialog Message
                alertDialog.setMessage("EP v1.0\nCopyright ©Muhammad Suryono 2019");

                // Setting Icon to Dialog
                alertDialog.setIcon(R.mipmap.ic_launcher);
                alertDialog.show();
            }
        });
    }

    private void checkLogin(final String username, final String password){
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
        pDialog.setMessage("Sedang Login...");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e(TAG, "Login Response : "+response.toString());
                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    success = jObj.getInt(TAG_SUCCESS);

                    // check for error node in json
                    if(success == 1){
                        String kode_user = jObj.getString(TAG_ID);
                        String nama = jObj.getString(TAG_NAMA);
                        String level = jObj.getString(TAG_LEVEL);

                        if (level.equals("Pegawai")){
                            Log.e("Succesfully Login!", jObj.toString());
                            Toast.makeText(getApplicationContext(), "Selamat datang " +nama, Toast.LENGTH_LONG).show();

                            // simpan login ke dalam session
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putBoolean(session_status, true);
                            editor.putString(TAG_ID, kode_user);
                            editor.putString(TAG_NAMA, nama);
                            editor.putString(TAG_LEVEL, level);
                            editor.commit();

                            // direct ke halaman utama
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            intent.putExtra(TAG_ID, kode_user);
                            intent.putExtra(TAG_NAMA, nama);
                            intent.putExtra(TAG_LEVEL, level);
                            finish();
                            startActivity(intent);
                        }else {
                            Toast.makeText(getApplicationContext(), "Admin tidak dapat masuk", Toast.LENGTH_LONG).show();
                        }

                    } else {
                        Toast.makeText(getApplicationContext(),
                                jObj.getString(TAG_MESSAGE), Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e){
                    //JSON Error
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Login Error: "+error.getMessage());
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }){
            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("username", username);
                params.put("password", password);

                return params;
            }
        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_json_obj);
    }


    @Override
    public void onPermissionGranted(int requestcode) {
        Toast.makeText(getApplicationContext(), "Permission granted", Toast.LENGTH_LONG).show();
    }

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }
}

