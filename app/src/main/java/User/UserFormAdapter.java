package User;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class UserFormAdapter extends RecyclerView.Adapter<UserFormAdapter.MyUserFormHolder> {

    String[] resources ={

            "enter your name",
            "reason for buying",
            "select any option",
            "how many rooms you want",
            "Select your location",
            "you are ",
            "Select your budget range",
            "working as a"
    };

    @NonNull
    @Override
    public MyUserFormHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull MyUserFormHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class MyUserFormHolder extends RecyclerView.ViewHolder {
        public MyUserFormHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
