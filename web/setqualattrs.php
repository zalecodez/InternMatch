<?php

require('./dbconnect.php');
require('./qualattrs.php');
require('./getmatches.php');

if(!isset($_POST['id']) || !isset($_POST['type'])){
    echo "error";
    exit;
}
else{
    $id = $_POST['id'];
    $type = $_POST['type'];
}

if(!isset($_POST['desired']) && !isset($_POST['possessed'])){
    echo "error";
    exit;
}

if(isset($_POST['desired'])){
    $desired = $_POST['desired'];
    removeDesired($db, $type, $id);        
}

if(isset($_POST['possessed'])){
    $possessed = $_POST['possessed'];
    removePossessed($db, $type, $id);        
}

if(isset($desired)){
    if(!empty($desired))
        setDesired($db, $type, $id, $desired);
}

if(isset($possessed)){
    if(!empty($possessed))
        setPossessed($db, $type, $id, $possessed);
}

$suggestions = "";
calculateSuggestion($db, $id, $type);
$suggestions = getSuggestions($db, $id, $type);

echo json_encode(array("suggestions"=>$suggestions));

?>
