package org.mule.extension.mule.telegram.internal;

import static org.mule.runtime.extension.api.annotation.param.MediaType.ANY;

import jdk.nashorn.internal.parser.JSONParser;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.mule.extension.mule.telegram.api.errors.BadRequestModuleException;
import org.mule.extension.mule.telegram.api.errors.ExecuteErrorProvider;
import org.mule.extension.mule.telegram.api.errors.NotFoundModuleException;
import org.mule.extension.mule.telegram.api.errors.UnknownErrorModuleException;
import org.mule.extension.mule.telegram.api.model.types.ParseMode;
import org.mule.extension.mule.telegram.api.model.types.ReplyMarkup;
import org.mule.extension.mule.telegram.api.model.SendResponse;
import org.mule.extension.mule.telegram.api.util.Mapper;
import org.mule.runtime.api.util.MultiMap;
import org.mule.runtime.extension.api.annotation.error.Throws;
import org.mule.runtime.extension.api.annotation.param.Connection;
import org.mule.runtime.extension.api.annotation.param.Optional;
import org.mule.runtime.http.api.domain.entity.ByteArrayHttpEntity;
import org.mule.runtime.http.api.domain.message.request.HttpRequest;
import org.mule.runtime.http.api.domain.message.response.HttpResponse;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * This class is a container for operations, every public method in this class will be taken as an extension operation.
 */
public class TelegramOperations {

  Mapper mapper = Mapper.getInstance();

  @Throws({ExecuteErrorProvider.class})
  public SendResponse sendMessage(@Connection TelegramConnection connection, String chatId, String text, @Optional ParseMode parseMode, @Optional(defaultValue = "false") boolean disableLinksPreview, @Optional(defaultValue = "false") boolean disableNotification, @Optional  Integer replyToMessageId, @Optional ReplyMarkup replyMarkup){
    HttpResponse httpResponse = null;

    String strUri = connection.getUrl()+"/sendMessage";

    MultiMap<String, String> headers = new MultiMap<String, String>();
    headers.put("Content-Type", "application/json");

    JSONObject payload = new JSONObject();
    payload.put("chat_id", chatId);
    payload.put("text", text);
    if(parseMode != null) payload.put("parse_mode", parseMode);
    payload.put("disable_web_page_preview", disableLinksPreview);
    payload.put("disable_notification", disableNotification);
    if (replyToMessageId != null) payload.put("reply_to_message_id", replyToMessageId);
    if (replyMarkup != null) payload.put("reply_markup", replyMarkup);

    HttpRequest request = HttpRequest.builder()
            .method("POST")
            .uri(strUri)
            .entity(new ByteArrayHttpEntity(payload.toString().getBytes()))
            .headers(headers)
            .build();

    try {
      httpResponse = connection.getHttpClient().send(request, connection.getTimeout(), false, null);

      JSONObject response = pareseResponse(httpResponse);
      return mapper.getSendResponse(response);

    } catch (IOException e) {
      e.printStackTrace();
      throw new UnknownErrorModuleException(e.getCause());
    } catch (TimeoutException e) {
      e.printStackTrace();
      throw new UnknownErrorModuleException(e.getCause());
    } catch (Exception e) {
      e.printStackTrace();
      throw new UnknownErrorModuleException(e.getCause());
    }
  }

  @Throws({ExecuteErrorProvider.class})
  private JSONObject pareseResponse(HttpResponse httpResponse) {

    JSONTokener tokener = new JSONTokener(httpResponse.getEntity().getContent());
    JSONObject response = new JSONObject(tokener);

    if(httpResponse.getStatusCode() == 400) {
      throw new BadRequestModuleException(response.getString("description"));
    }

    if(httpResponse.getStatusCode() == 404) {
      throw new NotFoundModuleException(response.getString("description"));
    }

    return response;
  }

}
