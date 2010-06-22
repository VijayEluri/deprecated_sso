package com.feedback20.users;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 *
 */
public class SSO {

    private static String[] TOKENIZED_PARAMS = {"avatar_url", "email", "expires", "firstname", "lastname", "role", "uuid"};
    private static String[] PARAMS = {"avatar_url", "charset", "email", "expires", "firstname", "lastname", "role", "service", "token", "uuid"};
    private Map<String, String> params;
    private String server;
    private String salt;

    public SSO() {
        params = new Hashtable<String, String>();
    }

    public SSO(String server, String salt) {
        this.server = server;
        this.salt = salt;
    }

    public SSO(String server, String salt, String service) {
        this(server, salt);
        setService(service);
    }

    public String getURL() {
        setToken();
        return getBaseURL() + join(joinPairs(params, "=", PARAMS), "&");
    }

    public String getToken() {
        String tokenString = join(joinPairs(params, "-", TOKENIZED_PARAMS), ":") + salt;
        try {
            return MessageDigest.getInstance("SHA-1").digest(tokenString.getBytes()).toString();
        } catch (NoSuchAlgorithmException e) {
            return "failed";
        }
    }

    private void setToken() {
        params.put("token", getToken());
    }

    private String getBaseURL() {
        return server + "cas/login?auth=sso&type=acceptor&";
    }

    public void setExpiresIn(int in) {
        params.put("expires", new Long(in + getTimestamp()).toString());
    }

    protected long getTimestamp() {
        return Calendar.getInstance().getTime().getTime();
    }

    public static List<String> joinPairs(Map<String, String> hash, String separator, String[] keys) {
        List<String> pairs = new ArrayList<String>();
        for (String key : keys) {
            if (hash.containsKey(key)) {
                pairs.add(key + separator + hash.get(key));
            }
        }
        return pairs;
    }

    public static String join(Collection s, String delimiter) {
        StringBuffer buffer = new StringBuffer();
        Iterator iter = s.iterator();
        while (iter.hasNext()) {
            buffer.append(iter.next());
            if (iter.hasNext()) {
                buffer.append(delimiter);
            }
        }
        return buffer.toString();
    }
    
    // Dummy getters/setters
    public String getAvatarUrl() {
        return params.get("avatar_url");
    }

    public void setAvatarUrl(String avatarUrl) {
        params.put("avatar_urml", avatarUrl);
    }

    public String getCharset() {
        return params.get("charset");
    }

    public void setCharset(String charset) {
        params.put("charset", charset);
    }

    public String getEmail() {
        return params.get("email");
    }

    public void setEmail(String email) {
        params.put("email", email);
    }

    public String getFirstName() {
        return params.get("firstname");
    }

    public void setFirstName(String firstName) {
        params.put("firstname", firstName);
    }

    public String getLastName() {
        return params.get("lastname");
    }

    public void setLastName(String lastName) {
        params.put("lastname", lastName);
    }

    public String getRole() {
        return params.get("role");
    }

    public void setRole(String role) {
        params.put("role", role);
    }

    public String getService() {
        return params.get("service");
    }

    public void setService(String service) {
        params.put("service", service);
    }

    public String getUuid() {
        return params.get("uuid");
    }

    public void setUuid(String uuid) {
        params.put("uuid", uuid);
    }
}
