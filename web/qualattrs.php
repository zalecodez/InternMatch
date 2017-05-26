<?php

function removeDesired(&$db, $type, $id){
    if($type == "student")
        $query = "delete from desiredattrs where sid = ?";
    else
        $query = "delete from desiredquals where cid = ?";
    $stmnt = $db->prepare($query);
    $stmnt->bind_param("i",$id);
    $stmnt->execute();
    $stmnt->close();
}


function removePossessed(&$db, $type, $id){
    if($type == "student")
        $query = "delete from possessedquals where sid = ?";
    else
        $query = "delete from possessedattrs where cid = ?";
    $stmnt = $db->prepare($query);
    $stmnt->bind_param("i",$id);
    $stmnt->execute();
    $stmnt->close();
}

function setPossessed(&$db, $type, $id, $list){

    if($type == "student")
        $query = "insert into possessedquals values (?,?)";
    else
        $query = "insert into possessedattrs values (?,?)";

    $stmnt = $db->prepare($query);

    foreach($list as $i){
        $stmnt->bind_param("ii",$i, $id);
        $stmnt->execute();
    }
    $stmnt->close();
}



function setDesired(&$db, $type, $id, $list){

    if($type == "student")
        $query = "insert into desiredattrs values (?,?)";
    else
        $query = "insert into desiredquals values (?,?)";

    $stmnt = $db->prepare($query);

    foreach($list as $i){
        $stmnt->bind_param("ii",$i, $id);
        $stmnt->execute();
    }
    $stmnt->close();
}


function getPossessed(&$db, $type, $id){

    if($type == "student"){
        $query = "select studentquals.qualid,studentquals.qualname 
            from students,possessedquals,studentquals
            where students.sid=?
            and students.sid=possessedquals.sid
            and possessedquals.qualid=studentquals.qualid";
    }
    else{
        $query = "select compattrs.attrid,compattrs.attrname 
            from companies,possessedattrs,compattrs
            where companies.cid=?
            and companies.cid=possessedattrs.cid
            and possessedattrs.attrid=compattrs.attrid";
    }
    $stmnt = $db->prepare($query);
    $stmnt->bind_param("i",$id);
    $stmnt->bind_result($possessedId, $possessedName);
    $stmnt->execute();

    $i = 0;
    while($stmnt->fetch()){
        $possessed[$i]['id']=$possessedId;
        $possessed[$i]['name']=$possessedName;
        $i++;
    }

    $stmnt->close();

    if(!isset($possessed))
        return null;
    return $possessed;
}

function getDesired(&$db, $type, $id){

    if($type == "company"){
        $query = "select studentquals.qualid,studentquals.qualname 
            from companies,desiredquals,studentquals
            where companies.cid=?
            and companies.cid=desiredquals.cid
            and desiredquals.qualid=studentquals.qualid";
    }
    else{
        $query = "select compattrs.attrid,compattrs.attrname 
            from students,desiredattrs,compattrs
            where students.sid=?
            and students.sid=desiredattrs.sid
            and desiredattrs.attrid=compattrs.attrid";
    }
    $stmnt = $db->prepare($query);
    $stmnt->bind_param("i",$id);
    $stmnt->bind_result($desiredId, $desiredName);
    $stmnt->execute();

    $i = 0;
    while($stmnt->fetch()){
        $desired[$i]['id']=$desiredId;
        $desired[$i]['name']=$desiredName;
        $i++;
    }
    $stmnt->close();

    if(!isset($desired))
        return null;
    return $desired;
}


function getAllStudentQualities(&$db){

    $query = "select * from studentquals"; 
    $stmnt = $db->prepare($query);
    $stmnt->bind_result($QualId, $QualName);
    $stmnt->execute();

    $i = 0;
    while($stmnt->fetch()){
        $qualities[$i]['id']=$QualId;
        $qualities[$i]['name']=$QualName;
        $i++;
    }
    $stmnt->close();

    if(!isset($qualities))
        return null;
    return $qualities;
}


function getAllCompanyAttributes(&$db){

    $query = "select * from compattrs"; 
    $stmnt = $db->prepare($query);
    $stmnt->bind_result($AttrId, $AttrName);
    $stmnt->execute();

    $i = 0;
    while($stmnt->fetch()){
        $attributes[$i]['id']=$AttrId;
        $attributes[$i]['name']=$AttrName;
        $i++;
    }
    $stmnt->close();

    if(!isset($attributes))
        return null;
    return $attributes;
}

?>
