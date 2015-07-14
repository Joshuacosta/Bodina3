<?php 
//
// Info : Aquest PHP es el encarregat de la administració de les associacions entre el client i les entitats
//
$Temps  	= getdate();
$Avui   	= $Temps[mday]."/".$Temps[mon]."/".$Temps[year]." ".$Temps[hours].":".$Temps[minutes].":".$Temps[seconds];
$Resposta 	= "";
// Array per la resposta JSON
$response 			= array();
$response["valids"] = "1";
//
// Operativa: 	0- Solicitar associacio
//            	1- 
//            	2- 
//            
$Operativa = $_POST['Operativa'];
// Obrim la BBDD 
$Conexio = mysql_connect("localhost:3307", "BodinaUser0", "Listillo01");
if (!$Conexio){
	$response["valids"] = "2";
	$gestor = fopen("errors/bd.txt","a");
	fwrite($gestor,$Avui.">>> Associacio.PHP//BD//Connect//".mysql_errno()."//".mysql_error()."\n");
    fclose($gestor);
}
else{
    $db = mysql_select_db("Bodina");
    if (!$db){
        $response["valids"] = "2";
        $gestor = fopen("errors/bd.txt","a");
        fwrite($gestor,$Avui.">>> Associacio.PHP//BD//Select_db//".mysql_errno()."//".mysql_error()."\n");
        fclose($gestor);
    }
    else{
		// Estudiem la operativa
		switch ($Operativa){
			//
			// OPERATIVA CLIENT
			//
			case 0: // Solicitem associacio
				$Ara = date("Y-m-d H:i:s");
				/* Recuperem les dades d'entrada */
				$CodiClient         = $_POST['CodiClient'];
				$CodiEntitat		= $_POST['CodiEntitat'];				
				$Contacte			= $_POST['Contacte'];
				$Descripcio			= $_POST['Descripcio'];
				$eMail				= $_POST['eMail'];
				// Inserim la petició de associacio, definim el seu estat en 2.
				$result = mysql_query("INSERT INTO Associacions (CodiClient, CodiEntitat, DataPeticio, Contacte, Descripcio, eMail, DataDarrerCanvi, Estat)
													VALUES ('".$CodiClient."',
															'".$CodiEntitat."',
															'".$Ara."',
															'".addslashes($Contacte)."',
															'".addslashes($Descripcio)."',
															'".$eMail."',
															'".$Ara."', 2)");
				if (!$result){
					$response["valids"] = "2";
					$gestor = fopen("errors/bd.txt","a");
					fwrite($gestor,$Avui.">>> Associacio.PHP//Associacions//Insert//".mysql_errno()."//".mysql_error()."//Values:".$CodiClient."/".$CodiEntitat."/".$Contacte."/".$Descripcio."/".$eMail."\n");
					fclose($gestor);
				}
				break;
				
			case 1: // Llegim les associacions del client, amb la informació de la entitat
				/* Recuperem les dades d'entrada */
				$CodiClient = $_POST['CodiClient'];
								// Fem la select amb les entitats del client
				$result = mysql_query("SELECT    E.Codi, E.Nom, E.eMail, E.Pais, E.Adresa, E.Contacte, E.TipusContacte, E.Telefon, E.Estat, 
												 A.Contacte As ContacteAssociacio, A.eMail As eMailAssociacio, A.Estat As EstatAssociacio,
												 A.DataAlta, A.DataFi, A.Descripcio, A.DataPeticio
                                       FROM      Associacions AS A
                                       LEFT JOIN Entitats AS E
                                       ON       (E.Codi = A.CodiEntitat)
                                       WHERE     A.CodiClient = '".$CodiClient."'
                                       GROUP BY  E.Codi");
				if (!$result){
					$response["valids"] = "2";
					$gestor = fopen("errors/bd.txt","a");
					fwrite($gestor,$Avui.">>> Associacio.PHP//Associacions//Select associacions client//".mysql_errno()."//".mysql_error()."//Values:".$CodiClient."\n");
					fclose($gestor);
				}
				else{
					$response["Associacions"] = array();
					if (mysql_num_rows($result) > 0) {	 
						while ($row = mysql_fetch_array($result)) {
							$Associacio = array();
							// Dades de la associacio
							$Associacio["Contacte"] = $row["ContacteAssociacio"];							
							$Associacio["eMail"] = $row["eMailAssociacio"];
							$Associacio["DataAlta"] = $row["DataAlta"];
							$Associacio["DataFi"] = $row["DataFi"];
							$Associacio["Descripcio"] = $row["Descripcio"];
							$Associacio["DataPeticio"] = $row["DataPeticio"];
							$Associacio["EstatAssociacio"] = $row["EstatAssociacio"];							
							// Dades de la entitat
							$Entitat = array();
							$Entitat["Codi"] = $row["Codi"];
							$Entitat["Nom"] = $row["Nom"];
							$Entitat["eMail"] = $row["eMail"];
							$Entitat["Pais"] = $row["Pais"];
							$Entitat["Adresa"] = $row["Adresa"];
							$Entitat["Contacte"] = $row["Contacte"];
							$Entitat["TipusContacte"] = $row["TipusContacte"];
							$Entitat["Telefon"] = $row["Telefon"];
							$Entitat["Estat"] = $row["Estat"];
							$Associacio["Entitat"] = $Entitat;
							//
							array_push($response["Associacions"], $Associacio);
						}							
					}						
				}
				break;
				
			case 2: // Updatem la associacio donada
				$Ara = date("Y-m-d H:i:s");
				/* Recuperem les dades d'entrada */
				$CodiClient         = $_POST['CodiClient'];
				$CodiEntitat		= $_POST['CodiEntitat'];
				$Contacte			= $_POST['ContacteAssociacio'];
				$Descripcio			= $_POST['DescripcioAssociacio'];
				$eMail				= $_POST['eMailAssociacio'];
				$DataFi   			= $_POST['DataFiAssociacio'];
				$Estat				= $_POST['EstatAssociacio'];
				$result = mysql_query("UPDATE Associacions SET 	eMail='".$eMail."', 
																Descripcio='".addslashes($Descripcio)."',
																Contacte='".addslashes($Contacte)."',
																DataFi='".$DataFi."',
																Estat=".$Estat.",
																DataDarrerCanvi='".$Ara."'
														   WHERE CodiClient='".$CodiClient."' AND CodiEntitat = '".$CodiEntitat."'");
				if (!$result){
					$response["valids"] = "2";
					$gestor = fopen("errors/bd.txt","a");
					fwrite($gestor,$Avui.">>> Associacio.PHP//Clients//Update associacions client//".mysql_errno()."//".mysql_error()."//Values:".$CodiClient."/".
																																				  $CodiEntitat."/".
																																				  $Contacte."/".
																																				  $Descripcio."/".
																																				  $eMail."/".
																																				  $DataFi."/".
																																				  $Estat."\n");
					fclose($gestor);
				}
				break;				
			//
			// OPERATIVA ENTITAT
			//
			case 3: // Acceptem la associacio
				$Ara = date("Y-m-d H:i:s");
				/* Recuperem les dades d'entrada */
				$CodiClient         = $_POST['CodiClient'];
				$CodiEntitat		= $_POST['CodiEntitat'];
				$result = mysql_query("UPDATE Associacions SET 	Estat = 1,
																DataDarrerCanvi='".$Ara."'
														   WHERE CodiClient='".$CodiClient."' AND CodiEntitat = '".$CodiEntitat."'");
				if (!$result){
					$response["valids"] = "2";
					$gestor = fopen("errors/bd.txt","a");
					fwrite($gestor,$Avui.">>> Associacio.PHP//Clients//Update acceptar//".mysql_errno()."//".mysql_error()."//Values:".$CodiClient."/".
																																	   $CodiEntitat."\n");
					fclose($gestor);
				}
				break;				
        }
	}
}
echo json_encode($response);
?>
