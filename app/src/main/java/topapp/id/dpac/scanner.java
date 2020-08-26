package topapp.id.dpac;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.zxing.Result;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import me.dm7.barcodescanner.zxing.ZXingScannerView;
import toppapp.id.dpac.R;

public class scanner extends AppCompatActivity implements ZXingScannerView.ResultHandler {

    Koneksi koneksi = new Koneksi();
    String service_api = koneksi.getUrl();

    int success;
    SharedPreferences sharedPreferences;
    String tag_json_obj = "json_obj_req";

    ProgressDialog pDialog;
    private ZXingScannerView mScannerView;
    public static final String TAG = scanner.class.getSimpleName();
    public static final String TAG_NOMOR = "nomor_hbi";
    public static final String TAG_MENU = "menu";
    public static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";
    public static final String TAG_DESKRIPSI = "deskripsi";
    public static final String TAG_JUMLAH = "jumlah";
    public static final String TAG_MERK = "merk";
    public static final String TAG_TYPE = "type";
    public static final String TAG_NOMORSERI = "no_seri";
    public static final String TAG_HARGA_RP = "harga_rp";
    public static final String TAG_HARGA_USD = "harga_usd";
    public static final String TAG_TANGGAL = "tanggal";
    public static final String TAG_AFE = "afe";
    public static final String TAG_LOKASI = "lokasi";
    public static final String TAG_APO = "apo";
    public static final String TAG_KETERANGAN = "ket";
    public static final String TAG_KONDISI = "kondisi";
    public static final String TAG_USERDEPT = "userdpt";
    public static final String TAG_HOLDER = "holder";
    private String menu_pilih;
    String s;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //s.equals(getIntent().getStringExtra("DATA"));
        //Intent intent = getIntent();
        //menu_pilih = intent.getStringExtra(MainActivity.TAG_MENU);


        mScannerView = new ZXingScannerView(this);
        setContentView(mScannerView);

    }

    @Override
    public void onResume() {
        super.onResume();
        mScannerView.setResultHandler(this);
        mScannerView.startCamera();
    }

    @Override
    public void onPause() {
        super.onPause();
        mScannerView.stopCamera();
    }

    @Override
    public void handleResult(Result rawresult) {
        Log.v("TAG", rawresult.getText());
        Log.v("TAG", rawresult.getBarcodeFormat().toString());
        String nomorhbi = rawresult.getText();

        if (nomorhbi.trim().length()>0){
            //Intent i = new Intent(getApplicationContext(), form_hbi.class);
            //finish();
            //startActivity(i);
//            check_scanner(nomorhbi);
            Toast.makeText(getApplicationContext(), nomorhbi, Toast.LENGTH_LONG).show();
            finish();
        }

        //AlertDialog.Builder builder = new AlertDialog.Builder(this);
        //builder.setTitle("Hasil Scan");
        //builder.setMessage(rawresult.getText());
        //AlertDialog alert1 = builder.create();
        //alert1.show();

        mScannerView.resumeCameraPreview(this);
    }

    private void check_scanner (final String nomorhbi){
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
        pDialog.setMessage("Sedang Memuat "+nomorhbi);
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST, service_api + "check_scanner_bmn.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e(TAG, "Login response : " + response.toString());
                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    success = jObj.getInt(TAG_SUCCESS);

                    if (success == 1) {
                        String nomor_hbi = jObj.getString(TAG_NOMOR);
                        String deksripsi_asset = jObj.getString(TAG_DESKRIPSI);
                        String jumlah_asset = jObj.getString(TAG_JUMLAH);
                        String merk_asset = jObj.getString(TAG_MERK);
                        String type_asset = jObj.getString(TAG_TYPE);
                        String nomor_seri_asset = jObj.getString(TAG_NOMORSERI);
                        String harga_perolehan_rp = jObj.getString(TAG_HARGA_RP);
                        String harga_perolehan_usd = jObj.getString(TAG_HARGA_USD);
                        String kondisi_asset = jObj.getString(TAG_KONDISI);
                        String user_dept = jObj.getString(TAG_USERDEPT);
                        String user_holder = jObj.getString(TAG_HOLDER);
                        String lokasi_asset = jObj.getString(TAG_LOKASI);
                        String tanggal_akuisisi = jObj.getString(TAG_TANGGAL);
                       String afe = jObj.getString(TAG_AFE);
                        String apo = jObj.getString(TAG_APO);
                        Log.e("Succesfully Load Data", jObj.toString());
                        Intent i = new Intent(getApplicationContext(), form_hbi.class);
                       i.putExtra(TAG_NOMOR, nomorhbi);
                       i.putExtra(TAG_DESKRIPSI, deksripsi_asset);
                       i.putExtra(TAG_JUMLAH, jumlah_asset);
                        i.putExtra(TAG_TYPE, type_asset);
                        i.putExtra(TAG_MERK, merk_asset);
                        i.putExtra(TAG_NOMORSERI, nomor_seri_asset);
                        i.putExtra(TAG_HARGA_RP, harga_perolehan_rp);
                        i.putExtra(TAG_HARGA_USD, harga_perolehan_usd);
                        i.putExtra(TAG_KONDISI, kondisi_asset);
                        i.putExtra(TAG_USERDEPT, user_dept);
                        i.putExtra(TAG_HOLDER, user_holder);
                        i.putExtra(TAG_TANGGAL, tanggal_akuisisi);
                        i.putExtra(TAG_AFE, afe);
                        i.putExtra(TAG_APO, apo);
                        finish();
                        startActivity(i);
                    } else {
                        Toast.makeText(getApplicationContext(), "Data tidak ditemukan", Toast.LENGTH_LONG).show();
                        hideDialog();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }


        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Scanner Error: "+error.getMessage());
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }){
            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("nomor_hbi", nomorhbi);

                return params;
            }
        };

        AppController.getInstance().addToRequestQueue(strReq, tag_json_obj);
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
