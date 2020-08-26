package topapp.id.dpac;


import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.squareup.picasso.RequestHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Closeable;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import topapp.id.dpac.ResultHandler;
import toppapp.id.dpac.R;

import static com.google.android.gms.maps.CameraUpdateFactory.newLatLng;

public class form_bmn extends AppCompatActivity implements OnMapReadyCallback {

    Koneksi konek = new Koneksi();
    String url = konek.getUrl();
    String url_kategori = url + "lihat_kategori";
    String url_sub_kategori = url + "lihat_sub_kategori";
    String simpan = url + "simpan_hbi.php";

    String tag_json_obj = "json_obj_req";

    //inisialisasi
    private TextView tvlat, tvlng;
    private EditText etnomorbmn, etdeskripsi, etjumlah, etmerk, etkelas, ettahunperolehan, etlokasiasset, etklasifikasi, etlokasitahunsebelumnya, etregional, etfield, etkpknl, etsatuan, etket;
    private Button btkirim_bmn, btbatal;
    private ImageButton btkamera, btgaleri;
    private Spinner spkondisi, spuserfungsi, spsatuan, sppenggunaanaset;
    private String nomorhbi;

    String nama_kondisi, nama_userdept, nama_holder, satuan ;
    SharedPreferences sharedPreferences;


    private DatePickerDialog datePickerDialog;
    private SimpleDateFormat dateFormatter;
    private TextView tanggal_akuisisi;
    private EditText tgl_akuisisi;
    private GoogleMap mMap;

    int success;
    private static final String TAG = form_hbi.class.getSimpleName();
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";
    public static final String TAG_ID = "kode";
    public static final String TAG_USERID = "id_user";
    public static final String TAG_JSON_ARRAY = "result";
    public static final String TAG_DESKRIPSI = "deskripsi";
    public static final String TAG_JUMLAH = "jumlah_asset";
    public static final String TAG_MERK = "merk";
    public static final String TAG_TAHUNPEROLEHAN = "tahun_perolehan";
    public static final String TAG_LOKASI_ASSET = "lokasi_asset";
    public static final String TAG_KLASIFIKASI_LOKASI= "klasifikasi_lokasi";
    public static final String TAG_LOKASI_TAHUN_SEBELUMNYA = "lokasi_sebelumnya";
    public static final String TAG_REGIONAL = "regional";
    public static final String TAG_FIELD = "field";
    public static final String TAG_KPKNL = "kpnl";
    public static final String TAG_SATUAN = "satuan";
    public static final String TAG_KETERANGAN = "ket";

    private ImageView ivImageCompress, ivImageCompress2, ivImageCompress3, ivImageCompress4;
    private ConnectionDetector cd;
    private Boolean upflag = false;
    private Uri selectedImage = null;
    private Bitmap bitmap, bitmapRotate;
    private ProgressDialog pDialog;
    String imagepath = "";
    String fname, fname2, fname3, fname4; // ini adalah file gambar yg akan di upload (.png)
    File file;

    //clas location
    LocationManager lm;
    LocationListener locationListener;
    String lat, lng, bestProvider;
    Criteria c = new Criteria();

    //from upload file tutorial final
    public static final String DATE_FORMAT = "yyyyMMdd_HHmmss";
    public static final String IMAGE_DIRECTORY = "Galeri_Asset";

    private File sourceFile;
    private File destFile, destFile2, destFile3, destFile4; // ini adalah nama file
    private Uri imageCaptureUri, imageCaptureUri2, imageCaptureUri3, imageCaptureUri4;
    Bitmap bmp, bmp2, bmp3, bmp4;

    private static final String SCHEME_FILE = "file";
    private static final String SCHEME_CONTENT = "content";
    private String nomorhbmn, deskripsi_bmn, jumlah_bmn, satuan_bmn, kelas_bmn, merk_bmn, tahun_perolehan_bmn, lokasi_bmn, klasifikasi_bmn, lokasi_sebelumya_bmn, regio_bmn, field_bmn, kpknl_bmn;


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

//        try {
//            // Customise the styling of the base map using a JSON object defined
//            // in a raw resource file.
//            boolean success = googleMap.setMapStyle(
//                    MapStyleOptions.loadRawResourceStyle(
//                            this, R.raw.style_json));
//
//            if (!success) {
//                Log.e(TAG, "Style parsing failed.");
//            }
//        } catch (Resources.NotFoundException e) {
//            Log.e(TAG, "Can't find style. Error: ", e);
//        }

        mMap.setMinZoomPreference(16);


        // Position the map's camera near Sydney, Australia.
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(0.507068, 101.447779)));


        mMap.setMyLocationEnabled(true);
    }

    // class for get current location
    private class MyLocationListener implements LocationListener {

        @Override
        public void onLocationChanged(Location loc) {
            if (loc != null) {
                lat = String.valueOf(loc.getLatitude());
                tvlat.setText(lat);

                lng = String.valueOf(loc.getLongitude());
                tvlng.setText(lng);
            }
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            String statusString = "";
            switch (status) {
                case LocationProvider.AVAILABLE:
                    statusString = "available";
                case LocationProvider.OUT_OF_SERVICE:
                    statusString = "out of service";
                case LocationProvider.TEMPORARILY_UNAVAILABLE:
                    statusString = "temporarily unavailable";
            }

            Toast.makeText(getBaseContext(),
                    provider + " " + statusString,
                    Toast.LENGTH_SHORT).show();


        }

        @Override
        public void onProviderEnabled(String provider) {
            Toast.makeText(getBaseContext(),
                    "Provider: " + provider + " enabled",
                    Toast.LENGTH_SHORT).show();

        }

        @Override
        public void onProviderDisabled(String provider) {
            Toast.makeText(getBaseContext(),
                    "Provider: " + provider + " disabled",
                    Toast.LENGTH_SHORT).show();
        }
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationListener = new MyLocationListener();

        List<String> locationProviders = lm.getAllProviders();
        for (String provider : locationProviders){
            Log.d("LocationProviders", provider);
        }

        c.setAccuracy(Criteria.ACCURACY_FINE);
        //c.setAccuracy(Criteria.ACCURACY_COARSE);
        c.setAltitudeRequired(false);
        c.setBearingRequired(false);
        c.setCostAllowed(true);
        c.setPowerRequirement(Criteria.POWER_HIGH);



        //obj initiate
        cd = new ConnectionDetector(this);
        cd = new ConnectionDetector(getApplicationContext());


        file = new File(Environment.getExternalStorageDirectory()
                + "/" + IMAGE_DIRECTORY);
        if (!file.exists()) {
            file.mkdirs();
        }

        dateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
        showlayoout();


    }

    @Override
    protected void onResume() {
        super.onResume();

        //-request for location update using GPS
        // get the best
        bestProvider = lm.getBestProvider(c, true);
        Log.d("LocationProviders", "Best provider is "+ bestProvider);

        // get the last know location
        Location location = lm.getLastKnownLocation(bestProvider);
        if (location != null) Log.d("LocationProviders", location.toString());

        lm.requestLocationUpdates(
                // Ambil lokasi dari BTS
                LocationManager.NETWORK_PROVIDER,

                // Ambil lokasi dari Satelit
                //bestProvider,
                0,
                0,
                locationListener);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (pDialog != null) {
            pDialog.dismiss();
            pDialog = null;
        }

        //--remove the location listener--
        lm.removeUpdates(locationListener);
    }


    private void showlayoout(){
        setContentView(R.layout.activity_form_bmn);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        //get sessions

        etnomorbmn = (EditText) findViewById(R.id.nomor_asset);
        etdeskripsi = (EditText) findViewById(R.id.etdeskripsi);
        etjumlah = (EditText) findViewById(R.id.jumlah);
        etmerk = (EditText) findViewById(R.id.merk);
        spsatuan = (Spinner) findViewById(R.id.satuan);
        etkelas =findViewById(R.id.kelas);
        ettahunperolehan = (EditText) findViewById(R.id.tahun_perolehan);
        spuserfungsi = findViewById(R.id.user_fungsi);
        etlokasiasset = findViewById(R.id.lokasi_asset);
        etklasifikasi = findViewById(R.id.klasifikasi_asset);
        etlokasitahunsebelumnya = findViewById(R.id.lokasi_tahun_sebelumnya);
        etregional = findViewById(R.id.regional);
        etfield = findViewById(R.id.field);
        etkpknl = findViewById(R.id.kpknl);
        spkondisi = findViewById(R.id.kondisi);
        sppenggunaanaset = findViewById(R.id.digunakan_oleh);
        ivImageCompress = findViewById(R.id.ivImageCompress);
        ivImageCompress2 = findViewById(R.id.ivImageCompress2);
        ivImageCompress3 = findViewById(R.id.ivImageCompress3);
        ivImageCompress4 = findViewById(R.id.ivImageCompress4);
        btkamera = findViewById(R.id.btnCamera);
        btgaleri = findViewById(R.id.btnGallery);
        btkirim_bmn = findViewById(R.id.btkirim);
        btbatal = findViewById(R.id.btbatal);
        tvlat = findViewById(R.id.tvlat);
        tvlng = findViewById(R.id.tvlng);
        etket =findViewById(R.id.etket);


        sharedPreferences = getSharedPreferences(LoginActivity.my_shared_preferences, Context.MODE_PRIVATE);
        Intent intent = getIntent();
        nomorhbmn = intent.getStringExtra(scanner_bmn.TAG_NOMOR);
        deskripsi_bmn = intent.getStringExtra(scanner_bmn.TAG_DESKRIPSI);
        jumlah_bmn = intent.getStringExtra(scanner_bmn.TAG_JUMLAH);
        kelas_bmn = intent.getStringExtra(scanner_bmn.TAG_KELAS);
        merk_bmn= intent.getStringExtra(scanner_bmn.TAG_MERK);
        tahun_perolehan_bmn = intent.getStringExtra(scanner_bmn.TAG_TAHUNPEROLEHAN);
        lokasi_bmn = intent.getStringExtra(scanner_bmn.TAG_LOKASI);
        klasifikasi_bmn = intent.getStringExtra(scanner_bmn.TAG_KLASIFIKASI);
        lokasi_sebelumya_bmn = intent.getStringExtra(scanner_bmn.TAG_LOKASITAHUNSEBELUMNYA);
        regio_bmn = intent.getStringExtra(scanner_bmn.TAG_REGION);
        field_bmn = intent.getStringExtra(scanner_bmn.TAG_REGION);
        kpknl_bmn = intent.getStringExtra(scanner_bmn.TAG_KPKNL);

        etnomorbmn.setText(nomorhbmn);
        etdeskripsi.setText(deskripsi_bmn);
        etjumlah.setText(jumlah_bmn);
        etkelas.setText(kelas_bmn);
        etmerk.setText(merk_bmn);
        ettahunperolehan.setText(tahun_perolehan_bmn);
        etlokasiasset.setText(lokasi_bmn);
        etklasifikasi.setText(klasifikasi_bmn);
        etlokasitahunsebelumnya.setText(lokasi_sebelumya_bmn);
        etregional.setText(regio_bmn);
        etfield.setText(field_bmn);
        etkpknl.setText(kpknl_bmn);
        //Toast.makeText(getApplicationContext(), nomorhbmn, Toast.LENGTH_SHORT).show();
        //getDataHbi();

        spkondisi.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                nama_kondisi = spkondisi.getSelectedItem().toString();

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        spuserfungsi.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                nama_userdept = spuserfungsi.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        sppenggunaanaset.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                nama_holder = sppenggunaanaset.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spsatuan.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                satuan = spsatuan.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        btkamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickCamera();
            }
        });

        btgaleri.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickGaleri();
            }
        });

        btbatal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // refresh page
                ivImageCompress.setImageResource(android.R.color.transparent);
                ivImageCompress.setVisibility(view.GONE);
                fname = null;
            }
        });

        btkirim_bmn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                kirimData();
            }
        });
    }

    private void kirimData() {
        // get datetime
        Date currentTime = Calendar.getInstance().getTime();
        // convert to dateTime format
        SimpleDateFormat date24Format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        // get session
        //sharedPreferences = getSharedPreferences(LoginActivity.my_shared_preferences, Context.MODE_PRIVATE);
        //id_calon = getIntent().getStringExtra(TAG_ID);

        //int selectId = rgkegiatan.getCheckedRadioButtonId();
        //rbkegiatan = findViewById(selectId);

        String strdeskripsi = etdeskripsi.getText().toString();
        String strjumlah = etjumlah.getText().toString();
        String strmerk = etjumlah.getText().toString();
        String stkelas = etkelas.getText().toString();
        String strtahunperolehan = ettahunperolehan.getText().toString();
        String strlokasiasset = etlokasiasset.getText().toString();
        String strklasifikasi = etklasifikasi.getText().toString();
        String strlokasitahunsebelumnya = etlokasitahunsebelumnya.getText().toString();
        String strregional = etregional.getText().toString();
        String strfield = etfield.getText().toString();
        String strkpknl = etkpknl.getText().toString();
        String strkdisi = nama_kondisi;
        String struserfungsi = nama_userdept;
        String strpenggunaanaset= nama_holder;
        String strsatuan = satuan;
        String strket = etket.getText().toString();
//        String strtanggal_akuisisi = .getText().toString();
        String strwaktu = date24Format.format(currentTime);
        String strfoto = fname;
        String strfoto2 = fname2;
        String strfoto3 = fname3;
        String strfoto4 = fname4;
        String strlat = lat;
        String strlng = lng;

        System.out.println("Nomor BMN : " + nomorhbmn + " Deskripsi : " + strdeskripsi + " Jumlah : " + strjumlah+ " Satuan : "+ strsatuan + " Kelas : "+ stkelas +" Merk : " + strmerk + " Tahun Perolahean : " + strtahunperolehan + " User Fungsi : " + struserfungsi + /*" Tanggal Unggah: " + strwaktu + */"Lokas ASset : " + strlokasiasset + " Klasifikasi lokasi : " + strklasifikasi + " Lokasi Tahun Sebelumnya : " + strlokasitahunsebelumnya + " Regional : " + strregional +  " Field : " + strfield + " KPKNL : " + strkpknl + " Kondisi : " + strkdisi + " Penggunaan Aset : "+ strpenggunaanaset +" Lat : " + strlat + " Lng : " + strlng + " Foto1 : " + strfoto + " Foto2 : " +strfoto2+ " Foto3 : " +strfoto3+ " Foto4 : " +strfoto4+ " Ket : " + strket);

        // pengecekkan form tidak boleh kosong
        if ((strdeskripsi).equals("") || (strjumlah.equals(""))|| strmerk.equals("")  || strfoto == null)
        {
            Toast.makeText(getApplicationContext(), "Form tidak boleh kosong", Toast.LENGTH_SHORT).show();
        } else if (lat == null || lng == null){
            Toast.makeText(getApplicationContext(), "Mohon menunggu, Aplikasi sedang membaca lokasi anda. ", Toast.LENGTH_SHORT).show();
            Toast.makeText(getApplicationContext(), "Pastikan koneksi internet dan GPS anda sudah diaktfikan. ", Toast.LENGTH_SHORT).show();
        } else {
            // upload gambar dilakukan saat tombol kirim ditekan
            if (cd.isConnectingToInternet()) {
                if (!upflag) {
                    Toast.makeText(getApplicationContext(), "Anda belum mengambil gambar..!", Toast.LENGTH_LONG).show();
                } else {
                    // lakukan upload gambar
                    saveFile(bitmapRotate, destFile);

                    if (ivImageCompress2.getDrawable()!=null){
                        Log.i(TAG, "kirimLaporan: menyimpan gambar ke-2");
                        saveFile2(bitmapRotate, destFile2);

                    } if (ivImageCompress3.getDrawable()!=null){
                        Log.i(TAG, "kirimLaporan: menyimpan gambar ke-3");
                        saveFile3(bitmapRotate, destFile3);

                    } if (ivImageCompress4.getDrawable()!=null){
                        Log.i(TAG, "kirimLaporan: menyimpan gambar ke-4");
                        saveFile4(bitmapRotate, destFile4);
                    }

                    // agar tidak error saat parameter==null
                    if (strfoto2==null) strfoto2="";
                    if (strfoto3==null) strfoto3="";
                    if (strfoto4==null) strfoto4="";

                    //tu lakukan insert data kecelakaan
                    saveToServer(nomorhbmn, strdeskripsi, stkelas, strmerk, strjumlah, strsatuan, strtahunperolehan, struserfungsi, strlokasiasset, strklasifikasi,  strlokasitahunsebelumnya, strregional, strfield, strkpknl, strkdisi, strpenggunaanaset, strlat, strlng, strfoto, strfoto2, strfoto3, strfoto4, strket);

                    // uncoment utk selesaikan activity

                    finish();
                    restartFirstActivity();

                }
            } else {
                Toast.makeText(getApplicationContext(), "Tidak ada koneksi internet !", Toast.LENGTH_LONG).show();
            }
        }

    } // end of kirimLaporan()

    private void restartFirstActivity()
    {
        Intent i = getBaseContext().getPackageManager()
                .getLaunchIntentForPackage(getBaseContext().getPackageName() );

        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK );
        startActivity(i);
    }

    private void saveToServer(final String nomor_bmn, final String deskripsi_asset, final String kelas, final String merk_asset, final String jumlah_asset, final String satuan, final String tahun_perolehan, final String user_fungsi, final String lokasi_asset, final String klasifikasi_lokasi, final String tahun_sebelumnya, final String regional, final String field, final String kpknl, final String kondisi, final String penggunaan_asset, final String lat, final String lng, final String foto, final String foto2, final String foto3, final String foto4, final String ket){

        StringRequest strReq = new StringRequest(Request.Method.POST, url+"submit_bmn.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e(TAG, "Submit Suara Response : "+response.toString());
                //  hideDialog();
//                pDialog.dismiss();

                try {
                    JSONObject jObj = new JSONObject(response);
                    success = jObj.getInt(TAG_SUCCESS);

                    // check for error node in json
                    if(success == 1){
                        Toast.makeText(getApplicationContext(), "Laporan berhasil dikirim", Toast.LENGTH_LONG).show();
                        // Yeah sukses, koding aksi berikutnya di sini
                    } else if (success == 0){
                        //oh tidak seesuatu yang buruk terjadi
                        Toast.makeText(getApplicationContext(),jObj.getString(TAG_MESSAGE), Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e){
                    //JSON Error
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Submit Kegiatan Error: "+error.getMessage());
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }){
            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to submit suara url
                Map<String, String> params = new HashMap<String, String>();
                params.put("nomor", nomor_bmn);
                params.put("deskripsi", deskripsi_asset);
                params.put("jumlah", jumlah_asset);
                params.put("kelas", kelas);
                params.put("merk", merk_asset);
                params.put("satuan", satuan);
                params.put("tahun_perolehan", tahun_perolehan);
                params.put("user_fungsi", user_fungsi);
                params.put("lokasi_asset", lokasi_asset);
                params.put("klasifikasi_lokasi", klasifikasi_lokasi);
                params.put("tahun_sebelumnya", tahun_sebelumnya);
                params.put("regional", regional);
                params.put("kondisi", kondisi);
                params.put("field", field);
                params.put("penggunaan_asset", penggunaan_asset);
                params.put("lat", lat);
                params.put("lng", lng);
                params.put("foto", foto);
                params.put("foto2", foto2);
                params.put("foto3", foto3);
                params.put("foto4", foto4);
                params.put("keterangan", ket);
                return params;
            }
        };

        // Adding request to request queue
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        try {
            switch (requestCode) {
                case 101:
                    if (resultCode == Activity.RESULT_OK) {
                        upflag = true;
                        Log.d(TAG + ".PICK_CAMERA_IMAGE", "Selected image uri path :" + imageCaptureUri);

                        //ivImage.setImageURI(imageCaptureUri);
                        bmp = decodeFile(destFile);
                        ivImageCompress.setVisibility(View.VISIBLE);
                        ivImageCompress.setImageBitmap(bmp);
                    }
                    break;
                case 103:
                    if (resultCode == Activity.RESULT_OK) {
                        upflag = true;
                        Log.d(TAG + ".PICK_CAMERA_IMAGE", "Selected image uri path :" + imageCaptureUri);

                        //ivImage.setImageURI(imageCaptureUri);
                        bmp2 = decodeFile2(destFile);
                        ivImageCompress2.setVisibility(View.VISIBLE);
                        ivImageCompress2.setImageBitmap(bmp2);
                    }
                    break;
                case 104:
                    if (resultCode == Activity.RESULT_OK) {
                        upflag = true;
                        Log.d(TAG + ".PICK_CAMERA_IMAGE", "Selected image uri path :" + imageCaptureUri);

                        //ivImage.setImageURI(imageCaptureUri);
                        bmp3 = decodeFile3(destFile);
                        ivImageCompress3.setVisibility(View.VISIBLE);
                        ivImageCompress3.setImageBitmap(bmp3);
                    }
                    break;
                case 105:
                    if (resultCode == Activity.RESULT_OK) {
                        upflag = true;
                        Log.d(TAG + ".PICK_CAMERA_IMAGE", "Selected image uri path :" + imageCaptureUri);

                        //ivImage.setImageURI(imageCaptureUri);
                        bmp4 = decodeFile4(destFile);
                        ivImageCompress4.setVisibility(View.VISIBLE);
                        ivImageCompress4.setImageBitmap(bmp4);
                    }
                    break;
                case 102:
                    if (resultCode == Activity.RESULT_OK){
                        upflag = true;

                        Uri uriPhoto = data.getData();
                        Log.d(TAG + ".PICK_GALLERY_IMAGE", "Selected image uri path :" + uriPhoto.toString());

                        ivImageCompress.setVisibility(View.VISIBLE);
                        ivImageCompress.setImageURI(uriPhoto);

                        sourceFile = new File(getPathFromGooglePhotosUri(uriPhoto));


                        destFile = new File(file,
                                dateFormatter.format(DateFormat.YEAR_FIELD).toString() +"_"+dateFormatter.format(DateFormat.MONTH_FIELD).toString() +"_"+dateFormatter.format(DateFormat.DATE_FIELD).toString() +"_"+nomorhbmn+ ".png");




                        Log.d(TAG, "Source File Path :" + sourceFile);

                        try {
                            copyFile(sourceFile, destFile);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        bmp = decodeFile(destFile);
//                        try {
//
//                            bmp = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
//                            bitmap = getResizedBitmap(bmp, 50);
//                            //bmp = decodeFile(destFile);
//                            ivImageCompress.setVisibility(View.VISIBLE);
//                            ivImageCompress.setImageBitmap(bitmap);
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        }
                    }
                    break;
                case 106:
                    if (resultCode == Activity.RESULT_OK){
                        upflag = true;

                        Uri uriPhoto = data.getData();
                        Log.d(TAG + ".PICK_GALLERY_IMAGE", "Selected image uri path :" + uriPhoto.toString());

                        ivImageCompress2.setVisibility(View.VISIBLE);
                        ivImageCompress2.setImageURI(uriPhoto);

                        sourceFile = new File(getPathFromGooglePhotosUri(uriPhoto));


                        destFile2 = new File(file,
                                dateFormatter.format(DateFormat.YEAR_FIELD).toString() +"_"+dateFormatter.format(DateFormat.MONTH_FIELD).toString() +"_"+dateFormatter.format(DateFormat.DATE_FIELD).toString() +"_"+nomorhbmn+ ".png");




                        Log.d(TAG, "Source File Path :" + sourceFile);

                        try {
                            copyFile(sourceFile, destFile2);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        bmp2 = decodeFile2(destFile2);
//                        try {
//
//                            bmp = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
//                            bitmap = getResizedBitmap(bmp, 50);
//                            //bmp = decodeFile(destFile);
//                            ivImageCompress.setVisibility(View.VISIBLE);
//                            ivImageCompress.setImageBitmap(bitmap);
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        }
                    }
                    break;
                case 107:
                    if (resultCode == Activity.RESULT_OK){
                        upflag = true;

                        Uri uriPhoto = data.getData();
                        Log.d(TAG + ".PICK_GALLERY_IMAGE", "Selected image uri path :" + uriPhoto.toString());

                        ivImageCompress3.setVisibility(View.VISIBLE);
                        ivImageCompress3.setImageURI(uriPhoto);

                        sourceFile = new File(getPathFromGooglePhotosUri(uriPhoto));


                        destFile3 = new File(file,
                                dateFormatter.format(DateFormat.YEAR_FIELD).toString() +"_"+dateFormatter.format(DateFormat.MONTH_FIELD).toString() +"_"+dateFormatter.format(DateFormat.DATE_FIELD).toString() +"_"+nomorhbmn+ ".png");




                        Log.d(TAG, "Source File Path :" + sourceFile);

                        try {
                            copyFile(sourceFile, destFile3);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        bmp3 = decodeFile3(destFile3);
//                        try {
//
//                            bmp = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
//                            bitmap = getResizedBitmap(bmp, 50);
//                            //bmp = decodeFile(destFile);
//                            ivImageCompress.setVisibility(View.VISIBLE);
//                            ivImageCompress.setImageBitmap(bitmap);
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        }
                    }
                    break;
                case 108:
                    if (resultCode == Activity.RESULT_OK){
                        upflag = true;

                        Uri uriPhoto = data.getData();
                        Log.d(TAG + ".PICK_GALLERY_IMAGE", "Selected image uri path :" + uriPhoto.toString());

                        ivImageCompress4.setVisibility(View.VISIBLE);
                        ivImageCompress4.setImageURI(uriPhoto);

                        sourceFile = new File(getPathFromGooglePhotosUri(uriPhoto));


                        destFile4 = new File(file,
                                dateFormatter.format(DateFormat.YEAR_FIELD).toString() +"_"+dateFormatter.format(DateFormat.MONTH_FIELD).toString() +"_"+dateFormatter.format(DateFormat.DATE_FIELD).toString() +"_"+nomorhbmn+ ".png");




                        Log.d(TAG, "Source File Path :" + sourceFile);

                        try {
                            copyFile(sourceFile, destFile4);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        bmp4 = decodeFile4(destFile4);
//                        try {
//
//                            bmp = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
//                            bitmap = getResizedBitmap(bmp, 50);
//                            //bmp = decodeFile(destFile);
//                            ivImageCompress.setVisibility(View.VISIBLE);
//                            ivImageCompress.setImageBitmap(bitmap);
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        }
                    }
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public static Bitmap rotateImage(Bitmap source, float angle) {
        Bitmap retVal;

        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        retVal = Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);

        return retVal;
    }

    //    In some mobiles image will get rotate so to correting that this code will help us
    private int getImageOrientation() {
        final String[] imageColumns = {MediaStore.Images.Media._ID, MediaStore.Images.ImageColumns.ORIENTATION};
        final String imageOrderBy = MediaStore.Images.Media._ID + " DESC";

        Cursor cursor = getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                imageColumns, null, null, imageOrderBy);

        if (cursor.moveToFirst()) {
            int orientation = cursor.getInt(cursor.getColumnIndex(MediaStore.Images.ImageColumns.ORIENTATION));
            System.out.println("orientation===" + orientation);
            cursor.close();
            return orientation;
        } else {
            return 0;
        }
    }

    //    Saving file to the mobile internal memory
    private void saveFile(Bitmap sourceUri, File destination) {
        if (destination.exists()) destination.delete();

        try {
            FileOutputStream out = new FileOutputStream(destFile);
            bmp.compress(Bitmap.CompressFormat.PNG, 100, out);
            out.flush();
            out.close();
            Log.i(TAG, "saveFile: Berhasil menyimpan gambar ke-1");
            if (cd.isConnectingToInternet()) {
                new DoFileUpload().execute();
            } else {
                Toast.makeText(getApplicationContext(), "Tidak ada koneksi internet..", Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void saveFile2(Bitmap sourceUri, File destination) {
        if (destination.exists()) destination.delete();

        try {
            FileOutputStream out = new FileOutputStream(destFile2);
            bmp2.compress(Bitmap.CompressFormat.PNG, 100, out);
            out.flush();
            out.close();
            Log.i(TAG, "saveFile: Berhasil menyimpan gambar ke-2");
            if (cd.isConnectingToInternet()) {
                new DoFileUpload2().execute();
            } else {
                Toast.makeText(getApplicationContext(), "Tidak ada koneksi internet..", Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void saveFile3(Bitmap sourceUri, File destination) {
        if (destination.exists()) destination.delete();

        try {
            FileOutputStream out = new FileOutputStream(destFile3);
            bmp3.compress(Bitmap.CompressFormat.PNG, 100, out);
            out.flush();
            out.close();
            Log.i(TAG, "saveFile: Berhasil menyimpan gambar ke-3");
            if (cd.isConnectingToInternet()) {
                new DoFileUpload3().execute();
            } else {
                Toast.makeText(getApplicationContext(), "Tidak ada koneksi internet..", Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void saveFile4(Bitmap sourceUri, File destination) {
        if (destination.exists()) destination.delete();

        try {
            FileOutputStream out = new FileOutputStream(destFile4);
            bmp4.compress(Bitmap.CompressFormat.PNG, 100, out);
            out.flush();
            out.close();
            Log.i(TAG, "saveFile: Berhasil menyimpan gambar ke-4");
            if (cd.isConnectingToInternet()) {
                new DoFileUpload4().execute();
            } else {
                Toast.makeText(getApplicationContext(), "Tidak ada koneksi internet..", Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private Bitmap decodeFile(File f) {
        Bitmap b = null;

        //Decode image size
        BitmapFactory.Options o = new BitmapFactory.Options();
        o.inJustDecodeBounds = true;

        FileInputStream fis = null;
        try {
            fis = new FileInputStream(f);
            BitmapFactory.decodeStream(fis, null, o);
            fis.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        int IMAGE_MAX_SIZE = 1024;
        int scale = 1;
        if (o.outHeight > IMAGE_MAX_SIZE || o.outWidth > IMAGE_MAX_SIZE) {
            scale = (int) Math.pow(2, (int) Math.ceil(Math.log(IMAGE_MAX_SIZE /
                    (double) Math.max(o.outHeight, o.outWidth)) / Math.log(0.5)));
        }

        //Decode with inSampleSize
        BitmapFactory.Options o2 = new BitmapFactory.Options();
        o2.inSampleSize = scale;
        try {
            fis = new FileInputStream(f);
            b = BitmapFactory.decodeStream(fis, null, o2);
            fis.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Log.d(TAG, "Width :" + b.getWidth() + " Height :" + b.getHeight());

        fname = "kegiatansatu_"
                + dateFormatter.format(new Date()).toString() + ".png";
        destFile = new File(file, fname);

        return b;
    }

    private Bitmap decodeFile2(File f) {
        Bitmap b = null;

        //Decode image size
        BitmapFactory.Options o = new BitmapFactory.Options();
        o.inJustDecodeBounds = true;

        FileInputStream fis = null;
        try {
            fis = new FileInputStream(f);
            BitmapFactory.decodeStream(fis, null, o);
            fis.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        int IMAGE_MAX_SIZE = 1024;
        int scale = 1;
        if (o.outHeight > IMAGE_MAX_SIZE || o.outWidth > IMAGE_MAX_SIZE) {
            scale = (int) Math.pow(2, (int) Math.ceil(Math.log(IMAGE_MAX_SIZE /
                    (double) Math.max(o.outHeight, o.outWidth)) / Math.log(0.5)));
        }

        //Decode with inSampleSize
        BitmapFactory.Options o2 = new BitmapFactory.Options();
        o2.inSampleSize = scale;
        try {
            fis = new FileInputStream(f);
            b = BitmapFactory.decodeStream(fis, null, o2);
            fis.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Log.d(TAG, "Width :" + b.getWidth() + " Height :" + b.getHeight());

        fname2 = "kegiatandua_"
                + dateFormatter.format(new Date()).toString() + ".png";
        destFile2 = new File(file, fname2);

        return b;
    }

    private Bitmap decodeFile3(File f) {
        Bitmap b = null;

        //Decode image size
        BitmapFactory.Options o = new BitmapFactory.Options();
        o.inJustDecodeBounds = true;

        FileInputStream fis = null;
        try {
            fis = new FileInputStream(f);
            BitmapFactory.decodeStream(fis, null, o);
            fis.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        int IMAGE_MAX_SIZE = 1024;
        int scale = 1;
        if (o.outHeight > IMAGE_MAX_SIZE || o.outWidth > IMAGE_MAX_SIZE) {
            scale = (int) Math.pow(2, (int) Math.ceil(Math.log(IMAGE_MAX_SIZE /
                    (double) Math.max(o.outHeight, o.outWidth)) / Math.log(0.5)));
        }

        //Decode with inSampleSize
        BitmapFactory.Options o2 = new BitmapFactory.Options();
        o2.inSampleSize = scale;
        try {
            fis = new FileInputStream(f);
            b = BitmapFactory.decodeStream(fis, null, o2);
            fis.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Log.d(TAG, "Width :" + b.getWidth() + " Height :" + b.getHeight());

        fname3 = "kegiatantiga_"
                + dateFormatter.format(new Date()).toString() + ".png";
        destFile = new File(file, fname3);

        return b;
    }

    private Bitmap decodeFile4(File f) {
        Bitmap b = null;

        //Decode image size
        BitmapFactory.Options o = new BitmapFactory.Options();
        o.inJustDecodeBounds = true;

        FileInputStream fis = null;
        try {
            fis = new FileInputStream(f);
            BitmapFactory.decodeStream(fis, null, o);
            fis.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        int IMAGE_MAX_SIZE = 1024;
        int scale = 1;
        if (o.outHeight > IMAGE_MAX_SIZE || o.outWidth > IMAGE_MAX_SIZE) {
            scale = (int) Math.pow(2, (int) Math.ceil(Math.log(IMAGE_MAX_SIZE /
                    (double) Math.max(o.outHeight, o.outWidth)) / Math.log(0.5)));
        }

        //Decode with inSampleSize
        BitmapFactory.Options o2 = new BitmapFactory.Options();
        o2.inSampleSize = scale;
        try {
            fis = new FileInputStream(f);
            b = BitmapFactory.decodeStream(fis, null, o2);
            fis.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Log.d(TAG, "Width :" + b.getWidth() + " Height :" + b.getHeight());

        fname4 = "kegiatanempat_"
                + dateFormatter.format(new Date()).toString() + ".png";
        destFile = new File(file, fname4);

        return b;
    }


    class DoFileUpload extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {

            pDialog = new ProgressDialog(form_bmn.this);
            pDialog.setCancelable(false);
            pDialog.setMessage("Mohon menunggu, sedang mengupload gambar ke-1..");
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {

            try {
                // Set your file path here
                FileInputStream fstrm = new FileInputStream(destFile);
                // Set your server page url (and the file title/description)
                HttpFileUpload hfu = new HttpFileUpload(konek.getUrl()+"file_upload_kegiatan.php", "ftitle", "fdescription", fname);
                upflag = hfu.Send_Now(fstrm);
            } catch (FileNotFoundException e) {
                // Error: File not found
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String file_url) {
//            if (pDialog.isShowing()) {
            pDialog.dismiss();
//            }
            if (upflag) {
                Toast.makeText(getApplicationContext(), "Upload gambar berhasil", Toast.LENGTH_LONG).show();
                Log.i(TAG, "onPostExecute: Upload gambar ke-1 berhasil");

            } else {
                Toast.makeText(getApplicationContext(), "Sayangnya gambar tidak bisa diupload..", Toast.LENGTH_LONG).show();
            }
        }
    }

    class DoFileUpload2 extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {

            pDialog = new ProgressDialog(form_bmn.this);
            pDialog.setCancelable(false);
            pDialog.setMessage("Mohon menunggu, sedang mengupload gambar ke-2..");
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {

            try {
                // Set your file path here
                FileInputStream fstrm = new FileInputStream(destFile2);
                // Set your server page url (and the file title/description)
                HttpFileUpload hfu = new HttpFileUpload(konek.getUrl()+"file_upload_kegiatan.php", "ftitle", "fdescription", fname2);
                upflag = hfu.Send_Now(fstrm);
            } catch (FileNotFoundException e) {
                // Error: File not found
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String file_url) {
//            if (pDialog.isShowing()) {
            pDialog.dismiss();
//            }
            if (upflag) {
                Toast.makeText(getApplicationContext(), "Upload gambar ke-2 berhasil", Toast.LENGTH_LONG).show();
                Log.i(TAG, "onPostExecute: Upload gambar ke-2 berhasil");

            } else {
                Toast.makeText(getApplicationContext(), "Sayangnya gambar ke-2 tidak bisa diupload..", Toast.LENGTH_LONG).show();
            }
        }
    }

    class DoFileUpload3 extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {

            pDialog = new ProgressDialog(form_bmn.this);
            pDialog.setCancelable(false);
            pDialog.setMessage("Mohon menunggu, sedang mengupload gambar ke-3..");
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {

            try {
                // Set your file path here
                FileInputStream fstrm = new FileInputStream(destFile3);
                // Set your server page url (and the file title/description)
                HttpFileUpload hfu = new HttpFileUpload(konek.getUrl()+"file_upload_kegiatan.php", "ftitle", "fdescription", fname3);
                upflag = hfu.Send_Now(fstrm);
            } catch (FileNotFoundException e) {
                // Error: File not found
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String file_url) {
//            if (pDialog.isShowing()) {
            pDialog.dismiss();
//            }
            if (upflag) {
                Toast.makeText(getApplicationContext(), "Upload gambar ke-3 berhasil", Toast.LENGTH_LONG).show();
                Log.i(TAG, "onPostExecute: Upload gambar ke-3 berhasil");
            } else {
                Toast.makeText(getApplicationContext(), "Sayangnya gambar ke-3 tidak bisa diupload..", Toast.LENGTH_LONG).show();
            }
        }
    }

    class DoFileUpload4 extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {

            pDialog = new ProgressDialog(form_bmn.this);
            pDialog.setCancelable(false);
            pDialog.setMessage("Mohon menunggu, sedang mengupload gambar ke-4..");
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {

            try {
                // Set your file path here
                FileInputStream fstrm = new FileInputStream(destFile4);
                // Set your server page url (and the file title/description)
                HttpFileUpload hfu = new HttpFileUpload(konek.getUrl()+"file_upload_kegiatan.php", "ftitle", "fdescription", fname4);
                upflag = hfu.Send_Now(fstrm);
            } catch (FileNotFoundException e) {
                // Error: File not found
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String file_url) {
//            if (pDialog.isShowing()) {
            pDialog.dismiss();
//            }
            if (upflag) {
                Toast.makeText(getApplicationContext(), "Upload gambar ke-4 berhasil", Toast.LENGTH_LONG).show();
                Log.i(TAG, "onPostExecute: Upload gambar ke-4 berhasil");
            } else {
                Toast.makeText(getApplicationContext(), "Sayangnya gambar ke-4 tidak bisa diupload..", Toast.LENGTH_LONG).show();
            }
        }
    }

//    private void clickCamera() {
//        Intent cameraintent = new Intent(
//                android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
//        startActivityForResult(cameraintent, 101);
//
//    }

    private void clickCamera(){
        destFile = new File(file, "kegiatan1_"
                + dateFormatter.format(new Date()).toString() + ".png");
        destFile2 = new File(file, "kegiatan2_"
                + dateFormatter.format(new Date()).toString() + ".png");
        destFile3 = new File(file, "kegiatan3_"
                + dateFormatter.format(new Date()).toString() + ".png");
        destFile4 = new File(file, "kegiatan4_"
                + dateFormatter.format(new Date()).toString() + ".png");
        imageCaptureUri = Uri.fromFile(destFile);

        Intent intentCamera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intentCamera.putExtra(MediaStore.EXTRA_OUTPUT, imageCaptureUri);

        if (ivImageCompress.getDrawable()==null){
            Log.i(TAG, "clickCamera: ImageView1 kosong, jalankan requestCode 101");
            startActivityForResult(intentCamera, 101);
            Toast.makeText(getApplicationContext(), "Sisa gambar yang dapat diambil: 4", Toast.LENGTH_SHORT).show();
        } else if (ivImageCompress2.getDrawable()==null){
            Log.i(TAG, "clickCamera: ImageView2 kosong, jalankan requestCode 103");
            startActivityForResult(intentCamera, 103);
            Toast.makeText(getApplicationContext(), "Sisa gambar yang dapat diambil: 3", Toast.LENGTH_SHORT).show();
        } else if (ivImageCompress3.getDrawable()==null){
            Log.i(TAG, "clickCamera: ImageView3 kosong, jalankan requestCode 104");
            startActivityForResult(intentCamera, 104);
            Toast.makeText(getApplicationContext(), "Sisa gambar yang dapat diambil: 2", Toast.LENGTH_SHORT).show();
        } else if (ivImageCompress4.getDrawable()==null){
            Log.i(TAG, "clickCamera: ImageView4 kosong, jalankan requestCode 105");
            startActivityForResult(intentCamera, 105);
            Toast.makeText(getApplicationContext(), "Sisa gambar yang dapat diambil: 1", Toast.LENGTH_SHORT).show();
        }


    }

    private void clickGaleri(){
        //        fname = "kegiatanPascagub_"
//                + dateFormatter.format(new Date()).toString() + ".png";
//        destFile = new File(file, fname);
//
//        imageCaptureUri = Uri.fromFile(destFile);

        Intent intent = new Intent(Intent.ACTION_PICK);

        intent.setType("image/*");

//        intent.setAction(Intent.ACTION_GET_CONTENT);

        if (ivImageCompress.getDrawable()==null){
            Log.i(TAG, "clickCamera: ImageView1 kosong, jalankan requestCode 102");
            startActivityForResult(Intent.createChooser(intent, "Select Image From Gallery"), 102);
            Toast.makeText(getApplicationContext(), "Sisa gambar yang dapat diambil: 4", Toast.LENGTH_SHORT).show();
        } else if (ivImageCompress2.getDrawable()==null){
            Log.i(TAG, "clickCamera: ImageView2 kosong, jalankan requestCode 106");
            startActivityForResult(Intent.createChooser(intent, "Select Image From Gallery"), 106);
            Toast.makeText(getApplicationContext(), "Sisa gambar yang dapat diambil: 3", Toast.LENGTH_SHORT).show();
        } else if (ivImageCompress3.getDrawable()==null){
            Log.i(TAG, "clickCamera: ImageView3 kosong, jalankan requestCode 107");
            startActivityForResult(Intent.createChooser(intent, "Select Image From Gallery"), 107);
            Toast.makeText(getApplicationContext(), "Sisa gambar yang dapat diambil: 2", Toast.LENGTH_SHORT).show();
        } else if (ivImageCompress4.getDrawable()==null){
            Log.i(TAG, "clickCamera: ImageView4 kosong, jalankan requestCode 108");
            startActivityForResult(Intent.createChooser(intent, "Select Image From Gallery"), 108);
            Toast.makeText(getApplicationContext(), "Sisa gambar yang dapat diambil: 1", Toast.LENGTH_SHORT).show();
        }


    }



    private void getDataHbi(){
        class GetDataBMN extends AsyncTask<Void,Void,String> {
            ProgressDialog loading;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(form_bmn.this,"Fetching...","Wait...",false,false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                showData(s);
            }

            @Override
            protected String doInBackground(Void... params) {
                ResultHandler rh = new ResultHandler();
                String s = rh.sendGetRequestParam(konek.getUrl()+"tampil_hbi.php?nomor_hbi=",nomorhbmn);
                return s;
            }
        }
        GetDataBMN ge = new GetDataBMN();
        ge.execute();
    }

    private void showData(String json) {
        try {
            JSONObject jsonObject = new JSONObject(json);
            JSONArray result = jsonObject.getJSONArray(TAG_JSON_ARRAY);
            JSONObject c = result.getJSONObject(0);
            String desk = c.getString(TAG_DESKRIPSI);
            String jum = c.getString(TAG_JUMLAH);
            String merk = c.getString(TAG_MERK);
            /*String type = c.getString(TAG_TYPE);
            String nomor_seri = c.getString(TAG_NOMORSERI);
            String harga_perolehan = c.getString(TAG_HARGA_RP);
            String dolar = c.getString(TAG_HARGA_USD);
            String tanggal = c.getString(TAG_TANGGAL);
            String afe = c.getString(TAG_AFE);
            String lokasi = c.getString(TAG_LOKASI);
            String apo = c.getString(TAG_APO);


            */
            etdeskripsi.setText(desk);
            etjumlah.setText(jum);
            etmerk.setText(merk);
            //ettype.setText(type);
        }catch (JSONException e){
            e.printStackTrace();
        }
    }





    /**
     * This is useful when an image is available in sdcard physically.
     *
     * @param uriPhoto
     * @return
     */
    public String getPathFromUri(Uri uriPhoto) {
        if (uriPhoto == null)
            return null;

        if (SCHEME_FILE.equals(uriPhoto.getScheme())) {
            return uriPhoto.getPath();
        } else if (SCHEME_CONTENT.equals(uriPhoto.getScheme())) {
            final String[] filePathColumn = {MediaStore.MediaColumns.DATA,
                    MediaStore.MediaColumns.DISPLAY_NAME};
            Cursor cursor = null;
            try {
                cursor = getContentResolver().query(uriPhoto, filePathColumn, null, null, null);
                if (cursor != null && cursor.moveToFirst()) {
                    final int columnIndex = (uriPhoto.toString()
                            .startsWith("content://com.google.android.gallery3d")) ? cursor
                            .getColumnIndex(MediaStore.MediaColumns.DISPLAY_NAME)
                            : cursor.getColumnIndex(MediaStore.MediaColumns.DATA);

                    // Picasa images on API 13+
                    if (columnIndex != -1) {
                        String filePath = cursor.getString(columnIndex);
                        if (!TextUtils.isEmpty(filePath)) {
                            return filePath;
                        }
                    }
                }
            } catch (IllegalArgumentException e) {
                // Nothing we can do
                Log.d(TAG, "IllegalArgumentException");
                e.printStackTrace();
            } catch (SecurityException ignored) {
                Log.d(TAG, "SecurityException");
                // Nothing we can do
                ignored.printStackTrace();
            } finally {
                if (cursor != null)
                    cursor.close();
            }
        }
        return null;
    }

    /**
     * This is useful when an image is not available in sdcard physically but it displays into photos application via google drive(Google Photos) and also for if image is available in sdcard physically.
     *
     * @param uriPhoto
     * @return
     */

    public String getPathFromGooglePhotosUri(Uri uriPhoto) {
        if (uriPhoto == null)
            return null;

        FileInputStream input = null;
        FileOutputStream output = null;
        try {
            ParcelFileDescriptor pfd = getContentResolver().openFileDescriptor(uriPhoto, "r");
            FileDescriptor fd = pfd.getFileDescriptor();
            input = new FileInputStream(fd);

            String tempFilename = getTempFilename(this);
            output = new FileOutputStream(tempFilename);

            int read;
            byte[] bytes = new byte[4096];
            while ((read = input.read(bytes)) != -1) {
                output.write(bytes, 0, read);
            }
            return tempFilename;
        } catch (IOException ignored) {
            // Nothing we can do
        } finally {
            closeSilently(input);
            closeSilently(output);
        }
        return null;
    }

    public static void closeSilently(Closeable c) {
        if (c == null)
            return;
        try {
            c.close();
        } catch (Throwable t) {
            // Do nothing
        }
    }

    private static String getTempFilename(Context context) throws IOException {
        File outputDir = context.getCacheDir();
        File outputFile = File.createTempFile("image", "tmp", outputDir);
        return outputFile.getAbsolutePath();
    }

    private void copyFile(File sourceFile, File destFile) throws IOException {
        if (!sourceFile.exists()) {
            return;
        }

        FileChannel source = null;
        FileChannel destination = null;
        source = new FileInputStream(sourceFile).getChannel();
        destination = new FileOutputStream(destFile).getChannel();
        if (destination != null && source != null) {
            destination.transferFrom(source, 0, source.size());
        }
        if (source != null) {
            source.close();
        }
        if (destination != null) {
            destination.close();
        }
    }
}
