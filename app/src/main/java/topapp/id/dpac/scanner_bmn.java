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

public class scanner_bmn extends AppCompatActivity implements ZXingScannerView.ResultHandler {

    Koneksi koneksi = new Koneksi();
    String service_api = koneksi.getUrl();

    int success;
    SharedPreferences sharedPreferences;
    String tag_json_obj = "json_obj_req";

    ProgressDialog pDialog;
    private ZXingScannerView mScannerView;
    public static final String TAG = scanner.class.getSimpleName();
    public static final String TAG_NOMOR = "nomor_asset";
    public static final String TAG_MENU = "menu";
    public static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";
    public static final String TAG_DESKRIPSI = "deskripsi";
    public static final String TAG_JUMLAH = "jumlah_asset";
    public static final String TAG_MERK = "merk";
    public static final String TAG_KELAS = "kelas";
    public static final String TAG_TAHUNPEROLEHAN = "tahun_perolehan";
    public static final String TAG_USER_FUNGSI = "user_fungsi";
    public static final String TAG_LOKASI= "lokasi_asset";
    public static final String TAG_KLASIFIKASI = "kelasifikasi_lokasi";
    public static final String TAG_LOKASITAHUNSEBELUMNYA= "lokasi_tahun_sebelumnya";
    public static final String TAG_REGION= "region";
    public static final String TAG_FIELD = "field";
    public static final String TAG_KPKNL = "kpknl";
    public static final String TAG_KETERANGAN = "ket";
    public static final String TAG_KONDISI = "kondisi";
    public static final String TAG_SATUAN = "satuan";
    public static final String TAG_DIGUNAKAN_OLEH = "digunakan_oleh";
    public static final String TAG_STATUS_PENGGUNAAN = "status_penggunaan";
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
        String nomorbmn = rawresult.getText();

        if (nomorbmn.trim().length()>0){
            check_scanner(nomorbmn);
        }

        //AlertDialog.Builder builder = new AlertDialog.Builder(this);
        //builder.setTitle("Hasil Scan");
        //builder.setMessage(rawresult.getText());
        //AlertDialog alert1 = builder.create();
        //alert1.show();

        mScannerView.resumeCameraPreview(this);
    }

    private void check_scanner (final String nomorbmn){
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
        pDialog.setMessage("Sedang Memuat "+nomorbmn);
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST, service_api + "check_hbi_scan.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e(TAG, "Scan response : " + response.toString());
                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    success = jObj.getInt(TAG_SUCCESS);

                    if (success == 1) {
                        Log.e("Succesfully Load Data", jObj.toString());
                        String nomor_bmn = jObj.getString(TAG_NOMOR);
                        String deksripsi_asset = jObj.getString(TAG_DESKRIPSI);
                        String jumlah_asset = jObj.getString(TAG_JUMLAH);
                       String merk_asset = jObj.getString(TAG_MERK);
                        String tahun_perolehan = jObj.getString(TAG_TAHUNPEROLEHAN);
                        //String user_fungsi = jObj.getString(TAG_USER_FUNGSI);
                        String klasifikasi_lokasi = jObj.getString(TAG_KLASIFIKASI);
                        String lokasi_tahun_sebelumnya = jObj.getString(TAG_LOKASITAHUNSEBELUMNYA);
                        //String kondisi_asset = jObj.getString(TAG_KONDISI);
                        String region = jObj.getString(TAG_REGION);
                        String field = jObj.getString(TAG_FIELD);
                        String lokasi_asset = jObj.getString(TAG_LOKASI);
                       String kpknl = jObj.getString(TAG_KPKNL);
                       // String satuan = jObj.getString(TAG_SATUAN);
      /*                  String digunakan_oleh= jObj.getString(TAG_DIGUNAKAN_OLEH);
                        String status_penggunaan = jObj.getString(TAG_STATUS_PENGGUNAAN);
                        String keterangan = jObj.getString(TAG_KETERANGAN);
  */                      String kelas = jObj.getString(TAG_KELAS);


                        Intent i = new Intent(getApplicationContext(), form_bmn.class);
                        i.putExtra(TAG_NOMOR, nomor_bmn);
                        i.putExtra(TAG_DESKRIPSI, deksripsi_asset);
                      i.putExtra(TAG_JUMLAH, jumlah_asset);
                          i.putExtra(TAG_KELAS, kelas);
                        i.putExtra(TAG_MERK, merk_asset);
                        i.putExtra(TAG_TAHUNPEROLEHAN, tahun_perolehan);
                    //    i.putExtra(TAG_USER_FUNGSI ,user_fungsi);
                       i.putExtra(TAG_LOKASITAHUNSEBELUMNYA, lokasi_tahun_sebelumnya);
                      //  i.putExtra(TAG_KONDISI, kondisi_asset);
                        i.putExtra(TAG_LOKASI, lokasi_asset);
                        i.putExtra(TAG_KLASIFIKASI, klasifikasi_lokasi);
                        i.putExtra(TAG_REGION, region);
                        i.putExtra(TAG_FIELD, field);
                        i.putExtra(TAG_KPKNL, kpknl);
                        i.putExtra(TAG_JUMLAH, jumlah_asset);
                  //      i.putExtra(TAG_SATUAN, satuan);
                    /*    i.putExtra(TAG_DIGUNAKAN_OLEH, digunakan_oleh);
                        i.putExtra(TAG_STATUS_PENGGUNAAN, status_penggunaan);
                        i.putExtra(TAG_KETERANGAN, keterangan);
*/
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
                params.put("nomor_bmn", nomorbmn);

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
