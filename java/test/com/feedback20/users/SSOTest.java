package com.feedback20.users;

import org.junit.Test;
import static org.junit.Assert.*;

public class SSOTest {

    static String SERVER = "https://example.users.feedback20.com/";
    static String SALT = "bfc9396b7c710746b19a1297e70d1716";
    static String SERVICE = "http://example.ideas.feedback20.com/";

    public SSOTest() {
    }

    @Test
    public void testRequiredParams() {
        String expected = "https://example.users.feedback20.com/cas/login?auth=sso&type=acceptor&expires=1277309412&firstname=Renaud&service=http://example.ideas.feedback20.com/&token=c34d2ee42be49635c027d1a7c39fd2f5d8411f39&uuid=42";
        SSO sso = new SSO(SERVER, SALT, SERVICE){{
            setFirstName("Renaud");
            setUuid("42");
            setExpires(1277309412);
        }};
        assertEquals(sso.getURL(), expected);
    }

    @Test
    public void testAllParams() {
        String expected = "https://example.users.feedback20.com/cas/login?auth=sso&type=acceptor&avatar_url=http://example.com/avatar.png&charset=utf-8&email=foo@example.com&expires=1277309412&firstname=Renaud&lastname=Morane&role=user&service=http://example.ideas.feedback20.com/&token=4b44a7e193ba638700f44186de6caf3ebc270548&uuid=42";
        SSO sso = new SSO(SERVER, SALT, SERVICE){{
            setFirstName("Renaud");
            setLastName("Morane");
            setAvatarUrl("http://example.com/avatar.png");
            setEmail("foo@example.com");
            setCharset("utf-8");
            setRole("user");
            setUuid("42");
            setExpires(1277309412);
        }};
        assertEquals(sso.getURL(), expected);
    }
}
