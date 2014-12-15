x <- c(0, 0, 3, 3, 5, 5, 6, 6)
y <- c(-3, 3, 1, -1, 1, -1, 1, -1)
xn <- c(-1, 1, 1, -1, 0, 0, -1, 1)
yn <- c(1, -1, 0, 0, 1, -1, -1, 1)
lbls <- paste0("(", xn, ", ", yn, ")")
cols <- rainbow(8)

plot(c(-0.5, 6.5), c(-3.5, 3.5), "n",
     main="y^2 = x^3 + 2 (mod 7), G = (Z_3)^2",
     xlab = "x", ylab = "y")

abline(h=c(-1, 1), col=cols[1:2])

abline(a=-3, b=-1, col=cols[3])
abline(a=4, b=-1, col=cols[3])

abline(a=3, b=1, col=cols[4])
abline(a=-4, b=1, col=cols[4])

abline(a=3, b=-2/3, col=cols[5])
abline(a=-5/3, b=-2/3, col=cols[5])
abline(a=2/3, b=-2/3, col=cols[5])

abline(a=-3, b=2/3, col=cols[6])
abline(a=5/3, b=2/3, col=cols[6])
abline(a=-2/3, b=2/3, col=cols[6])

abline(a=11, b=-2, col=cols[7])
abline(a=4, b=-2, col=cols[7])
abline(a=-3, b=-2, col=cols[7])

abline(a=-11, b=2, col=cols[8])
abline(a=-4, b=2, col=cols[8])
abline(a=3, b=2, col=cols[8])

points(x, y, pch=16)

text(x, y, lbls, pos = c(3, 1), cex=0.7)
