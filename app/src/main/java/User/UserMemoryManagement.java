package User;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.selflearn.alwarrenter.R;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import FirebaseConnectivity.StorageDevice;
import Utils.CustomDialogMaker;
import Utils.CustomProgressDialog;
import Utils.SettingMemoryData;
import de.hdodenhof.circleimageview.CircleImageView;

public class UserMemoryManagement {

    private static boolean PERMISSION_GRANTED;
    Context context;
    private static final String TAG = "UserMemoryManagement";
    private int requestCode = 1234;

    public UserMemoryManagement(Context context)
    {
        this.context = context;
    }
    
    public  String saveToInternalStorage(Bitmap bitmapImage){
        ContextWrapper cw = new ContextWrapper(context.getApplicationContext());
        // path to /data/data/yourApp/app_data/imageDir
        File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
        // Create imageDir
        File myPath=new File(directory,"profile.jpg");

        if (myPath.exists()) {
            Toast.makeText(context.getApplicationContext(), "folder exists", Toast.LENGTH_LONG).show();
            Log.d("path",myPath.getAbsolutePath());
        }

        FileOutputStream fos = null;
        try {
            fos = context.openFileOutput("Profile" , Context.MODE_PRIVATE);
            // Use the compress method on the BitMap object to write image to the OutputStream
            bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fos);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return directory.getAbsolutePath();
    }

    public static void fetchFromDataBase(final String account_type , String user_id, final Context context , final CircleImageView circleImageView)
    {

        if(account_type == null || user_id == null )
        {
            new CustomDialogMaker((Activity) context).createAndShowDialogWarning(" you are accessing invalid account or deleted account " +
                    "if you have not deleted your account then please make sure to mail us ");
         return;
        }
        final CustomProgressDialog customProgressDialog = new CustomProgressDialog((Activity) context);
        //customProgressDialog.startLoadingDailog();
        FirebaseStorage storage = FirebaseStorage.getInstance();

        StorageReference storageReference = storage.getReference("ProfileImages");

        StorageReference ref = storageReference.child(account_type).child(user_id).child("Profile");


        ref.getBytes(1024 * 1024 * 5).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                Bitmap bm = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                circleImageView.setImageBitmap(bm);
                //customProgressDialog.dismissDialog();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("error",e.getMessage()+ " "+ e.getClass().getName());
                //customProgressDialog.dismissDialog();

                AlertDialog dialog = new AlertDialog.Builder(context).create();
                dialog.setMessage("please upload your profile picture");
                dialog.setCancelable(false);
                dialog.setButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        Uri filePath = null;

                        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources() , R.drawable.demoimage);

                        if (bitmap != null)
                        {
                            takePermission(bitmap , context , account_type);
                        }
                        else Log.d(TAG, "onClick: bitmap is getting null");


                    }
                });
                dialog.show();
            }
        });
    }

    private static void takePermission(Bitmap bitmap, Context context, String account_type) {

        new UserMemoryManagement(context).getPermission();

        if (!PERMISSION_GRANTED) {
            new UserMemoryManagement(context).getPermission();
        }
        else {
            Log.d(TAG, "takePermission: uploading profile");
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
            String path = MediaStore.Images.Media.insertImage(context.getContentResolver(), bitmap, "Title", null);
            Uri filePath = Uri.parse(path);
            StorageDevice.UploadUserProfilePicture(account_type, new SettingMemoryData(context).getSharedPrefString(String.valueOf(R.string.KEY_USER_ID)
            ), context, filePath);
        }
    }

    private  void getPermission() {

        if (!(ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED) &&
                !(ContextCompat.checkSelfPermission(context , Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        ==PackageManager.PERMISSION_GRANTED) &&
                !(ContextCompat.checkSelfPermission(context , Manifest.permission.READ_EXTERNAL_STORAGE)
                        ==PackageManager.PERMISSION_GRANTED)
        ){
                ActivityCompat.requestPermissions((Activity) context, new String[] {Manifest.permission.CAMERA , Manifest.permission.WRITE_EXTERNAL_STORAGE}, requestCode);
        }else {
            PERMISSION_GRANTED = true;
        }
    }

}
