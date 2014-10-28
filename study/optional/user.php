<?php
if (isset($_POST['submit'])) {
	if (isset($_GET['id'])) {
		$id = $_GET['id'];
	} else {
		mysql_query("INSERT INTO uporabniki SET id=''");
		$id = mysql_insert_id();
	}
	
	mysql_query("UPDATE uporabniki SET
				ime = '".$_POST['ime']."',
				letnik = '".$_POST['letnik']."'
				WHERE id = '".$id."'");
	header("Location: index.php?page=ocene&id=".$id);
	exit;
} else if (isset($_POST['delete'])) {
	mysql_query("DELETE FROM uporabniki
				WHERE id = '".$id."'");
	mysql_query("DELETE FROM ocene
				WHERE uporabnik = '".$id."'");
	header("Location: index.php");
	exit;
}

if (isset($_GET['id'])) {
	list($ime,$letnik) = mysql_fetch_row(mysql_query("SELECT ime, letnik
													  FROM uporabniki
													  WHERE id = '".$_GET['id']."'"));
}
?>
<form action="" method="post">
Ime: <input type="text" name="ime" value="<?=$ime?>"/><br/>
Letnik: <input type="radio" name="letnik" value="3" <?=($letnik==3?'checked':'')?>/> 3.
<input type="radio" name="letnik" value="4" <?=($letnik==4?'checked':'')?>/> 4. <br/>
<input type="submit" name="submit" value="OK"/>
<?php
if (isset($_GET['id'])) {
?>
<input type="submit" name="delete" value="Izbriši"/>
<?php
}
?>
</form>