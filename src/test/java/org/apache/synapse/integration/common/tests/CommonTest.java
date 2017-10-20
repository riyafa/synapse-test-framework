package org.apache.synapse.integration.common.tests;

import io.netty.handler.codec.http.HttpMethod;
import org.apache.synapse.integration.BaseTest;
import org.apache.synapse.integration.utils.TestUtils;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.wso2.carbon.protocol.emulator.dsl.Emulator;
import org.wso2.carbon.protocol.emulator.http.client.contexts.HttpClientConfigBuilderContext;
import org.wso2.carbon.protocol.emulator.http.client.contexts.HttpClientRequestBuilderContext;
import org.wso2.carbon.protocol.emulator.http.client.contexts.HttpClientResponseBuilderContext;
import org.wso2.carbon.protocol.emulator.http.client.contexts.HttpClientResponseProcessorContext;

import java.io.File;
import java.io.IOException;

public class CommonTest extends BaseTest {

    private File kb100 = new File("100KB.txt");
    private File mb1 = new File("1MB.txt");

    @Test
    public void testClientServerLargePaload() throws IOException {
        HttpClientResponseProcessorContext response = Emulator.getHttpEmulator()
                .client()
                .given(
                        HttpClientConfigBuilderContext.configure()
                                .host(getConfig().getSynapseServer().getHostname())
                                .port(Integer.parseInt(getConfig().getSynapseServer().getPort()))
                )
                .when(
                        HttpClientRequestBuilderContext.request().withPath("/services/large_payload")
                                .withMethod(HttpMethod.POST).withBody(mb1)
                )
                .then(
                        HttpClientResponseBuilderContext.response().assertionIgnore()
                )
                .operation()
                .send();
        Assert.assertEquals(response.getReceivedResponseContext().getResponseBody(),
                            TestUtils.getFileBody(mb1));
    }

    @Test
    public void testSlowReadingClientLargePayloadServer() throws IOException {
        HttpClientResponseProcessorContext response = Emulator.getHttpEmulator()
                .client()
                .given(
                        HttpClientConfigBuilderContext.configure().host(getConfig().getSynapseServer().getHostname())
                                .port(Integer.parseInt(getConfig().getSynapseServer().getPort())).withReadingDelay(3000)
                )
                .when(
                        HttpClientRequestBuilderContext.request().withPath("/services/large_payload")
                                .withMethod(HttpMethod.POST).withBody(kb100)
                )
                .then(
                        HttpClientResponseBuilderContext.response().assertionIgnore()
                )
                .operation()
                .send();
        Assert.assertEquals(response.getReceivedResponseContext().getResponseBody(),
                            TestUtils.getFileBody(mb1));
    }

    @Test
    public void testClientLargePayloadSlowReadingBackend() {
        HttpClientResponseProcessorContext response = Emulator.getHttpEmulator()
                .client()
                .given(
                        HttpClientConfigBuilderContext.configure()
                                .host(getConfig().getSynapseServer().getHostname())
                                .port(Integer.parseInt(getConfig().getSynapseServer().getPort()))
                )
                .when(
                        HttpClientRequestBuilderContext.request().withPath("/services/reading_delay")
                                .withMethod(HttpMethod.POST).withBody(mb1)
                )
                .then(
                        HttpClientResponseBuilderContext.response().assertionIgnore()
                )
                .operation()
                .send();
        Assert.assertNull(response);
    }

    @Test
    public void testLargePayloadSlowWringBackend() throws IOException {
        HttpClientResponseProcessorContext response = Emulator.getHttpEmulator()
                .client()
                .given(
                        HttpClientConfigBuilderContext.configure()
                                .host(getConfig().getSynapseServer().getHostname())
                                .port(Integer.parseInt(getConfig().getSynapseServer().getPort()))
                )
                .when(
                        HttpClientRequestBuilderContext.request().withPath("/services/slow_large")
                                .withMethod(HttpMethod.POST).withBody(kb100)
                )
                .then(
                        HttpClientResponseBuilderContext.response().assertionIgnore()
                )
                .operation()
                .send();
        Assert.assertEquals(TestUtils.getFileBody(mb1), response.getReceivedResponseContext().getResponseBody());
    }

    @Test @Ignore("Result depends on the previous call to the backend")
    public void testWriteSlowClientReadSlowServer() {
        HttpClientResponseProcessorContext response = Emulator.getHttpEmulator()
                .client()
                .given(
                        HttpClientConfigBuilderContext.configure()
                                .host(getConfig().getSynapseServer().getHostname())
                                .port(Integer.parseInt(getConfig().getSynapseServer().getPort())).withWritingDelay(3000)
                )
                .when(
                        HttpClientRequestBuilderContext.request().withPath("/services/reading_delay")
                                .withMethod(HttpMethod.POST).withBody(new File("100KB.txt"))
                )
                .then(
                        HttpClientResponseBuilderContext.response().assertionIgnore()
                )
                .operation()
                .send();
        Assert.assertNull(response);
    }

    @Test
    public void testWriteSlowClientServer() {
        HttpClientResponseProcessorContext response = Emulator.getHttpEmulator()
                .client()
                .given(
                        HttpClientConfigBuilderContext.configure()
                                .host(getConfig().getSynapseServer().getHostname())
                                .port(Integer.parseInt(getConfig().getSynapseServer().getPort())).withWritingDelay(3000)
                )
                .when(
                        HttpClientRequestBuilderContext.request().withPath("/services/writing_delay")
                                .withMethod(HttpMethod.POST).withBody(new File("100KB.txt"))
                )
                .then(
                        HttpClientResponseBuilderContext.response().assertionIgnore()
                )
                .operation()
                .send();
        Assert.assertEquals("Slowly writing backend", response.getReceivedResponseContext().getResponseBody());
    }
}
