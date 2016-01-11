library(dplyr)
library(gsubfn)
library(reshape2)

sizerace <- function(a, b, n) {
  P <- matrix(NA, nrow = 2*n, ncol = 2*n)
  P[1,1] <- a/(a+b)
  P[1,2] <- 1 - 2*b^2/((a+2*b)*(a+b))
  P[2,1] <- 2*a^2/((2*a+b)*(a+b))
  for (i in 4:(2*n)) {
    P[1,i-1] <- (a + (i-1)*b*P[1,i-2])/(a + (i-1)*b)
    for (j in 2:(i-2)) {
      P[j,i-j] <- (j*a*P[j-1,i-j] + (i-j)*b*P[j,i-j-1])/(j*a + (i-j)*b)
    }
    P[i-1,1] <- ((i-1)*a*P[i-2,1])/((i-1)*a + b)
  }
  return(P)
}

showprobs <- function(P, draw = TRUE) {
  Q <- melt(data.frame(P, x = 1:nrow(P)), id.vars = "x", variable.name = "y",
            na.rm = TRUE)
  Q$y <- Q$y %>% strapplyc("([0-9]+)") %>% as.numeric()
  overy <- apply(P, 1, function(x) which(x > 0.5)[1])
  overy <- overy[!is.na(overy)]
  overx <- 1:length(overy)
  q <- lm(overy ~ overx + I(overx^2))
  if (draw) {
    plot(Q$x, Q$y, pch=16,
         col = ifelse(Q$value > 0.5, 1.5-Q$value, 0.5+Q$value) %>%
           {ifelse(Q$value > 0.5, rgb(., 0, 0), rgb(0, 0, .))})
    points(overx, overy - 1/2, pch = 16, col = "black")
    curve(predict(q, data.frame(overx = x)), col = "green", lwd = 3, add = TRUE)
  }
  return(q)
}

P12 <- sizerace(1, 2, 100)
showprobs(P12)

P23 <- sizerace(2, 3, 100)
showprobs(P23)

P45 <- sizerace(4, 5, 100)
showprobs(P45)

P78 <- sizerace(7, 8, 100)
showprobs(P78)

coeffs <- sapply(1:5, function(x) sapply(1:5, function(y)
  sizerace(x, y, 100) %>% showprobs(draw = FALSE) %>% {.$coefficients[3]}))
