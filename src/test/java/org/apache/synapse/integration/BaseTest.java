package org.apache.synapse.integration;

import org.apache.synapse.integration.clients.StockQuoteSampleClient;
import org.apache.synapse.integration.config.AutomationYamlFile;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class BaseTest {

    private static String automationFileLocation;
    private static AutomationYamlFile configurations;

    @BeforeClass
    public static void initParameters() {
        automationFileLocation = System.getProperty("framework.resource.location");

        //noinspection ObviousNullCheck System.getProperty can return null
        Assert.assertNotNull(automationFileLocation, "framework.resource.location property should be set");

        FileInputStream yamlInput = null;
        try {
            yamlInput = new FileInputStream(new File(
                    automationFileLocation + File.separator + "automation.yaml"));
        } catch (FileNotFoundException e) {
            Assert.fail(e.getMessage());
        }

        Yaml yaml = new Yaml();
        configurations = yaml.loadAs(yamlInput, AutomationYamlFile.class);
    }

    public AutomationYamlFile getConfig() {
        return configurations;
    }

    public String getSynapseAddress() {
        return getConfig().getSynapseServer().getHostAddress();
    }

    public String getBackendAddress() {
        return getConfig().getBackendServer().getHostAddress();
    }

    public StockQuoteSampleClient getStockQuoteClient() {
        return new StockQuoteSampleClient(configurations.getAxis2ClientConfig());
    }
}
