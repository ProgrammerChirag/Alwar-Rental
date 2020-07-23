package User;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.selflearn.alwarrenter.R;

import java.util.List;

import ModelClasses.TransactionData;
import de.hdodenhof.circleimageview.CircleImageView;

public class UserTransactionAdapter extends RecyclerView.Adapter<UserTransactionAdapter.MyUserTransactionViewHolder> {

    Context context ;
    List<TransactionData> transactionDataList;

    public UserTransactionAdapter()
    {}

    public UserTransactionAdapter(Context context , List<TransactionData> transactionDataList)
    {
        this.context = context;
        this.transactionDataList = transactionDataList;
    }


    @NonNull
    @Override
    public MyUserTransactionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view= inflater.inflate(R.layout.item_transaction_user, parent, false);

        return new MyUserTransactionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyUserTransactionViewHolder holder, int position) {

        holder.sellerName.setText(transactionDataList.get(position).getSellerName());
        holder.PurchaseType.setText(transactionDataList.get(position).getPurchaseType());
        holder.TransactionDate.setText(transactionDataList.get(position).getDate());

    }

    @Override
    public int getItemCount() {
        return transactionDataList.size();
    }

    public static class MyUserTransactionViewHolder extends RecyclerView.ViewHolder {

         CircleImageView profile_image;
         TextView sellerName , TransactionDate , PurchaseType;

        public MyUserTransactionViewHolder(@NonNull View itemView) {
            super(itemView);
            profile_image = itemView.findViewById(R.id.profile_pic_seller);
            sellerName = itemView.findViewById(R.id.seller_name);
            TransactionDate = itemView.findViewById(R.id.purchaseDate);
            PurchaseType = itemView.findViewById(R.id.purchaseType_history);
        }
    }
}
