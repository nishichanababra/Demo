package com.pg.alldemo;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by test on 27/2/18.
 */

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.MyViewHolder> {
    private String TAG = UserAdapter.this.getClass().getSimpleName();
    private List<User> listUser;
    private Context context;


    public UserAdapter(Context context, List<User> listUser) {
        this.context = context;
        this.listUser = listUser;
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_list, parent, false);
        MyViewHolder holder = new MyViewHolder(itemView);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        User user = listUser.get(position);
        holder.getBinding().setVariable(BR.USER, user);
        holder.getBinding().executePendingBindings();
        if (!user.getPicturePath().isEmpty()) {
            Picasso.with(context)
                    .load(user.getPicturePath())
                    .resize(50, 50)
                    .centerCrop()
                    .into(holder.userImage);
        }
    }

    @Override
    public int getItemCount() {
        return listUser.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private ViewDataBinding binding;
        private ImageView userImage;

        public MyViewHolder(View view) {
            super(view);
            binding = DataBindingUtil.bind(view);
            userImage = view.findViewById(R.id.userImage);
        }

        public ViewDataBinding getBinding() {
            return binding;
        }
    }
}
