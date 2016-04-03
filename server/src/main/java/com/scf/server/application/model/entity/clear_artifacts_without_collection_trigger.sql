DELIMITER //
create trigger clear_artifacts_without_collection
after delete
on col_art for each row
begin
	delete from artifact where id not in (select artifactList_id from col_art);
end; //
DELIMITER ;