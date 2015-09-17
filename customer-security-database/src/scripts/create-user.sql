create table user (
        userid integer not null,
	username varchar(35) not null,
        password varchar(250) not null,
        primary key (userid)
);

create table role (
        roleid integer not null,
        userid integer not null,
        rolename varchar(25) not null,
        primary key (roleid)
);

alter table role add constraint fk_role_to_user foreign key (userid) references user (userid);

INSERT INTO user(userid, username, password) VALUES (1, 'kylin', 'password1!');
INSERT INTO user(userid, username, password) VALUES (2, 'user1', 'password1!');
INSERT INTO user(userid, username, password) VALUES (3, 'user2', 'password1!');

INSERT INTO role(roleid, userid, rolename) VALUES(1, 1, 'test');
INSERT INTO role(roleid, userid, rolename) VALUES(2, 2, 'test');
INSERT INTO role(roleid, userid, rolename) VALUES(3, 3, 'test');

