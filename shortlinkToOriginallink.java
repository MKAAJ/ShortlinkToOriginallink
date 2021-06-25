package com.urlcheck;

import android.content.ComponentName;
import android.content.ContextWrapper;
import android.content.Intent;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import retrofit2.Retrofit;

public class ScanActivity extends AppCompatActivity {
    public int i = 0;
    private String urlToCheck;
    private Retrofit retrofit;
    private Intent browserIntent;
    private ComponentName browserCompName;

        urlToCheck = getIntent().getDataString(); // will get url once the link is clicked


        /// Start APIs for return the short link url to normal url---------------------------------------------------------------
        if(urlToCheck.length()<30){
            if(urlToCheck.contains("bit.ly")){
                OkHttpClient clientBitly=new OkHttpClient();
                String Bitly_API_Address="https://api-ssl.bitly.com";
                String Bitly_GET="/v3/expand?access_token=ENTER YOUR TOKEN";
                String Bitly_format="&format=txt";
                String Bitly_Request=Bitly_API_Address+Bitly_GET+urlToCheck+Bitly_format;
                Request requestBitly=new Request.Builder().url(Bitly_Request).build();
                clientBitly.newCall(requestBitly).enqueue(new okhttp3.Callback() {
                    @Override
                    public void onFailure(@NotNull okhttp3.Call call, @NotNull IOException e) {
                        Log.i("333in","onFailure***************BITLY");
                        e.printStackTrace();
                    }



                    @Override
                    public void onResponse(@NotNull okhttp3.Call call, @NotNull okhttp3.Response response) throws IOException {
                        if(response.isSuccessful()){
                            final String Response=response.body().string();
                            ScanActivity.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Log.i("555api_urlinBITLY1",Response);
                                    Log.i("777_orignal_link:1"+found_in_first,Response);
                                    setUrlToCheck(Response);
                                    scan_function2(urlToCheck);
                                    new Timer().schedule(new TimerTask() {
                                        @Override
                                        public void run() {
                                            if(!getfound_in_first()) {
                                                Log.i("00Exit::","scan_function//////"+found_in_first);
                                                scan_function(urlToCheck);
                                               // getScanIDAndExecuteScan();// this code will be executed after 2 seconds
                                            }}
                                    }, 1250);
                                    new Timer().schedule(new TimerTask() {
                                        @Override
                                        public void run() {
                                            if(isFound_in_second()) {
                                                Log.i("00Exit::","scan_function//////"+found_in_first);
                                                hideProgressDialog();
                                                showURLBeingAnalyzedDialog();
                                                // getScanIDAndExecuteScan();// this code will be executed after 2 seconds
                                            }}
                                    }, 3000);
                                    //  if(!found_in_first) {scan_function(urlToCheck); }
                                }
                            });
                        }
                    }
                });

            }else {
                OkHttpClient client=new OkHttpClient();
                final Gson gson=new Gson();
                String urlTOAPI="https://unshorten.me/json/"+urlToCheck;
                Request request=new Request.Builder().url(urlTOAPI).build();
                client.newCall(request).enqueue(new okhttp3.Callback() {
                    @Override
                    public void onFailure(@NotNull okhttp3.Call call, @NotNull IOException e) {
                        Log.i("333in","onFailure***************not bitly");
                        e.printStackTrace();
                    }
                    @Override
                    public void onResponse(@NotNull okhttp3.Call call, @NotNull okhttp3.Response response) throws IOException {
                        if(response.isSuccessful()){
                            final String Response=response.body().string();
                            extendREsponse extendREsponse=gson.fromJson(Response, com.AntiPhishingProject.urlcheck.extendREsponse.class);
                            final String myResponse=extendREsponse.getResolved_url();
                            ScanActivity.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Log.i("555api_urlin",myResponse+"not Bitly");
                                    Log.i("777_orignal_link:"+found_in_first,myResponse);

                                    setUrlToCheck(myResponse);
                                    scan_function2(urlToCheck);
                                    new Timer().schedule(new TimerTask() {
                                        @Override
                                        public void run() {
                                            if(!getfound_in_first()) {
                                                Log.i("00Exit::","scan_function//////"+found_in_first);
                                                scan_function(urlToCheck);
                                                //getScanIDAndExecuteScan();// this code will be executed after 2 seconds
                                            }}
                                    }, 1250);
                                    new Timer().schedule(new TimerTask() {
                                        @Override
                                        public void run() {
                                            if(isFound_in_second()) {
                                                Log.i("00Exit::","scan_function//////"+found_in_first);
                                                hideProgressDialog();
                                                showURLBeingAnalyzedDialog();
                                                // getScanIDAndExecuteScan();// this code will be executed after 2 seconds
                                            }}
                                    }, 3000);
                                    //   if(!found_in_first ) { scan_function(urlToCheck); }
                                }
                            });
                        }
                    }
                });
            }
        }
        ///End of APIs---------------------------------------------------------------
        else {
            setUrlToCheck(urlToCheck);
            scan_function2(urlToCheck);
            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    if(!getfound_in_first()) {
                        Log.i("00Exit::","scan_function//////"+found_in_first);
                        scan_function(urlToCheck);
                        //getScanIDAndExecuteScan();// this code will be executed after 2 seconds
                    }}
            }, 1250);
            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    if(isFound_in_second()) {
                        Log.i("00Exit::","scan_function//////"+found_in_first);
                        hideProgressDialog();
                        showURLBeingAnalyzedDialog();
                        // getScanIDAndExecuteScan();// this code will be executed after 2 seconds
                    }}
            }, 3000);
        }


//--------------------------------------------------------------------------------------------------------------------
    }
