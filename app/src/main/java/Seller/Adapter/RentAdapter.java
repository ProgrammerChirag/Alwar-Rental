package Seller.Adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ModelClasses.RequestData;

public class RentAdapter extends RecyclerView.Adapter<RentAdapter.MyRentAdapter> {

    Context context;
    List<RequestData> requestDataList;

    public RentAdapter(Context context , List<RequestData> requestDataList)
    {
        this.context = context;
        this.requestDataList = requestDataList;
    }

    public RentAdapter()
    {

    }

    @NonNull
    @Override
    public MyRentAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull MyRentAdapter holder, int position) {

    }

    @Override
    public int getItemCount() {
        return requestDataList.size();
    }

    public static class MyRentAdapter extends RecyclerView.ViewHolder {
        public MyRentAdapter(@NonNull View itemView) {
            super(itemView);
        }
    }
}
