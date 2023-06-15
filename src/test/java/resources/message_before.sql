DELETE from message;
delete from message_user;

INSERT INTO message(id,text,user_id,like_count)VALUES

(1,'again_msg',1,1),
(2,'again_msg_2',1,1);

insert into message_user(message_id,user_id)values
(1,1),
(2,1);