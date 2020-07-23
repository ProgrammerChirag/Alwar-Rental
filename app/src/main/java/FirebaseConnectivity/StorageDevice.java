package FirebaseConnectivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;

import Utils.CustomProgressDialog;

public class StorageDevice {

//    Context context ;
//private static boolean uploaded = false;

    private static final String TAG = "StorageDevice";

    public static void UploadImage(String account_type , String user_id , final Context context, final Uri filePath )
   {

       FirebaseStorage storage = FirebaseStorage.getInstance();

       StorageReference storageReference = storage.getReference(account_type);


       final ProgressDialog progressDialog = new ProgressDialog(context);
       progressDialog.setTitle("uploading");
       progressDialog.show();

       StorageReference ref = storageReference.child(user_id).child(filePath.toString().substring(filePath.toString().lastIndexOf("/")+1));

       ref.putFile(filePath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
           @Override
           public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
               progressDialog.dismiss();
               Toast.makeText(context.getApplicationContext() , "image uploaded" , Toast.LENGTH_LONG).show();
//               uploaded = true;
           }
       }).addOnFailureListener(new OnFailureListener() {
           @Override
           public void onFailure(@NonNull Exception e) {

               Log.d("error",e.getMessage()+e.getClass().getName());
//               uploaded = false;

           }
       }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
           @Override
           public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {
               double progress
                       = (100.0
                       * taskSnapshot.getBytesTransferred()
                       / taskSnapshot.getTotalByteCount());
               progressDialog.setMessage(
                       "Uploaded "
                               + (int)progress + "%");


           }
       });
   }

   public static void  UploadUserProfilePicture(String account_type , String user_id, final Context context,final Uri filePath ){

       FirebaseStorage storage = FirebaseStorage.getInstance();

       StorageReference storageReference = storage.getReference("ProfileImages");

       final CustomProgressDialog customProgressDialog = new CustomProgressDialog(((Activity) (context)));

       customProgressDialog.startLoadingDailog();

       StorageReference ref = storageReference.child(account_type).child(user_id).child("Profile");


       ref.putFile(filePath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
           @Override
           public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
               customProgressDialog.dismissDialog();
               Toast.makeText(context.getApplicationContext() , "Profile uploaded" , Toast.LENGTH_LONG).show();
//               customProgressDialog.dismissDialog();
//               uploaded = true;


           }
       }).addOnFailureListener(new OnFailureListener() {
           @Override
           public void onFailure(@NonNull Exception e) {

               Log.d("error",e.getMessage()+e.getClass().getName());
               customProgressDialog.dismissDialog();
//               uploaded = false;

           }
       }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
           @Override
           public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {
               Log.d("progress" , "uploading in progress");
               customProgressDialog.dismissDialog();
           }
       });

   }

   public static void UploadImageOfProperty(String account_type , final String seller_id, final Context context, Bitmap bitmap , final String dirName)
   {

       FirebaseStorage storage = FirebaseStorage.getInstance();

       StorageReference storageReference = storage.getReference("SellerPropertyImages");

       //       customProgressDialog.startLoadingDailog();
       final String name ;

       name = getAlphaNumericString();
       StorageReference ref = storageReference.child(account_type).child(seller_id).child(dirName).child(name);

       ByteArrayOutputStream bytes = new ByteArrayOutputStream();
       bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
       String path = MediaStore.Images.Media.insertImage(context.getContentResolver(), bitmap, "Title", null);
       Uri file =  Uri.parse(path);

       ref.putFile(file).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
           @Override
           public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//               customProgressDialog.dismissDialog();
               Toast.makeText(context.getApplicationContext() , "Profile uploaded" , Toast.LENGTH_LONG).show();
               //               customProgressDialog.dismissDialog();
//               uploaded = true;
               uploadImageName(dirName , seller_id , name);

               Log.d(TAG, "onSuccess: uploading successful");
           }
       }).addOnFailureListener(new OnFailureListener() {
           @Override
           public void onFailure(@NonNull Exception e) {

               Log.d("error",e.getMessage()+e.getClass().getName());
               Log.d(TAG, "onFailure: error in uploading images");
//               customProgressDialog.dismissDialog();
//               uploaded = false;

           }
       }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
           @Override
           public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {

               Log.d("progress" , "uploading in progress");
           }
       });

   }

    private static void uploadImageName(final String dirName, final String seller_id, final String name) {



        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("SellerPropertyImages");
                databaseReference = databaseReference.child("seller").child(seller_id).child(dirName);
                String key = databaseReference.push().getKey();

                if (key != null) {
                    databaseReference.child(key).setValue(name).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.d(TAG, "onSuccess: "+"file name uploaded");

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                            Log.d(TAG, "onFailure: "+e.getMessage()+" " +e.getClass().getName());
                        }
                    });
                }
            }
        };

        Thread thread = new Thread(runnable);
        thread.start();

    }

    static String getAlphaNumericString()
    {

        // chose a Character random from this String
        String AlphaNumericString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
                + "0123456789"
                + "abcdefghijklmnopqrstuvxyz";

        // create StringBuffer size of AlphaNumericString
        StringBuilder sb = new StringBuilder(10);

        for (int i = 0; i < 10; i++) {

            // generate a random number between
            // 0 to AlphaNumericString variable length
            int index
                    = (int)(AlphaNumericString.length()
                    * Math.random());

            // add Character one by one in end of sb
            sb.append(AlphaNumericString
                    .charAt(index));
        }

        return sb.toString();
    }

}
