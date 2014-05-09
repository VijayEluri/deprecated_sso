# Dimelo.SSO (This SSO protocol is deprecated)

Usage:

    var server = new Uri("https://example.users.dimelo.com/");
    var service = new Uri("http://example.ideas.dimelo.com/");
    var salt = "bfc9396b7c710746b19a1297e70d1716";

    var sso = new SSO(server, salt, service);
    sso.FirstName = "Renaud";
    sso.Uuid = "42";
    sso.ExpiresIn = 60 * 60 * 24;
    sso.LastName = "Morane";
    sso.AvatarUrl = new Uri("http://example.com/avatar.png");
    sso.Email = "foo@example.com";
    sso.Charset = "winlatin1";
    sso.Role = "user";
    
    Console.WriteLine(sso.BuildUri().ToString());
