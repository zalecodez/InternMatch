<?php

require('./dbconnect.php');
require('qualattrs.php');

$email = trim($_POST['email']);
$password = trim($_POST['password']);

if(!$email || !$password){
    echo "error";
    exit;
}

$query = "select id from emails where email=?";
$stmnt = $db->prepare($query);
$stmnt->bind_param("s",$email);
$stmnt->bind_result($id);
$stmnt->execute();

$stmnt->fetch();

if($id){
    echo "exists";
    exit;
}
$stmnt->close();

$usertype = trim($_POST['usertype']);

if($usertype == "student"){
    $usertable = "students"; 
    $query = "insert into students values(NULL,?,password(?),?,?,?,?,?)";

    $fname = trim($_POST['fname']);
    $lname = trim($_POST['lname']);
    $gender = "Other";
    $level = "Unknown";
    $gpa = 0;

    if(isset($gender))
        $gender = trim($_POST['gender']);
    if(isset($level))
        $level = trim($_POST['level']);
    if(isset($gpa))
        $gpa = trim($_POST['gpa']);
    

    if(!$fname || !$lname || !$gender || !$level || !$gpa){
        echo "error";
        exit;
    }
}
elseif($usertype == "company"){
    $query = "insert into companies values(NULL,?,password(?),?)";
    $usertable = "companies"; 

    $name = trim($_POST['name']);
    if(!$name){
        echo "error";
        exit;
    }
}
else{
    echo("false");
    exit;
}
   
$stmnt = $db->prepare($query);


if($usertype == 'student'){
    $stmnt->bind_param("ssssssd",$email, $password, $fname, $lname, $gender, $level, $gpa);
}
else{
    $stmnt->bind_param("sss",$email, $password, $name);
}

$result = $stmnt->execute();

if(!$result){
    echo "error";
    exit;
}


echo "true";

$stmnt->close();
$id=$db->insert_id;

$query = "insert into emails values(?,?,?)";
$stmnt = $db->prepare($query);
$stmnt->bind_param("sis",$email,$id,$usertype);
$stmnt->execute();
$stmnt->close();


if(isset($_POST['qualities']))
    $qualities = $_POST['qualities'];

if(isset($_POST['attributes']))
    $attributes = $_POST['attributes'];


if(isset($qualities)){
    if($usertype == "student")
        setPossessed($db, $usertype, $id, $qualities);
    else
        setDesired($db, $usertype, $id, $qualities);
}

if(isset($attributes)){
    if($usertype == "student")
        setDesired($db, $usertype, $id, $attributes);
    else
        setPossessed($db, $usertype, $id, $attributes);
}


$db->close();


?>
