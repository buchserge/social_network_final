delete from message_chat;

insert into message_chat( id,
                           chatroom_id,
                          created_at,
                           is_read,
                           receipt_id,
                           recipient_name,
                           sender_id,
                           sender_name,
                           text,
                           type,
                           user_id)
                           values
(1,1,null,0,2,"Paola",1,"Katty","text","CHAT",1);