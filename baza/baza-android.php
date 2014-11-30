<html>
<body>
<?php
$servername = "localhost";
$username = "";
$password = "";
$dbname = "test";
$login=$_POST["id"];
$haslo=$_POST["pass"];

// Create connection
$conn = new mysqli($servername, $username, $password, $dbname);

// Check connection
if ($conn->connect_error) {
    die("Connection failed: " . $conn->connect_error);
} 

$sql = "SELECT pass FROM log where id=$login";
$result = $conn->query($sql);

if ($result->num_rows == 1) {
    // output data of each row
    while($row = $result->fetch_assoc()) {
        if($row["pass"]==$haslo){
			echo "dupa";
		}
		else{
			echo "przykro";
		}
    }
} else {
    echo "0 results";
}
$conn->close();
?>

</body>
</html>