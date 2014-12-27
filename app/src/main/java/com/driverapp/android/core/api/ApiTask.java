package com.driverapp.android.core.api;
import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.util.List;

/**
 * Created by Jesus Christ. Amen.
 */
public abstract class ApiTask<ResultType> extends AsyncTask<Object,Void,Object> {
    private static final String LOG_TAG = "Api task";
    private static final String serverUrl = "http://driverapp.ru/api/";
    private final String methodName;
    private final boolean post;
    List<NameValuePair> arguments;
    public ApiTask(String methodName, List<NameValuePair> arguments, boolean post){
        this.arguments = arguments;
        this.methodName = methodName;
        this.post = post;
    }

    @Override
    protected Object doInBackground(Object... params) {
        try {

            String url = serverUrl+methodName;

            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpRequestBase request;
            if(post) {
                request = new HttpPost(url);
                ((HttpPost)request).setEntity(new UrlEncodedFormEntity(arguments));
            }else{
                String args = URLEncodedUtils.format(arguments, "utf-8");
                url += "?" + args;
                request = new HttpGet(url);
            }

            Log.i(LOG_TAG,"Executing request to url: " + url);
            HttpResponse httpResponse = httpClient.execute(request);
            HttpEntity httpEntity = httpResponse.getEntity();
            String responseString = EntityUtils.toString(httpEntity);
            Log.i(LOG_TAG,"Server response " + responseString);
            return parse(responseString);

        }
        catch (Exception exp){
            Log.e(LOG_TAG,"Loader error " + exp.toString());
            return exp;
        }
    }

    protected ResultType parse(String json) {

        return new Gson().fromJson(json, new TypeToken<ResultType>() {
        }.getType());
    }

    @Override
    protected void onPostExecute(Object o) {
        if(o instanceof Exception){
            Exception exp = (Exception) o;
            exp.printStackTrace();
            onError(exp);
        }else{
            onSuccess((ResultType) o);
        }
    }

    protected abstract void onSuccess(ResultType result);

    protected abstract void onError(Exception exp);
}