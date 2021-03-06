function shuffle(o) {
	for (var j, x, i = o.length; i; j = Math.floor(Math.random() * i), x = o[--i], o[i] = o[j], o[j] = x);
	return o;
};

function md5_hash(text) {
	var rawHash = Utilities.computeDigest(Utilities.DigestAlgorithm.MD5, text, Utilities.Charset.UTF_8);
	var txtHash = '';
	for (var j = 0; j < rawHash.length; j++) {
		var hashVal = rawHash[j];
		if (hashVal < 0) {
			hashVal += 256;
		}

		if (hashVal.toString(16).length == 1) {
			txtHash += "0";
		}

		txtHash += hashVal.toString(16);
	}

	return txtHash;
}
