package io.baas.startup;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import io.baas.Baasio;
import org.usergrid.android.client.callbacks.ApiResponseCallback;
import org.usergrid.java.client.entities.Entity;
import org.usergrid.java.client.response.ApiResponse;

import static io.baas.common.utils.LogUtils.LOGE;
import static io.baas.common.utils.LogUtils.makeLogTag;

public class StartupActivity extends Activity {
    private static final String TAG = makeLogTag(StartupActivity.class);

    private Context mContext;

    private EditText collection;

    private EditText key1;

    private EditText value1;

    private EditText key2;

    private EditText value2;

    private String mCreatedUuid;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mContext = this;

        collection = (EditText) findViewById(R.id.collectionTxt);

        key1 = (EditText) findViewById(R.id.keyTxt1);

        value1 = (EditText) findViewById(R.id.valueTxt1);

        key2 = (EditText) findViewById(R.id.keyTxt2);

        value2 = (EditText) findViewById(R.id.valueTxt2);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }


    private void setProps(Entity e, EditText key, EditText value) {
        String k = key.getText().toString();
        String v = value.getText().toString();

        if (!TextUtils.isEmpty(k) && !TextUtils.isEmpty(v)) {
            e.setProperty(k, v);
        }
    }

    public void submitEntity(View view) {
        final String collName = collection.getText().toString();
        Entity entity = new Entity(collName);
        setProps(entity, key1, value1);
        setProps(entity, key2, value2);

        Baasio.getInstance().createEntityAsync(entity, new ApiResponseCallback() {

            private void onError(String msg) {
                LOGE(TAG, "createEntityAsync:" + msg);

                Toast.makeText(mContext, "Error: " + msg, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onException(Exception e) {
                onError(e.getMessage());
            }

            @Override
            public void onResponse(ApiResponse response) {
                if (response == null) {
                    LOGE(TAG, "No response");
                    return;
                }

                if (TextUtils.isEmpty(response.getError())) {
                    Entity entity = response.getFirstEntity();
                    Toast.makeText(mContext, "Create Success: " + entity.toString(), Toast.LENGTH_LONG).show();
                } else {
                    onError(response.getErrorDescription());
                }
            }
        });
    }
}
