function rev(out, addr, i, j) {
	if (i == 0) {
		return out;
	}
	if (addr[i] == "") {
		out = out "0.0.0.0."
		if (j > i) {
			i = i+1;
		}
	} else {
		k = split(addr[i], b, "");
		h = k;
		while (h > 0) {
			out = out b[h] ".";
			h = h-1;
		}
		while (k < 4) {
			out = out "0.";
			k = k+1;
		}
	}
	return rev(out, addr, i-1, j-1);
}

function rev6(addr) {
	i = split(addr, c, ":");
	return rev("", c, i, 8);
}
