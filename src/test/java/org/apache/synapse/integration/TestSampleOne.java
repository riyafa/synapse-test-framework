package org.apache.synapse.integration;

import org.apache.synapse.integration.clients.SampleClientResult;
import org.junit.Assert;
import org.junit.Test;

public class TestSampleOne extends BaseTest {

    @Test
    public void testSmartClientMode() {

        String addUrl = "http://" + getBackendAddress() + "/services/SimpleStockQuoteService";
        String trpUrl = "http://" + getSynapseAddress() +"/";

        System.out.println("Running test: Smart Client mode");

        SampleClientResult result = getStockQuoteClient().requestStandardQuote(
                addUrl, trpUrl, null, "IBM" ,null);
        Assert.assertTrue("Client did not receive the expected response", result.responseReceived());
    }
}
