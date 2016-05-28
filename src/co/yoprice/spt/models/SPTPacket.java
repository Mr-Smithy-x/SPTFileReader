package co.yoprice.spt.models;

/**
 * Created by cj on 4/25/16.
 */
public class SPTPacket {
    private String title;
    private String content;

    private SPTPacket(){}

    public static SPTPacket Builder(){
        return new SPTPacket();
    }

    public String getTitle() {
        return title;
    }

    public SPTPacket setTitle(String title) {
        this.title = title;
        return this;
    }

    public String getContent() {
        return content;
    }

    public SPTPacket setContent(String content) {
        this.content = content;
        return this;
    }
}
