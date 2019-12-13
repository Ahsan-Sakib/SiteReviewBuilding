package com.example.buildingconstraction.is229443.Admin;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.buildingconstraction.R;
import com.example.buildingconstraction.is229443.Model.PostModel;

import java.util.ArrayList;


public class SiteListAdapter extends
        RecyclerView.Adapter<SiteListAdapter.ListViewHolder> {

    private ArrayList<PostModel> postModelCollection;
    private Context mContext;

    public SiteListAdapter(ArrayList<PostModel> postModelCollection, Context mContext) {
        this.postModelCollection = postModelCollection;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public SiteListAdapter.ListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View modelView;
        modelView = LayoutInflater.from(mContext)
                .inflate(R.layout.site_list_for_admin,parent,false);
        return new SiteListAdapter.ListViewHolder(modelView) ;
    }

    @Override
    public void onBindViewHolder(@NonNull SiteListAdapter.ListViewHolder holder, int position) {
        holder.tv_title_admin.setText(postModelCollection.get(position).getTitle());
        holder.tv_uploaderName.setText(postModelCollection.get(position).getPostedBy());
    }

    @Override
    public int getItemCount() {
        return postModelCollection.size();
    }

    public class ListViewHolder extends RecyclerView.ViewHolder {
        TextView tv_title_admin,tv_uploaderName;
        public ListViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_title_admin = itemView.findViewById(R.id.titleforadmin);
            tv_uploaderName = itemView.findViewById(R.id.uploadernameadmin);
        }
    }
}
