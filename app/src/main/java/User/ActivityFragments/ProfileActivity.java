package User.ActivityFragments;

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

import AccountHandler.ActivityChooseLoginType;
import ModelClasses.UserData;
import User.UserMemoryManagement;
import Utils.SettingMemoryData;

import java.io.IOException;

import FirebaseConnectivity.StorageDevice;
import User.ActivitySetting;
import User.ActivityUserEditProfile;
import Utils.CustomDialogMaker;
import Utils.CustomProgressDialog;
import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;


public class ProfileActivity  extends Fragment {


    String account_type;
    Button Sign_out;
    Button mailUsBtn , editPersonalDetails , editPersonalDetails_main, changePassword;
    TextView certified_text;
    ViewGroup container;
    CircleImageView profileImage;
    Button Save;
    ImageButton settingBtn;
//    ImageButton BackBtn;
    TextView Name ;
    Context context;
    private final int PICK_IMAGE_REQUEST = 22;
    Bitmap profile_bitmap;
    Button callUsBtn;
  //  CustomProgressDialog customProgressDialog;




    private View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            if(FirebaseAuth.getInstance().getCurrentUser() != null)
            {
                new SettingMemoryData(context).removeSharedPref();
                SignOut();
                ((Activity)(context)).finish();
                startActivity(new Intent(context.getApplicationContext() , ActivityChooseLoginType.class));
            }
        }
    };


    private View.OnClickListener changePasswordListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            new CustomDialogMaker((Activity) context).EnterCurrentPassword("UserData");
//            Log.d("password" , password);
        }
    };

    private View.OnClickListener profile_image_listener = new View.OnClickListener() {
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

    private View.OnClickListener listener_save_and_back = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            new CustomProgressDialog((Activity) context).startLoadingDailog();

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                 //   customProgressDialog.dismissDialog();
                    ((Activity) context).finish();
                    startActivity(new Intent(context.getApplicationContext() , ChooseLoginTypeActivity.class));
                }
            },1500);

        }
    };
//    private View.OnClickListener listener_back = new View.OnClickListener() {
//        @Override
//        public void onClick(View v) {
//            ((Activity)(context)).onBackPressed();
//        }
//    };

    private View.OnClickListener listener_edit_personalDetails = new View.OnClickListener() {
        @Override
        public void onClick(View v){
            openEditProfileActivity();
        }
    };
    private View.OnClickListener listener_setting = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            ((Activity)(context)).finish();
            startActivity(new Intent(context.getApplicationContext() , ActivitySetting.class));
        }
    };

    private void openEditProfileActivity() {

        Intent intent = new Intent(context.getApplicationContext() , ActivityUserEditProfile.class);
        ((Activity)(context)).finish();
        startActivity(intent);

    }

    private View.OnClickListener listener_mail = new View.OnClickListener() {
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

    private void SignOut() {

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

        //deleting all shared preferences
        new SettingMemoryData(context).removeSharedPref();
        firebaseAuth.signOut();
        ((Activity)(context)).finish();
        startActivity(new Intent(getActivity() , ChooseLoginTypeActivity.class));
    }

    public ProfileActivity(Context context, String account_type)
    {
        this.context = context;
        this.account_type = account_type;
        //customProgressDialog = new CustomProgressDialog((Activity) context);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_profile_fragment_xml , container , false);

        assert container != null;

        this.container = container;
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        findID(view);

//        OnBackPressedCallback  callback = new OnBackPressedCallback(true) {
//            @Override
//            public void handleOnBackPressed() {
//
//            }
//        };
//        requireActivity().getOnBackPressedDispatcher().addCallback((LifecycleOwner) context, callback );

        setAllOptions(account_type);
    }

    @SuppressLint("SetTextI18n")
    private void setAllOptions(String account_type) {
        switch (account_type){
            case "user":
                break;
            case "seller":
//                certified_text.setText("Already A seller");
                break;
        }
    }

    private void findID(View container) {

        Sign_out = container.findViewById(R.id.sign_out);
        Sign_out.setOnClickListener(listener);
        certified_text = container.findViewById(R.id.certified_text);
        mailUsBtn = container.findViewById(R.id.Mail_us);
        mailUsBtn.setOnClickListener(listener_mail);
        editPersonalDetails = container.findViewById(R.id.edit_personal_btn);
        editPersonalDetails.setOnClickListener(listener_edit_personalDetails);
        editPersonalDetails_main = container.findViewById(R.id.edit_personal_details);
        editPersonalDetails_main.setOnClickListener(listener_edit_personalDetails);
        Save = container.findViewById(R.id.switchAccountBtn);
//        BackBtn = container.findViewById(R.id.backBtn_to_dashboard);
        Save.setOnClickListener(listener_save_and_back);
//        BackBtn.setOnClickListener(listener_back);
        settingBtn = container.findViewById(R.id.Setting_btn);
        settingBtn.setOnClickListener(listener_setting);
        changePassword = container.findViewById(R.id.change_password);
        changePassword.setOnClickListener(changePasswordListener);
        profileImage = container.findViewById(R.id.profile_image_profileFragment);
        profileImage.setOnClickListener(profile_image_listener);
        UserMemoryManagement.fetchFromDataBase(account_type,
                new SettingMemoryData(context).getSharedPrefString(String.valueOf(R.string.KEY_USER_ID)),
                context , profileImage);

        Name = container.findViewById(R.id.name_account_holder);

        String name = new SettingMemoryData(context).getSharedPrefString(String.valueOf(R.string.KEY_NAME));
        if (name != null)
        {
            Log.d("found", "name found in memory");
            Name.setText(name);
        }
        else
        {
            new CustomDialogMaker((Activity) context).createAndShowDialogWarning("could not find name");
            findName();
        }

        callUsBtn = container.findViewById(R.id.call_us);

        callUsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String phoneNumber = "+919887822444";
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phoneNumber, null));
                startActivity(intent);

            }
        });

//        UserMemoryManagement.openFolder(context);
    }

    private void findName() {

        Log.d("name_finding" , "finding name from database");
        final SettingMemoryData settingMemoryData = new SettingMemoryData(context);
        final String userName = settingMemoryData.getSharedPrefString(String.valueOf(R.string.KEY_USER_ID));

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("UserData");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot snapshot1 : snapshot.getChildren())
                {
                    UserData userData = snapshot1.getValue(UserData.class);
                    if (userData != null && userData.getUsername().equals(userName)) {
                        Name.setText(userData.getName());
                        settingMemoryData.setSharedPrefString(String.valueOf(R.string.KEY_NAME) , userData.getName());
                        settingMemoryData.setSharedPrefString(String.valueOf(R.string.KEY_USER_ID) , userData.getUsername());
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

    @Override
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

                StorageDevice.UploadUserProfilePicture(account_type ,
                        new SettingMemoryData(context).getSharedPrefString(String.valueOf(R.string.KEY_USER_ID)),
                        context , filePath);

                //customProgressDialog.dismissDialog();

            }

            catch (IOException e) {
                // Log the exception
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onDestroyView() {
        //customProgressDialog.dismissDialog();
        super.onDestroyView();
    }
}
