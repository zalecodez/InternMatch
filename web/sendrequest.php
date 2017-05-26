<?php

require('./dbconnect.php');
require('getmatches.php');

$source = trim($_POST['source']);
$target = trim($_POST['target']);
$id = trim($_POST['id']);

if(!isset($id) || !isset($target) || !isset($source)){
    echo "error";
    exit;

try{
    request($db, $source, $target, $type);
}catch(Exception e){
    echo error;
    exit;
}

echo "true";

?>
