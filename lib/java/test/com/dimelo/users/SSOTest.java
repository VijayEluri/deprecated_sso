package com.dimelo.users;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;

import org.junit.Test;

public class SSOTest {

    private static final Date _EXPIRES = new Date(1277309412000L);
    private static final String _SALT = "bfc9396b7c710746b19a1297e70d1716";
    private static final URL _SERVER;
    private static final URL _SERVICE;

    static {
        try {
            _SERVER = new URL("https", "example.users.feedback20.com", "/");
            _SERVICE = new URL("http", "example.ideas.feedback20.com", "/");
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testSha1() {
        final SSO sso = new SSO(_SERVER, _SALT, _SERVICE);
        assertEquals("0beec7b5ea3f0fdbc95d0dd47f3c5bc275da8a33", sso.sha1("foo"));
        assertEquals("e40523e11b3fb87d0458d18fd7217c049127bbd9", sso.sha1("àéèïù"));

    }

    @Test
    public void testAllParams() {
        final String expected = "https://example.users.feedback20.com/cas/login?auth=sso&type=acceptor&avatar_url=http%3A%2F%2Fexample.com%2Favatar.png&charset=utf-8&email=foo%40example.com&expires=1277309412000&firstname=Renaud&lastname=Morane&role=user&service=http%3A%2F%2Fexample.ideas.feedback20.com%2F&token=f4660177e8e1d2f8fd90621dd283c1c4020b05a8&uuid=42";
        final SSO sso = new SSO(_SERVER, _SALT, _SERVICE) {

            {
                setFirstName("Renaud");
                setLastName("Morane");
                setAvatarUrl("http://example.com/avatar.png");
                setEmail("foo@example.com");
                setCharset("utf-8");
                setRole("user");
                setUuid("42");
                setExpires(_EXPIRES);
            }
        };
        assertEquals(sso.getURL().toString(), expected);
    }

    @Test
    public void testExpiresIn() {
        final SSO sso = new SSO(_SERVER, _SALT, _SERVICE);
        sso.setExpiresIn(60 * 24 * 24);
        final long expires = Long.parseLong(sso.getExpires());
        final long expected = System.currentTimeMillis() + (60 * 24 * 24 * 1000);

        assertTrue(expires < (expected + 50));
        assertTrue(expires > (expected - 50));
    }

    @Test
    public void testRequiredParams() {
        final String expected = "https://example.users.feedback20.com/cas/login?auth=sso&type=acceptor&expires=1277309412000&firstname=Renaud&service=http%3A%2F%2Fexample.ideas.feedback20.com%2F&token=46c21dc51d56606d1973770b240f69ffeadd9b97&uuid=42";
        final SSO sso = new SSO(_SERVER, _SALT, _SERVICE) {

            {
                setFirstName("Renaud");
                setUuid("42");
                setExpires(_EXPIRES);
            }
        };
        assertEquals(sso.getURL().toString(), expected);
    }
}
