package com.why.okhttp;

import android.app.Activity;
import android.os.Bundle;

import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;

/**
 * User: ysy
 * Date: 2015/8/31
 */
public class Test_Activity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        testGet();
        testPost();

    }

    private void testPost() {
        //创建okHttpClient对象
        OkHttpClient mOkHttpClient = new OkHttpClient();

        Request request = buildMultipartFormRequest();

        mOkHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {

            }

            @Override
            public void onResponse(Response response) throws IOException {
                String htmlStr2 = response.body().string();
                System.out.println("htmlstr2=" + htmlStr2);
            }
        });
    }

    private Request buildMultipartFormRequest() {

        String url = "https://github.com";
        FormEncodingBuilder builder = new FormEncodingBuilder();
        builder.add("username", "张鸿洋");

        Request request = new Request.Builder()
                .url(url)
                .post(builder.build())
                .build();

        return request;
    }

    private void testGet() {

        //创建okHttpClient对象
        OkHttpClient mOkHttpClient = new OkHttpClient();
        //创建一个Request
        final Request request = new Request.Builder()
                .url("https://github.com/hongyangAndroid").build();
        //new call
        Call call = mOkHttpClient.newCall(request);
        //请求加入调度
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {

            }

            @Override
            public void onResponse(Response response) throws IOException {
                String htmlStr = response.body().string();
                System.out.println("htmlstr=" + htmlStr);
            }
        });
    }
}
