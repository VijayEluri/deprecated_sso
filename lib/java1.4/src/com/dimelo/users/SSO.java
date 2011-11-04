package com.dimelo.users;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
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
 * This class generate SSO link for the Dimelo platform.
 * 
 * @version 0.1
 */
public class SSO
{

    private static String[] _PARAMS = { "avatar_url", "charset", "custom_field_1", "custom_field_10", "custom_field_2", "custom_field_3", "custom_field_4", "custom_field_5", "custom_field_6", "custom_field_7", "custom_field_8", "custom_field_9", "email",
            "expires", "firstname", "lastname", "role", "service", "token",
            "uuid" };
    private static String[] _TOKENIZED_PARAMS = { "avatar_url", "custom_field_1", "custom_field_10", "custom_field_2", "custom_field_3", "custom_field_4", "custom_field_5", "custom_field_6", "custom_field_7", "custom_field_8", "custom_field_9", "email",
            "expires", "firstname", "lastname", "role", "uuid" };

    private static String join(final Collection list, final String delimiter)
    {
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

    private static List joinPairs(Map hash, String separator, String[] keys)
    {
        final List pairs = new ArrayList();
        for (int index = 0; index < keys.length; ++index) {
            final String key = keys[index];
            if (hash.containsKey(key)) {
                pairs.add(key + separator + hash.get(key));
            }
        }
        return pairs;
    }

    private final Map _params;
    private String _salt;
    private String _server;

    public SSO()
    {
        _params = new HashMap();
    }

    /**
     * @param server URL of your authentication server generally something like
     *            <b>https</b>://your-app-name.users.dimelo.com/
     * @param salt The salt available in your administration panel (see SSO doc
     *            page 6)
     */
    public SSO(final URL server, final String salt)
    {
        this();
        setServer(server);
        setSalt(salt);
    }

    /**
     * @param server URL of your authentication server generally something like
     *            <b>https</b>://your-app-name.users.dimelo.com/
     * @param salt The salt available in your administration panel (see SSO doc
     *            page 6)
     * @param service URL of the application where the user will be redirected
     *            after the authentication. Generally something like
     *            http://your-app-name.ideas.dimelo.com/
     */
    public SSO(final URL server, final String salt, final URL service)
    {
        this(server, salt);
        setService(service);
    }

    private String getBaseURL()
    {
        return _server + "cas/login?auth=sso&type=acceptor&";
    }

    private Map getURLEncodedParams()
    {
        final Map urlEncodedParams = new HashMap();
        String param;
        for (int index = 0; index < _PARAMS.length; ++index) {
            final String key = _PARAMS[index];
            if (_params.containsKey(key)) {
                param = (String) _params.get(key);
                try {
                    param = URLEncoder.encode(param, "UTF-8");
                } catch (UnsupportedEncodingException e) {
                }
                urlEncodedParams.put(key, param);
            }
        }
        return urlEncodedParams;
    }

    private void setToken()
    {
        _params.put("token", getToken());
    }

    public String sha1(String text)
    {
        if (null == text)
            throw new NullPointerException("Can't has encode text: null given");
        try {
            MessageDigest sha1 = MessageDigest.getInstance("SHA-1");
            sha1.reset();
            final byte[] hash = sha1.digest(text.getBytes("utf-8"));
            
            final StringBuffer buffer = new StringBuffer();
            for (int index = 0; index < hash.length; ++index) {
                final byte c = hash[index];
                final String hex = Integer.toHexString(c);
                final int len = hex.length();
                if (0 == len)
                    continue;
                if (1 == len)
                    buffer.append('0').append(hex.substring(len - 1));
                else
                    buffer.append(hex.substring(len - 2));
            }
            return buffer.toString();
        } catch (UnsupportedEncodingException ex) {
            ex.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    protected long getTimestamp()
    {
        return Calendar.getInstance().getTime().getTime();
    }

    public String getAvatarUrl()
    {
        return (String) _params.get("avatar_url");
    }

    public String getCharset()
    {
        return (String) _params.get("charset");
    }

    public String getEmail()
    {
        return (String) _params.get("email");
    }

    public String getFirstName()
    {
        return (String) _params.get("firstname");
    }

    public String getLastName()
    {
        return (String) _params.get("lastname");
    }

    public String getRole()
    {
        return (String) _params.get("role");
    }

    public String getService()
    {
        return (String) _params.get("service");
    }

    public String getToken()
    {
        return sha1(join(joinPairs(_params, "-", _TOKENIZED_PARAMS), ":")
            + _salt);
    }

    public URL getURL()
    {
        setToken();
        try {
            return new URL(getBaseURL()
                + join(joinPairs(getURLEncodedParams(), "=", _PARAMS), "&"));
        } catch (MalformedURLException e) {
            return null;
        }
    }

    public String getUuid()
    {
        return (String) _params.get("uuid");
    }

    public void setAvatarUrl(String avatarUrl)
    {
        _params.put("avatar_url", avatarUrl);
    }

    public void setCharset(String charset)
    {
        _params.put("charset", charset);
    }

    public void setEmail(String email)
    {
        _params.put("email", email);
    }

    public void setExpires(long timestamp)
    {
        _params.put("expires", new Long(timestamp).toString());
    }

    public void setExpiresIn(int in)
    {
        _params.put("expires", new Long(in + getTimestamp() / 1000).toString());
    }

    public void setFirstName(String firstName)
    {
        _params.put("firstname", firstName);
    }

    public void setLastName(String lastName)
    {
        _params.put("lastname", lastName);
    }

    public void setRole(String role)
    {
        _params.put("role", role);
    }

    public void setSalt(String salt)
    {
        _salt = salt;
    }

    public void setServer(URL server)
    {
        _server = server.toString();
    }

    public void setService(URL service)
    {
        _params.put("service", service.toString());
    }

    public void setUuid(String uuid)
    {
        _params.put("uuid", uuid);
    }


    
    /*
     * For testing purpose
     */
    public static void main(String[] args) {
        final long _EXPIRES = 1277309412000L;
        final String _SALT = "bfc9396b7c710746b19a1297e70d1716";
        final URL _SERVER;
        final URL _SERVICE;

        try {
            _SERVER = new URL("https", "example.users.dimelo.com", "/");
            _SERVICE = new URL("http", "example.ideas.dimelo.com", "/");
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }

        final SSO sso = new SSO(_SERVER, _SALT, _SERVICE);
        System.out.println("SHA1(\"àéèïù\")");
        System.out.println(sso.sha1("àéèïù").equals("e40523e11b3fb87d0458d18fd7217c049127bbd9"));
    }

}
