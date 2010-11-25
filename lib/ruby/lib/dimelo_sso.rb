require 'uri'
require 'cgi'
require 'digest/sha1'

module Dimelo
  
  # This class generate SSO links for the Dimelo platform. All param keys should be strings not symbols
  # Required params:
  #   - salt   : SSO salt as String. Found in your dimelo admin panel
  #   - server : CAS server URL with scheme and trailing slash like http://example.users.feedback20.com/
  #
  # Optional params:
  #   - service : Service URL with scheme and trailing slash like http://example.ideas.feedback20.com/
  # 
  # Example:
  #   @sso = Dimelo::SSO.new(
  #     'service' => 'http://example.ideas.feedback20.com/',
  #     'server' => 'https://example.users.feedback20.com/',
  #     'salt' => 'bfc9396b7c710746b19a1297e70d1716'
  #   )
  #
  #   @sso.url(
  #     'firstname'   => 'Renaud',
  #     'lastname'    => 'Morane',
  #     'avatar_url'  => 'http://example.com/avatar.png',
  #     'email'       => 'foo@example.com',
  #     'charset'     => 'utf-8',
  #     'role'        => 'user',
  #     'uuid'        => 42,
  #     'expires_in'  => (60 * 60 * 24)
  #   )
  class SSO
    
    REQUIRED_PARAMS = %w(firstname expires uuid).sort.freeze
    TOKENIZED_PARAMS = (REQUIRED_PARAMS + %w(avatar_url email lastname role)).sort.freeze
    PARAMS = (TOKENIZED_PARAMS + %w(charset service token)).sort.freeze
    
    attr_accessor :salt, :service, :server
    
    def initialize(config={})
      @salt = config['salt'] or raise ArgumentError.new("Missing required parameter 'salt'")
      @service = URI.parse(config['service'].to_s)
      raise ArgumentError.new("Missing required parameter 'server'") unless config.has_key?('server')
      @server = URI.parse(config['server'].to_s)
    end 
    
    # Required params:
    #   - :firstname  : Your user's firstname
    #   - :expires_in : Link validity period in seconds as Integer
    #   - :uuid User  : Unique Identitfier, can be anything but should be unique
    #
    # Optional params:
    #   - :role       : Your user's role as string, e.g. 'user'
    #   - :avatar_url : Your user's avatar, it should be a valid URL and it will be downloaded
    #   - :email
    #   - :lastname
    #   - :charset    : Params encoding, can be latin1 or winlatin1 for Microsoft's softwares. Default to utf-8
    def url(params)
      normalize_params(params)
      check_params(params)
      query_string = ordered_slice(params, PARAMS).map{ |key, value| "#{key}=#{CGI.escape(value)}" }.join('&')
      server.scheme = 'https'
      server.path = '/cas/login'
      server.query = 'auth=sso&type=acceptor&' << query_string
      server
    end

    def token(params)
      Digest::SHA1.hexdigest(ordered_slice(params, TOKENIZED_PARAMS).map{ |key, value| "#{key}-#{value}" }.join(':') + salt)
    end
    
    private
    
    def normalize_params(params)
      params['service'] ||= service
      params['expires'] = params['expires_in'].to_i + Time.now.utc.to_i if params.has_key?('expires_in')
      params['token'] = token(params)
    end
    
    def check_params(params)
      missing_params = REQUIRED_PARAMS.reject{ |required_key| params.has_key?(required_key) }
      raise ArgumentError.new("Missing required parameters: #{missing_params.map{ |p| p.inspect }.join(', ')}") unless missing_params.empty?
    end
    
    def ordered_slice(params, keys)
      keys.inject([]) do |array, key|
        array << [key.to_s, params[key].to_s] if params[key]
        array
      end
    end
    
    def base_url
      "#{server}cas/login?"
    end
    
  end
  
end