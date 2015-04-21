drop table client_id;
drop table room_contents;
create table client_id(
	idx int(4) auto_increment,
	id varchar(100) not null unique,
	primary key(idx)
)default character set utf8 collate utf8_general_ci;

create table room_contents(
	clients varchar(500),
	contents text
)default character set utf8 collate utf8_general_ci;

select * from client_id;
select * from room_contents;
SELECT clients, contents FROM room_contents WHERE clients like'%a1%'
SELECT contents FROM room_contents WHERE clients like concat('%', a1, '%');
SELECT contents FROM room_contents WHERE clients = 'a1';

====================================================
create table client_id(
	idx int(4) auto_increment,
	id varchar(50) not null unique,
	primary key(idx)
)default character set utf8 collate utf8_general_ci;

create table chat_log(
	id varchar(50),
	log varchar(21000),
	FOREIGN KEY (id) REFERENCES client_id (id) 
			ON DELETE CASCADE 
			ON UPDATE CASCADE
)default character set utf8 collate utf8_general_ci;

select * from client_id;

select * from chat_log
-- clientId No.1


select * from chat_message;
)engine=InnoDB default character set utf8 collate utf8_general_ci;

=====================================================
