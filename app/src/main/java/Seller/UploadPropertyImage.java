package Seller;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.selflearn.alwarrenter.R;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import FirebaseConnectivity.StorageDevice;
import Utils.CustomDialogMaker;
import Utils.CustomProgressDialog;
import Utils.SettingMemoryData;

public class UploadPropertyImage extends AppCompatActivity {

    private static final int PICK_IMAGE = 100;
    private static final int REQUEST_IMAGE_CAPTURE =  102;
    private static boolean PERMISSION_GRANTED = false;
    RecyclerView recyclerView;
    ImageView camera , gallery;
    Bitmap bitmap;
    Button next;
    List<Bitmap> bitmapList ;
    private int requestCode = 101;
    CustomProgressDialog customProgressDialog;
    private static final String TAG = "UploadPropertyImage";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_property_images);

        bitmapList = new ArrayList<>();
        customProgressDialog = new CustomProgressDialog(UploadPropertyImage.this);
        findID();
        getPermission();
        setListeners();

    }

    private void getPermission() {
        if (!(ContextCompat.checkSelfPermission(UploadPropertyImage.this, Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED) &&
                !(ContextCompat.checkSelfPermission(UploadPropertyImage.this , Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        ==PackageManager.PERMISSION_GRANTED) &&
                !(ContextCompat.checkSelfPermission(UploadPropertyImage.this , Manifest.permission.READ_EXTERNAL_STORAGE)
                        ==PackageManager.PERMISSION_GRANTED)
        ){
            ActivityCompat.requestPermissions(UploadPropertyImage.this, new String[] {Manifest.permission.CAMERA , Manifest.permission.WRITE_EXTERNAL_STORAGE}, requestCode);
        }else {
            PERMISSION_GRANTED = true;
        }
    }

    private void setListeners() {

        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // opening camera
                if (PERMISSION_GRANTED)
                {
                    openCameraAndGetImage();
                }else {
                    getPermission();
                }
            }
        });

        gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // opening gallery;
                openGalleryAndGetImage();

            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(this , RecyclerView.HORIZONTAL , false));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(new ImageRecyclerViewAdapter(UploadPropertyImage.this , bitmapList));

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bitmapList.size() < 4)
                {
                    new CustomDialogMaker(UploadPropertyImage.this).createAndShowDialogWarning("please upload at least 4 image");
                }else
                {
                    String dirName;
                    Date today = new Date();
                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss a");
                     dirName = format.format(today);

                     customProgressDialog.dismissDialog();
                     customProgressDialog.startLoadingDailog();

                    for(int i=0 ; i <bitmapList.size() ; i++)
                    {
                        StorageDevice.UploadImageOfProperty("seller" ,
                                new SettingMemoryData(UploadPropertyImage.this).getSharedPrefString(String.valueOf(R.string.KEY_USER_ID)),
                                        UploadPropertyImage.this ,
                                        bitmapList.get(i),
                                        dirName
                                );
                    }
                    customProgressDialog.dismissDialog();

                    Log.d(TAG, "onClick: "+bitmapList.size());
                    returnToFormUI(dirName , bitmapList.size());
                }
            }
        });
    }

    private void returnToFormUI(String dirName, int size) {

//        customProgressDialog.dismissDialog();
        Intent intent = new Intent();
        Log.d(TAG, "returnToFormUI: "+bitmapList.size());
        intent.putExtra("numImage" ,size);
        intent.putExtra("dirName" , dirName);
        setResult(RESULT_OK , intent);
        finish();

    }

    private void openCameraAndGetImage() {

        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }

    }


    private void openGalleryAndGetImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);
    }

    private void findID() {

        camera = findViewById(R.id.click_from_camera);
        gallery = findViewById(R.id.choose_from_galary);
        next = findViewById(R.id.next_btn);
        recyclerView = findViewById(R.id.recycler_view_images);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == this.requestCode)
        {
            if (grantResults.length > 0)
            {
                for (int i=0 ; i <grantResults.length ; i++)
                {
                    if (grantResults[i] != PackageManager.PERMISSION_GRANTED)
                    {
                        PERMISSION_GRANTED = false;
                        return;
                    }
                }
                PERMISSION_GRANTED = true;
            }
        }
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if(requestCode == PICK_IMAGE && resultCode == RESULT_OK)
        {
            Uri uri = data.getData();
            Bitmap bitmap = null;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
                bitmapList.add(bitmap);
                recyclerView.setAdapter(new ImageRecyclerViewAdapter(UploadPropertyImage.this , bitmapList));
                Log.d(TAG, "onActivityResult: " + bitmapList.size());
            } catch (IOException e) {
                e.printStackTrace();
            }


        }else if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK)
        {
            bitmap = (Bitmap) data.getExtras().get("data");
            bitmapList.add(bitmap);
            recyclerView.setAdapter(new ImageRecyclerViewAdapter(UploadPropertyImage.this , bitmapList));
            Log.d(TAG, "onActivityResult: " + bitmapList.size());
        }else
        {
            Log.d(TAG, "onActivityResult: error while loading image ");
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onDestroy() {
        customProgressDialog.dismissDialog();
        super.onDestroy();
    }
}
