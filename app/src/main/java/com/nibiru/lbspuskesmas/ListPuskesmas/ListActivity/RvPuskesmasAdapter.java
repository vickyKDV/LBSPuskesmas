package com.nibiru.lbspuskesmas.ListPuskesmas.ListActivity;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import com.nibiru.lbspuskesmas.R;


/**
 * A custom adapter to use with the RecyclerView widget.
 */
public class RvPuskesmasAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private ArrayList<PuskesmasModel> modelList;

    private OnItemClickListener mItemClickListener;


    public RvPuskesmasAdapter(Context context, ArrayList<PuskesmasModel> modelList) {
        this.mContext = context;
        this.modelList = modelList;
    }

    public void updateList(ArrayList<PuskesmasModel> modelList) {
        this.modelList = modelList;
        notifyDataSetChanged();

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_recycler_list, viewGroup, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

        //Here you can fill your row view
        if (holder instanceof ViewHolder) {
            final PuskesmasModel model = getItem(position);
            ViewHolder genericViewHolder = (ViewHolder) holder;
            genericViewHolder.txt_nama.setText(model.getNama_puskesmas());

            Log.d("IMAGEnya",genericViewHolder.nama_image);
            genericViewHolder.imgUser.setImageResource(
                    mContext.getResources()
                            .getIdentifier(model.getAimage(),"drawable",mContext.getPackageName()));
        }
    }


    @Override
    public int getItemCount() {

        return modelList.size();
    }

    public void SetOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }

    private PuskesmasModel getItem(int position) {
        return modelList.get(position);
    }


    public interface OnItemClickListener {
        void onItemClick(View view, int position, PuskesmasModel model);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView imgUser;
        private TextView txt_nama;
        private String nama_image;
        public ViewHolder(final View itemView) {
            super(itemView);

            // ButterKnife.bind(this, itemView);

            this.imgUser    = itemView.findViewById(R.id.img_user);
            this.txt_nama   = itemView.findViewById(R.id.item_txt_title);
            this.nama_image      = "";


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mItemClickListener.onItemClick(itemView, getAdapterPosition(), modelList.get(getAdapterPosition()));


                }
            });

        }
    }

}

