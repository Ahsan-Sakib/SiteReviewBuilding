package com.example.buildingconstraction.is229443.Admin;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.buildingconstraction.R;
import com.example.buildingconstraction.is229443.Model.PostModel;
import com.example.buildingconstraction.is229443.User.SiteDescriptionForUser;
import com.example.buildingconstraction.is229443.contants.AppContants;

import java.util.ArrayList;


public class SiteListAdapterForBothUserAndAdmin extends
        RecyclerView.Adapter<SiteListAdapterForBothUserAndAdmin.ListViewHolder> {

    private ArrayList<PostModel> postModelCollection;
    private Context mContext;
    private int from;

    public SiteListAdapterForBothUserAndAdmin(ArrayList<PostModel> postModelCollection, Context mContext, int from) {
        this.postModelCollection = postModelCollection;
        this.mContext = mContext;
        this.from = from;
    }

    @NonNull
    @Override
    public SiteListAdapterForBothUserAndAdmin.ListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View modelView;
        modelView = LayoutInflater.from(mContext)
                .inflate(R.layout.site_list_item_for_user,parent,false);
        return new SiteListAdapterForBothUserAndAdmin.ListViewHolder(modelView) ;
    }

    @Override
    public void onBindViewHolder(@NonNull SiteListAdapterForBothUserAndAdmin.ListViewHolder holder, final int position) {
        holder.tv_title_admin.setText(postModelCollection.get(position).getTitle());
        if(from==0) { //admin
            holder.tv_uploaderName.setText(postModelCollection.get(position).getPostedBy());
        }else{   //user
            holder.tv_uploaderName.setText(postModelCollection.get(position).getDate());
        }

        if(postModelCollection.get(position).isApproval()){
            holder.tv_isApproved.setText("Approved");
        }else{
            holder.tv_isApproved.setText("Not Approved");
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(from==0){
                    //go to admin description
                    Intent intent = new Intent(mContext,AdminSiteDescription.class);
                    intent.putExtra(AppContants.Title,postModelCollection.get(position).getTitle());
                    intent.putExtra(AppContants.description,postModelCollection.get(position).getDescription());
                    intent.putExtra(AppContants.PostedBy,postModelCollection.get(position).getPostedBy());
                    intent.putExtra(AppContants.PostDate,postModelCollection.get(position).getDate());
                    intent.putExtra(AppContants.imageUriLink,postModelCollection.get(position).getImagePath());
                    intent.putExtra(AppContants.Approval,postModelCollection.get(position).isApproval());
                    intent.putExtra(AppContants.Key,postModelCollection.get(position).getPostKey());
                    mContext.startActivity(intent);

                }else{
                    //go to user description
                    Intent intent = new Intent(mContext, SiteDescriptionForUser.class);
                    intent.putExtra(AppContants.Title,postModelCollection.get(position).getTitle());
                    intent.putExtra(AppContants.description,postModelCollection.get(position).getDescription());
                    intent.putExtra(AppContants.PostedBy,postModelCollection.get(position).getPostedBy());
                    intent.putExtra(AppContants.PostDate,postModelCollection.get(position).getDate());
                    intent.putExtra(AppContants.imageUriLink,postModelCollection.get(position).getImagePath());
                    intent.putExtra(AppContants.Approval,postModelCollection.get(position).isApproval());
                    intent.putExtra(AppContants.Key,postModelCollection.get(position).getPostKey());
                    intent.putExtra(AppContants.Comment,postModelCollection.get(position).getRejectMessage());
                    mContext.startActivity(intent);

                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return postModelCollection.size();
    }

    public class ListViewHolder extends RecyclerView.ViewHolder {
        TextView tv_title_admin,tv_uploaderName,tv_isApproved;
        public ListViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_title_admin = itemView.findViewById(R.id.tv_site_title);
            tv_uploaderName = itemView.findViewById(R.id.tv_site_date);
            tv_isApproved = itemView.findViewById(R.id.tv_status);
        }
    }
}
