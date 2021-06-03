package com.example.gradutionthsis.adapter;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.gradutionthsis.activity.EditHealthActivity;
import com.example.gradutionthsis.activity.EditRelativeActivity;
import com.example.gradutionthsis.DBHelper;
import com.example.gradutionthsis.R;
import com.example.gradutionthsis.dto.Health;
import com.example.gradutionthsis.dto.Relative;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class CustomAdapter<T> extends ArrayAdapter<T> {
    private final Context context;
    private final int resource;
    private final ArrayList<T> listT;

    private TextView txtFullName, txtNickName, txtBirthdate, txtGender, txtWeight, txtHeight, txtTime;
    private CircleImageView imageView;

    private ArrayList<Relative> relativeArrayList;
    private ArrayList<Health> healthArrayList;
    private DBHelper dbHelper;


    public CustomAdapter(@NonNull Context context, int resource, List<T> objects) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
        this.listT = (ArrayList<T>) objects;
    }

    @SuppressLint("ViewHolder")
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        convertView = LayoutInflater.from(parent.getContext()).inflate(resource, parent, false);
        dbHelper = new DBHelper(getContext());


        Button btnEdit = convertView.findViewById(R.id.buttonEdit);
        Button btnDelete = convertView.findViewById(R.id.buttonDelete);

        T t = getItem(position);
        if (t instanceof Relative) {
            Log.d("TAG", "t instanceof Relative: " + true);

            txtFullName = convertView.findViewById(R.id.textFullName);
            txtNickName = convertView.findViewById(R.id.textNickName);
            txtBirthdate = convertView.findViewById(R.id.textBirthDate);
            txtGender = convertView.findViewById(R.id.textGender);
            imageView = convertView.findViewById(R.id.imageAvatar);

            relativeArrayList = (ArrayList<Relative>) listT;
            getInfoRelative(relativeArrayList.get(position));

            btnDelete.setOnClickListener(view -> {
                deleteRelative(relativeArrayList.get(position).getIdRelative(), position, parent);
                notifyDataSetChanged();
            });

            btnEdit.setOnClickListener(v -> sendObject(relativeArrayList.get(position), EditRelativeActivity.class));
        } else if (t instanceof Health) {
            Log.d("TAG", "t instanceof Health: " + true);

            txtWeight = convertView.findViewById(R.id.textWeight);
            txtHeight = convertView.findViewById(R.id.textHeight);
            txtTime = convertView.findViewById(R.id.textTime);

            healthArrayList = (ArrayList<Health>) listT;
            getInfoHealth(healthArrayList.get(position));

            btnDelete.setOnClickListener(v -> {
                deleteHealth(healthArrayList.get(position).getIdHealth(), position);
                notifyDataSetChanged();
            });

            btnEdit.setOnClickListener(v -> sendObject(healthArrayList.get(position), EditHealthActivity.class));
        }

        return convertView;
    }


    /**
     * @author: Nguyễn Thanh Tường
     * @date 02//2021 : 15h25p
     */
    //Lấy thông tin sức khỏe của thân nhân - getInfoHealth
    // [START getInfoHealth]
    private void getInfoHealth(@NonNull Health health) {
        txtWeight.setText(String.valueOf(health.getWeight()));
        txtHeight.setText(String.valueOf(health.getHeight()));
        txtTime.setText(health.getTime());
    }
    // [END getInfoHealth]


    /**
     * @param relative thân nhân
     * @author: Nguyễn Nhật Trường
     * @date 15/4/2021 : 19h16p
     */
    //Lấy thông tin thân nhân - getInfoRelative
    // [START getInfoRelative]
    private void getInfoRelative(@NonNull Relative relative) {
        txtFullName.setText(relative.getFullName());
        txtNickName.setText(relative.getNickName());
        txtGender.setText(relative.getGender());
        txtBirthdate.setText(relative.getBirthDate());

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        imageView.setImageBitmap(BitmapFactory.decodeByteArray(relative.getAvatar(), 0, relative.getAvatar().length, options));
    }
    // [END getInfoRelative]


    /**
     * @param id       mã của thân nhân
     * @param position vị trí cần xóa
     * @author: Nguyễn Nhật Trường
     * @date 12/4/2021 : 23h35p
     */
    //Xóa thân nhân theo id - deleteRelative
    // [START deleteRelative]
    public void deleteRelative(int id, int position, @NonNull ViewGroup viewGroup) {
        String text = "Bạn có chắc chắn muốn xóa không?";

        //Khởi tạo title của dialog
        TextView title = new TextView(viewGroup.getContext());
        title.setText(text);
        title.setPadding(20, 30, 20, 30);
        title.setTextSize(20F);
        title.setBackgroundColor(Color.CYAN);
        title.setTextColor(Color.WHITE);
        title.setCompoundDrawablesWithIntrinsicBounds(R.drawable.outline_help_outline_black_18, 0, 0, 0); //Set icon for title

        //Khởi tạo dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(viewGroup.getContext());
        builder.setCustomTitle(title);
        builder.setMessage(R.string.content_reset);
        builder.setPositiveButton(R.string.yes, (dialog, which) -> {
            if (dbHelper.deleteRelative(id)) {
                relativeArrayList.remove(position);
                notifyDataSetChanged();
                Log.i("Test", "deleteRelative position: " + position);
                Toast.makeText(getContext(), "Success!!!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getContext(), "Fail!!!", Toast.LENGTH_SHORT).show();
            }
            dialog.dismiss();
        });
        builder.setNegativeButton(R.string.cancel, (dialog, which) -> dialog.cancel());
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
    // [END deleteRelative]


    /**
     * @param id       mã sức khỏe
     * @param position vị trí cần xóa
     * @author: Nguyễn Nhật Trường
     * @date 02/5/2021 : 15h56p
     */
    //Xóa sức khỏe của trẻ em theo id - Delete Health by idHealth
    // [START deleteHealth]
    public void deleteHealth(int id, int position) {
        if (dbHelper.deleteHealth(id)) {
            healthArrayList.remove(position);
            notifyDataSetChanged();
            Log.i("Test", "deleteRelative position: " + position);
            Toast.makeText(getContext(), "Success!!!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getContext(), "Fail!!!", Toast.LENGTH_SHORT).show();
        }
    }
    // [END deleteHealth]


    /**
     * @param aClass lớp nhận đối tượng được truyền đến - The class that receives the object is passed on
     * @param object Dữ liệu đối tượng được gửi đi - Data object to be sent
     * @author Nguyễn Thanh Tường
     * @date 03/5/2021 : 15h06p
     */
    //Phương thức gửi object sang activity khác - Method of send the object to different activity
    // [START sendObject]
    @SuppressWarnings("uncheck")
    private void sendObject(Serializable object, Class aClass) {
        Intent intent = new Intent(getContext().getApplicationContext(), aClass);
        Bundle bundle = new Bundle();
        bundle.putSerializable("object", object);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtras(bundle);
        context.startActivity(intent);

    }
}
