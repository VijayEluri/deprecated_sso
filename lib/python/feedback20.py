#!/usr/bin/python
# -*- encoding: utf-8 -*-
from time import time
from urllib import quote_plus
try:
    import hashlib
    hashfunc = hashlib.sha1
except ImportError:
    import sha
    hashfunc = sha.new


class SSO(object):
    """This class generate SSO links for the Feedback2.0 platform.
    SSO(server, salt[, service])
    
    >>> server = 'https://example.users.feedback20.com/'
    >>> salt = 'bfc9396b7c710746b19a1297e70d1716'
    >>> service = 'http://example.ideas.feedback20.com/'
    >>> sso = SSO(server, salt, service)
    
    It can be used with minimal params
    >>> sso.url(firstname='Renaud', uuid=42, expires=1277309412)
    'https://example.users.feedback20.com/cas/login?auth=sso&type=acceptor&expires=1277309412&firstname=Renaud&service=http%3A%2F%2Fexample.ideas.feedback20.com%2F&token=c34d2ee42be49635c027d1a7c39fd2f5d8411f39&uuid=42'
    
    It can receive more params
    >>> sso.url(firstname='Renaud', lastname='Morane', avatar_url='http://example.com/avatar.png', email='foo@example.com', charset='utf-8', role='user', uuid=42, expires=1277309412)
    'https://example.users.feedback20.com/cas/login?auth=sso&type=acceptor&avatar_url=http%3A%2F%2Fexample.com%2Favatar.png&charset=utf-8&email=foo%40example.com&expires=1277309412&firstname=Renaud&lastname=Morane&role=user&service=http%3A%2F%2Fexample.ideas.feedback20.com%2F&token=4b44a7e193ba638700f44186de6caf3ebc270548&uuid=42'
    
    It can receive expires_in instead of expires
    >>> sso.url(expires_in=2, firstname='Renaud', lastname='Morane', avatar_url='http://example.com/avatar.png', email='foo@example.com', charset='utf-8', role='user', uuid=42)
    'https://example.users.feedback20.com/cas/login?auth=sso&type=acceptor&avatar_url=http%3A%2F%2Fexample.com%2Favatar.png&charset=utf-8&email=foo%40example.com&expires=1277309412&firstname=Renaud&lastname=Morane&role=user&service=http%3A%2F%2Fexample.ideas.feedback20.com%2F&token=4b44a7e193ba638700f44186de6caf3ebc270548&uuid=42'
    
    It can override __init__'s service by passing another one in url
    >>> sso.url(service='http://example.com/', expires_in=2, firstname='Renaud', lastname='Morane', avatar_url='http://example.com/avatar.png', email='foo@example.com', charset='utf-8', role='user', uuid=42)
    'https://example.users.feedback20.com/cas/login?auth=sso&type=acceptor&avatar_url=http%3A%2F%2Fexample.com%2Favatar.png&charset=utf-8&email=foo%40example.com&expires=1277309412&firstname=Renaud&lastname=Morane&role=user&service=http%3A%2F%2Fexample.com%2F&token=4b44a7e193ba638700f44186de6caf3ebc270548&uuid=42'
    
    It urlencode params 
    >>> sso.url(firstname='Fée cloch&tte', uuid=42, expires=1277309412)
    'https://example.users.feedback20.com/cas/login?auth=sso&type=acceptor&expires=1277309412&firstname=F%C3%A9e+cloch%26tte&service=http%3A%2F%2Fexample.ideas.feedback20.com%2F&token=02a6ace4166420db8f45c77af87ad42e4a600a0d&uuid=42'
    
    """
    TOKENIZED_PARAMS = sorted(['firstname', 'expires', 'uuid', 'avatar_url', 'email', 'lastname', 'role'])
    PARAMS = sorted(TOKENIZED_PARAMS + ['charset', 'service', 'token'])

    def __init__(self, server, salt, service=None):
        self.server = server
        self.salt = salt
        self.service = service

    def url(self, uuid, firstname, **kwargs):
        """url(uuid, firstname, expires|expires_in)
        Optional parameters:
            - lastname
            - email
            - avatar_url
            - role
            - service
            - charset
        """
        
        expires = self._compute_expires(kwargs)
        if 'service' not in kwargs:
            kwargs.update(service=self.service)
        kwargs.update(firstname=firstname, uuid=uuid, expires=expires)
        kwargs.update(token=self.token(kwargs))
        return self._base_url() + '&'.join('%s=%s' % (k, quote_plus(str(v))) for k, v in
            sorted(kwargs.items()) if k in self.PARAMS)

    def token(self, params):
        token_string = ':'.join('%s-%s' % (k, v) for k, v in
            sorted(params.items()) if k in self.TOKENIZED_PARAMS)
        return hashfunc(token_string + self.salt).hexdigest()

    def _base_url(self):
        return "%scas/login?auth=sso&type=acceptor&" % self.server

    def _compute_expires(self, kwargs):
        if 'expires' in kwargs:
            return int(kwargs['expires'])
        elif 'expires_in' in kwargs:
            return int(time()) + int(kwargs['expires_in'])
        raise TypeError("url takes an expires or expires_in argument")


if __name__ == "__main__":
    import doctest
    def time():
        return 1277309410
    doctest.testmod()
