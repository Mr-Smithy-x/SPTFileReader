import co.yoprice.spt.exceptions.SPTException;
import co.yoprice.spt.models.SPTPacket;
import co.yoprice.spt.utils.SPTParser;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
public class Main {

    public static void main(String[] args) {
        String dir = "/home/cj/Downloads/EvilToolz/Wpe Stuff/";
        File files = new File(dir);
        File[] fileArr = files.listFiles();
        ArrayList<SPTPacket> packetArrayList = new ArrayList<SPTPacket>();
        try {
            for(File f : fileArr) {
                if(f.isFile() && f.getName().endsWith(".spt")) {
                    System.out.println("FILE NAME: "+f.getName());
                    SPTParser sptParser = SPTParser.parse(f);
                    packetArrayList.addAll(sptParser.getPackets());
                }
            }
            System.out.println("SIZE: " + packetArrayList.size());
        }
        catch (SPTException | IOException e) {
            e.printStackTrace();
        }
    }
}
