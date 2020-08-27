package Seller.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.selflearn.alwarrenter.R;

import java.io.ObjectInputStream;
import java.util.List;
import ModelClasses.PropertyData;
import User.ShowDetailsOfProperty;

public class FindPropertyAdapter extends RecyclerView.Adapter<FindPropertyAdapter.MyPropertyViewHolder> {

    Context context ;
    List<PropertyData> list ;
//    List<Bitmap> bitmapList ;
    private static final String TAG = "FindPropertyAdapter";
    private  List<String> dirName;

    public FindPropertyAdapter(Context context , List<PropertyData> list , List<String> dirName)
    {
        this.context =context;
        this.list = list;
//        bitmapList = list1;
        this.dirName = dirName;
    }

    public FindPropertyAdapter()
    {}


    @NonNull
    @Override
    public MyPropertyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_find_post_layout , parent , false);
        return new MyPropertyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyPropertyViewHolder holder, final int position) {

        if (position < list.size()) {

            holder.NumImages.setText(String.format("Images : %s", list.get(position).getNumImages()));
            holder.NumRooms.setText(String.format("Rooms : %s", list.get(position).getNumRooms()));
            holder.NumBHK.setText(String.format("%sBHK House", list.get(position).getNumBHk()));
            holder.location.setText(list.get(position).getAddressProperty());
            holder.NumImages.setVisibility(View.INVISIBLE);
//
//            Log.d(TAG, "onBindViewHolder: " + bitmapList.size());
//            holder.imageView.setImageBitmap(bitmapList.get(position));
//            holder.imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);

//        else Log.d(TAG, "onBindViewHolder: null data");

            holder.cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context.getApplicationContext() , ShowDetailsOfProperty.class);
//                    intent.putExtra("propertyType" , "on rent");
                    intent.putExtra("dirName" , dirName.get(position));
                    intent.putExtra("sellername",list.get(position).getSellerId());

                    intent.putExtra("Name" , list.get(position).getSellerName()) ;
                    intent.putExtra(String.valueOf(R.string.numBHK) , list.get(position).getNumBHk());
                    intent.putExtra(String.valueOf(R.string.purchase_type) , list.get(position).getPurchaseType());
                    intent.putExtra(String.valueOf(R.string.area) , list.get(position).getArea());
                    intent.putExtra(String.valueOf(R.string.address) , list.get(position).getAddressProperty());
                    intent.putExtra(String.valueOf(R.string.background) , list.get(position).getBackground());
                    intent.putExtra(String.valueOf(R.string.cost) , list.get(position).getCost());
                    intent.putExtra(String.valueOf(R.string.numRooms) , list.get(position).getNumRooms());
                    intent.putExtra(String.valueOf(R.string.KEY_USER_ID) , list.get(position).getSellerId());
                    intent.putExtra(String.valueOf(R.id.cost) , list.get(position).getCost());
                    intent.putExtra(String.valueOf(R.id.purchaseDate) , list.get(position).getDateUpload());
                    context.startActivity(intent);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyPropertyViewHolder extends RecyclerView.ViewHolder {

        TextView location, NumBHK , NumRooms , NumImages ;
//        ImageView imageView;
        CardView cardView;

        public MyPropertyViewHolder(@NonNull View itemView) {
            super(itemView);
            location = itemView.findViewById(R.id.Date);
            NumBHK = itemView.findViewById(R.id.nuMBHK_text);
            NumRooms = itemView.findViewById(R.id.nuMRoom_text);
            NumImages = itemView.findViewById(R.id.numImages);
//            imageView = itemView.findViewById(R.id.imgView);
            cardView = itemView.findViewById(R.id.card);
        }
    }
}
