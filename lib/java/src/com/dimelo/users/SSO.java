package com.dimelo.users;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This class generate SSO link for the Dimelo platform.
 * 
 * @version 0.1
 */
public class SSO {

    private static String[] _PARAMS = {"avatar_url", "charset", "custom_field_1", "custom_field_10", "custom_field_2", "custom_field_3", "custom_field_4", "custom_field_5", "custom_field_6", "custom_field_7", "custom_field_8", "custom_field_9", "email",
        "expires", "firstname", "lastname", "role", "service", "token",
        "uuid"};
    private static String[] _TOKENIZED_PARAMS = {"avatar_url", "custom_field_1", "custom_field_10", "custom_field_2", "custom_field_3", "custom_field_4", "custom_field_5", "custom_field_6", "custom_field_7", "custom_field_8", "custom_field_9", "email",
        "expires", "firstname", "lastname", "role", "uuid"};

    private static String join(final Collection list, final String delimiter) {
        StringBuffer buffer = new StringBuffer();
        Iterator iter = list.iterator();
        while (iter.hasNext()) {
            buffer.append(iter.next());
            if (iter.hasNext()) {
                buffer.append(delimiter);
            }
        }
        return buffer.toString();
    }

    private static List<String> joinPairs(Map<String, String> hash,
            String separator, String... keys) {
        List<String> pairs = new ArrayList<String>();
        for (String key : keys) {
            if (hash.containsKey(key)) {
                pairs.add(key + separator + hash.get(key));
            }
        }
        return pairs;
    }
    private final Map<String, String> _params;
    private String _salt;
    private String _server;

    public SSO() {
        _params = new HashMap<String, String>();
    }

    /**
     * @param server URL of your authentication server generally something like
     *            <b>https</b>://your-app-name.users.feedback20.com/
     * @param salt The salt available in your administration panel (see SSO doc
     *            page 6)
     */
    public SSO(final URL server, final String salt) {
        this();
        setServer(server);
        setSalt(salt);
    }

    /**
     * @param server URL of your authentication server generally something like
     *            <b>https</b>://your-app-name.users.feedback20.com/
     * @param salt The salt available in your administration panel (see SSO doc
     *            page 6)
     * @param service URL of the application where the user will be redirected
     *            after the authentication. Generally something like
     *            http://your-app-name.ideas.feedback20.com/
     */
    public SSO(final URL server, final String salt, final URL service) {
        this(server, salt);
        setService(service);
    }

    private String getBaseURL() {
        return _server + "cas/login?auth=sso&type=acceptor&";
    }

    private Map<String, String> getURLEncodedParams() {
        final Map<String, String> urlEncodedParams = new HashMap<String, String>();
        String param;
        for (String key : _PARAMS) {
            if (_params.containsKey(key)) {
                param = _params.get(key);
                try {
                    param = URLEncoder.encode(param, "UTF-8");
                } catch (UnsupportedEncodingException e) {
                }
                urlEncodedParams.put(key, param);
            }
        }
        return urlEncodedParams;
    }

    private void setToken() {
        _params.put("token", getToken());
    }

    public String sha1(String text) {
        if (null == text) {
            throw new NullPointerException("Can't has encode text: null given");
        }
        try {
            MessageDigest sha1 = MessageDigest.getInstance("SHA-1");
            sha1.reset();
            final byte[] hash = sha1.digest(text.getBytes("utf-8"));
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
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(SSO.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    public String getAvatarUrl() {
        return _params.get("avatar_url");
    }

    public String getCharset() {
        return _params.get("charset");
    }

    public String getCustomField1() {
        return _params.get("custom_field_1");
    }

    public String getCustomField2() {
        return _params.get("custom_field_2");
    }

    public String getCustomField3() {
        return _params.get("custom_field_3");
    }

    public String getCustomField4() {
        return _params.get("custom_field_4");
    }

    public String getCustomField5() {
        return _params.get("custom_field_5");
    }

    public String getCustomField6() {
        return _params.get("custom_field_6");
    }

    public String getCustomField7() {
        return _params.get("custom_field_7");
    }

    public String getCustomField8() {
        return _params.get("custom_field_8");
    }

    public String getCustomField9() {
        return _params.get("custom_field_9");
    }

    public String getCustomField10() {
        return _params.get("custom_field_10");
    }

    public String getEmail() {
        return _params.get("email");
    }

    public String getExpires() {
        return _params.get("expires");
    }

    public String getFirstName() {
        return _params.get("firstname");
    }

    public String getLastName() {
        return _params.get("lastname");
    }

    public String getRole() {
        return _params.get("role");
    }

    public String getService() {
        return _params.get("service");
    }

    public String getToken() {
        return sha1(getTokenString());
    }

    public String getTokenString() {
        return join(joinPairs(_params, "-", _TOKENIZED_PARAMS), ":") + _salt;
    }

    public URL getURL() {
        setToken();
        try {
            return new URL(getBaseURL()
                    + join(joinPairs(getURLEncodedParams(), "=", _PARAMS), "&"));
        } catch (MalformedURLException e) {
            return null;
        }
    }

    public String getUuid() {
        return _params.get("uuid");
    }

    public void setAvatarUrl(String avatarUrl) {
        _params.put("avatar_url", avatarUrl);
    }

    public void setCharset(String charset) {
        _params.put("charset", charset);
    }

    public void setCustomField1(String customField1) {
        _params.put("custom_field_1", customField1);
    }

    public void setCustomField2(String customField2) {
        _params.put("custom_field_2", customField2);
    }

    public void setCustomField3(String customField3) {
        _params.put("custom_field_3", customField3);
    }

    public void setCustomField4(String customField4) {
        _params.put("custom_field_4", customField4);
    }

    public void setCustomField5(String customField5) {
        _params.put("custom_field_5", customField5);
    }

    public void setCustomField6(String customField6) {
        _params.put("custom_field_6", customField6);
    }

    public void setCustomField7(String customField7) {
        _params.put("custom_field_7", customField7);
    }

    public void setCustomField8(String customField8) {
        _params.put("custom_field_8", customField8);
    }

    public void setCustomField9(String customField9) {
        _params.put("custom_field_9", customField9);
    }

    public void setCustomField10(String customField10) {
        _params.put("custom_field_10", customField10);
    }

    public void setEmail(String email) {
        _params.put("email", email);
    }

    public void setExpires(final Date date) {
        _params.put("expires", new Long(date.getTime()).toString());
    }

    public void setExpiresIn(final int in) {
        setExpires(new Date(System.currentTimeMillis() + in * 1000));
    }

    public void setFirstName(String firstName) {
        _params.put("firstname", firstName);
    }

    public void setLastName(String lastName) {
        _params.put("lastname", lastName);
    }

    public void setRole(String role) {
        _params.put("role", role);
    }

    public void setSalt(String salt) {
        _salt = salt;
    }

    public void setServer(URL server) {
        _server = server.toString();
    }

    public void setService(URL service) {
        _params.put("service", service.toString());
    }

    public void setUuid(String uuid) {
        _params.put("uuid", uuid);
    }
}
