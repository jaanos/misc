#include <stdio.h>

int to_naf(int x) {
	int mask = 1;
	int carry = 0;
	while (mask) {
		x ^= carry;
		if (x & mask) {
			mask <<= 1;
			carry = x & mask;
		}
		mask <<= 1;
		carry <<= 1;
	}
	return x;
}

int from_naf(int x) {
	int mask = 1;
	int carry = 0;
	while (mask) {
		x ^= carry;
		if (carry ^ x & mask) {
			mask <<= 1;
			carry = x & mask;
		}
		mask <<= 1;
		carry <<= 1; 
	}
	return x;
}

int add_naf(int x, int y) {
	int z = 0;
	int mask = 1;
	int tmask = 3;
	int c = 0;
	int p = 0;
	int dx, dy, q;
	while (mask) {
		dx = x & tmask;
		dy = y & tmask;
		dx &= (dx << 1) | mask;
		dy &= (dy << 1) | mask;
		q = dx + dy + c;
		if (q & mask) {
			if (p) {
				z ^= mask;
				if (q == (p << 1)) {
					c = q << 1;
				} else {
					c = 0;
				}
				p = 0;
			} else {
				z |= q;
				p = q;
				c = 0;
			}
		} else {
			c = q;
			c &= (c << 1) | tmask;
			p = 0;
		}
		dx &= mask;
		dy &= mask;
		mask <<= 1;
		tmask <<= 1;
		if (dx) {
			x &= ~mask;
		}
		if (dy) {
			y &= ~mask;
		}
	}
	return z;
}

int main() {
	int a = 38094;
	int b = to_naf(a);
	int c = from_naf(b);
	int d = 12345;
	int e = to_naf(d);
	int f = from_naf(e);
	int g = add_naf(b, e);
	int h = from_naf(g);
	printf("%X -> %X -> %X\n", a, b, c);
	printf("%X -> %X -> %X\n", d, e, f);
	printf("%d + %d -> %X + %X = %X -> %d = %d\n", a, d, b, e, g, h, a+d);
	return 0;
}
