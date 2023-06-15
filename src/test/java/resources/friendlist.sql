delete from user_role;
delete from user;
delete from user_followers;
delete from image_info;

insert into user(id, name, password, email) values
(3, 'qwe', '$08$h2TrJ4RPyR1yIwuRhMq4AufWfB77yNSXhMsdhdC5hAf/jNedVR3SK', 'buchswork@gmail.com'),
(1, 'Katty', '$08$20Pj7ibX/EmHgFaNlMTlq.33i0p9XtK02KFOHk0YN29Cnw4pogMQ.', 'buchswork@gmail.com'),
(2, 'Paola', '$08$pXYPJ3Kj3Tg7dm43Gf5bOOSG28V9r35rFhqjt2RFM3tNbjAq93IdK', 'buchswork@gmail.com');

insert into user_role(user_id,roles) values
(3,'USER'),
(1,'USER'),
(2,'USER');

insert into user_followers(follower_id,friend_id)values
(1,2),
(1,3);

INSERT INTO image_info(id,image,name,user_id)VALUES
(1,null,"111",1),
(2,null,"222",2),
(3,null,"333",3);