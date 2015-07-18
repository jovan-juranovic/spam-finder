ko.validation.init({
    registerExtenders: true,
    messagesOnModified: true,
    insertMessages: false,
    parseInputAttributes: true,
    messageTemplate: null
}, true);

window.user.userViewModel = (function (ko, toastr, serverProxy) {
	
	var initData, UserFactory, errors;
	var self = this;
	
	UserFactory = function(serverItem) {
		
		var that = this;
		
		that.username = ko.observable(serverItem.username).extend({required: true});
		that.firstName = ko.observable(serverItem.first_name).extend({required: true});
		that.lastName = ko.observable(serverItem.last_name).extend({required: true});
		that.email = ko.observable(serverItem.email).extend({required: true, email: true});
	};
	
	UserFactory.prototype.editUser = function() {
		if (errors().length === 0) {
			serverProxy.editProfile(this.username, this.firstName, this.lastName, this.email, function(serverData) {
				if(serverData.Ok === 1) {
					serverProxy.getInitUser(initData);
					toastr.success("Profile successfully updated !");
				}
				else {
					toastr.error("Error while updating profile !");
				}
			});
		}
		else {
			toastr.error("Please fix all errors before proceeding.");
			errors.showAllMessages();
		}
	}
	
	initData = function(serverData) {
		self.user(new UserFactory(serverData));
		errors = ko.validation.group([self.user().username, self.user().firstName, self.user().lastName, self.user().email]);
	};
	
	// observables
	self.user = ko.observable();
	
	// get data
	serverProxy.getInitUser(initData);
	
})(ko, toastr, admin.serverProxy);

ko.applyBindings(window.user.userViewModel);