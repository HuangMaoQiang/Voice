package com.careyun.voiceassistant.eventbus;

/**
 * Created by Huangmq on 2017/4/21.
 */

public class RecogEvent {

    private int massage;
    private String match;


    public int getMassage(){
        return massage;
    }

    public void setMassage(int massage){
        this.massage = massage;
    }

    public String getMatch(){
        return match;
    }

    public void setMatch(String match){
        this.match = match;
    }


}
