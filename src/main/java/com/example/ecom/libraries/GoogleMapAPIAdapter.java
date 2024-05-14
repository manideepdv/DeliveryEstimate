package com.example.ecom.libraries;

import com.example.ecom.libraries.models.GLocation;

public class GoogleMapAPIAdapter implements MapApiAdapter {
    private GoogleMapsApi googleMapsApi;

    public GoogleMapAPIAdapter() {
        this.googleMapsApi = new GoogleMapsApi();
    }

    @Override
    public int estimateTravelTime(GLocation src, GLocation dest) {
        // Call the GoogleMapsApi to estimate travel time
        return googleMapsApi.estimate(src, dest);
    }
}