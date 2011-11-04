# Dimelo::SSO

Usage (Rails):

    # controller
    @sso = Dimelo::SSO.new(
      'service' => 'http://example.ideas.dimelo.com/',
      'server' => 'https://example.users.dimelo.com/',
      'salt' => 'bfc9396b7c710746b19a1297e70d1716'
    )
    @sso_link = @sso.url(
      'firstname' => 'Renaud',
      'lastname' => 'Morane',
      'avatar_url' => 'http://example.com/avatar.png',
      'email' => 'foo@example.com',
      'role' => 'user',
      'uuid' => 42,
      'expires_in' => 24.hours
    )
    
    # view
    <%= link_to "Feedback2.0", @sso_link %>
