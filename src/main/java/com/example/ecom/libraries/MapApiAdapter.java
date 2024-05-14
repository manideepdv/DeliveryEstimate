package com.example.ecom.libraries;

import com.example.ecom.libraries.models.GLocation;

public interface MapApiAdapter {
    int estimateTravelTime(GLocation src, GLocation dest);
}
