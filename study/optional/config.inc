<?php
/***********************************************************************
Config file - include here all the 'global' varianbles, constants etc..
include this file in every script with include_once
************************************************************************/

// Splosne nastavitve

//$CSELF = 'index.php?UID='.$UID;

$basedir = str_replace('include','',dirname(__FILE__)); // path for includes on the server


$siteURL = 'http://site.url/';
$baseurl = $siteURL;

// database info

$database = 'db';
$dbhost		=	'localhost';
$dbuser		=	'user';
$dbpasswd	=	'password';

$debug = 1;
?>
