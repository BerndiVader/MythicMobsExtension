package com.gmail.berndivader.mythicmobsext.botai;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

public 
class
Bot
{
    final String bid;

    public Bot(String s1) {
        this.bid=s1;
    }

    public Session createSession() {
        return new S();
    }

    private class S implements Session {
        final Map<String,String>vars1;

        public S() {
            vars1=new LinkedHashMap<String, String>();
            vars1.put("botid",bid);
            vars1.put("custid",UUID.randomUUID().toString());
        }

        public Thought think(Thought thought) throws Exception {
            vars1.put("input",thought.getText());
            String s1=Utils.request("https://www.pandorabots.com/pandora/talk-xml",null,null,vars1);
            Thought t1=new Thought();
            t1.setText(Utils.xPathSearch(s1,"//result/that/text()"));
            return t1;
        }

        public String think(String text) throws Exception {
        	Thought t1=new Thought();
            t1.setText(text);
            return think(t1).getText();
        }
    }
}