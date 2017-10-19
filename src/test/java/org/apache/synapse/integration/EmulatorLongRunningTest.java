package org.apache.synapse.integration;

import io.netty.handler.codec.http.HttpMethod;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.wso2.carbon.protocol.emulator.dsl.Emulator;
import org.wso2.carbon.protocol.emulator.http.client.contexts.HttpClientConfigBuilderContext;
import org.wso2.carbon.protocol.emulator.http.client.contexts.HttpClientRequestBuilderContext;
import org.wso2.carbon.protocol.emulator.http.client.contexts.HttpClientResponseBuilderContext;
import org.wso2.carbon.protocol.emulator.http.client.contexts.HttpClientResponseProcessorContext;

import java.net.MalformedURLException;

public class EmulatorLongRunningTest extends BaseTest {
    private static final Log log = LogFactory.getLog(EmulatorLongRunningTest.class);
    private long failed = 0;
    private long passed = 0;

    @Test @Ignore
    public void testEmulatorBackend() throws MalformedURLException {

        long count = 10;
        for (int i = 1; i <= count; i++) {
            try {
                HttpClientResponseProcessorContext response = Emulator.getHttpEmulator()
                        .client()
                        .given(HttpClientConfigBuilderContext.configure()
                                       .host(getConfig().getSynapseServer().getHostname())
                                       .port(Integer.parseInt(getConfig().getSynapseServer().getPort())))
                        .when(HttpClientRequestBuilderContext.request()
                                      .withPath(
                                              "/services/emulator_slow_read")
                                      .withMethod(
                                              HttpMethod
                                                      .POST))
                        .then(HttpClientResponseBuilderContext.response()
                                      .assertionIgnore())
                        .operation()
                        .send();
                if (response == null) {
                    log.error("[" + i + "] Failed: response is null");
                    failed++;
                    continue;
                }
                if (response.getReceivedResponseContext().getResponseBody().equals("Slowly responding backend")) {
                    log.info("[" + i + "] Passed");
                    passed++;
                } else {
                    log.error("[" + i + "] Failed: response is incorrect");
                    failed++;
                }
            } catch (Exception e) {
                log.error("[" + i + "] Failed", e);
                failed++;
            }
        }
        log.info(count + " tests were run");
        log.info(failed + " tests failed");
        log.info(passed + " tests passed");
        Assert.assertEquals(passed, count);
    }
}
