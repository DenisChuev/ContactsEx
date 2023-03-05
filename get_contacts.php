<?php
$user = "ulaffy";
$password = "pass";
$database = "laffy";
$table = "users";

$connection = mysqli_connect("localhost",$user,$password,$database) or die("Error " . mysqli_error($connection));

$sql = "select * from users";
$result = mysqli_query($connection, $sql) or die("Error in Selecting " . mysqli_error($connection));
$emparray = array();

while($row =mysqli_fetch_assoc($result))
    {
        $emparray[] = $row;
    }
echo json_encode($emparray);

mysqli_close($connection);
?>

