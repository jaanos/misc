# NAF

Converts between ordinary binary representation and NAF (non-adjacent form) of 32-bit integers.

NAF is a signed binary representation of integers (i.e., using digits `0`, `1`, and `-1`) such that no nonzero digits are adjacent in the representation. Each integer has a unique NAF representation. NAF can be used to improve efficiency of square-and-multiply type algorithms in the cases when the multiply operation and its inverse have similar complexity (for example, in elliptic curves).

The NAF form is represented as a sequence of bits. When read from the least significant bit to the most significant, the subsequences `01` and `11` represent NAF sequences `0,1` and `0,-1`, respectively, while the remaining zeros are zeros of NAF. The least significant bits here are the rightmost ones. If a `1` most significant bit remains unpaired, it should be understood as `01` - i.e., it corresponds to the NAF digit `1`.

Written in February 2009.
