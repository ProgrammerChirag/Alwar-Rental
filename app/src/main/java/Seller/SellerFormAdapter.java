package Seller;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class SellerFormAdapter extends RecyclerView.Adapter<SellerFormAdapter.MySellerViewHolder> {
    @NonNull
    @Override
    public MySellerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull MySellerViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class MySellerViewHolder extends RecyclerView.ViewHolder {
        public MySellerViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
