package com.example.sfcm;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.Set;

public class PermissionManager {
    private static final String TAG = PermissionManager.class.getSimpleName();

    private static final int READ_CONTACTS = 0;
    private Activity mActivity;
    private final Set<String> mPermissions;

    public PermissionManager(@NonNull Activity activity, @NonNull Set<String> permissions) {
        this.mActivity = activity;
        this.mPermissions = permissions;
    }

    public boolean hasAllPermissions() {
        if (mPermissions == null) return true;
        if (mPermissions.isEmpty()) return true;

        boolean hasPermissions = false;

        for (String i : mPermissions) {
            hasPermissions |= (ContextCompat.checkSelfPermission((Context) mActivity, i) == PackageManager.PERMISSION_GRANTED);
        }

        return hasPermissions;
    }

    public void requestAllPermissions() {
        if (mPermissions == null) return;
        if (mPermissions.isEmpty()) return;

        ActivityCompat.requestPermissions(mActivity,
                mPermissions.toArray(new String[0]), READ_CONTACTS);
    }

}
