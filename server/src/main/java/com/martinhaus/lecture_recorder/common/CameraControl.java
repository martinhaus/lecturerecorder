package com.martinhaus.lecture_recorder.common;

import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CameraControl {

    void setScene(String cameraIp, int sceneId) throws IOException {
        String url = String.format("http://%s/rcp.xml?command=0x09A5&type=P_OCTET&direction=WRITE&num=1&idstring=showScene&payload=0x80000620%s081",
                cameraIp,
                sceneId - 1);

        try (DefaultHttpClient client = new DefaultHttpClient()) {
            HttpGet request = new HttpGet(url);
            client.execute(request);
        }
    }
}
