package org.mule.extension.mule.telegram.api.util;

import org.json.JSONArray;
import org.json.JSONObject;
import org.mule.extension.mule.telegram.api.model.*;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;


public  class Mapper {

    private static Mapper instance;

    public static Mapper getInstance() {
        if(instance == null) {
            instance = new Mapper();
        }
        return instance;
    }

    public SendResponse getSendResponse(JSONObject json) {

        JSONObject result = json.getJSONObject("result");

        From from = getFrom(result.getJSONObject("from"));
        Chat chat = getChat(result.getJSONObject("chat"));

        List<Entity> entities = result.has("entities")? getEntities(result.getJSONArray("entities")) : null;
        SendResponse response = new SendResponse(
                result.getLong("message_id"),
                from,
                chat,
                new Date(result.getLong("date")),
                result.getString("text"),
                entities
        );

        return response;

    }


    public  From getFrom(JSONObject json) {
        From from = new From(
                json.getLong("id"),
                json.getBoolean("is_bot"),
                json.getString("first_name"),
                json.getString("username")
        );

        return from;
    }

    public Chat getChat(JSONObject json) {
        Chat chat = new Chat(
                json.getLong("id"),
                json.getString("first_name"),
                json.getString("last_name"),
                json.getString("username"),
                json.getString("type")
        );

        return chat;
    }

    public List<Entity> getEntities(JSONArray jsonArray) {
        if(jsonArray == null) {
            return null;
        }

        List<Entity> entities = new LinkedList<Entity>();
        for(int i=0; i<jsonArray.length(); i++) {
            JSONObject object = jsonArray.getJSONObject(i);
            Entity entity = new Entity(
                    object.getInt("offset"),
                    object.getInt("length"),
                    object.getString("type")
            );

            entities.add(entity);
        }

        return entities;
    }

    public UpdateMessage getUpdateMessage(JSONObject jsonObject) {

        From from = getFrom(jsonObject.getJSONObject("from"));
        Chat chat = getChat(jsonObject.getJSONObject("chat"));

        List<Entity> entities = jsonObject.has("entities")? getEntities(jsonObject.getJSONArray("entities")) : null;
        UpdateMessage response = new UpdateMessage(
                jsonObject.getLong("update_id"),
                from,
                chat,
                new Date(jsonObject.getLong("date")),
                jsonObject.getString("text"),
                entities
        );

        return response;
    }

    public List<UpdateMessage> getAllUpdateMessages(JSONObject jsonObject) {

        JSONArray jsonResults = jsonObject.getJSONArray("result");
        List<UpdateMessage> updates = new LinkedList<UpdateMessage>();

        for(int i=0; i<jsonResults.length(); i++) {
            updates.add(getUpdateMessage(jsonResults.getJSONObject(i)));
        }

        return updates;

    }


}
