-- phpMyAdmin SQL Dump
-- version 2.6.4
-- http://www.phpmyadmin.net
-- 

-- --------------------------------------------------------

-- 
-- Struktura tabele `ocene`
-- 

CREATE TABLE `ocene` (
  `id` smallint(6) unsigned NOT NULL auto_increment,
  `predmet` tinyint(3) unsigned NOT NULL,
  `uporabnik` smallint(6) unsigned NOT NULL,
  `ocena` tinyint(1) unsigned NOT NULL,
  PRIMARY KEY  (`id`),
  UNIQUE KEY `predmet` (`predmet`,`uporabnik`)
) ENGINE=MyISAM AUTO_INCREMENT=1992 DEFAULT CHARSET=utf8 COLLATE=utf8_slovenian_ci;

-- --------------------------------------------------------

-- 
-- Struktura tabele `predmeti`
-- 

CREATE TABLE `predmeti` (
  `id` tinyint(3) unsigned NOT NULL auto_increment,
  `ime` varchar(120) collate utf8_slovenian_ci NOT NULL,
  `izvajalec` varchar(60) collate utf8_slovenian_ci NOT NULL,
  `semester` tinyint(1) unsigned NOT NULL,
  `pogoj` tinyint(3) unsigned NOT NULL,
  `min` tinyint(3) unsigned NOT NULL,
  PRIMARY KEY  (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=27 DEFAULT CHARSET=utf8 COLLATE=utf8_slovenian_ci;

-- --------------------------------------------------------

-- 
-- Struktura tabele `uporabniki`
-- 

CREATE TABLE `uporabniki` (
  `id` smallint(6) unsigned NOT NULL auto_increment,
  `ime` varchar(120) collate utf8_slovenian_ci NOT NULL,
  `letnik` tinyint(1) unsigned NOT NULL,
  PRIMARY KEY  (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=27 DEFAULT CHARSET=utf8 COLLATE=utf8_slovenian_ci;
