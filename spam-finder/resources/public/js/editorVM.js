window.user.editorViewModel = (function(ko, toastr, serverProxy) {
	
	var EditorFactory;
	var self = this;
	
	EditorFactory = function(serverItem) {
		var that = this;
		
		that.word = serverItem[0];
		that.frequency = serverItem[1];
	}
	
	self.classify = function() {
		if(self.text().length > 0) {
			serverProxy.classifyText(self.text(), function(serverData) {
				self.probability(serverData.resp);
			});
			serverProxy.getWordCount(self.text(), function(serverData) {
				var newArray = [];
				for(i=0; i<serverData.data.length; i++) {
					newArray.push(new EditorFactory(serverData.data[i]));
				}
				self.isLoaded(true);
				self.frequencies(newArray);
			});
		}
		else {
			self.probability("");
			toastr.error("Text field can not be empty !");
		}
	};
	
	self.train = function() {
		if(self.text().length === 0) {
			toastr.error("Text field can not be empty !");
			return;
		}
		if(self.trainSpam() && self.trainHam()) {
			toastr.error("Please select only one of the provided options !");
			return;
		}
		if(self.trainSpam()) {
			serverProxy.trainOnText(self.text(), "spam", function(serverData) {
				if(serverData.Ok === 1) {
					self.trainSpam(false);
					toastr.warning("Provided message pattern is marked as potential spam message !");
				}
			});
		}
		else if(self.trainHam()) {
			serverProxy.trainOnText(self.text, "ham", function(serverData) {
				if(serverData.Ok === 1) {
					self.trainHam(false);
					toastr.success("Provided message pattern is marked as potential ham message !");
				}
			});
		}
		else {
			toastr.error("Please select one of the provided options !");
		}
	}
	
	// observables
	self.text = ko.observable("");
	self.trainSpam = ko.observable(false);
	self.trainHam = ko.observable(false);
	self.probability = ko.observable("");
	self.frequencies = ko.observableArray();
	self.isLoaded = ko.observable(false);
	
})(ko, toastr, admin.serverProxy);

ko.applyBindings(window.user.editorViewModel);