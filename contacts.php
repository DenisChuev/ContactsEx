<?php
$user = "ulaffy";
$password = "pass";
$database = "laffy";
$table = "users";


$post = json_decode(file_get_contents('php://input'), true);
$user_id = $_REQUEST['user_id'];

if(json_last_error() == JSON_ERROR_NONE)
{
    $numbers = [];

    foreach($post as $key=>$value){
        $num = $value['nomer_user'];
        array_push($numbers, "'{$num}'");
        #echo "{$value['nomer_user']}: {$value['name']}\n";
    }
    $num_search = implode (", ", $numbers);
    #echo $num_search;


    $connection = mysqli_connect("localhost",$user,$password,$database) or die("Error " . mysqli_error($connection));
    $sql_same_users = "select * from users where nomer_user in ($num_search)";


    $result = mysqli_query($connection, $sql_same_users);
    $emparray = array();
    if ($result->num_rows > 0) {
        while($row = mysqli_fetch_assoc($result))
        {
            $emparray[] = $row;

            $sql_save_same_contacts = "replace into users_contacts values ({$user_id}, {$row['id']})";
            mysqli_query($connection, $sql_save_same_contacts);

        }
    }
    echo json_encode($emparray);
    mysqli_close($connection);

#   print_r($post);
}

echo ""

?>
