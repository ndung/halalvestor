package id.halalvestor;

import android.app.Application;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.os.Build;
import android.os.StrictMode;
import android.support.multidex.MultiDex;
import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

import net.gotev.uploadservice.UploadService;

import id.halalvestor.util.LocaleHelper;
import id.halalvestor.util.LruBitmapCache;
import id.halalvestor.util.NotificationUtil;
import id.halalvestor.util.PreferenceManager;

public class App extends Application {

    private static final String TAG = App.class.getSimpleName();

    private static App mInstance;

    public static synchronized App getInstance() {
        return mInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;

        //FontsOverride.setDefaultFont(this, "SERIF", "ProximaNova-Regular.otf");
        UploadService.NAMESPACE = BuildConfig.APPLICATION_ID;

        if(Build.VERSION.SDK_INT>=24){
            try{
                StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
                StrictMode.setVmPolicy(builder.build());
            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }

    private ImageLoader mImageLoader;
    private LruBitmapCache mLruBitmapCache;

    public ImageLoader getImageLoader() {
        getRequestQueue();
        if (mImageLoader == null) {
            getLruBitmapCache();
            mImageLoader = new ImageLoader(this.mRequestQueue, mLruBitmapCache);
        }

        return this.mImageLoader;
    }

    public LruBitmapCache getLruBitmapCache() {
        if (mLruBitmapCache == null)
            mLruBitmapCache = new LruBitmapCache();
        return this.mLruBitmapCache;
    }

    private String appVersion;

    public String getAppVersion() {
        if (appVersion==null) {
            int verCode = 0;
            try {
                PackageInfo pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
                verCode = pInfo.versionCode;
            } catch (Exception ex) {
            }
            appVersion = String.valueOf(verCode);
        }
        return appVersion;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(LocaleHelper.onAttach(base));
        MultiDex.install(this);
    }

    private PreferenceManager preferenceManager;

    public PreferenceManager getPreferenceManager() {
        if (preferenceManager == null) {
            preferenceManager = new PreferenceManager(this);
        }

        return preferenceManager;
    }

    private NotificationUtil notificationUtil;

    public NotificationUtil getNotificationUtil(){
        if (notificationUtil == null) {
            notificationUtil = new NotificationUtil(this);
        }

        return notificationUtil;
    }

    private RequestQueue mRequestQueue;

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }

        return mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req, String tag) {
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestQueue().add(req);
    }

    public <T> void addToRequestQueue(Request<T> req) {
        req.setTag(TAG);
        getRequestQueue().add(req);
    }

    public void cancelPendingRequests(Object tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }
}