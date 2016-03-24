DELIMITER //
create trigger clear_artifacts_without_collection
after delete
on collection_artifact for each row
begin
	delete from artifact where id =
	(if ((select count(*) from collection_artifact where
	artifactList_id = old.artifactList_id) = 0, old.artifactList_id, -1));
end; //
DELIMITER ;