package com.example.john.foodtruck;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class FT_CreateActivity extends AppCompatActivity {
    Bitmap encodebitmap;
    String ftName = "";
    String ftPhone = "";
    String ftArea = "";
    String ftCtg = "";
    String ftIntro = "";
    String currentID = "";
    String ftPhoto="";
    Bitmap photo;
    Integer t=0;
    private ImageView fd_photo;

    int id_view;
    private Uri mImageCaptureUri;
    private static final int PICK_FROM_CAMERA = 0;
    private static final int PICK_FROM_ALBUM = 1;
    private static final int CROP_FROM_IMAGE = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ft__create);

        final MyApplication myApp = (MyApplication) getApplication();

        final EditText nameText = (EditText) findViewById(R.id.nameText);
        final EditText phoneText = (EditText) findViewById(R.id.phoneText);
        final EditText introText = (EditText) findViewById(R.id.introText);
        fd_photo = (ImageView) findViewById(R.id.imageView);
        fd_photo.setImageResource(android.R.drawable.ic_menu_report_image);

        if(!myApp.getTempFTphone().equals("-1")){
            nameText.setText(myApp.getTempFTname());
            phoneText.setText(myApp.getTempFTphone());
            introText.setText(myApp.getTempFTintro());

            byte[] encodebytearray = Base64.decode(myApp.getTempFTphoto(),Base64.DEFAULT);
            encodebitmap = BitmapFactory.decodeByteArray(encodebytearray,0,encodebytearray.length);
            fd_photo.setImageBitmap(encodebitmap);
        }

        currentID = myApp.getcurrentID();

        String[] tempArea = getResources().getStringArray(R.array.areaSpinnerArray);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, tempArea);
        final Spinner areaSpinner = (Spinner) findViewById(R.id.areaSpinner);
        areaSpinner.setAdapter(adapter);

        if(!myApp.getTempFTphone().equals("-1")){
            areaSpinner.setSelection(myApp.getTempFTarea());
        }

        areaSpinner.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        ftArea = String.valueOf(position);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                    }
                }
        );

        String[] tempCtg = getResources().getStringArray(R.array.ctgSpinnerArray);
        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, tempCtg);
        final Spinner ctgSpinner = (Spinner) findViewById(R.id.ctgSpinner);
        ctgSpinner.setAdapter(adapter2);

        if(!myApp.getTempFTphone().equals("-1")){
            ctgSpinner.setSelection(myApp.getTempFTctg());
        }

        ctgSpinner.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        ftCtg = String.valueOf(position);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                }
        );

        Button registerBtn = (Button) findViewById(R.id.registerButton);

        Button backButton = (Button) findViewById(R.id.backButton);
        Button logoutButton = (Button) findViewById(R.id.logoutButton);

        TextView title = (TextView) findViewById(R.id.title);
        title.setText("FoodTruck");

        registerBtn.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                ftName = nameText.getText().toString();
                ftPhone = phoneText.getText().toString();
                ftIntro = introText.getText().toString();
                if(t==1){
                    ByteArrayOutputStream byteArrayOS = new ByteArrayOutputStream();
                    photo.compress(Bitmap.CompressFormat.PNG,100,byteArrayOS);
                    byte[] byteArray = byteArrayOS.toByteArray();
                    ftPhoto = Base64.encodeToString(byteArray,Base64.DEFAULT);
                    new rTask().execute();
                }
                else {
                    AlertDialog.Builder alert1 = new AlertDialog.Builder(FT_CreateActivity.this);
                    alert1.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();     //닫기
                        }
                    });
                    alert1.setMessage("사진을 등록해 주십시오.");
                    alert1.show();
                }
            }
        });

        fd_photo.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                id_view = v.getId();
                if (id_view == R.id.imageView){
                    DialogInterface.OnClickListener cameraListener = new DialogInterface.OnClickListener(){

                        @Override
                        public void onClick(DialogInterface dialogInterface, int which) {
                            Log.d("camera_dialog_click", String.valueOf(123));
                            doTakePhotoAction();
                        }
                    };

                    DialogInterface.OnClickListener albumListener = new DialogInterface.OnClickListener(){
                        @Override
                        public void onClick(DialogInterface dialogInterface, int which) {
                            doTakeAlbumAction();
                        }
                    };

                    DialogInterface.OnClickListener cancelListener = new DialogInterface.OnClickListener(){
                        @Override
                        public void onClick(DialogInterface dialogInterface, int which) {
                            // photo 선택 cancel 시 오류 발생
                            dialogInterface.dismiss();
                            fd_photo.setImageBitmap(encodebitmap);
                        }
                    };

                    new AlertDialog.Builder(FT_CreateActivity.this)
                            .setTitle("푸드트럭 사진 선택")
                            .setPositiveButton("사진촬영",cameraListener)
                            .setNeutralButton("앨범선택",albumListener)
                            .setNegativeButton("취소",cancelListener)
                            .show(); //여기서 보여줌~
                }
            }
        });

        backButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                // backpress와 똑같이
                finish();
            }
        });

        logoutButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                final MyApplication myApp = (MyApplication) getApplication();
                myApp.setcurrentID("");
                Intent loginintent =  new Intent(FT_CreateActivity.this, LoginActivity.class);
                FT_CreateActivity.this.startActivity(loginintent);
                finish();
            }
        });
    }

    private void doTakeAlbumAction() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType(android.provider.MediaStore.Images.Media.CONTENT_TYPE);
        startActivityForResult(intent,PICK_FROM_ALBUM);
    }

    private void doTakePhotoAction() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        String url = "tmp_" + String.valueOf(System.currentTimeMillis()) + ".jpg";
        mImageCaptureUri = Uri.fromFile(new File(Environment.getExternalStorageDirectory(),url));
        intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, mImageCaptureUri);
        startActivityForResult(intent,PICK_FROM_CAMERA);
    }

    @Override
    public void onActivityResult(int requestCode,int resultCode,Intent data) {
        super.onActivityResult(requestCode,resultCode,data);
        if(resultCode != RESULT_OK)
            return;

        switch(requestCode){
            case PICK_FROM_ALBUM:
            {
                mImageCaptureUri = data.getData();
                Log.d("SmartWheel",mImageCaptureUri.getPath().toString());
            }

            case PICK_FROM_CAMERA:
            {
                Intent intent = new Intent("com.android.camera.action.CROP");
                intent.setDataAndType(mImageCaptureUri,"image/");

                intent.putExtra("outputX",200);
                intent.putExtra("outputY",150);
                intent.putExtra("aspectX",4);
                intent.putExtra("aspectY",3);
                intent.putExtra("scale",true);
                intent.putExtra("return-data",true);
                startActivityForResult(intent,CROP_FROM_IMAGE);
                break;
            }

            case CROP_FROM_IMAGE:
            {//crop 이후의 이미지를 받는다.

                if (resultCode != RESULT_OK){
                    return;
                }
                final Bundle extras = data.getExtras();

                String filePath = Environment.getExternalStorageDirectory().getAbsolutePath() +
                        "/SmartWheel/" + System.currentTimeMillis()+".jpg";

                if (extras != null)
                {

                    photo  = extras.getParcelable("data");
                    fd_photo.setImageBitmap(photo);
                    t=1;
                    break;
                }
            }
        }
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    private class rTask extends AsyncTask<String, Void, Integer> {
        int r = -1;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Integer doInBackground(String... strings) {
            try {
                r = postJsonToServer(ftName, ftPhone, ftArea, ftCtg, ftIntro, currentID, ftPhoto);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return r;
        }

        @Override
        protected void onPostExecute(Integer result) {
            AlertDialog.Builder alert = new AlertDialog.Builder(FT_CreateActivity.this);

            switch (result){
                case 0 :
                    alert.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                            Intent intent = new Intent(FT_CreateActivity.this, Main2Activity.class);
                            FT_CreateActivity.this.startActivity(intent);
                        }
                    });
                    alert.setMessage("푸드트럭 정보가 저장되었습니다.");
                    alert.show();

                    break;
                case 1:
                    alert.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();     //닫기
                        }
                    });
                    alert.setMessage("이미 등록된 매장 번호입니다.");
                    alert.show();
                    break;
                case 2:
                    alert.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();     //닫기
                        }
                    });
                    alert.setMessage("주 활동 지역을 선택하여 주십시오.");
                    alert.show();
                    break;
                case 3:
                    alert.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();     //닫기
                        }
                    });
                    alert.setMessage("모든 칸을 입력하여 주십시오.");
                    alert.show();
                    break;
                case 4:
                    alert.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();     //닫기
                        }
                    });
                    alert.setMessage("카테고리를 선택하여 주십시오.");
                    alert.show();
                    break;
                case 5:
                    alert.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();     //닫기
                        }
                    });
                    alert.setMessage("사진을 등록해 주십시오.");
                    alert.show();
                    break;
            }
        }
    }

    public int postJsonToServer(String name, String phone, String area, String ctg, String introduction, String currentID, String photo) throws IOException {

        ArrayList<NameValuePair> registerInfo = new ArrayList<NameValuePair>();
        registerInfo.add(new BasicNameValuePair("name", name));
        registerInfo.add(new BasicNameValuePair("phone", phone));
        registerInfo.add(new BasicNameValuePair("area", area));
        registerInfo.add(new BasicNameValuePair("ctg", ctg));
        registerInfo.add(new BasicNameValuePair("introduction", introduction));
        registerInfo.add(new BasicNameValuePair("id", currentID));
        registerInfo.add(new BasicNameValuePair("photo", photo));


        // 연결 HttpClient 객체 생성
        HttpClient httpClient = new DefaultHttpClient();

        // server url 받기
        String serverURL = getResources().getString(R.string.serverURL);
        HttpPost httpPost = new HttpPost(serverURL + "/foodtruck_enroll");

        //HttpPost httpPost = new HttpPost("http://143.248.199.31:8081/foodtruck_enroll");

        // 객체 연결 설정 부분, 연결 최대시간 등등
        //HttpParams params = client.getParams();
        //HttpConnectionParams.setConnectionTimeout(params, 5000);
        //HttpConnectionParams.setSoTimeout(params, 5000);

        // Post객체 생성


        try {
            UrlEncodedFormEntity entity = new UrlEncodedFormEntity(registerInfo, "UTF-8");
            httpPost.setEntity(entity);
            //httpClient.execute(httpPost);

            HttpResponse response = httpClient.execute(httpPost);
            String responseString = EntityUtils.toString(response.getEntity(), HTTP.UTF_8);

            if (responseString.contains("0")) {
                //Log.d("성", responseString);
                return 0;
            } else if (responseString.contains("1")) {
                //Log.d("휴대폰", responseString);
                return 1;
            } else if (responseString.contains("2")) {
                //Log.d("지역", responseString);
                return 2;
            } else if (responseString.contains("3")) {
                //Log.d("빈칸", responseString);
                return 3;
            } else if (responseString.contains("4")) {
                //Log.d("카테고리", responseString);
                return 4;
            } else if (responseString.contains("5")) {
                //Log.d("사진", responseString);
                return 5;
            } else {
                Log.d("Unknown Error", responseString);
                return -1;
            }

        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return -1;
    }
}


