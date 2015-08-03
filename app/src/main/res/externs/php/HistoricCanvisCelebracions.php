<?php 
//
// Info : Aquest PHP es el encarregat de la administració el historic de les celebracions
//
$Temps  	= getdate();
$Avui   	= $Temps[mday]."/".$Temps[mon]."/".$Temps[year]." ".$Temps[hours].":".$Temps[minutes].":".$Temps[seconds];
$Resposta 	= "";
// Array per la resposta JSON
$response 			= array();
$response["valids"] = "1";
//
// Operativa: 	0- Llista de canvis de una celebracio donada
//            	1- Alta canvi celebracio
//
$Operativa = $_POST['Operativa'];
// Obrim la BBDD 
$Conexio = mysql_connect("localhost:3307", "BodinaUser0", "Listillo01");
if (!$Conexio){
	$response["valids"] = "2";
	$gestor = fopen("errors/bd.txt","a");
	fwrite($gestor,$Avui.">>> HistoricCanvisCelebracions.PHP//BD//Connect//".mysql_errno()."//".mysql_error()."\n");
    fclose($gestor);
}
else{
    $db = mysql_select_db("Bodina");
    if (!$db){
        $response["valids"] = "2";
        $gestor = fopen("errors/bd.txt","a");
        fwrite($gestor,$Avui.">>> HistoricCanvisCelebracions.PHP//BD//Select_db//".mysql_errno()."//".mysql_error()."\n");
        fclose($gestor);
    }
    else{
		$CodiEntitat	= $_POST['CodiEntitat'];
		$CodiClient		= $_POST['CodiClient'];
		$CodiCelebracio	= $_POST['CodiCelebracio'];
		// Estudiem la operativa
		switch ($Operativa){
			//
			// OPERATIVA CLIENT i SERVIDOR
			//
			case 0: // Recuperem el historic de una celebracio
				$result = mysql_query("SELECT    Modificacio, Data
                                       FROM      HistoricCanvisCelebracions
                                       WHERE     CodiEntitat = '".$CodiEntitat."' AND CodiClient = '".$CodiClient."' AND CodiCelebracio = '".$CodiCelebracio."'");
				if (!$result){
					$response["valids"] = "2";
					$gestor = fopen("errors/bd.txt","a");
					fwrite($gestor,$Avui.">>> HistoricCanvisCelebracions.PHP//HistoricCanvisCelebracions//Select historic//".mysql_errno()."//".mysql_error()."//Values:".$CodiEntitat."/".$CodiClient."/".$CodiCelebracio."\n");
					fclose($gestor);
				}
				else{
					$response["HistoricCanvisCelebracions"] = array();
					if (mysql_num_rows($result) > 0) {
						while ($row = mysql_fetch_array($result)) {
							$Canvi = array();
							$Canvi["Modificacio"] = stripslashes($row["Modificacio"]);
							$Canvi["Data"] = $row["Data"];
							//
							array_push($response["HistoricCanvisCelebracions"], $Canvi);
						}
					}						
				}				
				break;				
			//
			// OPERATIVA ENTITAT
			//				
			case 1: // Afegim una entrada al historic
				$Ara = date("Y-m-d H:i:s");
				$Modificacio	= $_POST['Modificacio'];
				$result = mysql_query("INSERT INTO HistoricCanvisCelebracions (CodiEntitat, CodiClient, CodiCelebracio, Modificacio, Data)
													VALUES ('".$CodiEntitat."',
															'".$CodiClient."',
															'".$CodiCelebracio."',
															'".addslashes($Modificacio)."',
															'".$Ara."')");
				if (!$result){
					$response["valids"] = "2";
					$gestor = fopen("errors/bd.txt","a");
					fwrite($gestor,$Avui.">>> HistoricCanvisCelebracions.PHP//HistoricCanvisCelebracions//Insert//".mysql_errno()."//".mysql_error()."//Values:".$CodiEntitat."/".$CodiClient."/".$CodiCelebracio."/".$Modificacio."/".$Ara."\n");
					fclose($gestor);
				}					
				break;
								
        }
	}
}
echo json_encode($response);
?>
