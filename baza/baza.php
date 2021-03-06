<?php
/**
 * File to handle all API requests
 * Accepts GET and POST
 * 
 * Each request will be identified by TAG
 * Response will be JSON data
 
  /**
 * check for POST request 
 */
 

 
if (isset($_POST['tag']) && $_POST['tag'] != '') {
    // get tag
    $tag = $_POST['tag'];
 
    // include db handler
    require_once 'DB_Functions.php';
    $db = new DB_Functions();
 
    // response Array
    $response = array("tag" => $tag, "success" => 0, "error" => 0);
 
    // check for tag type
    if ($tag == "login") { //login wporzo
        // Request type is check Login
		session_start();
        $id = $_POST['id'];
        $password = $_POST['pass'];
		$ident = $_POST['ident'];
 
        // check for user
        $user = $db->getUserByIdAndPassword($id, $password, $ident);
        if ($user != false) {
            // user found
            // echo json with success = 1
            $response["success"] = 1;
			$response["sid"] = session_id();
            $response["uid"] = $user["unique_id"];
            $response["user"]["ident"] = $user["ident"];
            $response["user"]["id"] = $user["id"];
            $response["user"]["created_at"] = $user["created_at"];
            $response["user"]["updated_at"] = $user["updated_at"];
			if (!isset($_SESSION['zalogowany'])) //k
			{ //k
				$_SESSION['zalogowany'] = $user["unique_id"];//k
				$_SESSION['ip'] = $_SERVER['REMOTE_ADDR'];//k
			} //k
            echo json_encode($response);
        } else {
            // user not found
            // echo json with error = 1
            $response["error"] = 1;
            $response["error_msg"] = "Incorrect email or password!";
            echo json_encode($response);
			//echo ($przykro);
        }
    } else if ($tag == 'register') { // OK DODAWANIE USEROW DZIALA
        // Request type is Register new user
        $ident = $_POST['ident'];
        $id = $_POST['id'];
        $password = $_POST['pass'];
 
        // check if user is already existed
        if ($db->isUserExisted($id)) {
            // user is already existed - error response
            $response["error"] = 2;
            $response["error_msg"] = "User already existed";
            echo json_encode($response);
        } else {
            // store user
            $user = $db->storeUser($ident, $id, $password);
            if ($user) {
                // user stored successfully
                $response["success"] = 1;
                $response["uid"] = $user["unique_id"];
                $response["user"]["ident"] = $user["ident"];
                $response["user"]["id"] = $user["id"];
                $response["user"]["created_at"] = $user["created_at"];
                $response["user"]["updated_at"] = $user["updated_at"];
                echo json_encode($response);
            } else {
                // user failed to store
                $response["error"] = 1;
                $response["error_msg"] = "Error occured in Registartion";
                echo json_encode($response);
            }
        }
    }
	else if ($tag == 'dane_ogolne'){
		$uid=$_POST['uid'];
		$sid=$_POST['sid'];
		if($_SESSION['ip'] == $_SERVER['REMOTE_ADDR'] && $_SESSION['zalogowany'] == $uid){
			$data = $db->getPatientDataById($uid);
			if ($data != false) {
				$response["success"] = 1;
                $response["imie"] = $data["imie"];
                $response["nazwisko"] = $data["nazwisko"];
				$response["uraz_nazwa"] = $data["nazwa"];
				$response["eid"] = $data["eid"];
				$response["dlugosc"] = $data["dlugosc"];
				$response["postep"] = $data["postep"];
                echo json_encode($response);
			}
			else{
				$response["error"] = 1;
				$response["error_msg"] = "Blad! Dane w bazie nie zostaly uzupelnione";
				echo json_encode($response);
			}
		}
		else{
			echo "Zostales wylogowany. Zaloguj sie ponownie";
		}
		
	}
		
	else if ($tag == 'dzienny_plan'){
		$uid=$_POST['uid'];
		$sid=$_POST['sid'];
		session_id($_POST['sid']);
		session_start(); 
		if($_SESSION['ip'] == $_SERVER['REMOTE_ADDR'] && $_SESSION['zalogowany'] == $uid){
			$dane = $db->getTodayPlan($uid);
			if ($dane != false && $dane['namba'] == 1) {
				$dane=$dane['wynik'];
				$response["success"] = 1;
                $response["nazwa"] = $dane["nazwa"];
                $response["serie"] = $dane["serie"];
				$response["powtorzenia"] = $dane["powtorzenia"];
				$response["link"] = $dane["link"];
                echo json_encode($response);
			}
			else if ($dane != false && $dane['namba'] > 1) {
				$tmp=0;
				$stringjson="";
				while($tmp < $dane['namba']){
					$dane2 = $dane['wynik'][$tmp];
					$response["success"] = 1;
					$response["nazwa"] = $dane2["nazwa"];
					$response["serie"] = $dane2["serie"];
					$response["powtorzenia"] = $dane2["powtorzenia"];
					$response["link"] = $dane2["link"];
					$jason=(string)json_encode($response);
					$stringjson = $stringjson . $jason . "-";
					$tmp = $tmp + 1;
				}
				echo $stringjson;
			}
			else{
				$response["success"] = 0;
				$response["error"] = 1;
				$response["error_msg"] = "Blad! Dane niedostepne";
				echo json_encode($response);
			}
		}
	}
	
	else if ($tag == 'jutrzejszy_plan'){
		$uid=$_POST['uid'];
		$sid=$_POST['sid'];
		session_id($_POST['sid']);
		session_start();
		if($_SESSION['ip'] == $_SERVER['REMOTE_ADDR'] && $_SESSION['zalogowany'] == $uid){
			$dane = $db->getTommorowPlan($uid);
			if ($dane != false && $dane['namba'] == 1) {
				$dane=$dane['wynik'];
				$response["success"] = 1;
                $response["nazwa"] = $dane["nazwa"];
                $response["serie"] = $dane["serie"];
				$response["powtorzenia"] = $dane["powtorzenia"];
				$response["link"] = $dane["link"];
                echo json_encode($response);
			}
			else if ($dane != false && $dane['namba'] > 1) {
				$tmp=0;
				$stringjson="";
				while($tmp < $dane['namba']){
					$dane2 = $dane['wynik'][$tmp];
					$response["success"] = 1;
					$response["nazwa"] = $dane2["nazwa"];
					$response["serie"] = $dane2["serie"];
					$response["powtorzenia"] = $dane2["powtorzenia"];
					$response["link"] = $dane2["link"];
					$jason=(string)json_encode($response);
					$stringjson = $stringjson . $jason . "-";
					$tmp = $tmp + 1;
				}
				echo $stringjson;
			}
			else{
				$response["success"] = 0;
				$response["error"] = 1;
				$response["error_msg"] = "Blad! Dane niedostepne";
				echo json_encode($response);
			}
		}
	}
	
	else if ($tag == 'week_plan'){
		$uid=$_POST['uid'];
		$sid=$_POST['sid'];
		session_id($_POST['sid']);
		session_start();
		if($_SESSION['ip'] == $_SERVER['REMOTE_ADDR'] && $_SESSION['zalogowany'] == $uid){
			$dane = $db->getWeekPlan($uid);
			if ($dane != false && $dane['namba'] >= 1) {
				$tmp=0;
				$response["success"] = 1;
				$stringjson=(string)json_encode($response);
				$previous_data="";
				while($tmp < $dane['namba']){
					$dane2 = $dane['wynik'][$tmp];
					$response["nazwa"] = $dane2["nazwa"];
					$response["serie"] = $dane2["serie"];
					$response["powtorzenia"] = $dane2["powtorzenia"];
					$jason=(string)json_encode($response);
					if($dane2["dzien_leczenia"] != $previous_data){
						$stringjson = $stringjson . "--" . $jason;
						$previous_data = $dane2["dzien_leczenia"];
					}
					else{
						$stringjson =  $stringjson . "-" . $jason;
					}
					$tmp = $tmp + 1;
				}
				echo $stringjson;
			}
			else{
				$response["success"] = 0;
				$response["error"] = 1;
				$response["error_msg"] = "Blad! Dane niedostepne";
				echo json_encode($response);
			}
		}
	}
	
	else if ($tag == 'visit_plan'){
		$uid=$_POST['uid'];
		$sid=$_POST['sid'];
		session_id($_POST['sid']);
		session_start();
		if($_SESSION['ip'] == $_SERVER['REMOTE_ADDR'] && $_SESSION['zalogowany'] == $uid){
			$dane = $db->getVisitPlan($uid);
			if ($dane != false && $dane['namba'] >= 1) {
				$tmp=0;
				$response["success"] = 1;
				$stringjson=(string)json_encode($response);
				while($tmp < $dane['namba']){
					$dane2 = $dane['wynik'][$tmp];
					$response1["data"] = $dane2["data"];
					$response1["uwagi"] = $dane2["uwagi"];
					$jason=(string)json_encode($response1);
					$stringjson = $stringjson . "--" . $jason;
					$tmp = $tmp + 1;
				}
				echo $stringjson;
			}
			else{
				$response["success"] = 0;
				$response["error"] = 1;
				$response["error_msg"] = "Blad! Dane niedostepne";
				echo json_encode($response);
			}
		}
	}
	
	else if ($tag == 'message_box'){
		$uid=$_POST['uid'];
		$page_nr=$_POST['page_nr'];
		$sid=$_POST['sid'];
		session_id($_POST['sid']);
		session_start();
		if($_SESSION['ip'] == $_SERVER['REMOTE_ADDR'] && $_SESSION['zalogowany'] == $uid){
			$dane = $db->getMessageBox($uid, $page_nr);
			if ($dane != false && $dane['namba'] >= 1) {
				$tmp=0;
				$response["success"] = 1;
				$stringjson=(string)json_encode($response);
				while($tmp < $dane['namba']){
					$dane2 = $dane['wynik'][$tmp];
					$response1["tytul"] = $dane2["tytul"];
					$response1["data"] = $dane2["data"];
					$jason=(string)json_encode($response1);
					$stringjson = $stringjson . "--" . $jason;
					$tmp = $tmp + 1;
				}
				echo $stringjson;
			}
			else{
				$response["success"] = 0;
				$response["error"] = 1;
				$response["error_msg"] = "Blad! Dane niedostepne";
				echo json_encode($response);
			}
		}
	}
	
	else if ($tag == 'messages_by_title'){
		$uid = $_POST['uid'];
		$title = $_POST['title'];
		$sid = $_POST['sid'];
		session_id($_POST['sid']);
		session_start();
		if($_SESSION['ip'] == $_SERVER['REMOTE_ADDR'] && $_SESSION['zalogowany'] == $uid){
			$dane = $db->getMessagesByTitle($uid, $title);
			if ($dane != false && $dane['namba'] >= 1) {
				$tmp=0;
				$response["success"] = 1;
				$stringjson=(string)json_encode($response);
				while($tmp < $dane['namba']){
					$dane2 = $dane['wynik'][$tmp];
					$response1["tresc"] = $dane2["tresc"];
					$response1["data"] = $dane2["data"];
					$response1["tag"] = $dane2["tag"];
					$jason=(string)json_encode($response1);
					$stringjson = $stringjson . "--" . $jason;
					$tmp = $tmp + 1;
				}
				echo $stringjson;
			}
			else{
				$response["success"] = 0;
				$response["error"] = 1;
				$response["error_msg"] = "Blad! Dane niedostepne";
				echo json_encode($response);
			}
		}
	}
	
	else if ($tag == 'store_message'){
		$uid = $_POST['uid'];
		$sid = $_POST['sid'];
		$title = $_POST['title'];
		$tresc = $_POST['tresc'];
		$data = $_POST['data'];
		$user_tag = $_POST['user_tag'];
		session_id($_POST['sid']);
		session_start();
		if($_SESSION['ip'] == $_SERVER['REMOTE_ADDR'] && $_SESSION['zalogowany'] == $uid){
			$dane = $db->storeMessage($uid, $title, $tresc, $data, $user_tag);
			if ($dane != false) {
				$response["success"] = 1;
				echo json_encode($response);
			}
			else{
				$response["success"] = 0;
				$response["error"] = 1;
				$response["error_msg"] = "Blad! Dane niedostepne";
				echo json_encode($response);
			}
		}
	}
	
	else if ($tag == 'doctor_number'){
		$uid = $_POST['uid'];
		$sid = $_POST['sid'];
		session_id($_POST['sid']);
		session_start();
		if($_SESSION['ip'] == $_SERVER['REMOTE_ADDR'] && $_SESSION['zalogowany'] == $uid){
			$dane = $db->getPhone($uid);
			if ($dane != false) {
				$response["success"] = 1;
				$response["number"] = $data["telefon"];
				echo json_encode($response);
			}
			else{
				$response["success"] = 0;
				$response["error"] = 1;
				$response["error_msg"] = "Blad! Dane niedostepne";
				echo json_encode($response);
			}
		}
	}
	
	else if ($tag == 'wyloguj'){
		session_destroy();
	}
///////////////////////////////////////SAJMONA///////////////////////////////////////////////////////////


	else if ($tag == "loginD") { //login wporzo
        // Request type is check Login
		session_start();
        $id = $_POST['id'];
        $password = $_POST['pass'];
		$ident = $_POST['ident'];
 
        // check for user
        $user = $db->dLogin($id, $password, $ident);
        if ($user != false) {
            $response["success"] = 1;
			$response["sid"] = session_id();
			$response["imie"] = $user["imie"];
			$response["nazwisko"] = $user["nazwisko"];
			$response["nazwisko"] = $user["nazwisko"];
			$response["email"] = $user["email"];
			$response["login"]=$user["login"];
            
			if (!isset($_SESSION['zalogowany'])) //k
			{ //k
				$_SESSION['zalogowany'] = $user["login"];//k
				$_SESSION['ip'] = $_SERVER['REMOTE_ADDR'];//k
			} //k
            echo json_encode($response);
        } else {
            $response["error"] = 1;
            $response["error_msg"] = "Incorrect email or password!";
            echo json_encode($response);
        }
    }
	else if ($tag == 'Dpobierz_pacjentow'){
		$sid=$_POST['sid'];
		session_id($sid);
		session_start();
		$login=$_SESSION['zalogowany'];
		if($_SESSION['ip'] == $_SERVER['REMOTE_ADDR']){
			$data = $db->GetPatiensByDoctorLogin($login);
			if ($data != false) {
				echo json_encode($data);
			}
			else{
				$response["error"] = 1;
				$response["error_msg"] = "Blad! Dane w bazie nie zostaly uzupelnione";
				echo json_encode($response);
			}
		}
		else{
			echo "Zostales wylogowany. Zaloguj sie ponownie";
		}	
	}
	
	else if ($tag =='DdodajPacjenta'){
		$sid=$_POST['sid'];
		session_id($sid);
		session_start();
		$login=$_SESSION['zalogowany'];
		if($_SESSION['ip'] == $_SERVER['REMOTE_ADDR']){
			$data = $db->AddPatientByDoctorLogin($login,$_POST);
			if ($data != false) {
			$response['success']=1;
				echo json_encode($response);
			}
			else{
				$response["error"] = 1;
				$response["error_msg"] = "Blad! Nie dodano pacjenta";
				echo json_encode($response);
			}
		}
	}
	
	else if ($tag =='DpobierzKalendarz'){
		$sid=$_POST['sid'];
		session_id($sid);
		session_start();
		$login=$_SESSION['zalogowany'];
		if($_SESSION['ip'] == $_SERVER['REMOTE_ADDR']){
			$data = $db->GetCalendarByDoctorLogin($login);
			if ($data != false) {
				$response['success']=1;
				echo json_encode($data);
			}
			else{
				$response["error"] = 1;
				$response["error_msg"] = "Blad! Nie dodano pacjenta";
				echo json_encode($response);
			}
		}
	}
	
	else if ($tag == 'DpobierzWiadomosci'){
		$sid=$_POST['sid'];
		session_id($sid);
		session_start();
		$login=$_SESSION['zalogowany'];
		if($_SESSION['ip'] == $_SERVER['REMOTE_ADDR']){
			$data = $db->GetMessagesByDoctorLogin($login);
			if ($data != false) {
				$response['success']=1;
				echo json_encode($data);
			}
			else{
				$response["error"] = 1;
				$response["error_msg"] = "Blad! Nie dodano pacjenta";
				echo json_encode($response);
			}
		}
	}
	
	else if ($tag == 'DdodajWiadomosc'){
		$sid=$_POST['sid'];
		session_id($sid);
		session_start();
		$login=$_SESSION['zalogowany'];
		if($_SESSION['ip'] == $_SERVER['REMOTE_ADDR']){
			$data = $db->StoreMessageByDoctorLogin($login,$_POST);
			if ($data != false) {
				$response['success']=1;
				echo json_encode($response);
			}
			else{
				$response["error"] = 1;
				$response["error_msg"] = "Blad! Nie dodano pacjenta";
				echo json_encode($response);
			}
		}
	}
	
	else if ($tag == 'DdodajKalendarz'){
		$sid=$_POST['sid'];
		session_id($sid);
		session_start();
		$login=$_SESSION['zalogowany'];
		if($_SESSION['ip'] == $_SERVER['REMOTE_ADDR']){
			$data = $db->StoreCalendarByDoctorLogin($login,$_POST);
			if ($data != false) {
				$response['success']=1;
				echo json_encode($response);
			}
			else{
				$response["error"] = 1;
				$response["error_msg"] = "Blad! Nie dodano pacjenta (Ddodaj Kalendarz)";
				echo json_encode($response);
			}
		}
	}
	
	
	
//////////////////////////KONIEC TAGOW/////////////
	else {
        $response["success"] = 0;
		$response["error"] = 1;
		$response["error_msg"] = "Blad! Dane niedostepne";
		echo json_encode($response);
    }
	
	
} else {
    $response["success"] = 0;
	$response["error"] = 1;
	$response["error_msg"] = "Zostales wylogowany. Zaloguj sie ponownie";
	echo json_encode($response);
}
?>