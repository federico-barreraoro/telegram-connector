package org.mule.extension.mule.telegram.api.model;

import java.util.Date;
import java.util.List;

public class SendResponse {

    private Long messageId;
    private From from;
    private Chat chat;
    private Date date;
    private String text;
    private List<Entity> entities;

    public SendResponse(Long messageId, From from, Chat chat, Date date, String text, List<Entity> entities) {
        this.messageId = messageId;
        this.from = from;
        this.chat = chat;
        this.date = date;
        this.text = text;
        this.entities = entities;
    }

    public Long getMessageId() {
        return messageId;
    }

    public void setMessageId(Long messageId) {
        this.messageId = messageId;
    }

    public From getFrom() {
        return from;
    }

    public void setFrom(From from) {
        this.from = from;
    }

    public Chat getChat() {
        return chat;
    }

    public void setChat(Chat chat) {
        this.chat = chat;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public List<Entity> getEntities() {
        return entities;
    }

    public void setEntities(List<Entity> entities) {
        this.entities = entities;
    }
}





