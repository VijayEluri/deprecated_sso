# DimeloSSO

Usage:

    <?php
    $sso = new DimeloSSO(
        'https://example.users.dimelo.com/',
        'bfc9396b7c710746b19a1297e70d1716',
        'http://example.ideas.dimelo.com/');

    $sso_link = $sso->url(array(
        'firstname' => 'Renaud',
        'lastname' => 'Morane',
        'avatar_url' => 'http://example.com/avatar.png',
        'email' => 'foo@example.com',
        'role' => 'user',
        'uuid' => 42,
        'expires_in' => (60 * 60 * 24) // Valid for 24 hours
    );
    ?>
    <a href="<?php echo $sso_link; ?>">Go to ideas site</a>

