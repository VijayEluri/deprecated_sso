package com.feedback20.users;

import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
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
        params = new HashMap<String, String>();
    }

    public SSO(URL server, String salt) {
        this();
        setServer(server);
        setSalt(salt);
    }

    public SSO(URL server, String salt, URL service) {
        this(server, salt);
        setService(service);
    }

    public String getURL() {
        setToken();
        return getBaseURL() + join(joinPairs(params, "=", PARAMS), "&");
    }

    public String getToken() {
        return sha1(join(joinPairs(params, "-", TOKENIZED_PARAMS), ":") + salt);
    }


    public String sha1(String text) {
        if (null == text) {
            throw new NullPointerException("Can't has encode text: null given");
        }
        try {
            final byte[] hash = MessageDigest.getInstance("SHA-1").digest(
                    text.getBytes());
            final StringBuffer buffer = new StringBuffer();
            for (final byte c : hash) {
                final String hex = Integer.toHexString(c);
                final int len = hex.length();
                if (0 == len) {
                    continue;
                }
                if (1 == len) {
                    buffer.append('0').append(hex.substring(len - 1));
                } else {
                    buffer.append(hex.substring(len - 2));
                }
            }
            return buffer.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
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

    public void setExpires(long timestamp) {
        params.put("expires", new Long(timestamp).toString());
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

    public void setServer(URL server) {
        this.server = server.toString();
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    // Dummy getters/setters
    public String getAvatarUrl() {
        return params.get("avatar_url");
    }

    public void setAvatarUrl(String avatarUrl) {
        params.put("avatar_url", avatarUrl);
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

    public void setService(URL service) {
        params.put("service", service.toString());
    }

    public String getUuid() {
        return params.get("uuid");
    }

    public void setUuid(String uuid) {
        params.put("uuid", uuid);
    }
}
