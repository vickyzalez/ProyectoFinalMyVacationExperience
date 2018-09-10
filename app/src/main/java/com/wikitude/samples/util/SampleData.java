package com.wikitude.samples.util;

import com.wikitude.common.camera.CameraSettings.CameraFocusMode;
import com.wikitude.common.camera.CameraSettings.CameraPosition;
import com.wikitude.common.camera.CameraSettings.CameraResolution;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class SampleData implements Serializable {

    //TODO LLEVAR
    private final String name;
    private final String path;
    private final Class activityClass;
    private final List<String> extensions;

    private final int arFeatures;
    private final CameraPosition cameraPosition;
    private final CameraResolution cameraResolution;
    private final CameraFocusMode cameraFocusMode;
    private final boolean camera2Enabled;

    private SampleData(@NonNull Builder builder) {
        name = builder.name;
        path = builder.path;
        activityClass = builder.activityClass;
        extensions = builder.extensions;
        arFeatures = builder.arFeatures;
        cameraPosition = builder.cameraPosition;
        cameraResolution = builder.cameraResolution;
        cameraFocusMode = builder.cameraFocusMode;
        camera2Enabled = builder.camera2Enabled;
    }

    public String getName() {
        return name;
    }

    public String getPath() {
        return path;
    }

    public Class getActivityClass() {
        return activityClass;
    }

    public List<String> getExtensions() {
        return extensions;
    }

    public int getArFeatures() {
        return arFeatures;
    }

    public CameraPosition getCameraPosition() {
        return cameraPosition;
    }

    public CameraResolution getCameraResolution() {
        return cameraResolution;
    }

    public CameraFocusMode getCameraFocusMode() {
        return cameraFocusMode;
    }

    public boolean isCamera2Enabled() {
        return camera2Enabled;
    }

    public static class Builder {

        private final @NonNull String name;
        private final @NonNull String path;
        private Class activityClass = DefaultConfigs.DEFAULT_ACTIVITY;
        private @NonNull List<String> extensions = new ArrayList<>();
        private int arFeatures = DefaultConfigs.DEFAULT_AR_FEATURES;
        private @NonNull CameraPosition cameraPosition = DefaultConfigs.DEFAULT_CAMERA_POSITION;
        private @NonNull CameraResolution cameraResolution = DefaultConfigs.DEFAULT_CAMERA_RESOLUTION;
        private @NonNull CameraFocusMode cameraFocusMode = DefaultConfigs.DEFAULT_CAMERA_FOCUS_MODE;
        private boolean camera2Enabled = DefaultConfigs.DEFAULT_CAMERA_2_ENABLED;

        public Builder(@NonNull String name, @NonNull String path) {
            this.name = name;
            this.path = path;
        }

        public Builder extensions(@Nullable List<String> extensions) {
            if (extensions != null) {
                this.extensions = extensions;
            }
            return this;
        }

        public Builder arFeatures(int arFeatures) {
            this.arFeatures = arFeatures;
            return this;
        }

        public Builder activityClass(@Nullable Class activityClass) {
            if (activityClass != null) {
                this.activityClass = activityClass;
            }
            return this;
        }

        public Builder cameraPosition(@Nullable CameraPosition cameraPosition) {
            if (cameraPosition != null) {
                this.cameraPosition = cameraPosition;
            }
            return this;
        }

        public Builder cameraResolution(@Nullable CameraResolution cameraResolution) {
            if (cameraResolution != null) {
                this.cameraResolution = cameraResolution;
            }
            return this;
        }

        public Builder cameraFocusMode(@Nullable CameraFocusMode cameraFocusMode) {
            if (cameraFocusMode != null) {
                this.cameraFocusMode = cameraFocusMode;
            }
            return this;
        }

        public Builder camera2Enabled(boolean camera2Enabled) {
            this.camera2Enabled = camera2Enabled;
            return this;
        }

        public SampleData build() {
            return new SampleData(this);
        }
    }
}
