package User;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.selflearn.alwarrenter.R;

import java.util.List;

import ModelClasses.RequestData;

public class UserRequestViewAdapter extends RecyclerView.Adapter<UserRequestViewAdapter.MyViewAdapter> {

    Context context;
    List<RequestData> requestDataList;

    public UserRequestViewAdapter(Context context, List<RequestData> requestDataList)
    {
      this.context = context;
      this.requestDataList = requestDataList;
    }

    @NonNull
    @Override
    public UserRequestViewAdapter.MyViewAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view= inflater.inflate(R.layout.item_layout_request_user , parent , false);
        return new MyViewAdapter(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserRequestViewAdapter.MyViewAdapter holder, int position) {

        holder.name .setText(requestDataList.get(position).getLocation());
        holder.requestTypeItem.setText(requestDataList.get(position).getRequestType());
        holder.status.setText(String.format("Status : %s", requestDataList.get(position).getStatus()));
    }

    @Override
    public int getItemCount() {
        Log.d("number of values" , String.valueOf(requestDataList.size()));

        return requestDataList.size();
    }

    public static class MyViewAdapter extends RecyclerView.ViewHolder {
        TextView name  , status , requestTypeItem;

        public MyViewAdapter(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name_account_holder);
            status = itemView.findViewById(R.id.status);
            requestTypeItem = itemView.findViewById(R.id.requestTypeItem);
        }
    }
}
