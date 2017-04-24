package com.example.jimmivila.ClickersApp.Objects;

/**
 * Created by jimmivila on 4/24/17.
 */
public class Clicker {
    private String idClicker;
    private String idChannel;
    private String channelName;
    private String eventName;
    private String idRule;
    private String ruleName;

    public Clicker(String idClicker, String idChannel, String channelName, String eventName, String idRule, String ruleName){
        this.idClicker = idClicker;
        this.idChannel = idChannel;
        this.channelName = channelName;
        this.eventName = eventName;
        this.idRule = idRule;
        this.ruleName = ruleName;
    }

    public String getIdClicker() {
        return idClicker;
    }

    public String getIdChannel(){
        return idChannel;
    }

    public String getChannelName() {
        return channelName;
    }

    public String getEventName() {
        return eventName;
    }

    public String getIdRule() {
        return idRule;
    }

    public String getRuleName() {
        return ruleName;
    }
}
