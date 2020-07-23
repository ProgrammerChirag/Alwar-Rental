package User;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import Utils.CustomDialogMaker;
import Utils.CustomProgressDialog;
import de.hdodenhof.circleimageview.CircleImageView;

public class UserMemoryManagement {
    
    Context context;
    
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

    public static void fetchFromDataBase(String account_type , String user_id, final Context context , final CircleImageView circleImageView)
    {

        if(account_type == null || user_id == null )
        {
            new CustomDialogMaker((Activity) context).createAndShowDialogWarning(" you are accessing invalid account or deleted account " +
                    "if you have not deleted your account then please make sure to mail us ");
         return;
        }
        final CustomProgressDialog customProgressDialog = new CustomProgressDialog((Activity) context);
        customProgressDialog.startLoadingDailog();
        FirebaseStorage storage = FirebaseStorage.getInstance();

        StorageReference storageReference = storage.getReference("ProfileImages");

        StorageReference ref = storageReference.child(account_type).child(user_id).child("Profile");


        ref.getBytes(1024 * 1024 * 5).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                Bitmap bm = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                circleImageView.setImageBitmap(bm);
                customProgressDialog.dismissDialog();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("error",e.getMessage()+ " "+ e.getClass().getName());
                customProgressDialog.dismissDialog();

                AlertDialog dialog = new AlertDialog.Builder(context).create();
                dialog.setMessage("please upload your profile picture");
                dialog.setCancelable(false);
                dialog.setButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                dialog.show();

            }
        });
    }



}
