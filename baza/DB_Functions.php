<?php
 
class DB_Functions {
 
    private $db;
 
    //put your code here
    // constructor
    function __construct() {
        require_once 'DB_Connect.php';
        // connecting to database
        $this->db = new DB_Connect();
        $this->db->connect();
    }
 
    // destructor
    function __destruct() {
         
    }
 
    /**
     * Storing new user
     * returns user details
     */
    public function storeUser($ident, $id, $password) {
        $uuid = uniqid('', true);
        $hash = $this->hashSSHA($password);
        $encrypted_password = $hash["encrypted"]; // encrypted password
        $salt = $hash["salt"]; // salt
        $result = mysql_query("INSERT INTO login(unique_id, ident, id, encrypted_password, salt, created_at) VALUES('$uuid', '$ident', '$id', '$encrypted_password', '$salt', NOW())");
        // check for successful store
        if ($result) {
            // get user details 
            $uid = mysql_insert_id(); // last inserted id
            $result = mysql_query("SELECT * FROM login WHERE uid = $uid");
            // return user details
            return mysql_fetch_array($result);
        } else {
            return false;
        }
    }
 
    /**
     * Get user by email and password
     */
    public function getUserByIdAndPassword($id, $password, $ident) {
        $result = mysql_query("SELECT * FROM login WHERE id = '$id' AND ident = '$ident'") or die(mysql_error());
        // check for result 
        $no_of_rows = mysql_num_rows($result);
        if ($no_of_rows > 0) {
            $result = mysql_fetch_array($result);
            $salt = $result['salt'];
            $encrypted_password = $result['encrypted_password'];
            $hash = $this->checkhashSSHA($salt, $password);
            // check for password equality
            if ($encrypted_password == $hash) {
                // user authentication details are correct
                return $result;
            }
        } else {
            // user not found
            return false;
        }
    }
	
 
    /**
     * Check user is existed or not
     */
    public function isUserExisted($id) {
        $result = mysql_query("SELECT id from login WHERE id = '$id'");
        $no_of_rows = mysql_num_rows($result);
        if ($no_of_rows > 0) {
            // user existed 
            return true;
        } else {
            // user not existed
            return false;
        }
    }
 
    /**
     * Encrypting password
     * @param password
     * returns salt and encrypted password
     */
    public function hashSSHA($password) {
 
        $salt = sha1(rand());
        $salt = substr($salt, 0, 10);
        $encrypted = base64_encode(sha1($password . $salt, true) . $salt);
        $hash = array("salt" => $salt, "encrypted" => $encrypted);
        return $hash;
    }
 
    /**
     * Decrypting password
     * @param salt, password
     * returns hash string
     */
    public function checkhashSSHA($salt, $password) {
 
        $hash = base64_encode(sha1($password . $salt, true) . $salt);
 
        return $hash;
    }
	
	///////////////////////////////// TU ZACZYNAJA SIE WLASNE FUNKCJE ///////////////////////
	
	public function getPatientDataById($uid){
		$result = mysql_query("SELECT p.imie, p.nazwisko, u.nazwa, p.eid, p.dlugosc, p.postep FROM 
		pacjenci p, urazy u WHERE p.uid = u.uid AND (p.login = (SELECT id from login where unique_id='$uid'))") or die(mysql_error());
        // check for result 
        $no_of_rows = mysql_num_rows($result);
        if ($no_of_rows > 0) {
            $result = mysql_fetch_array($result);
            return $result;
        } 
		else {
            return false;
        }
	}
	
	public function getTodayPlan($uid){
		$today = date('Y/m/d');
		$rows = array();
		$result = mysql_query("SELECT c.nazwa as nazwa, d.serie as serie, d.powtorzenia as powtorzenia, 
		c.link as link FROM cwiczenia c, dzienny_plan d WHERE c.cid = d.cid AND d.dzien_leczenia = '$today'  
		AND (d.pid = (SELECT p.pid from pacjenci p, login l WHERE p.login = l.id AND l.unique_id='$uid'))");
		$no_of_rows = mysql_num_rows($result);
		if ($no_of_rows == 1) {
            $result = mysql_fetch_array($result);
            return array('namba' => $no_of_rows, 'wynik' => $result);
        } 
        else if ($no_of_rows > 1) {
            while($row = mysql_fetch_array($result)) {
				array_push($rows,$row);
			}
			return array('namba' => $no_of_rows, 'wynik' => $rows);
        } 
		else {
            return false;
        }
	}
	
	public function getTommorowPlan($uid){
		$today = date_create(date('Y/m/d'));
		date_modify($today, '+1 day');
		$today = date_format($today, 'Y/m/d');
		$rows = array();
		$result = mysql_query("SELECT c.nazwa, d.serie, d.powtorzenia, c.link 
		FROM cwiczenia c, dzienny_plan d WHERE c.cid = d.cid AND d.dzien_leczenia = '$today'  
		AND (d.pid = (SELECT p.pid from pacjenci p, login l WHERE p.login = l.id AND l.unique_id='$uid'))");
		$no_of_rows = mysql_num_rows($result);
		if ($no_of_rows == 1) {
            $result = mysql_fetch_array($result);
            return array('namba' => $no_of_rows, 'wynik' => $result);
        } 
        else if ($no_of_rows > 1) {
            while($row = mysql_fetch_array($result)) {
				array_push($rows,$row);
			}
			return array('namba' => $no_of_rows, 'wynik' => $rows);
        } 
		else {
            return false;
        }
	}
	
	public function getWeekPlan($uid){
		$today = date_create(date('Y-m-d'));
		date_modify($today, '+2 day');
		$today2 = date_format($today, 'Y-m-d');
		date_modify($today, '+4 day');
		$today3 = date_format($today, 'Y-m-d');
		$rows = array();
		$result = mysql_query("SELECT c.nazwa, d.serie, d.powtorzenia, d.dzien_leczenia FROM cwiczenia c, dzienny_plan d 
		WHERE c.cid = d.cid AND d.dzien_leczenia >= '$today2' AND d.dzien_leczenia <= '$today3' AND (d.pid = (SELECT p.pid from pacjenci p, login l 
		WHERE p.login = l.id AND l.unique_id='$uid')) order by d.dzien_leczenia");
		$no_of_rows = mysql_num_rows($result);
        if ($no_of_rows >= 1) {
            while($row = mysql_fetch_array($result)) {
				array_push($rows,$row);
			}
			return array('namba' => $no_of_rows, 'wynik' => $rows);
        } 
		else {
            return false;
        }
	}
	
	public function getVisitPlan($uid){
		$today = date_create(date('Y-m-d'));
		$today2 = date_format($today, 'Y-m-d');
		$rows = array();
		$result = mysql_query("SELECT h.data, h.uwagi FROM historia_wizyt h WHERE 
		h.data >= '$today2'  AND (h.pid = (SELECT p.pid FROM pacjenci p, login l 
		WHERE p.login = l.id AND l.unique_id='$uid')) order by h.data");
		$no_of_rows = mysql_num_rows($result);
        if ($no_of_rows >= 1) {
            while($row = mysql_fetch_array($result)) {
				array_push($rows,$row);
			}
			return array('namba' => $no_of_rows, 'wynik' => $rows);
        } 
		else {
            return false;
        }
	}
	
	public function getMessageBox($uid, $page){
		$rows = array();
		$page = (int)$page;
		$limit2 = $page * 20;
		$limit1 = $limit2 - 20; 
		$result = mysql_query("SELECT w.tytul, max(w.data) as data FROM wiadomosci w WHERE 
		(w.pid = (SELECT p.pid from pacjenci p, login l WHERE p.login = l.id AND 
		l.unique_id='$uid')) group by w.tytul ORDER BY data DESC LIMIT $limit1, $limit2");
		$no_of_rows = mysql_num_rows($result);
        if ($no_of_rows >= 1) {
            while($row = mysql_fetch_array($result)) {
				array_push($rows,$row);
			}
			return array('namba' => $no_of_rows, 'wynik' => $rows);
        } 
		else {
            return false;
        }
	}
	
	public function getMessagesByTitle($uid, $title){
		$rows = array();
		//$limit2 = $page * 20;
		//$limit1 = $limit2 - 20; 
		$result = mysql_query("SELECT w.tresc, w.data as data, w.tag FROM wiadomosci w WHERE 
		w.tytul = '$title' AND (w.pid = (SELECT p.pid from pacjenci p, login l WHERE p.login = l.id 
		AND l.unique_id='$uid')) ORDER BY data");
		$no_of_rows = mysql_num_rows($result);
        if ($no_of_rows >= 1) {
            while($row = mysql_fetch_array($result)) {
				array_push($rows,$row);
			}
			return array('namba' => $no_of_rows, 'wynik' => $rows);
        } 
		else {
            return false;
        }
	}
	
	public function storeMessage($uid, $title, $tresc, $data, $user_tag){
		$result = mysql_query("SELECT pp.lid, pp.pid FROM pacjenci pp WHERE 
		(pp.pid = (SELECT p.pid from pacjenci p, login l WHERE p.login = l.id 
		AND l.unique_id='$uid'))");
		$no_of_rows = mysql_num_rows($result);
        if ($no_of_rows >= 1) {
            $result = mysql_fetch_array($result);
			$lid = $result["lid"];
			$pid = $result["pid"];
			$result = mysql_query("INSERT INTO wiadomosci(lid, pid, tytul, tresc, data, tag) 
			VALUES('$lid', '$pid', '$title', '$tresc', '$data', '$user_tag')");
			// check for successful store
			if ($result) { 
				return true;
			} else {
				return false;
			}
        } 
		else {
            return false;
        }
	}
	
	public function getPhone($uid){
		$result = mysql_query("SELECT l.telefon FROM lekarze l, pacjenci p WHERE p.lid = l.lid AND
		(p.pid = (SELECT p.pid from pacjenci p, login l WHERE p.login = l.id 
		AND l.unique_id='$uid'))");
		$no_of_rows = mysql_num_rows($result);
        if ($no_of_rows >= 1) {
            $result = mysql_fetch_array($result);
            return $result;
        } 
		else {
            return false;
        }
	}
	
	 public function dLogin($id, $password, $ident) {
        $result = mysql_query("SELECT * FROM login WHERE id = '$id' AND ident = '$ident'") or die(mysql_error());
        // check for result 
        $no_of_rows = mysql_num_rows($result);
        if ($no_of_rows > 0) {
            $result = mysql_fetch_array($result);
            $salt = $result['salt'];
            $encrypted_password = $result['encrypted_password'];
            $hash = $this->checkhashSSHA($salt, $password);
            // check for password equality
            if ($encrypted_password == $hash) {
                // user authentication details are correct
				$result = mysql_query("SELECT imie,nazwisko,email FROM lekarze WHERE login = '$id'") or die(mysql_error());
				$no_of_rows = mysql_num_rows($result);
				if ($no_of_rows > 0) {
					$result = mysql_fetch_array($result);
					return $result;
				}
				else{
					return false;
				}
            }
        } else {
            // user not found
            return false;
        }
    }
}
 
?>