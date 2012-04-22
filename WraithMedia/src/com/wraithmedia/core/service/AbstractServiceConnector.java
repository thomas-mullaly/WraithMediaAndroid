package com.wraithmedia.core.service;

import android.app.Activity;

public abstract class AbstractServiceConnector<ServiceConnectionCallback> {
    public ServiceToken bindToService(Activity context) {
        return bindToService(context, null);
    }

    public abstract ServiceToken bindToService(Activity context, ServiceConnectionCallback connectionCallback);

    public abstract void unbindFromService(ServiceToken serviceToken);
}
