require File.expand_path(File.dirname(__FILE__) + '/../lib/dimelo_sso.rb')

describe Dimelo::SSO do
  
  before(:each) do
    @sso = Dimelo::SSO.new(
      'service' => 'http://example.ideas.dimelo.com/',
      'server' => 'https://example.users.dimelo.com/',
      'salt' => 'bfc9396b7c710746b19a1297e70d1716'
    )
  end
  
  it "should works with only required params" do
    @sso.url(
      'firstname' => 'Renaud',
      'uuid' => 42,
      'expires' => 1277309412
    ).to_s.should == "https://example.users.dimelo.com/cas/login?auth=sso&type=acceptor&expires=1277309412&firstname=Renaud&service=http%3A%2F%2Fexample.ideas.dimelo.com%2F&token=c34d2ee42be49635c027d1a7c39fd2f5d8411f39&uuid=42"
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
      'custom_field_1' => 'custom field 1',
      'expires' => 1277309412
    ).to_s.should == "https://example.users.dimelo.com/cas/login?auth=sso&type=acceptor&avatar_url=http%3A%2F%2Fexample.com%2Favatar.png&charset=utf-8&custom_field_1=custom+field+1&email=foo%40example.com&expires=1277309412&firstname=Renaud&lastname=Morane&role=user&service=http%3A%2F%2Fexample.ideas.dimelo.com%2F&token=d84559ad53d21e1bd0ce88cd3178cc7e91da1499&uuid=42"
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
    ).to_s.should == "https://example.users.dimelo.com/cas/login?auth=sso&type=acceptor&avatar_url=http%3A%2F%2Fexample.com%2Favatar.png&charset=utf-8&email=foo%40example.com&expires=1277309412&firstname=Renaud&lastname=Morane&role=user&service=http%3A%2F%2Fexample.ideas.dimelo.com%2F&token=4b44a7e193ba638700f44186de6caf3ebc270548&uuid=42"
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
    ).to_s.should == "https://example.users.dimelo.com/cas/login?auth=sso&type=acceptor&avatar_url=http%3A%2F%2Fexample.com%2Favatar.png&charset=utf-8&email=foo%40example.com&expires=1277309412&firstname=Renaud&lastname=Morane&role=user&service=http%3A%2F%2Fexample.com%2F&token=4b44a7e193ba638700f44186de6caf3ebc270548&uuid=42"
  end
  
end
