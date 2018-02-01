package main.java.com.gmail.berndivader.mythicmobsext.botai;

public interface Session {
    Thought think(Thought t) throws Exception;
    String think(String t) throws Exception;
}