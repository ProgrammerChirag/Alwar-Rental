package Seller.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.selflearn.alwarrenter.R;

import java.util.List;

import ModelClasses.CustomerData;

public class MyCustomerAdapter extends RecyclerView.Adapter<MyCustomerAdapter.MyPostViewHolder> {

    List<CustomerData> customerDataList ;
    Context context;

    public MyCustomerAdapter(Context context , List<CustomerData> customerDataList)
    {
        this.context = context;
        this.customerDataList = customerDataList;
    }


    @NonNull
    @Override
    public MyPostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.item_layout_user_data , parent , false);

        return new MyPostViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyPostViewHolder holder, int position) {

        holder.number.setText(customerDataList.get(position).getPhone());
        holder.email.setText(customerDataList.get(position).getEmail());
        holder.name.setText(customerDataList.get(position).getName());

    }

    @Override
    public int getItemCount() {
        return customerDataList.size();
    }

    public static class MyPostViewHolder extends RecyclerView.ViewHolder {

        TextView name , email , number;

        public MyPostViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.nameUser);
            email = itemView.findViewById(R.id.emailitem);
            number = itemView.findViewById(R.id.callUser);

        }
    }
}
