package com.bytedance.practice5;


import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bytedance.practice5.model.Message;
import com.bytedance.practice5.model.UploadResponse;
import com.facebook.drawee.view.SimpleDraweeView;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Multipart;

public class UploadActivity extends AppCompatActivity {
    private static final String TAG = "chapter5";
    private static final long MAX_FILE_SIZE = 2 * 1024 * 1024;
    private static final int REQUEST_CODE_COVER_IMAGE = 101;
    private static final String COVER_IMAGE_TYPE = "image/*";
    private IApi api;
    private Uri coverImageUri;
    private SimpleDraweeView coverSD;
    private EditText toEditText;
    private EditText contentEditText ;
    private Retrofit retrofit;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initNetwork();
        setContentView(R.layout.activity_upload);
        coverSD = findViewById(R.id.sd_cover);
        toEditText = findViewById(R.id.et_to);
        contentEditText = findViewById(R.id.et_content);
        findViewById(R.id.btn_cover).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFile(REQUEST_CODE_COVER_IMAGE, COVER_IMAGE_TYPE, "选择图片");
            }
        });


        findViewById(R.id.btn_submit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submit();
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (REQUEST_CODE_COVER_IMAGE == requestCode) {
            if (resultCode == Activity.RESULT_OK) {
                coverImageUri = data.getData();
                coverSD.setImageURI(coverImageUri);

                if (coverImageUri != null) {
                    Log.d(TAG, "pick cover image " + coverImageUri.toString());
                } else {
                    Log.d(TAG, "uri2File fail " + data.getData());
                }

            } else {
                Log.d(TAG, "file pick fail");
            }
        }
    }

    private void initNetwork() {
        //TODO 3
        // 创建Retrofit实例
        retrofit = new Retrofit.Builder()
                .baseUrl("https://api-sjtu-camp-2021.bytedance.com/homework/invoke/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        // 生成api对象
        api = retrofit.create(IApi.class);
    }

    private void getFile(int requestCode, String type, String title) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType(type);
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, false);
        intent.putExtra(Intent.EXTRA_TITLE, title);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(intent, requestCode);
    }

    private void submit() {
        byte[] coverImageData = readDataFromUri(coverImageUri);
        if (coverImageData == null || coverImageData.length == 0) {
            Toast.makeText(this, "封面不存在", Toast.LENGTH_SHORT).show();
            return;
        }
        String to = toEditText.getText().toString();
        if (TextUtils.isEmpty(to)) {
            Toast.makeText(this, "请输入TA的名字", Toast.LENGTH_SHORT).show();
            return;
        }
        String content = contentEditText.getText().toString();
        if (TextUtils.isEmpty(content)) {
            Toast.makeText(this, "请输入想要对TA说的话", Toast.LENGTH_SHORT).show();
            return;
        }

        if ( coverImageData.length >= MAX_FILE_SIZE) {
            Toast.makeText(this, "文件过大", Toast.LENGTH_SHORT).show();
            return;
        }
        //TODO 5
        // 使用api.submitMessage()方法提交留言
        // 如果提交成功则关闭activity，否则弹出toast
        new Thread(new Runnable() {
            @Override
            public void run() {
                MultipartBody.Part pto = MultipartBody.Part.createFormData("to", to);
                MultipartBody.Part pfrom = MultipartBody.Part.createFormData("from", Constants.USER_NAME);
                MultipartBody.Part pcontent = MultipartBody.Part.createFormData("content", content);
                RequestBody tbody = RequestBody.create(MediaType.parse("image"), coverImageData);
                MultipartBody.Part pcoverImageData = MultipartBody.Part.createFormData("coverImageData",
                        "coverIt.png", RequestBody.create(MediaType.parse("multipart/form-data"), coverImageData));

                Call<UploadResponse> call = api.submitMessage(Constants.STUDENT_ID,
                        "",
                        pfrom,
                        pto,
                        pcontent,
                        pcoverImageData,
                        "U0pUVS1ieXRlZGFuY2UtYW5kcm9pZA==");
                try {
                    Response<UploadResponse> response = call.execute();
                    Message msg = response.body().message;
                    if (response.isSuccessful() && response.body().success) {
                        finish();
                    }
                    else {
                        new Handler(getMainLooper()).post(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getApplicationContext(), "Upload message wrong!", Toast.LENGTH_SHORT).show();
                                Log.e(TAG, response.body().error);
                            }
                        });

                    }
                } catch (IOException e) {
                    Log.i(TAG, "E/wrong when upload");
                }

            }
        }).start();
    }


    // TODO 7 选做 用URLConnection的方式实现提交
    private void submitMessageWithURLConnection(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                String urlStr =
                        String.format("https://api-sjtu-camp-2021.bytedance.com/homework/invoke/messages?student_id=%s&extra_value=", Constants.STUDENT_ID);
                List<Message> result = null;
                try {
                    URL url = new URL(urlStr);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setConnectTimeout(6000);
                    conn.setRequestMethod("Post");
                    conn.setRequestProperty("token", "U0pUVS1ieXRlZGFuY2UtYW5kcm9pZA==");
                    byte[] coverImageData = readDataFromUri(coverImageUri);
                    if (coverImageData == null || coverImageData.length == 0) {
                        Toast.makeText(getApplicationContext(), "封面不存在", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    String to = toEditText.getText().toString();
                    if (TextUtils.isEmpty(to)) {
                        Toast.makeText(getApplicationContext(), "请输入TA的名字", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    String content = contentEditText.getText().toString();
                    if (TextUtils.isEmpty(content)) {
                        Toast.makeText(getApplicationContext(), "请输入想要对TA说的话", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if ( coverImageData.length >= MAX_FILE_SIZE) {
                        Toast.makeText(getApplicationContext(), "文件过大", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    Map<String, String> requestText = new HashMap<String, String>();
                    requestText.put("to", to);
                    requestText.put("content", content);
                    requestText.put("from", Constants.USER_NAME);
                    Map<String, MultipartBody> requestFile = new HashMap<String, MultipartBody>();



                } catch (Exception e) {

                }

            }
        }).start();
    }


    private byte[] readDataFromUri(Uri uri) {
        byte[] data = null;
        try {
            InputStream is = getContentResolver().openInputStream(uri);
            data = Util.inputStream2bytes(is);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }


}
