package util;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

/**
 *
 * Created by mgh on 4/11/17.
 */
public final class Util {

    private Util(){}

    public static String getLocalIP(){
        try {
            Enumeration e = NetworkInterface.getNetworkInterfaces();

            String res = "";
            while(e.hasMoreElements()) {
                NetworkInterface networkInterface = (NetworkInterface) e.nextElement();
                Enumeration ee = networkInterface.getInetAddresses();

                while(ee.hasMoreElements()) {
                    InetAddress i = (InetAddress) ee.nextElement();
                    res += i.getHostAddress() + '\t';
                }
            }

            return res.equals("") ? null : res;
        }  catch (SocketException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String genDataKey(int partitionID, String key){return Integer.toString(partitionID) + '\t' + key;}

    public static String genMetaKey(int partitionID){
        return Integer.toString(partitionID);
    }

    public static String genMetaValue(String dbAddress, int numOfPartitions){
        return dbAddress + "\t" + Integer.toString(numOfPartitions);
    }

    public static int getLocalDBId(int partitionID, int numOfPartitions){
        return partitionID % numOfPartitions;
    }

    public static int getMetaDBId(int numOfPartitions){  // meta db id depends on number of partitions
        return numOfPartitions;
    }

    public static String[] parseMetaValue(String metaValue){
        return metaValue.split("\t");
    }


}
