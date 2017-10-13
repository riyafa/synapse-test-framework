package org.apache.synapse.integration;

import org.junit.Assert;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class EmulatorBackendTest extends BaseTest{
    String trpUrl = "http://" + getSynapseAddress() +"/services/emulator_backend";

    @Test
    public void testEmulatorBackend() throws IOException {
        URL url = new URL(trpUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        BufferedReader in = new BufferedReader(
                new InputStreamReader(
                        connection.getInputStream()));
        String inputLine;

        while ((inputLine = in.readLine()) != null) {
            Assert.assertEquals(inputLine,"User1");
        }
        in.close();
    }

}
