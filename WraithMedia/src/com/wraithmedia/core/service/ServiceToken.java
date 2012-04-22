package com.wraithmedia.core.service;

import android.content.ContextWrapper;

public class ServiceToken {
    private final ContextWrapper mContextWrapper;

    public ServiceToken(ContextWrapper contextWrapper) {
        mContextWrapper = contextWrapper;
    }

    public ContextWrapper getContextWrapper() {
        return mContextWrapper;
    }
}
