source("uvozi.zemljevid.r")

bisector.th <- function(ph1, th1, ph2, th2) {
  cth1 <- cospi(th1/180)
  cth2 <- cospi(th2/180)
  sth <- sinpi(th2/180) - sinpi(th1/180)
  return(function(ph) {
    180*atan((cospi((ph1-ph)/180)*cth1 - cospi((ph2-ph)/180)*cth2)/sth)/pi
  })
}

capitals <- read.csv2("capitals.csv", row.names = 1)

svet <- uvozi.zemljevid("http://www.naturalearthdata.com/http//www.naturalearthdata.com/download/110m/cultural/ne_110m_admin_0_countries.zip",
                             "ne_110m_admin_0_countries", encoding = "Windows-1252")

divide.world <- function(c1, c2, from = -180, to = 180, points = 1000, axes = FALSE,
                         xlim=c(from, to), ylim=c(-90, 90), add = FALSE, col = "red") {
  if (!add) {
    plot(svet, xlim=xlim, ylim=ylim, axes=axes)
  }
  x <- seq(from, to, (to-from)/(points-1))
  y <- bisector.th(capitals[c1,"Longitude"], capitals[c1,"Latitude"],
                   capitals[c2,"Longitude"], capitals[c2,"Latitude"])(x)
  lines(x, y, col = col)
}
