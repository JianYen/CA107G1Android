package com.yen.CA107G1.Pet;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.yen.CA107G1.R;
import com.yen.CA107G1.Util.Util;
import com.yen.CA107G1.VO.PetImgVO;
import com.yen.CA107G1.VO.PetVO;
import com.yen.CA107G1.Server.PetLisImageTask;
import com.yen.CA107G1.Server.ServerURL;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class Activity_PetDetail extends AppCompatActivity {
    private PetVO petVO;
    private PetLisImageTask petImgTask;
    private Bitmap bitmap, picture;
    private EditText detailPetName, detailPetType, detailPetBreed, detailPetBirth, detailPetWeight;
    private Button btnSendCancel, btnSendPetInf;
    private CircleImageView detailPetImg;
    private ProgressDialog progressDialog;
    private File file;
    private AsyncTask dataUploadTask;
    private static final int REQ_TAKE_PICTURE = 0;
    private final static int REQ_PERMISSIONS = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pet_detail);

        findViews();
        //取得前一頁帶來的物件
        petVO = (PetVO) getIntent().getSerializableExtra("petVOList");
        if (petVO == null) {
            Toast.makeText(this, "房型詳情無法顯示", Toast.LENGTH_SHORT);
        } else {
            showDetail(petVO);
        }


    }

    public void findViews() {
        detailPetName = findViewById(R.id.detailPetName);
        detailPetType = findViewById(R.id.detailPetType);
        detailPetBreed = findViewById(R.id.detailPetBreed);
        detailPetBirth = findViewById(R.id.detailPetBirth);
        detailPetWeight = findViewById(R.id.detailPetWeight);
        btnSendCancel = findViewById(R.id.btnSendCancel);
        btnSendPetInf = findViewById(R.id.btnSendPetInf);
        detailPetImg = findViewById(R.id.detailPetImg);
    }

    public void showDetail(PetVO petVO) {
        String pet_no = petVO.getPet_no();
        int imageSize = getResources().getDisplayMetrics().widthPixels / 1;
        bitmap = null;

        try {
            petImgTask = new PetLisImageTask(ServerURL.Pet_URL, pet_no, imageSize);
            bitmap = petImgTask.execute().get();

        } catch (Exception e) {
            Log.e("我是PetDetailActivity", e.toString());
        }

        if (bitmap != null) {
            detailPetImg.setImageBitmap(bitmap);
        } else {
            detailPetImg.setImageResource(R.drawable.default_pet);
        }

        detailPetName.setText(petVO.getPet_name());
        detailPetBirth.setText(petVO.getPet_birth().toString());
        detailPetType.setText(petVO.getPet_type());
        detailPetBreed.setText(petVO.getPet_breed());
        detailPetWeight.setText(petVO.getPet_weight().toString());

    }

    public void btnCacncelBack(View view) {
        btnSendCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public void takePicClick(View view) {
        takePicture();
    }
    private void takePicture() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // 指定存檔路徑
        file = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        file = new File(file, "picture.jpg");
        // targeting Android 7.0 (API level 24) and higher,
        // storing images using a FileProvider.
        // passing a file:// URI across a package boundary causes a FileUriExposedException.
        Uri contentUri = FileProvider.getUriForFile(
                this, getPackageName() + ".provider", file);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, contentUri);

        if (isIntentAvailable(this, intent)) {
            startActivityForResult(intent, REQ_TAKE_PICTURE);
        } else {
            Toast.makeText(this, "LALALLALA", Toast.LENGTH_SHORT).show();
        }
    }

    public boolean isIntentAvailable(Context context, Intent intent) {
        PackageManager packageManager = context.getPackageManager();
        List<ResolveInfo> list = packageManager.queryIntentActivities(intent,
                PackageManager.MATCH_DEFAULT_ONLY);
        return list.size() > 0;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                // 手機拍照App拍照完成後可以取得照片圖檔
                case REQ_TAKE_PICTURE:
                    BitmapFactory.Options opt = new BitmapFactory.Options();
                    // inSampleSize值即為縮放的倍數 (數字越大縮越多)
                    opt.inSampleSize = Util.getImageScale(file.getPath(), 640, 1280);
                    picture = BitmapFactory.decodeFile(file.getPath(), opt);
                    detailPetImg.setImageBitmap(picture);

                    byte[] image = Util.bitmapToPNG(picture);
                    if (networkConnected()) {
                        dataUploadTask = new DataUploadTask().execute(ServerURL.PetImgUpLoad_URL, image, petVO.getPet_no());
                    } else {
                        Toast.makeText(this, "HiHi", Toast.LENGTH_SHORT).show();
                    }
                    break;
            }
        }
    }

    private class DataUploadTask extends AsyncTask<Object, Integer, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(Activity_PetDetail.this);
            progressDialog.setMessage("圖片上傳中...");
            progressDialog.show();
        }

        @Override
        protected String doInBackground(Object... params) {
            String url = params[0].toString();
            byte[] image = (byte[]) params[1];
            String pet_no = params[2].toString();
            String jsonIn;
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("imgBase64", Base64.encodeToString(image, Base64.DEFAULT));
            jsonObject.addProperty("pet_no", pet_no);
            try {
                jsonIn = getRemoteData(url, jsonObject.toString());
            } catch (IOException e) {
                Log.e("我是PET頭像上傳", e.toString());
                return null;
            }

            return null;
        }

        protected void onPostExecute(PetImgVO petImgVO) {
            Bitmap bitmap = BitmapFactory.decodeByteArray(petImgVO.getImage(), 0,
                    petImgVO.getImage().length);
            detailPetImg.setImageBitmap(bitmap);
            progressDialog.cancel();
        }
    }

    private boolean networkConnected() {
        ConnectivityManager conManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = conManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }


    private String getRemoteData(String url, String jsonOut) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
        connection.setDoInput(true); // allow inputs
        connection.setDoOutput(true); // allow outputs
        connection.setUseCaches(false); // do not use a cached copy
        connection.setRequestMethod("POST");
        connection.setRequestProperty("charset", "UTF-8");
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(connection.getOutputStream()));
        bw.write(jsonOut);
        Log.d("TAG111", "jsonOut: " + jsonOut);
        bw.close();

        int responseCode = connection.getResponseCode();
        StringBuilder jsonIn = new StringBuilder();
        if (responseCode == 200) {
            BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;
            while ((line = br.readLine()) != null) {
                jsonIn.append(line);
            }
        } else {
            Log.d("TAG22222", "response code: " + responseCode);
        }
        connection.disconnect();
        Log.d("TAG3333333", "jsonIn: " + jsonIn);
        return jsonIn.toString();
    }

    @Override
    protected void onPause() {
        if (dataUploadTask != null) {
            dataUploadTask.cancel(true);
        }
        super.onPause();
    }

    @Override
    protected void onStart() {
        super.onStart();
        askPermissions();
    }

    private void askPermissions() {
        String[] permissions = {
                Manifest.permission.WRITE_EXTERNAL_STORAGE
        };

        int result = ContextCompat.checkSelfPermission(this, permissions[0]);
        if (result != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    this,
                    permissions,
                    REQ_PERMISSIONS);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQ_PERMISSIONS:
                String text = "";
                for (int i = 0; i < grantResults.length; i++) {
                    if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                        text += permissions[i] + "\n";
                    }
                }
                if (!text.isEmpty()) {
                    text += "no granted";
                    Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
}
