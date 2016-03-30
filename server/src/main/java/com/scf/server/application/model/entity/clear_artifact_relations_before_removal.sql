DELIMITER //
create trigger clear_artifact_relations_before_removal
before delete
on artifact for each row
begin
	delete from col_art where artifactList_id = old.id;
end; //
DELIMITER ;