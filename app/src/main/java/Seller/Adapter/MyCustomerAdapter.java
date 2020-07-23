package Seller.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.selflearn.alwarrenter.R;

import java.util.List;

import ModelClasses.CustomerData;

public class MyCustomerAdapter extends RecyclerView.Adapter<MyCustomerAdapter.MyPostViewHolder> {

    List<CustomerData> customerDataList ;
    Context context;

    @NonNull
    @Override
    public MyPostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.item_my_post_layout , parent , false);

        return new MyPostViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyPostViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return customerDataList.size();
    }

    public static class MyPostViewHolder extends RecyclerView.ViewHolder {
        public MyPostViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
