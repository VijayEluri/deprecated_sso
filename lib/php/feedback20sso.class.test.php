<?php
require_once 'feedback20sso.class.php';

class SSOTest extends Feedback20SSO {
    
    protected function get_timestamp() {
        return 1277309410;
    }
    
}

$errors = array();
function assertEquals($got, $expect) {
    global $errors;
    if ($got == $expect) {
        print '.';
    } else {
        print 'F';
        array_push($errors, "Expected:
$expect
got:
$got
");
    }
}

function printErrors() {
    global $errors;
    echo "\n\n";
    foreach($errors as $error) {
        echo $error;
    }
}


$sso = new SSOTest(
    'https://example.users.feedback20.com/',
    'bfc9396b7c710746b19a1297e70d1716',
    'http://example.ideas.feedback20.com/');

//it should works with only required params
assertEquals(
    $sso->url(array(
        'firstname' => 'Renaud',
        'uuid' => 42,
        'expires' => 1277309412
    )),
    "https://example.users.feedback20.com/cas/login?auth=sso&type=acceptor&expires=1277309412&firstname=Renaud&service=http%3A%2F%2Fexample.ideas.feedback20.com%2F&token=c34d2ee42be49635c027d1a7c39fd2f5d8411f39&uuid=42"
);

//it should works with all params
assertEquals(
    $sso->url(array(
        'firstname' => 'Renaud',
        'lastname' => 'Morane',
        'avatar_url' => 'http://example.com/avatar.png',
        'email' => 'foo@example.com',
        'charset' => 'utf-8',
        'role' => 'user',
        'uuid' => 42,
        'expires' => 1277309412
    )),
    "https://example.users.feedback20.com/cas/login?auth=sso&type=acceptor&avatar_url=http%3A%2F%2Fexample.com%2Favatar.png&charset=utf-8&email=foo%40example.com&expires=1277309412&firstname=Renaud&lastname=Morane&role=user&service=http%3A%2F%2Fexample.ideas.feedback20.com%2F&token=4b44a7e193ba638700f44186de6caf3ebc270548&uuid=42"
);

// it could set expires with expires_in
assertEquals(
    $sso->url(array(
        'firstname' => 'Renaud',
        'lastname' => 'Morane',
        'avatar_url' => 'http://example.com/avatar.png',
        'email' => 'foo@example.com',
        'charset' => 'utf-8',
        'role' => 'user',
        'uuid' => 42,
        'expires_in' => 2
    )),
    "https://example.users.feedback20.com/cas/login?auth=sso&type=acceptor&avatar_url=http%3A%2F%2Fexample.com%2Favatar.png&charset=utf-8&email=foo%40example.com&expires=1277309412&firstname=Renaud&lastname=Morane&role=user&service=http%3A%2F%2Fexample.ideas.feedback20.com%2F&token=4b44a7e193ba638700f44186de6caf3ebc270548&uuid=42"
);

//  it can override service into #url
assertEquals(
    $sso->url(array(
        'service' => 'http://example.com/',
        'firstname' => 'Renaud',
        'lastname' => 'Morane',
        'avatar_url' => 'http://example.com/avatar.png',
        'email' => 'foo@example.com',
        'charset' => 'utf-8',
        'role' => 'user',
        'uuid' => 42,
        'expires' => 1277309412
    )),
    "https://example.users.feedback20.com/cas/login?auth=sso&type=acceptor&avatar_url=http%3A%2F%2Fexample.com%2Favatar.png&charset=utf-8&email=foo%40example.com&expires=1277309412&firstname=Renaud&lastname=Morane&role=user&service=http%3A%2F%2Fexample.com%2F&token=4b44a7e193ba638700f44186de6caf3ebc270548&uuid=42"
);


printErrors();

?>
