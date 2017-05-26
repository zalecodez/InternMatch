<?php
try{
    $db = new mysqli('localhost', 'web', 'yungZmissY~Q', 'internmatch');
}catch(Exception $e){
    echo "Error, cannot connect to database. Please try again later";
    echo $e;
    exit;
}
?>
