package id.halalvestor.activity.adapter;

import android.app.Activity;
import android.app.Dialog;
import android.os.Environment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import id.halalvestor.Constants;
import id.halalvestor.R;
import id.halalvestor.model.ChatMessage;
import id.halalvestor.ui.TouchImageView;

public class ChatThreadAdapter extends RecyclerView.Adapter<ChatThreadAdapter.ViewHolder> {

    private static String TAG = ChatThreadAdapter.class.getSimpleName();

    private int SELF = 100;
    private static String today;
    private ArrayList<ChatMessage> messageArrayList;
    private Activity activity;

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView message, timestamp;
        ImageView imageView, status;

        public ViewHolder(View view) {
            super(view);
            message = itemView.findViewById(R.id.message);
            timestamp = itemView.findViewById(R.id.timestamp);
            imageView = itemView.findViewById(R.id.img);
            status = itemView.findViewById(R.id.iv_status);
        }
    }

    String path = "";

    public ChatThreadAdapter(Activity activity, ArrayList<ChatMessage> messageArrayList) {
        this.activity = activity;
        this.messageArrayList = messageArrayList;

        Calendar calendar = Calendar.getInstance();
        today = String.valueOf(calendar.get(Calendar.DAY_OF_MONTH));
        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), Constants.IMAGE_DIRECTORY_NAME);
        if (!file.exists()) {
            file.mkdirs();
        }
        path = file.getPath();
    }

    @Override
    public ChatThreadAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView;

        // view type is to identify where to render the chat message
        // left or right
        if (viewType == SELF) {
            // self message
            itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_item_self, parent, false);
        } else {
            // others message
            itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_item_other, parent, false);
        }


        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final ChatMessage message = messageArrayList.get(position);

        boolean bool =  message.getMessage().toLowerCase().startsWith("http") &&
                        (message.getMessage().toLowerCase().endsWith(".jpg") || message.getMessage().toLowerCase().endsWith(".png") ||
                                message.getMessage().toLowerCase().endsWith(".jpeg") || message.getMessage().toLowerCase().endsWith(".gif") ||
                                message.getMessage().toLowerCase().endsWith(".tif") || message.getMessage().toLowerCase().endsWith(".bmp"));
        if (bool) {
            final String url = message.getMessage().trim();
            String fileName = url.substring(url.lastIndexOf("/") + 1);

            String uriString = path + File.separator + fileName;

            holder.imageView.setVisibility(View.VISIBLE);

            final File imgFile = new File(uriString);
            final boolean isFileExist = imgFile.exists();

            if (message.isSelf() && isFileExist) {
                Picasso.with(activity).load(imgFile).into(holder.imageView);
            } else {
                Picasso.with(activity).load(url).into(holder.imageView);
            }
            holder.message.setVisibility(View.GONE);

            holder.imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final Dialog nagDialog = new Dialog(activity, android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);
                    nagDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    nagDialog.setCancelable(false);
                    nagDialog.setContentView(R.layout.preview_image_layout);
                    TouchImageView ivPreview = nagDialog.findViewById(R.id.iv_preview_image);
                    if (message.isSelf() && isFileExist) {
                        Picasso.with(activity).load(imgFile).into(ivPreview);
                    } else {
                        Picasso.with(activity).load(url).into(ivPreview);
                    }
                    ivPreview.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View arg0) {
                            nagDialog.dismiss();
                        }
                    });
                    nagDialog.show();
                }
            });
        } else {
            holder.message.setVisibility(View.VISIBLE);
            holder.imageView.setVisibility(View.GONE);
            holder.message.setText(message.getMessage());
        }
        String timestamp = getTimeStamp(message.getCreatedAt());

        if (message.getUser() != null)
            timestamp = message.getUser() + ", " + timestamp;
        holder.timestamp.setText(timestamp);

        if (message.isSelf() && holder.status != null) {
            if (message.isRead()) {
                holder.status.setImageResource(R.drawable.ic_ceklis_read);
            } else {
                holder.status.setImageResource(R.drawable.ic_ceklis_sent);
            }
        }
    }

    @Override
    public int getItemViewType(int position) {
        ChatMessage message = messageArrayList.get(position);
        if (message.isSelf()) {
            return SELF;
        }

        return position;
    }

    @Override
    public int getItemCount() {
        return messageArrayList.size();
    }

    private static String getTimeStamp(String dateStr) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String timestamp = "";

        today = today.length() < 2 ? "0" + today : today;

        try {
            Date date = format.parse(dateStr);
            SimpleDateFormat todayFormat = new SimpleDateFormat("dd");
            String dateToday = todayFormat.format(date);
            format = dateToday.equals(today) ? new SimpleDateFormat("hh:mm a") : new SimpleDateFormat("dd LLL, hh:mm a");
            String date1 = format.format(date);
            timestamp = date1.toString();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return timestamp;
    }
}
