<?php
include('../include/config.inc');
header("Content-type: text/html; charset=utf-8");

$page = $_GET['page'];
if (!$page || $page == 'index') $page = 'main';

ob_start();
include($page.'.php');
$contents = ob_get_contents();
ob_end_clean();

?>
<html>
<head>
<title>Izbirni predmeti</title>
<meta http-equiv="Content-type" content="text/html; charset=utf-8">
</head>
<body>
<?=$contents?>
</body>
</html>