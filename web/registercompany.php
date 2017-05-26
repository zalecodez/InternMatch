<html>
<head>
</head>

<body>

<?php

require('../dbconnect.php');

$email = trim($_POST['email']);
$password = trim($_POST['password']);
$fname = trim($_POST['name']);

$query = "insert into companies values(NULL,?,password(?),?)";
$stmnt = $db->prepare($query);
$stmnt->bind_param("ssss",$email, $password, $name);
$result = $stmnt->execute();

if(!$result){
    echo "Error";
    exit;
}

echo $stmnt->affected_rows." user entered";


$stmnt->close();
$db->close();


?>

</body>
</html>
