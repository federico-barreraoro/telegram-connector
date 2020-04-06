package org.mule.extension.mule.telegram.api.model;

public class From {

    private Long id;
    private boolean bot;
    private String firstName;
    private String username;

    public From(Long id, boolean bot, String firstName, String username) {
        this.id = id;
        this.bot = bot;
        this.firstName = firstName;
        this.username = username;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public boolean isBot() {
        return bot;
    }

    public void setBot(boolean bot) {
        this.bot = bot;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}