package com.example.gradutionthsis.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.database.sqlite.SQLiteDiskIOException;
import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gradutionthsis.DBHelper;
import com.example.gradutionthsis.R;
import com.example.gradutionthsis.activity.KnowledgeActivity;
import com.example.gradutionthsis.activity.NotifyActivity;
import com.example.gradutionthsis.adapter.CustomAdapterString;
import com.example.gradutionthsis.dto.NotificationTask;
import com.example.gradutionthsis.dto.Relative;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.play.core.review.ReviewInfo;
import com.google.android.play.core.review.ReviewManager;
import com.google.android.play.core.review.ReviewManagerFactory;
import com.google.android.play.core.tasks.Task;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class SettingFragment extends Fragment {
    private static final String TAG = SettingFragment.class.getSimpleName();
    ReviewManager reviewManager;
    View root;
    List<Relative> relatives = new ArrayList<>();
    DBHelper dbHelper;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_setting, container, false);

        dbHelper = new DBHelper(getContext());


        ListView listView = root.findViewById(R.id.listSetting);
        int[] icon = {R.drawable.outline_restore_black_18, R.drawable.outline_notifications_black_18,
                R.drawable.outline_school_black_18, R.drawable.outline_error_outline_black_18,
                R.drawable.outline_thumb_up_black_18, R.drawable.outline_share_black_18};

        CustomAdapterString arrayAdapter = new CustomAdapterString(getContext(), getResources().getStringArray(R.array.settings), icon);
        listView.setAdapter(arrayAdapter);
        listView.setOnItemClickListener((parent, view, position, id) -> {
            if (position == 0) {
                contentReset();
            }
            if (position == 1) {
                startActivity(new Intent(getContext(), NotifyActivity.class));
            }
            if (position == 2) {
                startActivity(new Intent(getContext(), KnowledgeActivity.class));
            }
            if (position == 3) {

                contentWaring();
            }
            if (position == 4) {
//                reviewManager = ReviewManagerFactory.create(Objects.requireNonNull(getContext(),"null"));
//                showRateApp();
                Toast.makeText(getContext(), "Chức năng này hiện đang được cập nhật ^_^!!!", Toast.LENGTH_SHORT).show();
            }
            if (position == 5) {
                Toast.makeText(getContext(), "Chức năng này hiện đang được cập nhật ^_^!!!", Toast.LENGTH_SHORT).show();
            }
        });
        return root;
    }

    /**
     * @author: Nguyễn Thanh Tường
     * @date 10/05/2021 : 15h59p
     */
    //Nội dung của thông tin lưu ý - content waring
    //[START contentWaring]
    private void contentWaring() {
        //Khởi tạo title của dialog
        TextView title = new TextView(getContext());
        title.setText(R.string.warning);
        title.setPadding(20, 30, 20, 30);
        title.setTextSize(20F);
        title.setBackgroundColor(Color.CYAN);
        title.setTextColor(Color.WHITE);
        title.setCompoundDrawablesWithIntrinsicBounds(R.drawable.outline_error_outline_black_18, 0, 0, 0);//Set icon for title

        //Khởi tạo dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setCustomTitle(title);
        builder.setMessage(R.string.content_waring);
        builder.setPositiveButton(R.string.yes, (dialog, which) -> dialog.cancel());
        AlertDialog alertDialog = builder.create();
        alertDialog.show();

    }
    //[END contentWaring]


    /**
     * @author: Nguyễn Thanh Tường
     * @date 10/05/2021 : 15h59p
     */
    //Đặt lại ứng dụng như ban đầu - content reset
    //[START contentReset]
    private void contentReset() {
        //Khởi tạo title của dialog
        TextView title = new TextView(getContext());
        title.setText(R.string.reset);
        title.setPadding(20, 30, 20, 30);
        title.setTextSize(20F);
        title.setBackgroundColor(Color.CYAN);
        title.setTextColor(Color.WHITE);
        title.setCompoundDrawablesWithIntrinsicBounds(R.drawable.outline_help_outline_black_18, 0, 0, 0); //Set icon for title

        //Khởi tạo dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setCustomTitle(title);
        builder.setMessage(R.string.content_reset);
        builder.setPositiveButton(R.string.yes, (dialog, which) -> {
            setDefaultsForTask();
            deleteRelatives();
            dialog.dismiss();
        });
        builder.setNegativeButton(R.string.cancel, (dialog, which) -> dialog.cancel());
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
    //[END contentReset]


    /**
     * @author: Nguyễn Thanh Tường
     * @date 10/05/2021 : 16h27p
     */
    //Xóa hết trẻ em - delete all relative
    //[START deleteRelatives]
    private void deleteRelatives() {
        relatives = dbHelper.getAllRelatives();
        if (relatives.size() > 0)
            for (Relative relative : relatives)
                dbHelper.deleteRelative(relative.getIdRelative());
    }
    //[END deleteRelatives]

    /**
     * @author: Nguyễn Thanh Tường
     * @date 14/05/2021 : 16h08p
     */
    //Đặt giá trị mặc định cho các tác vụ - setDefaultsForTask
    //[START setDefaultsForTask]
    private void setDefaultsForTask() {
        NotificationTask task = new NotificationTask(1, 1, 0, 7, 0);
        try {
            if (dbHelper.updateTask(task) > 0){
                Toast.makeText(getContext(), "Cập nhật thành công", Toast.LENGTH_SHORT).show();
            }
        }catch (SQLiteDiskIOException e){
            Log.e(TAG, "setDefaultsForTask: ", e);
            Toast.makeText(getContext(), "Cập nhật notify thất bại!", Toast.LENGTH_SHORT).show();
        }
    }
    //[END setDefaultsForTask]

    /**
     * Shows rate app bottom sheet using In-App review API
     * The bottom sheet might or might not shown depending on the Quotas and limitations
     * https://developer.android.com/guide/playcore/in-app-review#quotas
     * We show fallback dialog if there is any error
     */
    public void showRateApp() {
        Task<ReviewInfo> request = reviewManager.requestReviewFlow();
        request.addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                // We can get the ReviewInfo object
                ReviewInfo reviewInfo = task.getResult();

                Task<Void> flow = reviewManager.launchReviewFlow((Activity) Objects.requireNonNull(getActivity(), "null").getApplicationContext(), reviewInfo);
                flow.addOnCompleteListener(task1 -> {
                    // The flow has finished. The API does not indicate whether the user
                    // reviewed or not, or even whether the review dialog was shown. Thus, no
                    // matter the result, we continue our app flow.
                });
            } else {
                // There was some problem, continue regardless of the result.
                // show native rate app dialog on error
                showRateAppFallbackDialog();
            }
        });
    }

    /**
     * Showing native dialog with three buttons to review the app
     * Redirect user to playstore to review the app
     */
    private void showRateAppFallbackDialog() {
        new MaterialAlertDialogBuilder(Objects.requireNonNull(getContext(),"null"))
                .setTitle(R.string.rate_app_title)
                .setMessage(R.string.rate_app_message)
                .setPositiveButton(R.string.rate_btn_pos, (dialog, which) -> {

                })
                .setNegativeButton(R.string.rate_btn_neg,
                        (dialog, which) -> {
                        })
                .setNeutralButton(R.string.rate_btn_nut,
                        (dialog, which) -> {
                        })
                .setOnDismissListener(dialog -> {
                })
                .show();
    }

}