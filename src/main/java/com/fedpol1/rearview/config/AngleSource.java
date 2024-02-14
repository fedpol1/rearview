package com.fedpol1.rearview.config;

public enum AngleSource {
    SELF,
    SELF_MOTION,
    VEHICLE,
    VEHICLE_MOTION,
    ROOT_VEHICLE,
    ROOT_MOTION,
    ZERO;

    public boolean isMotion() {
        return this == AngleSource.SELF_MOTION || this == AngleSource.VEHICLE_MOTION || this == AngleSource.ROOT_MOTION;
    }

    public boolean isRotation() {
        return this == AngleSource.SELF || this == AngleSource.VEHICLE || this == AngleSource.ROOT_VEHICLE;
    }
}
