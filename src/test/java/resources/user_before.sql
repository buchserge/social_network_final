delete from user_role;
delete from user;

insert into user(id, name, password, email) values
(1, 'Katty', '$2a$08$20Pj7ibX/EmHgFaNlMTlq.33i0p9XtK02KFOHk0YN29Cnw4pogMQ.', 'buchswork@gmail.com'),
(2, 'Paola', '$08$pXYPJ3Kj3Tg7dm43Gf5bOOSG28V9r35rFhqjt2RFM3tNbjAq93IdK', 'buchswork@gmail.com');

insert into user_role(user_id,roles) values
(1,'USER'),
(2,'USER');