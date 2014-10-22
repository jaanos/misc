p := 61;

H := [[1,1,1], [0,3,1], [2,0,1]]*Z(p)^0;

# šumniki:
# č -> ~
# š -> {
# ž -> `
# Č -> ^
# Š -> [
# Ž -> @

abc := "abc~defghijklmnopqrs{tuvwxyz`ABC^DEFGHIJKLMNOPQRS[TUVWXYZ@.,-";
s := "GN`@hO`tJDkmBcmxtAsytJFSDGGzXruyzuLqNpi`@ruyzHlauPkrx{sMgnF~BcmxpJnAiNcLGDOm-zy-WOAt`JBDBP`Oz{gEqGjGNO`hgDct^nTDp[[cTzrJyOvo-C{h^xOtJ,HwD{piuxrC[qJfXqsyt.k,m-zDQ^ufL";
# V-QR-kodi-so-podatki-nešifrirani-in-zapisani-v-obliki,-ki-je-posebej-primerna-za-avtomatično-obdelavo,-šifrirano-sporočilo-pa-lahko-prebere-le-tisti,-ki-pozna-ključ.

s2 := "GN`@hO`tJDkmBcmxtAsytJFSDGGzXruyzuLqNpi`@ruyzHlauPkrx{sMgnF~BcmxpJnAiNcLGDOm-zy-W^mUwqFJ@FoQ-tz{IvoqqpDGGzXr`OzTcWGSJGUDoS,kG,ESCqX,ZUkwFvpL~Akg[kRhhgqfzl-xDQ^ufL";

toNumbers := str -> List(str, x -> (Position(abc, x)-1)*Z(p)^0);
toLetters := vec -> List(vec, x -> abc[Int(x)+1]);
toMatrix := vec -> [vec{[1..Size(vec)/3]*3-2}, vec{[1..Size(vec)/3]*3-1}, vec{[1..Size(vec)/3]*3}];
toVector := M -> Concatenation(TransposedMat(M));

encipher := m -> toLetters(toVector(H * toMatrix(toNumbers(m))));
decipher := s -> toLetters(toVector(H^-1 * toMatrix(toNumbers(s))));

decipher(s);
