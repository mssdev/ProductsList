package com.walmartlabs.android.productlist.api;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Build;
import android.provider.Settings;
import android.util.Log;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.walmartlabs.android.productlist.api.retrofit_services.WalmartLabsRestServices;
import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.content.ContentValues.TAG;

/***
 * utility class used to create the retrofit calls.
 */
public class ApiUtils {


    private static final String TAG = ApiUtils.class.getSimpleName();
    private String baseUrl;
    private Retrofit retrofit;
    private WalmartLabsRestServices service;
    private ConnectivityManager connectivityManager;
    private Context context;
    private SharedPreferences sharedPreferences;

    public ApiUtils(String baseUrl,Context context) {
        this.baseUrl = baseUrl;

        retrofit =provideRetrofit(context);
        service = retrofit.create(WalmartLabsRestServices.class);
        this.context = context;
        connectivityManager =   (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        sharedPreferences = context.getSharedPreferences("data", Context.MODE_PRIVATE);

    }
    /* lets use thread pooling and
     a Executor for OkHttpClient. I want to do this do that the callbacks from
     the Call method are returned on the background thread. we will have to post
     them to the main thread but it allows me to do any business logic on the backround thread
     before passing it to the main thread. */

    private static final int CPU_COUNT = Runtime.getRuntime().availableProcessors();
    private static final int CORE_POOL_SIZE = CPU_COUNT + 1;
    public static final Executor THREAD_POOL_EXECUTOR =
        Executors.newFixedThreadPool(CORE_POOL_SIZE);

    public Retrofit provideRetrofit(Context context) {

        return new Retrofit.Builder().callbackExecutor(THREAD_POOL_EXECUTOR)
            .baseUrl(baseUrl)
            .client(getOkHttpClient(context))


            .addConverterFactory(GsonConverterFactory.create(getGson()))
            .build();
    }

    public SharedPreferences getSharedPreferences() {
        return sharedPreferences;
    }

    private Interceptor provideCacheInterceptor () {
        return new Interceptor() {
            @Override
            public Response intercept (Interceptor.Chain chain) throws IOException {
                Response response = chain.proceed( chain.request() );
                CacheControl cacheControl = new CacheControl.Builder()
                    .maxAge( 10, TimeUnit.MINUTES )
                    .build();

                Response.Builder r =   response.newBuilder()
                    .header("Cache-Control", cacheControl.toString() );
                Log.d(TAG, "ADDING HEADER " + sharedPreferences.getString("etag", ""));
                if( sharedPreferences.getString("etag", "").length() > 0 ) {
                    r.header("etag", sharedPreferences.getString("etag", ""));
                }
                return   r.build();
            }
        };
    }
    //
    private Interceptor provideOfflineCacheInterceptor () {
        return new Interceptor() {
            @Override
            public Response intercept (Interceptor.Chain chain) throws IOException {

                Request request = chain.request();

                if( isAirplaneModeOn() ) {
                    CacheControl cacheControl =
                        new CacheControl.Builder().maxStale(7, TimeUnit.DAYS).build();

                    request = request.newBuilder().cacheControl(cacheControl).build();
                }
                return chain.proceed(request);
            }
        };
    }


    public OkHttpClient getOkHttpClient(Context context) {

        Log.d(TAG, "Context.getCacheDir()" + context.getCacheDir());

        Cache cache = new Cache(new File(context.getCacheDir(), "http-cache"), 10 * 1024 * 1024);

        final OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(60 * 1000, TimeUnit.MILLISECONDS)
            .readTimeout(60 * 1000, TimeUnit.MILLISECONDS);
        builder.cache(cache).addNetworkInterceptor(provideCacheInterceptor());
        builder.cache(cache).addInterceptor(provideOfflineCacheInterceptor());

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        builder.addInterceptor(interceptor);


        OkHttpClient client = builder.build();

        return client;
    }

    /***
     * set it up forcing the expose annotation. I like it this way. then we can have
     * member vars that are not exposed in the JSON if they are not actually used.
     * @return Gson object.
     */
    private Gson getGson() {
        GsonBuilder builder = new GsonBuilder().excludeFieldsWithoutExposeAnnotation();
        return builder.create();
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public void init() {

    }

    public WalmartLabsRestServices getService() {
        return service;
    }



        @SuppressWarnings("deprecation")
        @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
        public boolean isAirplaneModeOn() {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR1) {
                return Settings.System.getInt(context.getContentResolver(),
                    Settings.System.AIRPLANE_MODE_ON, 0) != 0;
            } else {
                return Settings.Global.getInt(context.getContentResolver(),
                    Settings.Global.AIRPLANE_MODE_ON, 0) != 0;
            }
        }


}
