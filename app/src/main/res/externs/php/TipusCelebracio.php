<?php 
//
// Info : Aquest PHP es el encarregat de la administració dels tipus de celebració de una entitat
//
$Temps  	= getdate();
$Avui   	= $Temps[mday]."/".$Temps[mon]."/".$Temps[year]." ".$Temps[hours].":".$Temps[minutes].":".$Temps[seconds];
$Resposta 	= "";
// Array per la resposta JSON
$response 			= array();
$response["valids"] = "1";
//
// Operativa: 	0- Llista de celebracions de una entitat
//            	1- Alta tipus de celebracio per una entitat
//            	2- Baixa tipus de celebracio per una entitat
//              3- Modificacio tipus de celebracio per una entitat
//
$Operativa = $_POST['Operativa'];
// Obrim la BBDD 
$Conexio = mysql_connect("localhost:3307", "BodinaUser0", "Listillo01");
if (!$Conexio){
	$response["valids"] = "2";
	$gestor = fopen("errors/bd.txt","a");
	fwrite($gestor,$Avui.">>> TipusCelebracio.PHP//BD//Connect//".mysql_errno()."//".mysql_error()."\n");
    fclose($gestor);
}
else{
    $db = mysql_select_db("Bodina");
    if (!$db){
        $response["valids"] = "2";
        $gestor = fopen("errors/bd.txt","a");
        fwrite($gestor,$Avui.">>> TipusCelebracio.PHP//BD//Select_db//".mysql_errno()."//".mysql_error()."\n");
        fclose($gestor);
    }
    else{
		$CodiEntitat	= $_POST['CodiEntitat'];					
		// Estudiem la operativa
		switch ($Operativa){
			//
			// OPERATIVA CLIENT
			//
			case 0: // Recuperem els tipus de celebracio de una entitat							
				$result = mysql_query("SELECT    Codi, Nom, Descripcio, Estat
                                       FROM      TipusCelebracio
                                       WHERE     CodiEntitat = '".$CodiEntitat."'");
				if (!$result){
					$response["valids"] = "2";
					$gestor = fopen("errors/bd.txt","a");
					fwrite($gestor,$Avui.">>> TipusCelebracio.PHP//TipusCelebracio//Select tipus celebracio entitat//".mysql_errno()."//".mysql_error()."//Values:".$CodiEntitat."\n");
					fclose($gestor);
				}
				else{
					$response["TipusCelebracions"] = array();
					if (mysql_num_rows($result) > 0) {
						while ($row = mysql_fetch_array($result)) {
							$TipusCelebracio = array();
							$TipusCelebracio["Codi"] = $row["Codi"];
							$TipusCelebracio["Nom"] = $row["Nom"];
							$TipusCelebracio["Descripcio"] = $row["Descripcio"];
							//
							array_push($response["TipusCelebracions"], $TipusCelebracio);
						}
					}						
				}				
				break;				
			//
			// OPERATIVA ENTITAT
			//				
			case 1: // Afegim el tipus de celebracio
				// Calculem codi de tipus
				$result = mysql_query("SELECT Codi FROM TipusCelebracions WHERE CodiEntitat='".$CodiEntitat."'");
				if (!$result){
					$response["valids"] = "2";
					$gestor = fopen("errors/bd.txt","a");
					fwrite($gestor,$Avui.">>> TipusCelebracions.PHP//Clients//Select calcul//".mysql_errno()."//".mysql_error()."//WHERE:".$CodiEntitat."\n");
					fclose($gestor);
				}				
				else{
					$Codi = mysql_num_rows($result) + 1;
					// Llegim camps de entrada
					$Nom = $_POST['Nom'];
					$Descripcio = $_POST['Descripcio'];
					// Inserim
					$result = mysql_query("INSERT INTO TipusCelebracions (CodiEntitat, Codi, Nom, Descripcio, Estat)
														VALUES ('".$CodiEntitat."',
																".$Codi.",
																'".addslashes($Nom)."',
																'".addslashes($Descripcio)."',
																TRUE)");
					if (!$result){
						$response["valids"] = "2";
						$gestor = fopen("errors/bd.txt","a");
						fwrite($gestor,$Avui.">>> TipusCelebracions.PHP//TipusCelebracions//Insert//".mysql_errno()."//".mysql_error()."//Values:".$CodiEntitat."/".$Codi."/".$Nom."/".$Descripcio."\n");
						fclose($gestor);
					}					
				}
				break;
				
			case 2: // Baixa tipus
				$Codi = $_POST['Codi'];
				$result = mysql_query("UPDATE TipusCelebracions SET Estat = FALSE WHERE CodiEntitat = '".$CodiEntitat."'");
				if (!$result){
					$response["valids"] = "2";
					$gestor = fopen("errors/bd.txt","a");
					fwrite($gestor,$Avui.">>> TipusCelebracions.PHP//Clients//Baixa Tipus//".mysql_errno()."//".mysql_error()."//Values:".$CodiEntitat."\n");
					fclose($gestor);
				}				
				break;				
			
			case 3: // Modifica tipus de celebracio
				$Codi 		= $_POST['Codi'];
				$Nom 		= $_POST['Nom'];
				$Descripcio	= $_POST['Descripcio'];
				$result = mysql_query("UPDATE Nom='".addslashes($Nom)."', 
											  Descripcio='".addslashes($Descripcio)."'
										WHERE CodiEntitat = '".$CodiEntitat."' AND Codi = ".$Codi);

				if (!$result){
					$response["valids"] = "2";
					$gestor = fopen("errors/bd.txt","a");
					fwrite($gestor,$Avui.">>> TipusCelebracions.PHP//Clients//Modifica Tipus//".mysql_errno()."//".mysql_error()."//Values:".$CodiEntitat."/".$Codi."/".$Nom."/".$Descripcio."\n");
					fclose($gestor);
				}				
				break;				
				
        }
	}
}
echo json_encode($response);
?>
