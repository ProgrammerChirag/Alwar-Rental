package Seller.Fragement;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.selflearn.alwarrenter.ChooseLoginTypeActivity;
import com.selflearn.alwarrenter.R;

import java.io.IOException;

import AccountHandler.ActivityChooseLoginType;
import FirebaseConnectivity.StorageDevice;
import ModelClasses.SellerData;
import Seller.ActivitySetting;
import User.ActivityUserEditProfile;
import User.UserMemoryManagement;
import Utils.CustomDialogMaker;
import Utils.CustomProgressDialog;
import Utils.SettingMemoryData;
import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;


public class SellerProfileFragment extends Fragment{

    String account_type;
    Context context;
    TextView Name;
    Bitmap profile_bitmap;
    SellerData sellerData;
 //   CustomProgressDialog customProgressDialog;
    CircleImageView  profileImage;
    private final int PICK_IMAGE_REQUEST = 22;

    Button mailUsBtn , editPersonalDetails , editPersonalDetails_main, changePassword , callUsBtn;
    TextView certified_text;
    Button Save;
    ImageButton settingBtn;
    Button Sign_out;

    public SellerProfileFragment()
    {}

    private View.OnClickListener listener_profile_image = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
// choosing image from gallery
            selectImage();
            // updating on profile view.
            // then uploading to storage of firebase.

            //saving to rhe main memory.
            new UserMemoryManagement(context).saveToInternalStorage(profile_bitmap);
        }
    };

    private View.OnClickListener listener_sign_out = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

            //deleting all shared preferences
            new SettingMemoryData(context).removeSharedPref();
            firebaseAuth.signOut();
            ((Activity)(context)).finish();
            startActivity(new Intent(getActivity() , ActivityChooseLoginType.class));
        }
    };

    private View.OnClickListener listener_change_password = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            new CustomDialogMaker((Activity) context).EnterCurrentPassword("SellerData");

        }
    };

    private View.OnClickListener listener_edit_profile =new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            Intent intent = new Intent(context.getApplicationContext() , ActivityUserEditProfile.class);
            ((Activity)(context)).finish();
            startActivity(intent);
        }
    };

    private View.OnClickListener listener_save_details = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            new CustomProgressDialog((Activity) context).startLoadingDailog();

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
//                    customProgressDialog.dismissDialog();
                    ((Activity) context).finish();
                    startActivity(new Intent(context.getApplicationContext() , ChooseLoginTypeActivity.class));
                }
            },1500);
        }
    };

    private View.OnClickListener listener_setting_btn = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            ((Activity)(context)).finish();
            startActivity(new Intent(context.getApplicationContext() , ActivitySetting.class));
        }
    };

    private View.OnClickListener listener_mail_us = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
//
//            Intent mailClient = context.getPackageManager().getLaunchIntentForPackage("com.google.android.gm.ComposeActivityGmail");
//            if (mailClient != null) {
//                mailClient.setType("plain/text")
//                        .setData(Uri.parse("alwarrentalservice@gmail.com"));
//                mailClient.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            }
//            try {
//                context.startActivity(mailClient);
//            }catch (Exception e)
//            {
//                Log.d("error",e.getMessage()+" "+e.getClass().getName());
//            }
            Intent emailIntent = new Intent(Intent.ACTION_SEND );
//            emailIntent.setClassName("com.google.android.gm","com.google.android.gm.ComposeActivityGmail");
            emailIntent.setDataAndType(Uri.parse("alwarrentalservice@gmail.com"),"text/plain");
//            emailIntent.setData(Uri.parse("alwarrentalservice@gmail.com"));
            startActivity(emailIntent);

        }
    };


    public SellerProfileFragment(Context context, String account_type) {
        this.context = context;
        this.account_type = account_type;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        return super.onCreateView(inflater, container, savedInstanceState);

        return  inflater.inflate(R.layout.layout_profile_fragment_xml , container , false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        findID((ViewGroup) view);
//        customProgressDialog = new CustomProgressDialog((Activity) context);
    }

    private void findID(ViewGroup container) {

        Name = container.findViewById(R.id.name_account_holder);
        if (Name != null) {
            String name = new SettingMemoryData(context).getSharedPrefString(String.valueOf(R.string.KEY_NAME));
            Name.setText(name);
        }else
        {
            findName();
        }

        profileImage = container.findViewById(R.id.profile_image_profileFragment);
        profileImage.setOnClickListener(listener_profile_image);

        UserMemoryManagement.fetchFromDataBase(account_type,
                new SettingMemoryData(context).getSharedPrefString(String.valueOf(R.string.KEY_USER_ID)),
                context , profileImage);

        Sign_out = container.findViewById(R.id.sign_out);
        Sign_out.setOnClickListener(listener_sign_out);
        certified_text = container.findViewById(R.id.certified_text);
        mailUsBtn = container.findViewById(R.id.Mail_us);
        mailUsBtn.setOnClickListener(listener_mail_us);
        editPersonalDetails = container.findViewById(R.id.edit_personal_btn);
        editPersonalDetails.setOnClickListener(listener_edit_profile);
        editPersonalDetails_main = container.findViewById(R.id.edit_personal_details);
        editPersonalDetails_main.setOnClickListener(listener_edit_profile);
        Save = container.findViewById(R.id.switchAccountBtn);
//        BackBtn = container.findViewById(R.id.backBtn_to_dashboard);
        Save.setOnClickListener(listener_save_details);
//        BackBtn.setOnClickListener(listener_back);
        settingBtn = container.findViewById(R.id.Setting_btn);
        settingBtn.setOnClickListener(listener_setting_btn);
        changePassword = container.findViewById(R.id.change_password);
        changePassword.setOnClickListener(listener_change_password);

        callUsBtn = container.findViewById(R.id.call_us);

        callUsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String phoneNumber = "+919887822444";
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phoneNumber, null));
                startActivity(intent);

            }
        });
    }

    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_IMAGE_REQUEST
                && resultCode == RESULT_OK
                && data != null
                && data.getData() != null) {

//            btn_choose.setVisibility(View.INVISIBLE);

            // Get the Uri of data
            Uri filePath = data.getData();
            try {

                // Setting image on image view using Bitmap
                Bitmap bitmap = MediaStore
                        .Images
                        .Media
                        .getBitmap(
                                context.getContentResolver(),
                                filePath);
                profileImage.setImageBitmap(bitmap);
                profile_bitmap = bitmap;

                StorageDevice.UploadUserProfilePicture(account_type,
                        new SettingMemoryData(context).getSharedPrefString(String.valueOf(R.string.KEY_USER_ID)),
                        context , filePath);

//                customProgressDialog.dismissDialog();

            }

            catch (IOException e) {
                // Log the exception
                e.printStackTrace();
            }
        }
    }

    private void selectImage() {
        // Defining Implicit Intent to mobile gallery
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(
                Intent.createChooser(
                        intent,
                        "Select Image from here..."),
                PICK_IMAGE_REQUEST);
    }

    private void findName() {

        Log.d("name_finding" , "finding name from database");
        final SettingMemoryData settingMemoryData = new SettingMemoryData(context);
        final String userName = settingMemoryData.getSharedPrefString(String.valueOf(R.string.KEY_USER_ID));

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("SellerData");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot snapshot1 : snapshot.getChildren())
                {
                    sellerData = snapshot1.getValue(SellerData.class);
                    if (sellerData != null && sellerData.getUsername().equals(userName)) {
                        Name.setText(sellerData.getName());
                        settingMemoryData.setSharedPrefString(String.valueOf(R.string.KEY_NAME) , sellerData.getName());
                        settingMemoryData.setSharedPrefString(String.valueOf(R.string.KEY_USER_ID) , sellerData.getUsername());
                    }else{
                        Name.setText("User");
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("error" , error.getMessage() + " " + error.getClass().getName());
            }
        });
    }

    @Override
    public void onDestroyView() {
//        customProgressDialog.dismissDialog();
        super.onDestroyView();
    }
}