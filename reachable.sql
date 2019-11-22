DELIMITER \\

DROP PROCEDURE IF EXISTS graph.reachable;
CREATE PROCEDURE graph.reachable (IN startNode VARCHAR(10), OUT nodename VARCHAR(10))
BEGIN

	SET @a = 'DROP TABLE IF EXISTS result';
	PREPARE stmt1 FROM @a;
	EXECUTE stmt1;
	DEALLOCATE PREPARE stmt1;
	SET @b = 'CREATE TABLE result(startnode varchar(10), endnode varchar(10), cost int, primary key (startnode,endnode), foreign key (startnode) references nodes(nodename), foreign key (endnode) references nodes(nodename))';
	PREPARE stmt2 FROM @b;
	EXECUTE stmt2;
	DEALLOCATE PREPARE stmt2;

	SET @c = 'DROP TABLE IF EXISTS temp';
	PREPARE stmt3 FROM @v;
	EXECUTE stmt3;
	DEALLOCATE PREPARE stmt3;
	SET @d = 'CREATE TABLE temp(startnode varchar(10), endnode varchar(10), cost int, primary key (startnode,endnode), foreign key (startnode) references nodes(nodename), foreign key (endnode) references nodes(nodename))';
	PREPARE stmt4 FROM @d;
	EXECUTE stmt4;
	DEALLOCATE PREPARE stmt4;

	INSERT INTO temp SELECT * FROM edges WHERE edges.startnode=startNode;

	SET @e = 'DROP TABLE IF EXISTS newneighbors';
	PREPARE stmt5 FROM @e;
	EXECUTE stmt5;
	DEALLOCATE PREPARE stmt5;
	SET @f = 'CREATE TABLE newneighbors(startnode varchar(10), endnode varchar(10), cost int, primary key (startnode,endnode), foreign key (startnode) references nodes(nodename), foreign key (endnode) references nodes(nodename))';
	PREPARE stmt6 FROM @f;
	EXECUTE stmt6;
	DEALLOCATE PREPARE stmt6;
	
	CALL reach;
	
    SELECT endnode FROM result;
END\\

DELIMITER \\

DROP PROCEDURE IF EXISTS graph.reach;
CREATE PROCEDURE graph.reach ()
BEGIN

	INSERT INTO result SELECT * FROM temp;
	TRUNCATE newneighbors;
	INSERT INTO newneighbors SELECT * FROM edges INNER JOIN temp ON temp.endnode=edges.startnode;

	IF EXISTS (SELECT * FROM newneighbors) THEN
		TRUNCATE temp;
		INSERT INTO temp SELECT * FROM newneighbors;
		call reach;
	END IF;

END\\