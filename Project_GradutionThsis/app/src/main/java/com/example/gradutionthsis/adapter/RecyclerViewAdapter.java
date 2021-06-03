package com.example.gradutionthsis.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gradutionthsis.R;
import com.example.gradutionthsis.dto.Relative;

import java.util.Collections;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder> {
    private static final String TAG = RecyclerViewAdapter.class.getSimpleName();

    private final Context context;
    static final int TYPE_RELATIVE = 1;
    static final int TYPE_DEFAULT = 2;
    private final int resource;
    private Object object;
    private List<Object> list;

    private List<Relative> relativeList;

    private View convertView;

    public RecyclerViewAdapter(Context context, int resource) {
        this.context = context;
        this.resource = resource;
    }

    public RecyclerViewAdapter(Context context, int resource, List<Object> objects) {
        this.context = context;
        this.resource = resource;
        this.list = objects;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        convertView = LayoutInflater.from(parent.getContext()).inflate(resource, parent, false);
        return new MyViewHolder(convertView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        object = list.get(position);
//        if (resource == R.layout.item_relative) {
//            holder.txtFullName.setText(relativeList.get(position).getFullName());
//            holder.txtNickName.setText(relativeList.get(position).getNickName());
//            holder.txtBirthdate.setText(relativeList.get(position).getBirthDate());
//            holder.txtGender.setText(relativeList.get(position).getGender());
//            BitmapFactory.Options options = new BitmapFactory.Options();
//            options.inPreferredConfig = Bitmap.Config.RGB_565;
//            holder.circleImageView.setImageBitmap(BitmapFactory.decodeByteArray(relativeList.get(position).getAvatar(), 0, relativeList.get(position).getAvatar().length, options));
//
//
//        } else if (TYPE_DEFAULT == holder.getItemViewType()) {
//
//        }

        if (object instanceof Relative){
            relativeList = Collections.singletonList((Relative) list);
            Log.d(TAG, "onBindViewHolder: " + relativeList.toString());
        }

    }

    @Override
    public int getItemCount() {
        if (list != null) {
            Log.d("Adapter", "getItemCount: Có dữ liệu");
            return list.size();
        }
        return 0;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        CircleImageView circleImageView;
        TextView txtFullName, txtNickName, txtBirthdate, txtGender, txtWeight, txtHeight, txtTime;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
