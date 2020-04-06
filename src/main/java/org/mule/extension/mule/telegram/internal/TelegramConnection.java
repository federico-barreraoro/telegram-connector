package org.mule.extension.mule.telegram.internal;


import org.mule.extension.mule.telegram.internal.configurations.TelegramConfiguration;
import org.mule.runtime.http.api.HttpService;
import org.mule.runtime.http.api.client.HttpClient;
import org.mule.runtime.http.api.client.HttpClientConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class represents an extension connection just as example (there is no real connection with anything here c:).
 */
public final class TelegramConnection {

   private static final Logger LOGGER = LoggerFactory.getLogger(TelegramConnection.class);
   private String url;
   private Integer timeout;
   private HttpClient httpClient;

  public TelegramConnection(HttpService httpService, TelegramConfiguration config) {
      this.url = config.getHost()+":"+config.getPort()+"/bot"+config.getToken();
      this.timeout = config.getTimeout();
      initHttpClient(httpService);
  }

  public void initHttpClient(HttpService httpService){
    HttpClientConfiguration.Builder builder = new HttpClientConfiguration.Builder();
    builder.setName("telegram");
    httpClient = httpService.getClientFactory().create(builder.build());
    httpClient.start();
  }

  public void invalidate() {
    httpClient.stop();
  }

  public boolean isConnected() throws Exception{
    return true;
  }

  public String getUrl() {
      return this.url;
  }

  public HttpClient getHttpClient() {
      return this.httpClient;
  }

    public Integer getTimeout() {
        return timeout;
    }

    //  public InputStream getUpdates(String chatId, boolean watermark, String lastUpdateId){
//      HttpResponse httpResponse = null;
//      String strUri = genConfig.getHost()+":"+genConfig.getPort()+"/bot"+genConfig.getToken()+"/getUpdates";
//
//      MultiMap<String, String> qParams = new MultiMap<String, String>();
//      qParams.put("chat_id", chatId);
//      if(watermark && lastUpdateId != null) {
//          qParams.put("offset", lastUpdateId);
//      }
//
//      HttpRequest request = HttpRequest.builder()
//              .method("GET")
//              .uri(strUri)
//              .queryParams(qParams)
//              .build();
//      try {
//        httpResponse = httpClient.send(request,genConfig.getTimeout(),false,null);
//        return httpResponse.getEntity().getContent();
//      } catch (IOException e) {
//        e.printStackTrace();
//      } catch (TimeoutException e) {
//        e.printStackTrace();
//      } catch (Exception e) {
//        e.printStackTrace();
//      }
//      return null;
//  }


}
