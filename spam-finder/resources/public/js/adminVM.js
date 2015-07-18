ko.validation.init({
    registerExtenders: true,
    messagesOnModified: true,
    insertMessages: false,
    parseInputAttributes: true,
    messageTemplate: null
}, true);

window.admin.adminViewModel = (function(ko, toastr, utility, serverProxy) {

	var initData, AdminFactory, User, mustEqual;
	var self = this;

	AdminFactory = function(serverItem) {
		
		var that = this;
		
		that.firstName = serverItem.first_name;
		that.lastName = serverItem.last_name;
		that.email = serverItem.email;
		that.username = serverItem.username;
	};
	
	mustEqual = function (val, other) {
		return val == other;
	};
	
	User = function() {
		
		var that = this;
		
		that.username = ko.observable("").extend({required: true});
		that.firstName = ko.observable("").extend({required: true});
		that.lastName = ko.observable("").extend({required: true});
		that.email = ko.observable("").extend({required: true, email: true});
		that.password = ko.observable("").extend({minLength: {message: "Password must contains at least 5 characters", params: 5 }});
		that.confirmPassword = ko.observable("").extend({
			validation: 
			{ 
				validator: mustEqual, 
				message: "Passwords do not match !", 
				params: this.password 
			}
		});
		that.isAdmin = ko.observable(false);
    };

	User.prototype.confirmPassword = ko.observable("").extend({
		validation: 
		{ 
			validator: mustEqual, 
			message: "Passwords do not match !", 
			params: this.password 
		}
	});
	
	initData = function(serverData) {
		utility.rebuildObservableArray(self.users, serverData, AdminFactory);
	};
	
	AdminFactory.prototype.goToUser = function() {
		serverProxy.getUser(this.username, function(serverData) {
			self.user(new AdminFactory(serverData));
		});
	};
	
	AdminFactory.prototype.editUser = function() {
		serverProxy.editUser(this.username, this.firstName, this.lastName, this.email, function(serverData) {
			if(serverData.Ok === 1) {
				serverProxy.getInitData(initData);
				toastr.success("User successfully updated !");
			}
			else {
				toastr.error("Error while updating user !");
			}
		});
	};
	
	AdminFactory.prototype.removeUser = function() {
		serverProxy.removeUser(this.username, function(serverData) {
			if(serverData.Ok === 1) {
				self.user(null);
				serverProxy.getInitData(initData);
				toastr.success("User successfully deleted !");
			}
			else {
				toastr.error("Error while deleting user !");
			}
		});
	};
	
	User.prototype.createUser = function() {
		if (self.errors().length === 0) {
			serverProxy.createUser(self.userVm().username, self.userVm().firstName, self.userVm().lastName, self.userVm().email, self.userVm().password, self.userVm().isAdmin, function(serverData) {
				if(serverData.Ok === 1) {
					self.toCreate(false);
					self.userVm(new User());
					serverProxy.getInitData(initData);
					toastr.success("User successfully created !");
				}
				else {
					toastr.success("Error while creating user !");
				}
			});
		} else {
			toastr.error("Please fix all errors before proceeding.");
			self.errors.showAllMessages();
		}
	};
	
	self.goToCreateUser = function() {
		self.toCreate(true);
	};
	
	self.goToUsers = function() {
		self.toCreate(false);
	};
	
	// observables
	self.users = ko.observableArray();
	self.user = ko.observable();
	self.userVm = ko.observable(new User());
	self.toCreate = ko.observable(false);
	
	self.errors = ko.validation.group([self.userVm().username, self.userVm().firstName, self.userVm().lastName, self.userVm().email, self.userVm().password, self.userVm().confirmPassword]);
	
	// get data
	serverProxy.getInitData(initData);
	
})(ko, toastr, admin.utility, admin.serverProxy);

ko.applyBindings(window.admin.adminViewModel);