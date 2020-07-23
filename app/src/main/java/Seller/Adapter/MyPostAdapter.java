package Seller.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.selflearn.alwarrenter.R;

import java.util.Date;
import java.util.List;

import ModelClasses.PropertyData;

public class MyPostAdapter extends RecyclerView.Adapter<MyPostAdapter.MyPostViewHolder> {

    List<PropertyData> propertyDataList ;
    Context context;

    public MyPostAdapter(List<PropertyData> propertyDataList , Context context)
    {
        this.context = context;
        this.propertyDataList = propertyDataList;
    }

    public MyPostAdapter()
    {}



    @NonNull
    @Override
    public MyPostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_my_post_layout , parent , false);
        return new MyPostViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyPostViewHolder holder, int position) {

        holder.NumImages.setText(String.format("Images : %s", propertyDataList.get(position).getNumImages()));
        holder.NumRooms.setText(String.format("Rooms : %s", propertyDataList.get(position).getNumRooms()));
        holder.NumBHK.setText(String.format("%sBHK House", propertyDataList.get(position).getNumBHk()));
        holder.Date.setText(propertyDataList.get(position).getDateUpload());
    }

    @Override
    public int getItemCount() {
        return propertyDataList.size();
    }

    public static class MyPostViewHolder extends RecyclerView.ViewHolder {
        TextView Date , NumBHK , NumRooms , NumImages ;

        public MyPostViewHolder(@NonNull View itemView) {
            super(itemView);
            Date = itemView.findViewById(R.id.Date);
            NumBHK = itemView.findViewById(R.id.nuMBHK_text);
            NumRooms = itemView.findViewById(R.id.nuMRoom_text);
            NumImages = itemView.findViewById(R.id.numImages);
        }
    }
}
