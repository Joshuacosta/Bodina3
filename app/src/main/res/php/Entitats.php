<?php 
//
// Info : Aquest PHP es el encarregat de la administració de les entitats
//
$Temps  	= getdate();
$Avui   	= $Temps[mday]."/".$Temps[mon]."/".$Temps[year]." ".$Temps[hours].":".$Temps[minutes].":".$Temps[seconds];
$Resposta 	= "";
// Array per la resposta JSON
$response 			= array();
$response["valids"] = "1";
//
// Operativa: 	0- Llegir Entitats del pais indicat
//            	1- 
//            	2- 
//            
$Operativa = $_POST['Operativa'];
// Obrim la BBDD 
$Conexio = mysql_connect("localhost:3307", "BodinaUser0", "Listillo01");
if (!$Conexio){
	$response["valids"] = "2";
	$gestor = fopen("errors/bd.txt","a");
	fwrite($gestor,$Avui.">>> Entitats.PHP//BD//Connect//".mysql_errno()."//".mysql_error()."\n");
    fclose($gestor);
}
else{
    $db = mysql_select_db("Bodina");
    if (!$db){
        $response["valids"] = "2";
        $gestor = fopen("errors/bd.txt","a");
        fwrite($gestor,$Avui.">>> Entitats.PHP//BD//Select_db//".mysql_errno()."//".mysql_error()."\n");
        fclose($gestor);
    }
    else{
		// Estudiem la operativa
		switch ($Operativa){
			case 0: // Solicitem associacio
				/* Recuperem les dades d'entrada */
				$Pais = $_POST['Pais'];
				
				// DE MOMENT HO RECUPEREM TOT!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
				//$result = mysql_query("SELECT * FROM Entitats Where Pais='".$Pais."'"); 
				$result = mysql_query("SELECT * FROM Entitats"); 
				
				if (!$result){
					$response["valids"] = "2";
					$gestor = fopen("errors/bd.txt","a");
					fwrite($gestor,$Avui.">>> Entitats.PHP//Entitats//Select//".mysql_errno()."//".mysql_error()."//Values:".$Pais."\n");
					fclose($gestor);
				}
				else{
					$response["Entitats"] = array();
					if (mysql_num_rows($result) > 0) {	 
						while ($row = mysql_fetch_array($result)) {
							// temp user array
							$Entitat = array();
							$Entitat["Codi"] = $row["Codi"];
							$Entitat["Nom"] = $row["Nom"];
							$Entitat["eMail"] = $row["eMail"];
							$Entitat["Pais"] = $row["Pais"];
							$Entitat["Adresa"] = $row["Adresa"];
							$Entitat["Contacte"] = $row["Contacte"];
							$Entitat["Telefon"] = $row["Telefon"];
							$Entitat["Estat"] = $row["Estat"];
							array_push($response["Entitats"], $Entitat);
						}							
					}	
				}
				break;
				
			case 1: //
				break;
				
			case 2: //
				break;				
        }
	}
}
// 
echo json_encode($response);
?>
