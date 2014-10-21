package utils;

import utils.Utils;

import javax.servlet.http.HttpServletRequest;
import java.security.MessageDigest;
import java.util.Hashtable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by WojtawDesktop on 21.10.2014.
 */
public class Sessions<E> {
    // storage for the session data;
    private Hashtable<String, E> sessions = new Hashtable<String, E>();
    /** Returns session id based on the information in the http request **/
    public String getSessionID(HttpServletRequest request) throws Exception {
        String sid = null;
// extract the session id from the cookie
        if (request.getHeader("cookie") != null) {
            Pattern p = Pattern.compile(".*session-id=([a-zA-Z0-9]+).*");
            Matcher m = p.matcher(request.getHeader("cookie"));
            if (m.matches()) sid = m.group(1);
        }
// create the session id md5 hash; use random number to generate a client-id
// note that this is a simple solution but not very reliable
        if (sid == null || sessions.get(sid) == null) {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(new String(request.getRemoteAddr() +
                    Math.floor(Math.random()*1000)).getBytes());
            sid = Utils.toHexString(md.digest());
        }
        return sid;
    }

    // returns session data from sessions object
    public E getData(String sid){
        return sessions.get(sid);
    }

    // sets session data to sessions object
    public void setData(String sid, E d){
        sessions.put(sid, d);
    }
}

