
package io.baas.startup;

import android.app.Application;
import android.os.AsyncTask;
import android.util.Log;
import io.baas.Baasio;
import io.baas.gcm.GcmUtils;
import io.baas.gcm.callback.GcmTaskCallback;

import static io.baas.common.utils.LogUtils.makeLogTag;

public class StartupApplication extends Application {
    private static final String TAG = makeLogTag(StartupApplication.class);

    private AsyncTask mGCMRegisterTask;

    @Override
    public void onCreate() {
        super.onCreate();

        // Baasio.getInstance().init(this, BaasioConfig.BAASIO_URL,
        // BaasioConfig.ORGANIZATION_ID,
        // BaasioConfig.APPLICATION_ID, BaasioConfig.GCM_SENDER_ID);

        Baasio.getInstance().init(this, BaasioConfig.BAASIO_URL, BaasioConfig.ORGANIZATION_ID,
                BaasioConfig.APPLICATION_ID);

        if (Baasio.getInstance().isGcmEnabled()) {
            mGCMRegisterTask = GcmUtils.registerGCMClient(this, new GcmTaskCallback() {

                @Override
                public void onResponse(String response) {
                    Log.i(TAG, response);
                }
            });
        }
    }

    @Override
    public void onTerminate() {
        if (mGCMRegisterTask != null) {
            mGCMRegisterTask.cancel(true);
        }

        GcmUtils.onDestroy(this);

        super.onTerminate();
    }
}
