package com.mp3.player.retromusic.ads;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

public class LoadingDialog {
    public static AlertDialog loadingDialog;

    public static void showLoadingDialog(Context context) {
        try{
            LinearLayout layout = new LinearLayout(context);
            layout.setOrientation(LinearLayout.VERTICAL);
            layout.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM);
            layout.setPadding(50, 50, 50, 50);
//            layout.setBackgroundColor(Color.TRANSPARENT);

            ProgressBar progressBar = new ProgressBar(context);
            progressBar.setIndeterminate(true);
//            progressBar.getIndeterminateDrawable().setColorFilter(Color.RED, PorterDuff.Mode.SRC_IN);
//            progressBar.getProgressDrawable().setColorFilter(Color.GREEN, PorterDuff.Mode.SRC_IN);


            TextView textView = new TextView(context);
            textView.setText("Please wait...");
            textView.setTextSize(16);
            textView.setTextColor(Color.RED);
            textView.setGravity(Gravity.CENTER);

            layout.addView(progressBar);
            layout.addView(textView);

            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setView(layout);
            builder.setCancelable(false);

            loadingDialog = builder.create();
            // Loại bỏ background của dialog
            loadingDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            if (loadingDialog.getWindow() != null) {
                Window window = loadingDialog.getWindow();
                window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT)); // Xóa nền đen
                window.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND); // Loại bỏ shadow
                window.setDimAmount(0f); // Không làm tối nền sau
            }
            loadingDialog.show();
        } catch ( Exception e) {

        }

    }

    public static void dismissLoadingDialog() {
        if (loadingDialog != null) {
            loadingDialog.dismiss();
            loadingDialog = null; // Đặt lại null để tránh lỗi
        }
    }
}
