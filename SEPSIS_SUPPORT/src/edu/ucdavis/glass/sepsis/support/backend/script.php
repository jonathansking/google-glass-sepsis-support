<?php
	

	//the script to populate the entire currentState and stateID column
	include "dbconnect.php";

	echo 'Begin Updating DB currentState and stateID column<br><br><br>';
	function updateRow( $db, $entryID, $cur_state, $cur_stateID)
	{
		$sql = "UPDATE `patients` SET currentState='". $cur_state . "', stateID = ". $cur_stateID . " WHERE entryID = ". $entryID;
		$stmt = $db->prepare( $sql );
		if ( !$stmt->execute() )
		{
			echo '    ' . $entryID. '----' . $cur_state . ' ERROR<br>';
			exit;
		}
		
		echo $sql . '<br>';
	}

	$total_state_transitions = 0;
	$cur_state = "";
	$cur_stateID = "";
	for ( $entryID = 1 ; $entryID <= 26486; $entryID++ )
	{
		$sql = "SELECT * FROM `patients` where entryID = " . $entryID;
		$stmt = $db->prepare( $sql );

		
		if ( $stmt->execute() )
		{
			$obj = $stmt->fetch(PDO::FETCH_OBJ);



			if( $obj->STATE != "" )
			{
				echo 'state change at entryID = ' . $entryID . '<br>';
				$cur_state = $obj->STATE;
				$cur_stateID = $obj->stateID;
				updateRow( $db, $entryID, $cur_state, $cur_stateID);
				
				$total_state_transitions++;
			}
			else if ( $obj->STATE == "")
			{
				updateRow( $db, $entryID, $cur_state, $cur_stateID);
			}

		}

	}

	echo '<br><br>Updates done. Total State Transitions were <br>' . $total_state_transitions;

?>