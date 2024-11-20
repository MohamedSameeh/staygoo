package com.phptravelsnative.utality.Views;

import android.content.Context;

import com.android.volley.Cache;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.ImageLoader;

/**
 * Created by im_ha on 4/4/2016.
 */
public class SingleTonRequest {
    private static SingleTonRequest mInctance;
    private static Context mtx;
    private RequestQueue requestQueue;
    private ImageLoader mImageLoader;

    private SingleTonRequest(Context context)
    {
        mtx=context;
        requestQueue=getRequestQueue();
    }
    public static synchronized SingleTonRequest getmInctance(Context context)
    {
        if(mInctance==null)
        {
            mInctance=new SingleTonRequest(context);
        }
        return  mInctance;
    }
    public RequestQueue getRequestQueue()
    {
        if(requestQueue==null)
        {
            Cache cache=new DiskBasedCache(mtx.getCacheDir(),10*1024*1024);
            com.android.volley.Network network=new BasicNetwork(new HurlStack());
            requestQueue=new RequestQueue(cache,network);
            requestQueue.start();
        }
        return  requestQueue;
    }
}
