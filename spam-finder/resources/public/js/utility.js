window.admin.utility = (function() {
	var rebuildObservableArray;
	
	rebuildObservableArray = function (observableArray, serverArray, ItemFactory) {
		var newArray = [];
		$.each(serverArray, function (index) {
			newArray.push(new ItemFactory(this, index));
		});

		observableArray(newArray);
	};
	
	return {
		rebuildObservableArray: rebuildObservableArray
	};
})();