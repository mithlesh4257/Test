package com.example.mithlesh4257.sampleprojectpinterest;

import com.crashlytics.android.Crashlytics;
import com.pinterest.android.pdk.PDKCallback;
import com.pinterest.android.pdk.PDKClient;
import com.pinterest.android.pdk.PDKException;
import com.pinterest.android.pdk.PDKResponse;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import io.fabric.sdk.android.Fabric;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {
    private PDKClient pdkClient;
    private static final String appID = "12345";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_main);

        pdkClient = PDKClient.configureInstance(this, appID);

        // Call onConnect() method to make link between App id and Pinterest SDK
        pdkClient.onConnect(this);
        pdkClient.setDebugMode(true);



    }

    public void onLogin(View view) {

        List scopes = new ArrayList<String>();
        scopes.add(PDKClient.PDKCLIENT_PERMISSION_READ_PUBLIC);
        scopes.add(PDKClient.PDKCLIENT_PERMISSION_WRITE_PUBLIC);
        scopes.add(PDKClient.PDKCLIENT_PERMISSION_READ_RELATIONSHIPS);
        scopes.add(PDKClient.PDKCLIENT_PERMISSION_WRITE_RELATIONSHIPS);
        scopes.add(PDKClient.PDKCLIENT_PERMISSION_READ_PRIVATE);
        scopes.add(PDKClient.PDKCLIENT_PERMISSION_WRITE_PRIVATE);

        pdkClient.login(this, scopes, new PDKCallback() {

            /**
             * It called, when Authentication success
             * @param response
             */
            @Override
            public void onSuccess(PDKResponse response) {

                Log.e(getClass().getName(), response.getData().toString());

            }

            /**
             * It called, when Authentication failed
             * @param exception
             */
            @Override
            public void onFailure(PDKException exception) {
                Log.e(getClass().getName(), exception.getDetailMessage());
            }
        });
    }

    /**
     * It handle reuslt and switch back to own app when authentication process complete
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        pdkClient.onOauthResponse(requestCode, resultCode,
                data);
        // call onLoginSuccess() method
        onLoginSuccess();
    }

    /**
     * Start HomeActivity class
     */
    private void onLoginSuccess() {
        Intent i = new Intent(this, HomeActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
        finish();
    }
}
