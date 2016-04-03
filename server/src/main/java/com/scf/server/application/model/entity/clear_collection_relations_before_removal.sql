DELIMITER //
create trigger clear_collection_relations_before_removal
before delete
on collection for each row
	begin
		delete from col_col where collectionList_id = old.id;
		delete from col_art where collection_id = old.id;
	end; //
DELIMITER ;