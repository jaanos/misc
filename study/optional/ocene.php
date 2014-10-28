<?php
if (isset($_POST['submit'])) {
	foreach ($_POST['ocena'] as $pr=>$ocena) {
		mysql_query("REPLACE INTO ocene
					SET predmet = '".$pr."',
					uporabnik = '".$_GET['id']."',
					ocena = '".$ocena."'");
	}
	header("Location: index.php");
	exit;
}

$user = mysql_fetch_assoc(mysql_query("SELECT * FROM uporabniki
									   WHERE id = '".$_GET['id']."'"));
$query = mysql_query("SELECT *, predmeti.id FROM predmeti
					  LEFT JOIN ocene
					  ON predmeti.id = predmet
					  AND uporabnik = '".$_GET['id']."'
					  ".($user['letnik']==3?"WHERE semester = 2 AND pogoj = 0":'')."
					  ORDER BY predmeti.id");
?>
Uporabnik: <?=$user['ime']?>, <?=$user['letnik']?>. letnik<br/><br/>
Oceni predmete po lestvici od 0 do 5:<br/>
0 (ali prazno) - nikakor<br/>
1 - v skrajni sili izberem tega<br/>
2 - vseeno raje ne<br/>
3 - mogoče<br/>
4 - zveni zanimivo<br/>
5 - tega hočem!<br/>
<form action="" method="post">
<?php
while ($row = mysql_fetch_assoc($query)) {
	if ($row['semester'] != $sem) {
		$sem = $row['semester'];
?>
<br/><?=$sem?>. semester:<br/>
<?php
	}
?>
<input type="text" name="ocena[<?=$row['id']?>]" value="<?=$row['ocena']?>" size="4"/>
<?=($row['min']>0?'(*) ':'')?><?=$row['ime']?> (<?=$row['izvajalec']?>)<br/>
<?php
}
?>
<br/><input type="submit" name="submit" value="OK"/>
</form>