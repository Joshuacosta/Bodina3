<?php 
//
// Info : Aquest PHP es el encarregat de la administraci? de clients. Si l'alta es correcta envia un e-mail de confirmaci? al client
//
$Temps  	= getdate();
$Avui   	= $Temps[mday]."/".$Temps[mon]."/".$Temps[year]." ".$Temps[hours].":".$Temps[minutes].":".$Temps[seconds];
$Resposta 	= "";
/* Recuperem les dades d'entrada */
$CodiClient         = $_POST['CodiClient'];
$CodiClientIntern	= $_POST['CodiClientIntern'];
$eMail				= $_POST['eMail'];
$Nom				= $_POST['Nom'];
$Pais				= $_POST['Pais'];
$Contacte			= $_POST['Contacte'];
$Idioma				= $_POST['Idioma'];
// Array per la resposta JSON
$response 			= array();
$response["valids"] = "1";
//
// Operativa: 	0- Alta
//            	1- Update
//            	2- Recuperar dades amb CodiClient
//				21- Recuperar dades amb CodiClientIntern
//            
$Operativa = $_POST['Operativa'];
// Obrim la BBDD 
$Conexio = mysql_connect("localhost:3307", "BodinaUser0", "Listillo01");
if (!$Conexio){
	$response["valids"] = "2";
	$gestor = fopen("errors/bd.txt","a");
	fwrite($gestor,$Avui.">>> Clients.PHP//BD//Connect//".mysql_errno()."//".mysql_error()."\n");
    fclose($gestor);
}
else{
    $db = mysql_select_db("Bodina");
    if (!$db){
        $response["valids"] = "2";
        $gestor = fopen("errors/bd.txt","a");
        fwrite($gestor,$Avui.">>> Clients.PHP//BD//Select_db//".mysql_errno()."//".mysql_error()."\n");
        fclose($gestor);
    }
    else{
		// Estudiem la operativa
		switch ($Operativa){
			case 0: // Alta
				$Ara = date("Y-m-d H:i:s");
				$result = mysql_query("SELECT CodiClient FROM Clients WHERE Pais='".$Pais."'");
				if (!$result){
					$response["valids"] = "2";
					$gestor = fopen("errors/bd.txt","a");
					fwrite($gestor,$Avui.">>> Clients.PHP//Clients//Select calcul//".mysql_errno()."//".mysql_error()."//WHERE:".$Pais."\n");
					fclose($gestor);
				}				
				else{
					$Numero = mysql_num_rows($result) + 1;				
					$CodiClient = $Pais."/".$Numero;
					$result = mysql_query("INSERT INTO Clients (CodiClient,CodiClientIntern,eMail,Nom,Pais,Contacte,DataAlta,Idioma,DataDarrerCanvi,Estat)
														VALUES ('".$CodiClient."',
																'".$CodiClientIntern."',
																'".$eMail."',
																'".addslashes($Nom)."',
																'".$Pais."',
																'".addslashes($Contacte)."',
																'".$Ara."','".$Idioma."','".$Ara."',TRUE)");
					if (!$result){
						$response["valids"] = "2";
						$gestor = fopen("errors/bd.txt","a");
						fwrite($gestor,$Avui.">>> Clients.PHP//Clients//Insert//".mysql_errno()."//".mysql_error()."//Values:".$CodiClient."/".$CodiClientIntern."/".$eMail."/".$Nom."/".$Pais."/".$Contacte."/".$Idioma."\n");
						fclose($gestor);
					}
					else{					
						// Anotem el codi de client per la resposta					
						$response["CodiClient"] = $CodiClient;
						// Enviem mail de confirmació
						require "class.phpmailer.php";
						$mail = new phpmailer();
						$mail->PluginDir = "";
						$mail->Mailer    = "smtp";
						$mail->Host      = "Localhost";
						$mail->SMTPAuth  = true;
						$mail->Username  = "info@virtuol.com";
						$mail->Password  = "listillo";
						$mail->From      = "info@virtuol.com";
						$mail->FromName  = "Bodina";
						$mail->Timeout   = 30;
						$mail->AddAddress($eMail);
						$mail->Subject   = "Alta Bodina";
						switch ($Idioma){
							case "es_ES":
								$Missatge = "\n Se ha dado de alta satisfactoriamente en nuestro sistema.\n\n";
								$Missatge = $Missatge." Atentamente,\n";
								$Missatge = $Missatge." Equipo Virtuol";
								break;
							case "ca_CA":
								$Missatge = "\n Se ha donat d'alta satisfactoriament en el nostre sistema.\n\n";
								$Missatge = $Missatge." Atentament,\n";
								$Missatge = $Missatge." Equip Virtuol";
								break;
							default:
								$Missatge = "\n Se ha dado de alta satisfactoriament en nuestro sistema.\n\n";
								$Missatge = $Missatge." Atentament,\n";
								$Missatge = $Missatge." Equip Virtuol";
								break;									
						}
						$mail->Body	= $Missatge;
						$exito = $mail->Send();
						$intentos=1;
						while ((!$exito) && ($intentos < 5)) {
							sleep(5);
							$exito = $mail->Send();
							$intentos=$intentos+1;
						}
						if(!$exito){
							$response["valids"] = "3";
							$gestor = fopen("errors/bd.txt","a");
							fwrite($gestor,$Avui.">>> Clients.PHP//Mail//Enviant mail//".$mail->ErrorInfo."//WHERE:".$eMail."\n");
							fclose($gestor);
						}
					}
                }
				break;
				
			case 1: // Update
				$Ara = date("Y-m-d H:i:s");
				$result = mysql_query("UPDATE Clients SET eMail='".$eMail."', 
														  Nom='".addslashes($Nom)."', 
														  Pais='".$Pais."',
														  Contacte='".addslashes($Contacte)."',
														  Idioma='".$Idioma."',
														  DataDarrerCanvi='".$Ara."'
														  WHERE CodiClient='".$CodiClient."' AND Estat = TRUE");
				if (!$result){
					$response["valids"] = "2";
					$gestor = fopen("errors/bd.txt","a");
					fwrite($gestor,$Avui.">>> Clients.PHP//Clients//Update//".mysql_errno()."//".mysql_error()."//Values:".$CodiClient."/".
																														  $eMail."/".
																														  $Nom."/".
																														  $Pais."/".
																														  $Contacte."/".
																														  $Idioma."\n");
					fclose($gestor);
				}					
				break;
				
			case 2: // Recuperem les dades amb CodiClient
				$result = mysql_query("SELECT eMail, Nom, Contacte, Pais, Idioma, DataAlta FROM Clients WHERE CodiClient='".$CodiClient."' AND Estat = TRUE");
				if (!$result){
					$response["valids"] = "2";
					$gestor = fopen("errors/bd.txt","a");
					fwrite($gestor,$Avui.">>> Clients.PHP//Clients//Select amb CodiClient//".mysql_errno()."//".mysql_error()."//WHERE:".$CodiClient."\n");
					fclose($gestor);
				}
				else{
					if (mysql_num_rows($result) == 1){
						$row = mysql_fetch_array($result);						
						//
						$client = array();
						$client["CodiClient"] = $row[CodiClient];
						$client["eMail"] = stripslashes($row[eMail]);
						$client["Nom"] = stripslashes($row[Nom]);
						$client["Contacte"] = stripslashes($row[Contacte]);
						$client["DataAlta"] = $row[DataAlta];
						$client["Pais"] = $row[Pais];
						$client["Idioma"] = $row[Idioma];		
						// 
						$response["client"] = array(); 
						array_push($response["client"], $client);	 
					}
				}
				break;
				
			case 21: // Recuperem les dades amb CodiClientIntern (es el que fem servir quan no hi ha BBDD client perque es la primera vegada o s'esborrat)				
				$result = mysql_query("SELECT CodiClient, eMail, Nom, Contacte, Pais, Idioma, DataAlta FROM Clients WHERE CodiClientIntern='".$CodiClientIntern."' AND Estat = TRUE");
				if (!$result){
					$response["valids"] = "2";
					$gestor = fopen("errors/bd.txt","a");
					fwrite($gestor,$Avui.">>> Clients.PHP//Clients//Select amb CodiClientIntern//".mysql_errno()."//".mysql_error()."//WHERE:".$CodiClientIntern."\n");
					fclose($gestor);
				}
				else{
					$client = array();
					// Si existeix, retornem les dades que l'usuari s'ha esborrat localment
					if (mysql_num_rows($result) == 1){
						$row = mysql_fetch_array($result);
						$client["CodiClient"] = $row[CodiClient];
						$client["eMail"] = stripslashes($row[eMail]);
						$client["Nom"] = stripslashes($row[Nom]);
						$client["Contacte"] = stripslashes($row[Contacte]);
						$client["DataAlta"] = $row[DataAlta];
						$client["Pais"] = $row[Pais];
						$client["Idioma"] = $row[Idioma];
					}
					else{
						// Es nou, nou
						$client["CodiClient"] = "NOU";
					}
					$response["client"] = array(); 
					array_push($response["client"], $client);	 
					
					$gestor = fopen("errors/bd.txt","a");
					fwrite($gestor,$Avui.">>> Clients.PHP//BD//Hola 3//\n");
					fclose($gestor);
					
				}
				break;				
        }
	}
}
// 
echo json_encode($response);
?>
