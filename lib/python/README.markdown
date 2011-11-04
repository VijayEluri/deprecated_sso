# dimelo.SSO

Usage:

    import dimelo
    sso = dimelo.SSO(
        'https://example.users.dimelo.com/',
        'http://example.ideas.dimelo.com/',
        'bfc9396b7c710746b19a1297e70d1716'
    )
    sso_link = sso.url(
      firstname='Renaud',
      lastname='Morane',
      avatar_url='http://example.com/avatar.png',
      email='foo@example.com',
      role='user',
      uuid=42,
      expires_in=(60 * 60 * 24) # valid for 24 hours
    )
    print sso_link
