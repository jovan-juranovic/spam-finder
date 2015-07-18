window.admin = window.admin || {};
window.user = window.user || {};
toastr.options.closeButton = true;

window.admin.serverProxy = (function() {
	
	return {
		
		getInitData: function(callback) {
			$.getJSON("/users", callback);
		},
		
		getUser: function(uid, callback) {
			$.getJSON("/user", {uid: uid}, callback);
		},
		
		editUser: function(username, firstName, lastName, email, callback) {
			$.ajax({
				dataType: "json",
				type: "POST",
				url: "/user",
				data: {uid: username, first_name: firstName, last_name: lastName, email: email},
				success: callback
			});
		},
		
		removeUser: function(username, callback) {
			$.ajax({
				dataType: "json",
				type: "DELETE",
				url: "/user",
				data: {uid: username},
				success: callback
			});
		},
		
		classifyText: function(text, callback) {
			$.ajax({
				dataType: "json",
				type: "POST",
				url: "/analyze",
				data: {text: text},
				success: callback
			});
		},
		
		trainOnText: function(text, type, callback) {
			$.ajax({
				dataType: "json",
				type: "POST",
				url: "/train",
				data: {val: text, type: type},
				success: callback
			});
		},
		
		getWordCount: function(text ,callback) {
			$.ajax({
				dataType: "json",
				type: "POST",
				url: "/counter",
				data: {text: text},
				success: callback
			});
		},
		
		createUser: function(username, firstName, lastName, email, password, isAdmin, callback) {
			$.ajax({
				dataType: "json",
				type: "POST",
				url: "/create_user",
				data: {uid: username, first_name: firstName, last_name: lastName, email: email, password: password, isAdmin: isAdmin},
				success: callback
			});
		},
		
		getInitUser: function(callback) {
			$.getJSON("/user_profile", callback);
		},
		
		editProfile: function(username, firstName, lastName, email, callback) {
			$.ajax({
				dataType: "json",
				type: "POST",
				url: "/edit_profile",
				data: {uid: username, first_name: firstName, last_name: lastName, email: email},
				success: callback
			});
		},

	};
})();