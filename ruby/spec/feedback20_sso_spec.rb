require File.dirname(__FILE__) + '/../lib/feedback20_sso.rb'

describe Feedback20::SSO do
  
  before(:each) do
    @salt = 'bfc9396b7c710746b19a1297e70d1716'
    @sso = Feedback20::SSO.new(
      'service' => 'http://example.ideas.feedback20.com/',
      'server' => 'https://example.users.feedback20.com/',
      'salt' => @salt
    )
  end
  
  it "should works with only required params" do
    @sso.url(
      'firstname' => 'Renaud',
      'uuid' => 42,
      'expires' => 1277309412
    ).should == "https://example.users.feedback20.com/cas/login?auth=sso&type=acceptor&expires=1277309412&firstname=Renaud&service=http://example.ideas.feedback20.com/&token=c34d2ee42be49635c027d1a7c39fd2f5d8411f39&uuid=42"
  end
  
  it 'should works with all params' do
    @sso.url(
      'firstname' => 'Renaud',
      'lastname' => 'Morane',
      'avatar_url' => 'http://example.com/avatar.png',
      'email' => 'foo@example.com',
      'charset' => 'utf-8',
      'role' => 'user',
      'uuid' => 42,
      'expires' => 1277309412
    ).should == "https://example.users.feedback20.com/cas/login?auth=sso&type=acceptor&avatar_url=http://example.com/avatar.png&charset=utf-8&email=foo@example.com&expires=1277309412&firstname=Renaud&lastname=Morane&role=user&service=http://example.ideas.feedback20.com/&token=4b44a7e193ba638700f44186de6caf3ebc270548&uuid=42"
  end
  
  it 'could set expires with expires_in' do
    Time.stub!(:now => mock(:now, :utc => 1277309410))
    @sso.url(
      'firstname' => 'Renaud',
      'lastname' => 'Morane',
      'avatar_url' => 'http://example.com/avatar.png',
      'email' => 'foo@example.com',
      'charset' => 'utf-8',
      'role' => 'user',
      'uuid' => 42,
      'expires_in' => 2
    ).should == "https://example.users.feedback20.com/cas/login?auth=sso&type=acceptor&avatar_url=http://example.com/avatar.png&charset=utf-8&email=foo@example.com&expires=1277309412&firstname=Renaud&lastname=Morane&role=user&service=http://example.ideas.feedback20.com/&token=4b44a7e193ba638700f44186de6caf3ebc270548&uuid=42"
  end
  
  it 'can override service into #url' do
    @sso.url(
      'service' => 'http://example.com/',
      'firstname' => 'Renaud',
      'lastname' => 'Morane',
      'avatar_url' => 'http://example.com/avatar.png',
      'email' => 'foo@example.com',
      'charset' => 'utf-8',
      'role' => 'user',
      'uuid' => 42,
      'expires' => 1277309412
    ).should == "https://example.users.feedback20.com/cas/login?auth=sso&type=acceptor&avatar_url=http://example.com/avatar.png&charset=utf-8&email=foo@example.com&expires=1277309412&firstname=Renaud&lastname=Morane&role=user&service=http://example.com/&token=4b44a7e193ba638700f44186de6caf3ebc270548&uuid=42"
  end
  
end
