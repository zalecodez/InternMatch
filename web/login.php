<?php

require('./dbconnect.php');
require('getmatches.php');
require('qualattrs.php');

$user = array();

$email = trim($_POST['email']);
$password = trim($_POST['password']);

if(!get_magic_quotes_gpc()){
    $email = addslashes($email);
    $password=addslashes($password);
}

$query = "select id,usertype from emails where email=?";
$stmnt = $db->prepare($query);
if(!$stmnt){
    echo "error";
    exit;
}
$stmnt->bind_param("s",$email);
$stmnt->bind_result($id,$user['type']);
$stmnt->execute();

$stmnt->fetch();

if($user['type'] == "student")
    $usertable="students";
else
    $usertable="companies";

$stmnt->close();

$query = "select * from $usertable where email=? and password=password(?)";

$stmnt = $db->prepare($query);

if(!$stmnt){
    echo "error";
    exit;
}

$stmnt->bind_param("ss",$email,$password);

if($user['type'] == "student")
    $stmnt->bind_result($user['id'], $user['email'], $user['password'], $user['fname'], $user['lname'], $user['gender'], $user['level'], $user['gpa']);
else
    $stmnt->bind_result($user['id'], $user['email'], $user['password'], $user['name']);

$result = $stmnt->execute();

if(!$result){
    echo "error";
    exit;
}

if(!$stmnt->fetch()){
    echo "null";
    exit;
}
$stmnt->close();

$possessed = getPossessed($db, $user['type'],$user['id']);
if($possessed)
    $user['possessed']=$possessed;

$desired = getDesired($db,$user['type'],$user['id']);
if($desired)
    $user['desired']=$desired;

$match = getMatches($db,$user['id'],$user['type']);
if($match)
    $user['matches'] = $match;

calculateSuggestion($db, $user['id'],$user['type']);
$match = getSuggestions($db,$user['id'],$user['type']);
if($match)
    $user['suggestions'] = $match;

$match = getSentRequests($db,$user['id'],$user['type']);
if($match)
    $user['sentRequests'] = $match;

$match = getReceivedRequests($db,$user['id'],$user['type']);
if($match)
    $user['receivedRequests'] = $match;


echo json_encode($user);

$db->close();
?>
