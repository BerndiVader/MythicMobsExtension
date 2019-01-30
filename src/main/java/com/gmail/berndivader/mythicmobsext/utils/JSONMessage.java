package com.gmail.berndivader.mythicmobsext.utils;

import com.gmail.berndivader.mythicmobsext.NMS.NMSUtils;

public class JSONMessage {
    private String json;

    public JSONMessage(String json) {
        this.json = json;
    }

    public String getJson() {
        return this.json;
    }

    public Object getBaseComponent() {
    	return NMSUtils.getJSONfromString(this.json);
//        return IChatBaseComponent.ChatSerializer.a((String)this.json);
    }
}

