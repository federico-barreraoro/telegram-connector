package org.mule.extension.mule.telegram;

import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Test;
import org.mule.extension.mule.telegram.api.errors.BadRequestModuleException;
import org.mule.functional.junit4.MuleArtifactFunctionalTestCase;
import org.mule.runtime.api.event.Event;
import org.mule.runtime.api.exception.MuleException;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class TelegramOperationsTestCase extends MuleArtifactFunctionalTestCase {

    @Override
    protected String getConfigFile() {
        return "test-mule-config.xml";
    }

    @Test
    public void executeGetResources() throws Exception {
        Event event = flowRunner("sendMessage").run();
        System.out.println(event.getMessage().getPayload().getValue());

    }

    @Test
    public void executeNotFoundGetResources() throws Exception {
        try {
            Event event = flowRunner("sendNotFoundMessage").run();
        } catch(MuleException e) {
            e.printStackTrace();
        }

    }

    @Test
    public void executeGetUpdate() throws Exception {

        Event event = flowRunner("getUpdates").run();
        System.out.println(event.getMessage().getPayload().getValue());

    }

}
