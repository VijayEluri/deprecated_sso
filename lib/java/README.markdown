com.feedback20.users.SSO
=============================

Example of use:
    
    URL server = new URL("https", "example.users.feedback20.com", "/");
    String salt = "bfc9396b7c710746b19a1297e70d1716";
    URL service = new URL("http", "example.ideas.feedback20.com", "/");
    
    SSO sso = new SSO(server, salt, service){{
        setFirstName("Renaud");
        setLastName("Morane");
        setAvatarUrl("http://example.com/avatar.png");
        setEmail("foo@example.com");
        setRole("user");
        setUuid("42");
        setExpiresIn(60 * 60 * 24); // Valid for 24 hours
    }};
    System.out.println(sso.getURL().toString());
