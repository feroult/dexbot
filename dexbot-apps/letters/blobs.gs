var Blobs = (function() {

	var blobFunctions = {};

	function register(blobKey, blobFunction) {
		blobFunctions[blobKey] = blobFunction;
	}

	function fromFunction(blobKey) {
		if (!blobFunctions[blobKey]) {
			return null;
		}

		return blobFunctions[blobKey]();
	}

	function fromUrl(url) {
		return UrlFetchApp.fetch(url).getBlob();
	}

	return {
		register : register,
		fromFunction : fromFunction,
		fromUrl : fromUrl
	}

})();