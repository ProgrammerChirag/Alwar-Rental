package Seller.Adapter;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class YourRequestAdapter extends RecyclerView.Adapter<YourRequestAdapter.MyPostViewHolder> {
    @NonNull
    @Override
    public MyPostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull MyPostViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public static class MyPostViewHolder extends RecyclerView.ViewHolder {
        public MyPostViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
