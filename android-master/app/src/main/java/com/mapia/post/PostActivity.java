package com.mapia.post;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.mapia.MainActivity;
import com.mapia.R;
import com.mapia.common.CommonConstants;
import com.mapia.s3.Util;
import com.mapia.s3.network.TransferController;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by daehyun on 15. 6. 8..
 */
public class PostActivity extends MainActivity{

    final int REQ_CODE_SELECT_IMAGE=100;
    private LatLng latlng;
    private String address;
    HighlightEditText edtPost;
    RelativeLayout btnPostSubmit;
    ImageView btnPostCancel;
    ImageButton btnPostPhoto, btnPostVideo;
    private ArrayList<String> mFileNameList = new ArrayList<String>();


    public LatLng getLatLng(){
        return this.latlng;
    }
    public String getAddress(){
        if(address != null) {
            return this.address;
        }
        return getLatLng().toString();
    }
    @Override
    protected void onCreate(Bundle bundle){
        super.onCreate(null);
        this.setContentView(R.layout.activity_main);
        this.init(5);
        this.mainApplication.setPostActivity(this);
        this.latlng = (LatLng) getIntent().getParcelableExtra("latlng");
//        this.address =  getIntent().getParcelableExtra("address");

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        this.mainApplication.setPostActivity(null);
    }

    //선택한 이미지 데이터 받기
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        Toast.makeText(getBaseContext(), "resultCode : " + resultCode, Toast.LENGTH_LONG).show();


        if(requestCode == CommonConstants.POST_PIC){
            if(resultCode == CommonConstants.POST_PIC_SUC){

            }
        }
        if(requestCode == REQ_CODE_SELECT_IMAGE)
        {
            if(resultCode== Activity.RESULT_OK && data != null)
            {

                Uri uri = data.getData();
                if (uri != null) {
                    TransferController.upload(this, uri);

                    String uriString = uri.toString();
                    mFileNameList.add(Util.getFileName(uriString));
                    Log.i("file", uriString);
                    Log.i("file", mFileNameList.toString());
                }

                try {
                    //Uri에서 이미지 이름을 얻어온다.
                    //String name_Str = getImageNameToUri(data.getData());

                    //이미지 데이터를 비트맵으로 받아온다.
                    Bitmap image_bitmap 	= MediaStore.Images.Media.getBitmap(getContentResolver(), data.getData());
                    ImageView image = (ImageView)findViewById(R.id.imageView1);

                    //배치해놓은 ImageView에 set
                    image.getLayoutParams().height = 200;
                    image.setImageBitmap(image_bitmap);


                    //Toast.makeText(getBaseContext(), "name_Str : "+name_Str , Toast.LENGTH_SHORT).show();

                } catch (FileNotFoundException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        }
    }



//    @Override
//    public void onClick(View view) {
//        switch(view.getId()){
//            case R.id.btnPostSubmit:
//                Intent intent = new Intent();
//                intent.putExtra("comment",edtPost.getText().toString());
//                intent.putExtra("latlng",latlng);
//                if(mFileNameList.size() > 0)
//                    intent.putExtra("filelist",mFileNameList);
//                setResult(RESULT_OK, intent);
//                finish();
//                break;
//            case R.id.btnPostCancel:
//                setResult(RESULT_CANCELED);
//                finish();
//                break;
//            case R.id.btnPostPhoto:
//                Intent intentImage = new Intent(Intent.ACTION_GET_CONTENT);
//                intentImage.setType("image/*");
//                startActivityForResult(intentImage, REQ_CODE_SELECT_IMAGE); //REQ_CODE_SELECT_IMAGE);
//                break;
////            case R.id.actBtnAddVideo:
////                Intent intentVideo = new Intent(Intent.ACTION_GET_CONTENT);
////                intentVideo.setType("video/*");
////                startActivityForResult(intentVideo, 0);
////                break;
//
//        }
//    }

}
