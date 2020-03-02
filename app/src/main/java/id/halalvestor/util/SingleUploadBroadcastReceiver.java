package id.halalvestor.util;

import android.content.Context;

import net.gotev.uploadservice.ServerResponse;
import net.gotev.uploadservice.UploadInfo;
import net.gotev.uploadservice.UploadServiceBroadcastReceiver;

public class SingleUploadBroadcastReceiver extends UploadServiceBroadcastReceiver {

    public interface Delegate {
        void onProgress(int progress);

        void onError(Exception exception);

        void onCompleted(int serverResponseCode, int requestCode, String serverResponseBody);

        void onCancelled();
    }

    private String mUploadID;
    private int requestCode;
    private Delegate mDelegate;

    public void setUploadID(String uploadID) {
        mUploadID = uploadID;
    }

    public void setDelegate(Delegate delegate) {
        mDelegate = delegate;
    }

    public void setRequestCode(int requestCode) {
        this.requestCode = requestCode;
    }

    @Override
    public void onProgress(Context context, UploadInfo uploadInfo) {
        if (uploadInfo.getUploadId().equals(mUploadID) && mDelegate != null) {
            mDelegate.onProgress(uploadInfo.getProgressPercent());
        }
    }

    @Override
    public void onError(Context context, UploadInfo uploadInfo, ServerResponse serverResponse, Exception exception) {

        //Log.e("error", serverResponse.getBodyAsString());
        if (uploadInfo.getUploadId().equals(mUploadID) && mDelegate != null) {
            mDelegate.onError(exception);
        }
    }

    @Override
    public void onCompleted(Context context, UploadInfo uploadInfo, ServerResponse serverResponse) {
        if (uploadInfo.getUploadId().equals(mUploadID) && mDelegate != null) {
            mDelegate.onCompleted(serverResponse.getHttpCode(), requestCode, serverResponse.getBodyAsString());
        }
    }

    @Override
    public void onCancelled(Context context, UploadInfo uploadInfo) {
        if (uploadInfo.getUploadId().equals(mUploadID) && mDelegate != null) {
            mDelegate.onCancelled();
        }
    }
}