package com.pkisi.dkuc.couriers;

import org.osmdroid.util.GeoPoint;

public class Point {
    private boolean seen;
    private boolean current;
    private GeoPoint geoPoint;

    public boolean isSeen() {
        return seen;
    }

    public void setSeen(boolean seen) {
        this.seen = seen;
    }

    public GeoPoint getGeoPoint() {
        return geoPoint;
    }

    public void setGeoPoint(GeoPoint geoPoint) {
        this.geoPoint = geoPoint;
    }

    public Point(){}

    public Point(boolean seen, boolean current, GeoPoint geoPoint) {
        this.seen = seen;
        this.current = current;
        this.geoPoint = geoPoint;
    }

    public boolean isCurrent() {
        return current;
    }

    public void setCurrent(boolean current) {
        this.current = current;
    }

}
