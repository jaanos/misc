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

int main() {
	int a = 38094;
	int b = to_naf(a);
	int c = from_naf(b);
	printf("%X -> %X -> %X\n", a, b, c);
	return 0;
}
