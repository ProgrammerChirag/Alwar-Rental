package Admin.AdminAdapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.selflearn.alwarrenter.R;

import java.util.List;

import ModelClasses.UserData;
import de.hdodenhof.circleimageview.CircleImageView;

public class UserListAdapter extends RecyclerView.Adapter<UserListAdapter.UserListBinder> {

    List<UserData> userDataList;
    Context context;
    List<Bitmap> bitmapList;

    public UserListAdapter()
    {}

    public UserListAdapter(List<UserData> list , Context context , List<Bitmap> bitmaps)
    {
        this.context = context;
        this.userDataList = list;
        this.bitmapList = bitmaps;
     }


    @NonNull
    @Override
    public UserListBinder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_layout_user_data , parent , false);

        return new UserListBinder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserListBinder holder, int position) {

        holder.profile.setImageBitmap(bitmapList.get(position));
        holder.phone.setText(userDataList.get(position).getNumber());
        holder.email.setText(userDataList.get(position).getEmail());
        holder.Name.setText(userDataList.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return userDataList.size();
    }

    public static class UserListBinder extends RecyclerView.ViewHolder {

        CircleImageView profile;
        TextView Name , email , phone;

        public UserListBinder(@NonNull View itemView) {

            super(itemView);
            profile = itemView.findViewById(R.id.profile_image_user);
            Name = itemView.findViewById(R.id.nameUser);
            email = itemView.findViewById(R.id.emailitem);
            phone = itemView.findViewById(R.id.callUser);
        }
    }
}
