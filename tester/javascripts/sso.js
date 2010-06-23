
var SSOParam = Class.create({
    
    initialize: function(name, value, isRequired) {
        this.name = name;
        this.value = value;
        this.isRequired = isRequired;
    },
    
    getStatus: function() {
        if (this.value) {
            return 'present';
        }
        return this.isRequired ? 'missing' : 'absent';
    },
    
    valueRepresentation: function() {
        return this.value ? this.value : '';
    },
    
    toRow: function() {
        return [this.name, this.valueRepresentation(), this.isRequired, this.getStatus()];
    }
    
});

var SSOTester = Class.create({
    
    REQUIRED_PARAMS: ['expires', 'firstname', 'uuid'],
    TOKENIZED_PARAMS: ['avatar_url', 'email', 'expires', 'firstname', 'lastname', 'role', 'uuid'],
    PARAMS: ['avatar_url', 'charset', 'email', 'expires', 'firstname', 'lastname', 'role', 'service', 'token', 'uuid'],
    
    initialize: function(form) {
        this.submit = form.down('#run');
        this.stdout = $('stdout');
        this.submit.observe('click', this.runTest.bind(this));
        form.down('#salt').observe('keyup', this.updateSubmit.bind(this));
        form.down('#url').observe('keyup', this.updateSubmit.bind(this));
        this.updateSubmit();
    },
    
    updateSubmit: function() {
        if ($F('salt').strip() && $F('url').strip()) {
            this.submit.enable();
        } else {
            this.submit.disable();
        }
    },
    
    runTest: function(event) {
        event.stop();
        this.salt = $F('salt').strip();
        this.params = $F('url').strip().toQueryParams();
        this.stdout.innerHTML = '';
        this.checkParamsPresence();
        this.showParamsTable();
        this.showTokenProcess();
    },
    
    checkParamsPresence: function() {
        this.paramsTable = this.PARAMS.map(function(name) {
            return new SSOParam(name, this.params[name], this.REQUIRED_PARAMS.include(name));
        }.bind(this));
    },
    
    showParamsTable: function() {
        var table = new Element('table');
        var header = new Element('tr');
        header.insert(new Element('td').update("Name"));
        header.insert(new Element('td').update("Required ?"));
        header.insert(new Element('td').update("Value"));
        header.insert(new Element('td').update("Status"));
        table.insert(header);
        
        this.paramsTable.each(function(param) {
            var row = new Element('tr', {class: param.getStatus()});
            row.insert(new Element('td', {class: 'param_name'}).update(param.name));
            row.insert(new Element('td', {class: 'param_required_' + param.isRequired }).update(param.isRequired));
            row.insert(new Element('td', {class: 'param_value'}).update(param.value));
            row.insert(new Element('td', {class: 'param_status'}).update(param.getStatus()));
            table.insert(row);
        });
        
        this.stdout.insert(table);
    },
    
    getTokenString: function() {
        return this.TOKENIZED_PARAMS.map(function(name) {
            if (this.params[name]) return name + '-' + this.params[name];
        }.bind(this)).compact().join(':') + this.salt;
    },
    
    showTokenProcess: function() {
        var tokenString = this.getTokenString();
        var token = SHA1(tokenString);
        this.stdout.insert('<p>Token string before sha-1 should be: <br/>'+ tokenString +'</p>');
        this.stdout.insert('<p>Final token after sha-1 should be: <br/>'+ token +'</p>');
        if (token == this.params['token']) {
            this.stdout.insert('<p class="success">The token match !</p>');
        } else {
            this.stdout.insert('<p class="failure">The token does not match !</p>');
        }
    }
});