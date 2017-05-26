<html>
<head></head>
<body>

<h1>Register</h1>
<form action="registeruser.php" method='post'>
    <table border='0';>
        <tr>
            <td>User Type:</td>
            <td>
                <select name="usertype">
                    <option value="student">Student</option>
                    <option value="company">Company</option>
                </select>
            </td>
        </tr>

        <tr>
            <td>Company Name:</td>
            <td>
                <input name='name' type='text' size='50'>
            </td>
        </tr>


        <tr>
            <td>First Name:</td>
            <td>
                <input name='fname' type='text' size='50'>
            </td>
        </tr>

        <tr>
            <td> Last Name:</td>
            <td>
                <input name='lname' type='text' size='50'>
            </td>
        </tr>
        <tr>
            <td>Email:</td>
            <td>
                <input name='email' type='email'/>
            </td>
        </tr>
        <tr>
            <td>Password:</td>
            <td>
                <input name='password' type='password'/>
            </td>
        </tr>

        <tr>
            <td>Gender:</td>
            <td>
                <input name='gender' type='text'/>
            </td>
        </tr>


        <tr>
            <td>Level:</td>
            <td>
                <input name='level' type='text'/>
            </td>
        </tr>


        <tr>
            <td>Gpa:</td>
            <td>
                <input name='gpa' type='text'/>
            </td>
        </tr>
<?php
require('./dbconnect.php');
require('qualattrs.php');

$qualities = getAllStudentQualities($db);
$attributes = getAllCompanyAttributes($db);

if($qualities){
    echo "<tr>\n
        <td>Student Qualities:</td>\n
        <td>\n";
    $i = 0;
    foreach($qualities as $qual){
        echo "<input type='checkbox' name='qualities[$i]' 
            value='".($qual['id'])."'/>".$qual['name']."<br/>";
        $i++;
    }
    echo "<br/>";

    echo "</td>\n</tr>\n";
}

if($attributes){
    echo "<tr>\n
        <td>Company Attributes:</td>\n
        <td>\n";
    $i = 0;
    foreach($attributes as $attr){
        echo "<input type='checkbox' name='attributes[$i]' 
            value='".($attr['id'])."'/>".$attr['name']."<br/>";
        $i++;
    }

    echo "</td>\n</tr>\n";
}
?>
        <tr>
            <td colspan='2'>
                <input type='submit' value='Register'/>
            </td>
        </tr>

</form>

</body>

</html>
