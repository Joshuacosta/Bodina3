<?php 
//
// Info : Aquest PHP es el encarregat de la administraci? de clients. Si l'alta es correcta envia un e-mail de confirmaci? al client
//
$Temps  	= getdate();
$Avui   	= $Temps[mday]."/".$Temps[mon]."/".$Temps[year]." ".$Temps[hours].":".$Temps[minutes].":".$Temps[seconds];
$Resposta 	= "";
/* Recuperem les dades d'entrada */
$Codi       = $_POST['Codi'];          // Aquest valor no hi serà definit quan donem d'alta un client (recorda que es generat)
$CodiIntern	= $_POST['CodiIntern'];
$eMail		= $_POST['eMail'];
$Nom		= $_POST['Nom'];
$Pais		= $_POST['Pais'];
$Contacte	= $_POST['Contacte'];
$Idioma		= $_POST['Idioma'];
// Array per la resposta JSON
$response 	= array();
$response["valids"] = "1";
//
// Operativa: 	0- Alta
//            	1- Update
//            	2- Recuperar dades amb Codi
//				21- Recuperar dades amb CodiIntern
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
				$result = mysql_query("SELECT Codi FROM Clients WHERE Pais='".$Pais."'");
				if (!$result){
					$response["valids"] = "2";
					$gestor = fopen("errors/bd.txt","a");
					fwrite($gestor,$Avui.">>> Clients.PHP//Clients//Select calcul//".mysql_errno()."//".mysql_error()."//WHERE:".$Pais."\n");
					fclose($gestor);
				}				
				else{
					$Numero = mysql_num_rows($result) + 1;
					// Construim el codi
					$Aux = explode($Pais,"-");
					$CodiPais = $Aux[count($Aux)-1]; // Així triem el darrer element, ho fem per si a la definició del pais hi han mes "-"
					$Codi = $CodiPais."/".$Numero;
					$result = mysql_query("INSERT INTO Clients (Codi,CodiIntern,eMail,Nom,Pais,Contacte,DataAlta,Idioma,DataDarrerCanvi,Estat)
														VALUES ('".$Codi."',
																'".$CodiIntern."',
																'".$eMail."',
																'".addslashes($Nom)."',
																'".$Pais."',
																'".addslashes($Contacte)."',
																'".$Ara."','".$Idioma."','".$Ara."',TRUE)");
					if (!$result){
						$response["valids"] = "2";
						$gestor = fopen("errors/bd.txt","a");
						fwrite($gestor,$Avui.">>> Clients.PHP//Clients//Insert//".mysql_errno()."//".mysql_error()."//Values:".$Codi."/".$CodiIntern."/".$eMail."/".$Nom."/".$Pais."/".$Contacte."/".$Idioma."\n");
						fclose($gestor);
					}
					else{					
						// Anotem el codi de client per la resposta					
						$response["Codi"] = $Codi;
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
														  WHERE Codi='".$Codi."' AND Estat = TRUE");
				if (!$result){
					$response["valids"] = "2";
					$gestor = fopen("errors/bd.txt","a");
					fwrite($gestor,$Avui.">>> Clients.PHP//Clients//Update//".mysql_errno()."//".mysql_error()."//Values:".$Codi."/".
																														  $eMail."/".
																														  $Nom."/".
																														  $Pais."/".
																														  $Contacte."/".
																														  $Idioma."\n");
					fclose($gestor);
				}					
				break;
				
			case 2: // Recuperem les dades amb Codi
				$result = mysql_query("SELECT eMail, Nom, Contacte, Pais, Idioma, DataAlta FROM Clients WHERE Codi='".$Codi."' AND Estat = TRUE");
				if (!$result){
					$response["valids"] = "2";
					$gestor = fopen("errors/bd.txt","a");
					fwrite($gestor,$Avui.">>> Clients.PHP//Clients//Select amb Codi//".mysql_errno()."//".mysql_error()."//WHERE:".$Codi."\n");
					fclose($gestor);
				}
				else{
					if (mysql_num_rows($result) == 1){
						$row = mysql_fetch_array($result);						
						//
						$client = array();
						$client["Codi"] = $row[Codi];
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
				
			case 21: // Recuperem les dades amb CodiIntern (es el que fem servir quan no hi ha BBDD client perque es la primera vegada o s'esborrat)				
				$result = mysql_query("SELECT Codi, eMail, Nom, Contacte, Pais, Idioma, DataAlta FROM Clients WHERE CodiIntern='".$CodiIntern."' AND Estat = TRUE");
				if (!$result){
					$response["valids"] = "2";
					$gestor = fopen("errors/bd.txt","a");
					fwrite($gestor,$Avui.">>> Clients.PHP//Clients//Select amb CodiIntern//".mysql_errno()."//".mysql_error()."//WHERE:".$CodiIntern."\n");
					fclose($gestor);
				}
				else{
					$client = array();
					// Si existeix, retornem les dades que l'usuari s'ha esborrat localment
					if (mysql_num_rows($result) == 1){
						$row = mysql_fetch_array($result);
						$client["Codi"] = $row[Codi];
						$client["eMail"] = stripslashes($row[eMail]);
						$client["Nom"] = stripslashes($row[Nom]);
						$client["Contacte"] = stripslashes($row[Contacte]);
						$client["DataAlta"] = $row[DataAlta];
						$client["Pais"] = $row[Pais];
						$client["Idioma"] = $row[Idioma];
					}
					else{
						// Es nou, nou
						$client["Codi"] = "NOU";
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
