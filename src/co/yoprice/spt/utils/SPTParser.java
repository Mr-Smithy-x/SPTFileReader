package co.yoprice.spt.utils;

import co.yoprice.spt.exceptions.SPTException;
import co.yoprice.spt.models.SPTPacket;

import java.io.*;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created by cj on 4/25/16.
 */
public class SPTParser {
    private InputStream inputStream;
    private byte[] content;
    private SPTParser(){}

    private SPTParser(InputStream inputStream) throws IOException {
        this.inputStream = inputStream;
        setContent(inputStream);
    }

    private void setContent(InputStream is) throws IOException {
        byte b;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        while ((b = (byte) is.read()) != -1){
            baos.write(b);
        }
        baos.flush();
        is.close();
        content = baos.toByteArray();
        baos.close();
    }

    public ArrayList<SPTPacket> getPackets() throws SPTException{
        ArrayList<SPTPacket> sptPacket = null;
        try {
            sptPacket = _parse(content);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(sptPacket != null) return sptPacket;
        else throw  new SPTException("This file is not a packet", SPTException.SPTERROR.PARSE_FAILED);
    }

    public static void reverse(byte[] array) {
        if (array == null) {
            return;
        }
        int i = 0;
        int j = array.length - 1;
        byte tmp;
        while (j > i) {
            tmp = array[j];
            array[j] = array[i];
            array[i] = tmp;
            j--;
            i++;
        }
    }
    private ArrayList<SPTPacket> _parse(byte[] content) throws IOException, SPTException {
        try {
            ArrayList<SPTPacket> packetArrayList = new ArrayList<>();
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(content);
            byte[] pktSize = new byte[4];
            byteArrayInputStream.read(pktSize, 0, pktSize.length);
            reverse(pktSize);
            int packet_size = ByteBuffer.wrap(pktSize).getInt();
            for (int i = 0; i < packet_size; i++) {
                byte[] titleSize = new byte[4];
                byteArrayInputStream.read(titleSize, 0, titleSize.length);
                reverse(titleSize);
                int title_size = ByteBuffer.wrap(titleSize).getInt();
                byte[] title = new byte[title_size]; //Convert To String
                byteArrayInputStream.read(title, 0, title.length);
                byte[] contentSize = new byte[4];
                byteArrayInputStream.read(contentSize, 0, contentSize.length);
                reverse(contentSize);
                int content_size = ByteBuffer.wrap(contentSize).getInt();
                byte[] _content = new byte[content_size];
                byteArrayInputStream.read(_content, 0, _content.length);
                StringBuffer title_str = new StringBuffer(), content_str = new StringBuffer();
                for (byte b : _content) content_str.append((char) b);
                for (byte b : title) title_str.append((char) b);
                System.out.println("Packet Info: " + title_str + ": " + content_str);
                packetArrayList.add(SPTPacket.Builder().setTitle(title_str.toString()).setContent(content_str.toString()));
            }
            byteArrayInputStream.close();
            byteArrayInputStream = null;
            if (packetArrayList.size() == 0) return null;
            return packetArrayList;
        }catch (Throwable t){
            throw new SPTException("This pkt is corrupt.", SPTException.SPTERROR.CORRUPT_PKT);
        }
    }

    //region Static methods
    public static SPTParser parse(String file) throws IOException {
        return parse(new File(file));
    }

    public static SPTParser parse(File file) throws IOException {
        return parse(new FileInputStream(file));
    }

    public static SPTParser parse(InputStream inputStream) throws IOException {
        return new SPTParser(inputStream);
    }


    //endregion
}
