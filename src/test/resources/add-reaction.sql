insert into Review(id, body, title) values (1, 'Great movie!', 'Absolute classic of a movie!');
insert into Comment (id, author, body) values (1, 'Antti A', 'Great review!');
insert into Reaction (id, amount, type) values (1, 2, 'LIKE');
insert into Comment_Reaction (Comment_id, reactions_id) values (1, 1);
insert into Reaction (id, amount, type) values (2, 1, 'ANGRY');

insert into Comment_Reaction (Comment_id, reactions_id) values (1, 2);

insert into Comment (id, author, body) values (2, '', 'Your comment is not valid');
insert into Review_Comment (Review_id, comments_id) values (1, 1);

insert into Review_Comment (Review_id, comments_id) values (1, 2);
