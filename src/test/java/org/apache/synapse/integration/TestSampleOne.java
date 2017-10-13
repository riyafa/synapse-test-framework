package org.apache.synapse.integration;

import org.apache.synapse.integration.clients.SampleClientResult;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

public class TestSampleOne extends BaseTest {

    @Test
    @Ignore("Test is ignored as a demonstration")
    public void testSmartClientMode() {

        String addUrl = "http://" + getBackendAddress() + "/services/SimpleStockQuoteService";
        String trpUrl = "http://" + getSynapseAddress() +"/";

        System.out.println("Running test: Smart Client mode");

        SampleClientResult result = getStockQuoteClient().requestStandardQuote(
                addUrl, trpUrl, null, "IBM" ,null);

        Assert.assertTrue("Client did not receive the expected response", result.responseReceived());
    }

    @Test
    @Ignore("Test is ignored as a demonstration")
    public void testSynapseAsHTTPProxy() {
        String addUrl = "http://" + getBackendAddress() + "/services/SimpleStockQuoteService";
        String prxUrl = "http://" + getSynapseAddress() +"/";

        System.out.println("Running test: Using Synapse as a HTTP Proxy");
        SampleClientResult result = getStockQuoteClient().requestStandardQuote(
                addUrl, null, prxUrl, "IBM", null);

        Assert.assertTrue("Client did not receive the expected response", result.responseReceived());    }
}
