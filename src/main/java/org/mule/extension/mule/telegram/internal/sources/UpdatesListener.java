package org.mule.extension.mule.telegram.internal.sources;

import static org.mule.runtime.extension.api.annotation.param.MediaType.*;
import static org.mule.runtime.api.metadata.MediaType.APPLICATION_JAVA;
import static org.mule.runtime.api.meta.ExpressionSupport.SUPPORTED;


import java.io.*;

import org.json.JSONTokener;
import org.mule.extension.mule.telegram.api.model.UpdateMessage;
import org.mule.extension.mule.telegram.api.util.Mapper;
import org.mule.runtime.api.util.MultiMap;
import org.mule.runtime.extension.api.annotation.Expression;
import org.mule.runtime.extension.api.annotation.Alias;
import org.mule.runtime.extension.api.annotation.dsl.xml.ParameterDsl;
import org.mule.runtime.extension.api.annotation.execution.OnError;
import org.mule.runtime.extension.api.annotation.execution.OnSuccess;
import org.mule.runtime.extension.api.annotation.param.Config;
import org.mule.runtime.extension.api.annotation.param.Connection;
import org.mule.runtime.extension.api.annotation.param.MediaType;
import org.mule.runtime.extension.api.annotation.param.Optional;
import org.mule.runtime.extension.api.annotation.source.EmitsResponse;
import org.mule.runtime.extension.api.runtime.source.Source;
import org.mule.runtime.extension.api.runtime.source.PollContext;
import org.mule.runtime.extension.api.runtime.source.PollingSource;
import org.mule.runtime.extension.api.runtime.source.SourceCallbackContext;
import org.mule.runtime.api.connection.ConnectionProvider;
import org.mule.runtime.api.exception.MuleException;
import org.mule.runtime.api.connection.ConnectionException;
import org.mule.runtime.extension.api.runtime.operation.Result;
import org.mule.runtime.extension.api.annotation.param.Parameter;
import org.mule.extension.mule.telegram.internal.*;
import java.util.*;

import org.apache.commons.io.IOUtils;

import java.io.BufferedReader;

import org.json.JSONArray;
import org.json.JSONObject;
import java.io.IOException;
import java.util.concurrent.TimeoutException;
import java.util.stream.Stream;

import org.mule.runtime.http.api.domain.message.request.HttpRequest;
import org.mule.runtime.http.api.domain.message.response.HttpResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@Alias("Get-Updates")
@EmitsResponse
@MediaType(value = ANY, strict = false)
public class UpdatesListener extends PollingSource<String, Void> {

    private static final Logger LOGGER = LoggerFactory.getLogger(UpdatesListener.class);
    private static final String ID_FIELD = "ID";
    List<String> lines;
    private String lastUpdateId;

    private Mapper mapper = Mapper.getInstance();

    @Parameter
    @Optional
    protected String chatId;

    @Parameter
    @Optional(defaultValue = "false")
    protected boolean watermark;

    @Connection
    private ConnectionProvider<TelegramConnection> connectionProvider;
    protected TelegramConnection telegramConnection;

    @Override
    protected void doStart() throws MuleException {}

    @Override
    protected void doStop() {
        connectionProvider.disconnect(telegramConnection);
    }

    @Override
    public void poll(PollContext<String, Void> pollContext) {
        StringBuilder responseStrBuilder = new StringBuilder();
        if(pollContext.isSourceStopping()){
            return;
        }

        try {
            telegramConnection = connectionProvider.connect();
        } catch (ConnectionException e){
            e.printStackTrace();
        }

        List<UpdateMessage> content = getUpdates(telegramConnection, watermark, lastUpdateId);
        if(watermark) {
            /* logic to get lasUpdateId */
//            try {
//                BufferedReader bR = new BufferedReader(new InputStreamReader(content));
//
//                String line = "";
//                while ((line = bR.readLine()) != null) {
//                    responseStrBuilder.append(line);
//                }
//                JSONObject jsonObject = new JSONObject(responseStrBuilder.toString());
//                LOGGER.trace("payload: " + jsonObject.toString(3));
//                JSONArray arrayResults = jsonObject.getJSONArray("result");
//                if (arrayResults.length() > 0) {
//                    JSONObject lastObj = arrayResults.getJSONObject(arrayResults.length() - 1);
//                    lastUpdateId = String.valueOf(lastObj.getInt("update_id")+1);
//                }
//                LOGGER.debug("Watermark is using lastUpdateId: " + lastUpdateId);
//            } catch (IOException e) {
//                e.printStackTrace();
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//        /* End logic to get lasUpdateId */
            lastUpdateId = content.get(content.size() - 1).getUpdateId().toString();
        }

        ObjectOutputStream out = null;
        try {
            Stream<UpdateMessage> stream = content.stream();

            pollContext.accept(item -> {
                Result<String, Void> result = Result.<String, Void>builder()
                        .output(Arrays.toString(stream.toArray()))
                        .mediaType(APPLICATION_JAVA)
                        .build();
                item.setResult(result);
                item.setId(ID_FIELD.toString());
            });
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            try {
                if (out != null) out.close();
            } catch (Exception e) {

            }
        }
    }

    @Override
    public void onRejectedItem(Result<String, Void> result, SourceCallbackContext callbackContext) {
        // ...
    }

    private List<UpdateMessage> getUpdates(TelegramConnection connection, boolean watermark, String lastUpdateId){
      HttpResponse httpResponse = null;
      String strUri = connection.getUrl() + "/getUpdates";

      MultiMap<String, String> qParams = new MultiMap<String, String>();

      if(watermark && lastUpdateId != null) {
          qParams.put("offset", lastUpdateId);
      }

      HttpRequest request = HttpRequest.builder()
              .method("GET")
              .uri(strUri)
              .build();
      try {
        httpResponse = connection.getHttpClient().send(request, connection.getTimeout(), false, null);

        JSONTokener tokener = new JSONTokener(httpResponse.getEntity().getContent());
        JSONObject response = new JSONObject(tokener);

        return mapper.getAllUpdateMessages(response);

      } catch (IOException e) {
        e.printStackTrace();
      } catch (TimeoutException e) {
        e.printStackTrace();
      } catch (Exception e) {
        e.printStackTrace();
      }
      return null;
  }


}