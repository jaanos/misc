<?php
header("Content-type: application/xhtml+xml");
?>
<rss xmlns:a10="http://www.w3.org/2005/Atom" version="2.0">
    <channel>
        <title>xkcd Geohashing</title>
        <link>http://wiki.xkcd.com/geohashing/Main_Page</link>
        <description>XKCD comic #426 contains an algorithm that generates random coordinates across the graticule every day.</description>
    </channel>
<?php
$zoom = 8;
if (isset($_GET['lat'])) {
    $lat = $_GET['lat'];
} else {
    $lat = 46;
}
if (isset($_GET['lon'])) {
    $lon = $_GET['lon'];
} else {
    $lon = 14;
}
$time = time();
if ($lon >= -30) {
    $j = 24*60*60;
} else {
    $j = 0;
}
$i = 0;
$arr = array();
error_reporting(E_ALL & ~E_WARNING & ~E_NOTICE);
while ($dji = file_get_contents('http://carabiner.peeron.com/xkcd/map/data/'.date('Y/m/d', $time+$i-$j))) {
    $arr[date('Y-m-d', $time+$i)] = $dji;
    $i += 24*60*60;
}
$i = 0;
while (count($arr) < 4) {
    $i -= 24*60*60;
    $arr[date('Y-m-d', $time+$i)] = file_get_contents('http://carabiner.peeron.com/xkcd/map/data/'.date('Y/m/d', $time+$i-$j));
}
krsort($arr);
if ($lat < 0) {
    $lat = '-'.abs($lat+1);
}
if ($lon < 0) {
    $lon = '-'.abs($lon+1);
}
foreach($arr as $date=>$dji) {
    $hash = md5($date.'-'.$dji);
    $dlat = 0.0;
    $dlon = 0.0;
    for ($i=0; $i < 16; $i++) {
        $dlat += intval($hash[15-$i], 16);
        $dlon += intval($hash[31-$i], 16);
        $dlat /= 16;
        $dlon /= 16;
    }
    $hlat = $lat.substr($dlat, 1);
    $hlon = $lon.substr($dlon, 1);
    $rlat = round($hlat, 6);
    $rlon = round($hlon, 6);
?>
    <item>
        <link>http://carabiner.peeron.com/xkcd/map/map.html?date=<?=$date?>&amp;lat=<?=$lat?>&amp;long=<?=$lon?>&amp;zoom=<?=$zoom?>&amp;abs=-1</link>
        <title><?=$date?>: <?=$rlat?> <?=$rlon?></title>
        <description>Latitude: <?=$hlat?> &lt;br /&gt;Longitude: <?=$hlon?></description>
        <point xmlns="http://www.georss.org/georss"><?=$hlat?> <?=$hlon?></point>
        <pubDate><?=date("D, M j Y", strtotime($date))?> 15:30:00 +0100</pubDate>
        <guid><?=$hash?></guid>
    </item>
<?php
}
?>
</rss>
