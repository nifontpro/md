insert into dep.dept
    (parent_id, name, code, classname, top_level)
values (null, '/', 'R', 'ROOT', true),
       (1, 'Owner Dept id=2', 'U', 'Owner Dept', false),
       (2, 'Dept 3, id=3', 'D', 'Dept', false),
       (2, 'Dept 4, id=4', 'D', 'Dept', false),
       (2, 'Dept 5, id=5', 'D', 'Dept', false)
;;

insert into dep.dept_details (dept_id, description)
values
    (1, 'ROOT DEPT'),
    (2, 'Descr. dept 2'),
    (3, 'Descr. dept 3'),
    (4, 'Descr. dept 4'),
    (5, 'Descr. dept 5')
;;

insert into users.user_data (dept_id, firstname, lastname)
values (2, 'Owner User 1 - dept 2', 'User 1'),
       (3, 'User 2 - dept 3', 'User 2'),
       (3, 'User 3 - dept 3', 'User 3'),
       (3, 'User 4 - dept 3', 'User 4')
;;

insert into users.user_details (user_id, description)
values (1, 'Owner User 1 - dept 2'),
       (2, 'User 2 - dept 3'),
       (3, 'User 3 - dept 3'),
       (4, 'User 4 - dept 3')
;;

insert into users.user_roles (user_id, role_code)
values (1, 'O'),
       (1,'A'),
       (1,'U'),
       (2,'U'),
       (3,'U'),
       (4,'U')
;;


