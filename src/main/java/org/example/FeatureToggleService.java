package org.example;

import org.ff4j.FF4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FeatureToggleService {

    @Autowired
    private FF4j ff4j;

    public void enableGraphCreationFeature() {
        ff4j.enable("graphGenerator");
    }

    public void disableGraphCreationFeature() {
        ff4j.disable("graphGenerator");
    }

    public boolean isGraphCreationFeatureEnabled() {
        return ff4j.check("enableGraphCreation");
    }
}
