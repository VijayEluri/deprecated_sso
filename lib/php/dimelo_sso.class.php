<?php
/**
 * This class generate SSO link for the Dimelo platform.
 * DimeloSSO(string $server, string $salt[, string $service])
 */
class DimeloSSO {
    
    function __construct($server, $salt, $service=null) {
        //consts
        $this->REQUIRED_PARAMS = array('expires', 'firstname', 'uuid');
        $this->TOKENIZED_PARAMS = array('avatar_url', 'email', 'expires', 'firstname', 'lastname', 'role', 'uuid');
        $this->PARAMS = array('avatar_url', 'charset', 'email', 'expires', 'firstname', 'lastname', 'role', 'service', 'token', 'uuid');
        
        $this->server   = $server;
        $this->salt     = $salt;
        $this->service  = $service;
    }
    
    /**
     * string url(array $params)
     * Required parameters:
     *   - int expires_in   : link validity period in seconds
     *   - string uuid      : users's unique identifier
     *   - string firstname : users's firstname
     * Optional parameters:
     *   - lastname
     *   - email
     *   - avatar_url
     *   - role
     *   - service
     *   - charset
     */
    function url($params) {
        $params['expires'] = $this->compute_expires($params);
        
        if (! array_key_exists('service', $params)) {
            $params['service'] = $this->service;
        }
        
        $params['token'] = $this->token($params);
        
        $params = $this->ordered_slice($params, $this->PARAMS);
        foreach($params as $key => $value) {
            $params[$key] = rawurlencode($value);
        }
        return $this->base_url().$this->two_way_join('&', '=', $params);
    }
    
    function token($params) {
        $params = $this->ordered_slice($params, $this->TOKENIZED_PARAMS);
        return sha1($this->two_way_join(':', '-', $params).$this->salt);
    }
    
    private function compute_expires($params) {
        if (array_key_exists('expires_in', $params)) {
            return $this->get_timestamp() + intval($params['expires_in']);
        }
        if (array_key_exists('expires', $params)) {
            return intval($params['expires']);
        }
    }
    
    protected function get_timestamp() {
        return time();
    }
    
    private function base_url() {
        return $this->server.'cas/login?auth=sso&type=acceptor&';
    }
    
    private function ordered_slice($params, $keys) {
        $return_array = array();
        foreach($keys as $key) {
            if (array_key_exists($key, $params)) $return_array[$key] = $params[$key];
        }
        return $return_array;
    }
    
    private function two_way_join($item_sep, $pair_sep, $array) {
        $items = array();
        foreach($array as $k => $v) {
            array_push($items, $k.$pair_sep.$v);
        }
        return join($item_sep, $items);
    }
}
?>