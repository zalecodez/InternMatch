<?php

function getStudentMatch(&$db, $id, $query){
    $stmnt = $db->prepare($query);
    $stmnt->bind_param("i", $id);
    $stmnt->bind_result($matchId,$matchName,$matchEmail);
    $stmnt->execute();

    $i = 0;
    while($stmnt->fetch()){
        $match[$i++] = array("id"=>$matchId,"name"=>$matchName, "email"=>$matchEmail);
    }

    $stmnt->close();
    if(isset($match))
        return $match;
    return null;
}

function getCompanyMatch(&$db, $id, $query){
    $stmnt = $db->prepare($query);
    $stmnt->bind_param("i", $id);
    $stmnt->bind_result($matchId,$matchFname, $matchLname, $matchEmail);
    $stmnt->execute();

    $i = 0;
    while($stmnt->fetch())
        $match[$i++] = array("id"=>$matchId,"fname"=>$matchFname,"lname"=>$matchLname, "email"=>$matchEmail);

    $stmnt->close();
    
    if(isset($match))
        return $match;
    return null;
}


function getMatches(&$db, $id, $type){
    if($type == "student"){
        $query = "select companies.cid,companies.name,companies.email
            from students,matches,companies 
            where matches.sid=students.sid
            and matches.cid=companies.cid
            and students.sid=?"; 

        return getStudentMatch($db, $id, $query);
    }
    else{
        $query = "select students.sid,students.fname,students.lname,companies.email
            from students,matches,companies 
            where matches.sid=students.sid
            and matches.cid=companies.cid
            and companies.cid=?"; 

        return getCompanyMatch($db, $id, $query);
    }
}

function getSuggestions(&$db, $id, $type){
    if($type == "student"){
        $query = "select companies.cid,companies.name,companies.email
            from students,suggestions,companies 
            where suggestions.sid=students.sid
            and suggestions.cid=companies.cid
            and students.sid=?"; 

        return getStudentMatch($db, $id, $query);

    }
    else{
        $query = "select students.sid,students.fname,students.lname,students.email
            from students,suggestions,companies 
            where suggestions.sid=students.sid
            and suggestions.cid=companies.cid
            and companies.cid=?"; 

        return getCompanyMatch($db, $id, $query);
    }
}

function getSentRequests(&$db, $id, $type){
    if($type == "student"){
        $query = "select companies.cid,companies.name,companies.email
            from students,studentrequests,companies 
            where studentrequests.sourceid=students.sid
            and studentrequests.targetid=companies.cid
            and students.sid=?"; 

        return getStudentMatch($db, $id, $query);

    }
    else{
        $query = "select students.sid,students.fname,students.lname,students.email
            from students,companyrequests,companies 
            where companyrequests.sourceid=companies.cid
            and companyrequests.targetid=students.sid
            and companies.cid=?"; 

        return getCompanyMatch($db, $id, $query);
    }
}


function getReceivedRequests(&$db, $id, $type){
    if($type == "student"){
        $query = "select companies.cid,companies.name,companies.email
            from students,studentrequests,companies 
            where studentrequests.targetid=students.sid
            and studentrequests.sourceid=companies.cid
            and students.sid=?"; 

        return getStudentMatch($db, $id, $query);

    }
    else{ 
        $query = "select students.sid,students.fname,students.lname,students.email 
            from students,companyrequests,companies 
            where companyrequests.targetid=companies.cid
            and companyrequests.sourceid=students.sid
            and companies.cid=?"; 

        return getCompanyMatch($db, $id, $query);
    }
}

function request(&$db, $source, $target, $type){
    $query = "insert into ".$type."requests values (?,?)";
    $stmnt = $db->prepare($query);
    $stmnt->bind_param("ii",$source,$target);
    $stmnt->execute();
    $stmnt->close();
}

function acceptRequest(&$db, $source, $id, $type){
    if($type == "student"){
        $sid = $id;
        $cid = $source;
    }
    else{
        $sid = $source;
        $cid = $id;
    }

    //future.. verify that request exists
    
    $query = "insert into matches values (?,?)";
    $stmnt = $db->prepare($query);
    $stmnt->bind_param("ii",$cid,$sid);
    $stmnt->execute();
    $stmnt->close();

    $query = "delete from ".$type."requests where sourceid=? and targetid=?";
    $stmnt = $db->prepare($query);
    $stmnt->bind_param("ii",$source,$id);
    $stmnt->execute();
    $stmnt->close();
}

function calculateSuggestion(&$db, $id, $type){

    $insertQuery = "insert into suggestions values(?,?,1.00)";

    if($type == "student"){
        $removeQuery = "delete from suggestions where sid=?";

        /*$selectQuery = 
            "select companies.cid
            from companies,possessedattrs,desiredattrs,students
            where companies.cid=possessedattrs.cid
            and possessedattrs.attrid=desiredattrs.attrid
            and desiredattrs.sid=?";
         */

        $selectQuery = 
            "select companies.cid
            from companies,possessedattrs,desiredattrs,possessedquals,desiredquals
            where companies.cid=possessedattrs.cid
            and possessedattrs.attrid=desiredattrs.attrid
            and desiredattrs.sid=?
            and desiredattrs.sid=possessedquals.sid
            and possessedquals.qualid=desiredquals.qualid
            and desiredquals.cid=companies.cid;
            ";
    }
    else{
        $removeQuery = "delete from suggestions where cid=?";
        /*$selectQuery = 
            "select students.sid
            from students,possessedquals,desiredquals,companies
            where students.sid=possessedquals.sid
            and possessedquals.qualid=desiredquals.qualid
            and desiredquals.cid=?";*/

        $selectQuery = 
            "select students.sid
            from students,possessedquals,desiredquals,possessedattrs,desiredattrs
            where students.sid=possessedquals.sid
            and possessedquals.qualid=desiredquals.qualid
            and desiredquals.cid=possessedattrs.cid
            and possessedattrs.cid=?
            and possessedattrs.attrid=desiredattrs.attrid
            and desiredattrs.sid=students.sid
            ";
    }

    $stmnt = $db->prepare($removeQuery);
    $stmnt->bind_param("i",$id);
    $stmnt->execute();
    $stmnt->close();

    $stmnt = $db->prepare($selectQuery);
    $stmnt->bind_param("i",$id);
    $stmnt->bind_result($matchId);
    $stmnt->execute();

    $i = 0;
    while($stmnt->fetch()){
        $suggestions[$i++] = $matchId;
    }

    $stmnt->close();

    if(isset($suggestions)){
        $stmnt = $db->prepare($insertQuery);

        if($type=="student")
            $stmnt->bind_param("ii",$mId,$id);
        else
            $stmnt->bind_param("ii",$id,$mId);

        foreach($suggestions as $mId){
            $stmnt->execute();
        }
    
        $stmnt->close();

    }
}

?>
