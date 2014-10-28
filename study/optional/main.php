<?php
function make_price($price)
{
	$price = number_format($price,2,',','.');
	return $price;
}

function color($num,$min) {
	$out = make_price($num);
	if (!$min) return $out;
	return '<font color="'.($num >= $min ? '#007F00' : '#FF0000').'">'.$out.'</font>';
}

function color2($ime,$ocena,$split,$min) {
	if ($min) $ime = '(*) '.$ime;
	if ($ocena > $split) return $ime;
	return '<font color="'.($ocena == $split ? '#FF0000' : '#7F7F7F').'">'.$ime.'</font>';
}

$query = mysql_query("SELECT * FROM uporabniki");
$predmeti = array();
while ($row = mysql_fetch_assoc($query)) {
	$oc_q = mysql_query("SELECT * FROM ocene
						 JOIN predmeti
						 ON predmeti.id = predmet
						 WHERE uporabnik = '".$row['id']."'
						 AND ocena > 0
						 ORDER BY semester, ocena DESC");
	$top = array();
	$mytop = array();
	$max = array(1=>0, 2=>0);
	while ($oc_r = mysql_fetch_assoc($oc_q)) {
		if ($oc_r['ocena'] > $max[$oc_r['semester']]) {
			$max[$oc_r['semester']] = $oc_r['ocena'];
		}
		$top[$oc_r['semester']][$oc_r['ocena']][] = $oc_r;
	}
	foreach ($top as $sem=>$oc) {
		$cnt = 0;
		for ($i=$max[$sem]; $i > 0; $i--) {
			if (is_array($oc[$i])) {
				if (!$row['split'][$sem] && $cnt+count($oc[$i]) > ($row['letnik']==4 ? 3 : 1)) {
					$q = (($row['letnik']==4 ? 3 : 1)-$cnt)/count($oc[$i]);
					$row['split'][$sem] = $i;
				} else $q = 1;
				foreach ($oc[$i] as $pr) {
					$mytop[$sem][$pr['ocena']][] = $pr;
					if ($row['split'][$sem] <= $i) {
						$predmeti[$pr['predmet']]['cnt'] += $q;
						$predmeti[$pr['predmet']]['ocena'] += $pr['ocena']*$q;
					}
				}
				$cnt += count($oc[$i]);
				if ($cnt == ($row['letnik']==4 ? 3 : 1)) $row['split'][$sem] = $i-0.5;
			}
		}
	}
	$row['top'] = $mytop;
	$users[] = $row;
}

$query = mysql_query("SELECT * FROM predmeti
					  ".($_GET['show']=='all'?'':"WHERE min > 0"));
//$ocene = array();
?>

<h2>Predmeti</h2>
<?php
if ($_GET['show']=='all') {
?>
<a href="index.php">Skrij predmete brez minimalnega števila kandidatov</a>
<?php
} else {
?>
<a href="index.php?show=all">Pokaži vse predmete</a>
<?php
}
?>
<table>
<?php
$begin = true;
while ($row = mysql_fetch_assoc($query)) {
	list($row['skupaj'],$row['sk_ocena']) = mysql_fetch_row(mysql_query("SELECT COUNT(*),
																		SUM(ocena)/COUNT(*)
																		FROM ocene
																		WHERE predmet = '".$row['id']."'
																		AND ocena > 0"));
	$row['cnt'] = $predmeti[$row['id']]['cnt'];
	if (!$row['cnt']) {
		$row['cnt'] = 0;
		$row['ocena'] = 0;
	} else {
		$row['ocena'] = $predmeti[$row['id']]['ocena']/$row['cnt'];
	}
	if (!$row['sk_ocena']) $row['sk_ocena'] = 0;
	//$ocene[$row['id']] = $row;
	if ($begin) {
?>
<tr>
<?php
	}
?>
<td>
<?=($row['min']?'(*) ':'')?><?=$row['ime']?> (<?=$row['izvajalec']?>), <?=$row['semester']?>. semester, potrebnih <?=$row['min']?> glasov<br/>
Glasov: <?=color($row['cnt'],$row['min'])?>, ocena: <?=make_price($row['ocena'])?><br/>
Skupaj glasov: <?=$row['skupaj']?>, skupaj ocena: <?=make_price($row['sk_ocena'])?><br/>
</td>
<?php
	if (!$begin) {
?>
<tr>
<?php
	}
	$begin = !$begin;
}
if (!$begin) echo '</tr>';
?>
</table>

<h2>Kandidati</h2>
<table>
<?php
$begin = true;
foreach ($users as $user) {
	if ($begin) {
?>
<tr>
<?php
	}
?>
<td valign="top">
<a href="index.php?page=ocene&id=<?=$user['id']?>"><?=$user['ime']?></a>, <a href="index.php?page=user&id=<?=$user['id']?>"><?=$user['letnik']?>. letnik</a><br/>
<?php
	foreach ($user['top'] as $sem=>$top) {
		if ($user['letnik']+$sem < 5) continue;
		$req = $user['letnik']==4 ? 3 : 1;
?>
<?=$sem?>. semester:
<table>
<?php
		foreach ($top as $oc=>$gr) {
			foreach ($gr as $pr) {
?>
<tr><td><?=color2($pr['ime'],$oc,$user['split'][$sem],$pr['min'])?> (<?=$pr['izvajalec']?>), ocena: <?=$oc?></td></tr>
<?php
				if (!$pr['min']) $req--;
			}
			if ($req <= 0) break;
		}
?>
</table>
<?php
	}
?>
</td>
<?php
	if (!$begin) {
?>
</tr>
<?php
	}
	$begin = !$begin;
}
if (!$begin) echo '</tr>';
?>
</table>

<a href="index.php?page=user">Nov uporabnik</a>