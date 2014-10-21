package utils;

/**
 * Created by WojtawDesktop on 21.10.2014.
 */
public class Output {
    public static void printLog(String message){
        System.out.println("LOG: "+message);
    }

    public static void printLog(Object message){
        System.out.println("LOG: "+message.toString());
    }
}
