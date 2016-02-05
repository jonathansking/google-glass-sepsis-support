<?php
	include "dbconnect.php";
	
	date_default_timezone_set('America/Los_Angeles');


	function getcurrentState( $db, $patient_id )
	{
		$result_status = "0";
		$sql = "SELECT  patients.patientID AS patientID, patients.stateID,patients.currentState 
		FROM patients
		WHERE patients.patientID = " . $patient_id ."
		ORDER BY patients.entryID DESC 
		LIMIT 1";

		$stmt = $db->prepare( $sql );
		if ( $stmt->execute() )    //returns true on success
		{
		    while ( $obj = $stmt->fetch(PDO::FETCH_OBJ) ) 
			{	

				return $obj->currentState;
			}
		}
		//end of getting currentstate
	}


	if ( isset($_REQUEST["patient_id"]) && $_REQUEST["patient_id"] != "" )
	{
		//get overview data
		$patient_id  = $_REQUEST["patient_id"]; 

		$patient_exists = FALSE; 
		$result_status = "0";
		$sql = "SELECT patientinfo.patientID, patientinfo.name, patientinfo.sex, patientinfo.hosp_admission, patientinfo.dob, patients.currentState
				FROM patients, patientinfo
				WHERE patientinfo.patientID = ". $patient_id . "
				AND patientinfo.patientID = patients.patientID
				ORDER BY patients.entryID DESC 
				LIMIT 1";

		$stmt = $db->prepare( $sql );

		if ( $stmt->execute() )    //returns true on success
		{
			// fetch object array 
			//echo 'entered';
		    while ( $obj = $stmt->fetch(PDO::FETCH_OBJ) ) 
			{
				$overview["name"] = $obj->name;
				$overview["dob"] = date( "m/d/y", strtotime($obj->dob) );
				$overview["gender"] = $obj->sex;
				$overview["admission_time_stamp"] = date( "m/d/y H:m", strtotime($obj->hosp_admission) );
				$overview["current_state"] = $obj->currentState;
				$patient_exists = TRUE;  
				
			}

			$result_status = "1";  //setting it to 1 if the $stmt->execute() is true ie. no db error
			$result[$patient_id]["Overview"] = $overview; //if no results $overview is null and $patient_exists = FALSE. 
			
		}
		//end of overview

		//get all the data for vitals view
		if ( $patient_exists && $result_status == "1" )
		{
			$result_status = "0";
			//need to select vitals.patientID as using $obj->patientID down below
			$sql = "SELECT  vitals.patientID, vitals.id AS vitalID,vitals.stateID, vitals.Temperature, vitals.Respiratory, 
							vitals.WBC, vitals.SBP, vitals.MAP, vitals.bacteria_in_blood
					FROM vitals
					WHERE vitals.patientID = " . $patient_id ."
					ORDER BY vitals.id DESC
					LIMIT 10";
				
			$stmt = $db->prepare( $sql );

			if ( $stmt->execute() )    //returns true on success
			{
				$index = 0;
			    while ( $obj = $stmt->fetch(PDO::FETCH_OBJ) ) 
				{	
					$vital[$index]['temperature'] = $obj->Temperature;
					$vital[$index]['respiratory_rate'] = $obj->Respiratory;
					$vital[$index]['WBC'] = $obj->WBC;
					$vital[$index]['SBP'] = $obj->SBP;
					$vital[$index]['MAP'] = $obj->MAP;

					if ($index == 0) 
					{ //executes only in the first iteration
						$bacteria_in_blood = $obj->bacteria_in_blood; //get the bacteria_in_blood field's value for the latest vital					

					}
					$index++;
				}

				$result[$patient_id]["Vitals"]["bacteria_in_blood"] = $bacteria_in_blood;
				$result[$patient_id]["Vitals"]["state"] = getcurrentState( $db, $patient_id ); 
				$result[$patient_id]["Vitals"]["other_vitals"] = $vital;
				$result_status = "1";
			}
			//end of getting vitals for vitals view

		}
		//end of vitals view


		//get all the data for events view
		if ( $patient_exists  && $result_status == "1" )
		{
			$result_status = "0";
			$sql = "SELECT events.id AS eventID, events.patientID, events.stateID, states.States as stateName, events.name, events.info, events.time
				FROM events , states
				WHERE events.patientID = " .$patient_id . "
				AND events.stateID = states.stateID
				ORDER BY events.time DESC 
				LIMIT 10";
				
			$stmt = $db->prepare( $sql );

			if ( $stmt->execute() )    //returns true on success
			{
				$index = 0;
			    while ( $obj = $stmt->fetch(PDO::FETCH_OBJ) ) 
				{
					$event[$index]['time_stamp'] = date( "m/d/y H:i", strtotime($obj->time) );
					$event[$index]['event'] = $obj->name;
					$event[$index]['attribute'] = $obj->info;
					$event[$index]['state'] = $obj->stateName;						
					$index++;
				}
				$result[$patient_id]["Events"] = $event;
				$result_status = "1";
			}
		}
		//end of events view

		//get all data for decision support 
		if( $patient_exists  && $result_status == "1")
		{
			$result_status = "0";
			$sql = "SELECT decision_support.id,decision_support.patientID, 
					states.States as currentState, decision_support.stateID,optimal_action,
					alternative_action, next_probable_state as NPS,probability_percent as PP
				FROM decision_support, states
				WHERE decision_support.stateID = states.stateID
				AND patientID =  " .$patient_id . "
				ORDER BY id DESC
				LIMIT 1";
				

			//echo '<br>'.$sql .'<br>';
			$stmt = $db->prepare( $sql );

			if ( $stmt->execute() )    //returns true on success
			{
				$index = 0;
			    while ( $obj = $stmt->fetch(PDO::FETCH_OBJ) ) 
				{
					$decision_support['current_State'] = $obj->currentState;
					$decision_support['optimal_action'] = $obj->optimal_action;
					$decision_support['alternative_action'] = $obj->alternative_action;
					$decision_support['next_probable_state'] = $obj->NPS . ' ('.$obj->PP.'%)';
					$index++;
				}
				$result[$patient_id]["Decision_Support"] = $decision_support;
				$result_status = "1";
			}
		}

		//meaning for all of overview,vitals,events and decision support, $stmt->execute() was true. Doesnt matter if a result wasnt returned. 
		//if no result then just null. ie "Events":null as $event(similarly $vital, $decision_support) arrays wont be set as never enters the while loop
		if ( $patient_exists  && $result_status == "1") 
		{
			$result[$patient_id]["result_status"] = "success";
			echo json_encode( $result[$patient_id] );
		}
		else 
		{
			echo json_encode( array( "result_status" => "error"));
		}
	}		
	else //glass requests with no $patient_id, or with $patient_id == "";
	{
		echo json_encode( array( "result_status" => "error"));
	}

?>