package com.wraithmedia.playback;

import android.app.Activity;
import android.content.*;
import android.os.IBinder;
import android.util.Log;
import com.wraithmedia.core.service.AbstractServiceConnector;
import com.wraithmedia.core.service.ServiceToken;
import com.wraithmedia.playback.MediaPlaybackService.MediaPlaybackServiceBinder;

import java.util.HashMap;

public class MediaPlaybackServiceConnector extends AbstractServiceConnector<MediaPlaybackServiceConnectionCallback> {
    private static final String TAG_NAME = "MediaPlaybackServiceConnector";

    private HashMap<Context, ServiceBinder> mConnectionMap;

    public MediaPlaybackServiceConnector() {
        mConnectionMap = new HashMap<Context, ServiceBinder>();
    }

    private static class ServiceBinder implements ServiceConnection {
        private final MediaPlaybackServiceConnectionCallback mServiceConnectionCallback;

        public ServiceBinder(MediaPlaybackServiceConnectionCallback callback) {
            mServiceConnectionCallback = callback;
        }

        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            if (mServiceConnectionCallback != null) {
                MediaPlaybackServiceBinder binder = (MediaPlaybackServiceBinder)iBinder;
                mServiceConnectionCallback.onServiceConnected(componentName, binder.getService());
            }
        }

        public void onServiceDisconnected(ComponentName componentName) {
            if (mServiceConnectionCallback != null) {
                mServiceConnectionCallback.onServiceDisconnected(componentName);
            }
        }
    }

    @Override
    public ServiceToken bindToService(Activity context, MediaPlaybackServiceConnectionCallback connectionCallback) {
        ContextWrapper wrapper = new ContextWrapper(context);
        ServiceBinder binder = new ServiceBinder(connectionCallback);

        Intent serviceIntent = new Intent(wrapper, MediaPlaybackService.class);
        wrapper.startService(serviceIntent);

        if (wrapper.bindService(serviceIntent, binder, Context.BIND_AUTO_CREATE)) {
            mConnectionMap.put(wrapper, binder);
            return new ServiceToken(wrapper);
        }

        Log.e(TAG_NAME, "Failed to bind to the MediaPlaybackService");
        return null;
    }

    @Override
    public void unbindFromService(ServiceToken serviceToken) {
        if (serviceToken == null) {
            return;
        }

        ContextWrapper wrapper = serviceToken.getContextWrapper();
        ServiceBinder serviceBinder = mConnectionMap.remove(wrapper);
        if (serviceBinder != null) {
            wrapper.unbindService(serviceBinder);
        }
    }
}
