package com.example.lingez.sample_app;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by lingez on 1/21/18.
 */

public class RequestQueueSingleton {
    private static RequestQueueSingleton requestQueueSingleton;

    private RequestQueue requestQueue;
    private static Context context;

    private RequestQueueSingleton(Context ctx){
        context = ctx;
        requestQueue = getRequestQueue();
    }

    public static synchronized RequestQueueSingleton getInstance(Context context){
        if (requestQueueSingleton == null){
            requestQueueSingleton = new RequestQueueSingleton(context);
        }
        return requestQueueSingleton;
    }

    public RequestQueue getRequestQueue() {
        if(requestQueue == null){
            requestQueue = Volley.newRequestQueue(context.getApplicationContext());
        }
        return requestQueue;
    }

    public<T> void addToRequestQueue(Request<T> request){
        requestQueue.add(request);
    }
}
