package com.movies.moviesapp.HelperClasses;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.movies.moviesapp.R;

// Common methods that can reuse
public class ConstantMethods {

    public static Dialog dialog;


    /**
     * To show the toast message
     * @param context
     * @param msg :- what you want to specify as message to user
     */
    public static void showToast(Context context, String msg) {
        if (context != null) {
            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * To get screen width
     *
     * @param context
     * @return
     */
    public static int getScreenWidth(Context context) {
        Point size = new Point();
        ((Activity) context).getWindowManager().getDefaultDisplay().getSize(size);
        return size.x;
    }

    /**
     * Checks whether if there is active internet connection
     *
     * @param context
     * @return
     */
    public static boolean isNetworkAvailable(Context context) {
        try {
            ConnectivityManager connectivityManager
                    = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            if (connectivityManager != null && connectivityManager.getActiveNetworkInfo() != null) {
                NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
                return activeNetworkInfo != null && activeNetworkInfo.isConnected();
            } else {
                return false;
            }

        } catch (Exception e) {
            e.printStackTrace();
            return false;

        }
    }

    public static void dismissDialogIfAny(Context context) {
        if (dialog != null) {
            if (dialog.isShowing()) {
                dialog.dismiss();
            }
        }

    }

    public static Dialog showErrorInfo(Context context, String msg) {
        if (dialog != null) {
            if (dialog.isShowing()) {
                dialog.dismiss();
            }
        }
        dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.common_error_alert);
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(false);
        dialog.getWindow().setLayout((int) (ConstantMethods.getScreenWidth(context) * .9), LinearLayout.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        ImageView ivPic = dialog.findViewById(R.id.ivPic);
        TextView tvErrorMessage = dialog.findViewById(R.id.tvErrorMessage);
        TextView tvClose = dialog.findViewById(R.id.tvClose);
        tvErrorMessage.setText(msg + "");
        if (msg.equals(context.getString(R.string.no_internet_msg))) {
            ivPic.setVisibility(View.VISIBLE);
        } else {
            ivPic.setVisibility(View.GONE);
        }
        tvClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
        return dialog;

    }


    public static Dialog showApiCallProgress(Context context) {
        if (dialog != null) {
            if (dialog.isShowing()) {
                dialog.dismiss();
            }
        }
        dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.common_progress_alert);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
        return dialog;
    }

}
