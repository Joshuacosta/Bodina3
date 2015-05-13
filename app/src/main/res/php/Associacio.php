<?php 
//
// Info : Aquest PHP es el encarregat de la administració de les associacions entre el client i les entitats
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
			case 0: // Solicitem associacio
				/* Recuperem les dades d'entrada */
				$CodiClient         = $_POST['CodiClient'];
				$CodiEntitat		= $_POST['CodiEntitat'];
				$DataPeticio		= $_POST['DataPeticioAssociacio'];
				$Contacte			= $_POST['ContacteAssociacio'];
				$Descripcio			= $_POST['DescripcioAssociacio'];
				$eMail				= $_POST['eMailAssociacio'];
				//
				$result = mysql_query("INSERT INTO Associacio (CodiClient, CodiEntitat, DataPeticio, Contacte, Descripcio, eMail, DataAlta, DataDarrerCanvi, Estat)
													VALUES ('".$CodiClient."',
															'".$CodiEntitat."',
															'".$DataPeticio."',
															'".addslashes($Contacte)."',
															'".addslashes($Descripcio)."',
															'".$eMail."',
															'".$Ara."','".$Ara."', TRUE)");
				if (!$result){
					$response["valids"] = "2";
					$gestor = fopen("errors/bd.txt","a");
					fwrite($gestor,$Avui.">>> Associacio.PHP//Clients//Insert//".mysql_errno()."//".mysql_error()."//Values:".$CodiClient."/".$CodiEntitat."/".$DataPeticio."/".$Contacte."/".$Descripcio."/".$eMail."\n");
					fclose($gestor);
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
