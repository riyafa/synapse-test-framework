package org.apache.synapse.integration;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Assert;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


public class EmulatorBackendTest extends BaseTest {
    String trpUrl = "http://" + getSynapseAddress() + "/services/emulator_backend";
    private static final Log log = LogFactory.getLog(EmulatorBackendTest.class);
    long failed = 0;
    long passed = 0;
    long count = 100;

    @Test
    public void testEmulatorBackend() throws MalformedURLException {
        URL url = new URL(trpUrl);
        for (int i = 0; i < count; i++) {
            try {
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(
                                connection.getInputStream()));
                String inputLine;
                inputLine = in.readLine();
                if (inputLine == null) {
                    log.error("[" + i + "] Test failed ");
                    failed++;
//            Assert.fail();
                }

                while (inputLine != null) {
                    if (inputLine.equals("User1")) {
                        passed++;
                        log.info("[" + i + "] Test passed ");
                    }
//            Assert.assertEquals(inputLine, "User1");
                    inputLine = in.readLine();
                }
                in.close();
            } catch (Exception e) {
                log.error("Failed ", e);
                failed++;
            }
        }
        log.info(count + " tests were run");
        log.info(failed + " tests failed");
        log.info(passed + " tests passed");
        Assert.assertEquals(passed, count);
    }

}
