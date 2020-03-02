package id.halalvestor.util;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import id.halalvestor.R;

public class IRDialogUtils {

    Context context;

    public IRDialogUtils(Context context) {
        this.context = context;
    }

    public Dialog createDialog(String title, int id, boolean cancelable){
        LayoutInflater inflater = LayoutInflater.from(context);
        View alertLayout = inflater.inflate(id, null);
        AlertDialog.Builder alert = new AlertDialog.Builder(context);
        alert.setView(alertLayout);
        alert.setCancelable(cancelable);

//        final Dialog dialog = new Dialog(context);
//        dialog.setContentView(id);
//        dialog.setTitle(title);
//        dialog.setCancelable(cancelable);

        final AlertDialog dialog = alert.create();
        dialog.create();

        TextView textView = (TextView) dialog.findViewById(R.id.tv_title);
        textView.setText(title);

        ImageView imageView = (ImageView) dialog.findViewById(R.id.iv_close);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        return dialog;
    }

    public Dialog createDialogNoTitle(int id, boolean cancelable){
        LayoutInflater inflater = LayoutInflater.from(context);
        View alertLayout = inflater.inflate(id, null);
        AlertDialog.Builder alert = new AlertDialog.Builder(context);
        alert.setView(alertLayout);
        alert.setCancelable(cancelable);

        //final AlertDialog dialog = alert.create();
        final Dialog dialog = new Dialog(context);
        dialog.setContentView(id);
        //dialog.setTitle(title);
        dialog.setCancelable(cancelable);

        TextView textView = (TextView) dialog.findViewById(R.id.tv_title);
        textView.setVisibility(View.GONE);

        ImageView imageView = (ImageView) dialog.findViewById(R.id.iv_close);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        imageView.setVisibility(View.GONE);

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        return dialog;
    }

    public Dialog showDialogNoTitle(String title, int id, boolean cancelable){
        Dialog dialog = createDialogNoTitle(id, cancelable);
        dialog.show();

        return dialog;
    }

    public Dialog showDialog(String title, int id, boolean cancelable){
        Dialog dialog = createDialog(title, id, cancelable);
        dialog.show();

        return dialog;
    }
}
