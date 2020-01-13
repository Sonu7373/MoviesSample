package com.movies.moviesapp.ServiceManager;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonSyntaxException;
import com.movies.moviesapp.CustomViews.shimmer.ShimmerFrameLayout;
import com.movies.moviesapp.HelperClasses.ConstantMethods;
import com.movies.moviesapp.R;

import org.json.JSONException;

import java.io.IOException;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public abstract class RetrofitCallback<T> implements Callback<T> {
    private Dialog progressDialog, dialogError;
    private ShimmerFrameLayout shimmerLayout;
    private Context context;
    private boolean validateResponse = true;

    public RetrofitCallback(Context c) {
        context = c;
    }

    public RetrofitCallback(Context c, Dialog dialog) {
        context = c;
        progressDialog = dialog;
    }

    public RetrofitCallback(Context context, Dialog progressDialog, boolean validateResponse) {
        this.context = context;
        this.progressDialog = progressDialog;
        this.validateResponse = validateResponse;
    }

    public RetrofitCallback(Context context, boolean validateResponse) {
        this.context = context;
        this.validateResponse = validateResponse;
    }

    public RetrofitCallback(Context context, ShimmerFrameLayout shimmerLayout, boolean validateResponse) {
        this.context = context;
        this.validateResponse = validateResponse;
        this.shimmerLayout = shimmerLayout;
    }

    public abstract void onSuccess(T arg0);

    public abstract void onFailure();

    @Override
    public void onResponse(Call<T> call, Response<T> response) {
        if (shimmerLayout != null) {
            shimmerLayout.setVisibility(View.GONE);
        }
        if (!(((Activity) context).isFinishing()) && progressDialog != null && progressDialog.isShowing()) {
            if (progressDialog != null && progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
        }
        if (response.isSuccessful() && response.code() == 200) {
            onSuccess(response.body());
        } else {
            if (response.code() == 401) {
                //code for session expiry
            } else {
                onFailure();
                Toast.makeText(context, context.getString(R.string.something_wrong), Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onFailure(Call<T> call, Throwable error) {

        Log.e("Error--", "-->" + error);
        Log.e("Error--", "-->" + call.request().toString());
        onFailure();
        if (!validateResponse)
            return;
        String errorMsg;
        error.printStackTrace();
        if (error instanceof SocketTimeoutException) {
            errorMsg = context.getString(R.string.connection_timeout);
        } else if (error instanceof UnknownHostException) {
            errorMsg = context.getString(R.string.no_internet);
        } else if (error instanceof ConnectException) {
            errorMsg = context.getString(R.string.server_not_responding);
        } else if (error instanceof JSONException || error instanceof JsonSyntaxException) {
            errorMsg = context.getString(R.string.parse_error);
        } else if (error instanceof IOException) {
            //errorMsg = error.getMessage();
            errorMsg = context.getString(R.string.server_down);
        } else {
            errorMsg = context.getString(R.string.something_wrong);
        }

        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }

        if (shimmerLayout != null) {
            shimmerLayout.setVisibility(View.GONE);
        }

        ConstantMethods.dismissDialogIfAny(context);
        showApiResponseError(context, errorMsg, call);

    }

    private void showApiResponseError(Context context, String msg, final Call<T> call) {
        if (dialogError != null) {
            dialogError.dismiss();
        }
        dialogError = new Dialog(context);
        dialogError.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogError.setContentView(R.layout.common_api_call_error_alert);
        dialogError.setCancelable(true);
        dialogError.setCanceledOnTouchOutside(false);
        dialogError.getWindow().setLayout((int) (ConstantMethods.getScreenWidth(context) * .9), LinearLayout.LayoutParams.WRAP_CONTENT);
        dialogError.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));


        TextView tvErrorMessage = dialogError.findViewById(R.id.tvErrorMessage);
        TextView tvClose = dialogError.findViewById(R.id.tvClose);
        TextView tvRetry = dialogError.findViewById(R.id.tvRetry);
        tvErrorMessage.setText(msg + "");

        tvClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogError.dismiss();
            }
        });

        tvRetry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogError.dismiss();
                retry(call);
            }
        });

        dialogError.show();
    }

    private void retry(Call<T> call) {
        call.clone().enqueue(this);
    }
}