<?php
require('./dbconnect.php');
require('./qualattrs.php');


$qualities = getAllStudentQualities($db);
$attributes = getAllCompanyAttributes($db);

if(!empty($qualities))
    $qualattrs['qualities'] = $qualities;

if(!empty($attributes))
    $qualattrs['attributes'] = $attributes;

if(empty($qualattrs))
    echo error;
else
    echo json_encode($qualattrs);



?>
