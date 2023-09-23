-- DEPTS

insert into dep.dept
    (parent_id, name, code, classname)
values (null, '/', 'R', 'ROOT'),
       (1, 'Owner Dept id=2', 'U', 'Owner Dept'),
       (2, 'Dept 3, id=3', 'D', 'Dept'),
       (2, 'Dept 4, id=4', 'D', 'Dept'),
       (2, 'Dept 5, id=5', 'D', 'Dept')
;;

insert into dep.dept_details (dept_id, description)
values
    (1, 'ROOT DEPT'),
    (2, 'Descr. dept 2'),
    (3, 'Descr. dept 3'),
    (4, 'Descr. dept 4'),
    (5, 'Descr. dept 5')
;;

-- USERS

insert into users.user_data (dept_id, firstname, lastname, auth_email)
values (2, 'Owner User 1 - dept 2', 'User 1', 'necmedals@yandex.ru'),
       (3, 'User 2 - dept 3', 'User 2', null),
       (3, 'User 3 - dept 3', 'User 3', null),
       (4, 'User 4 - dept 4', 'User 4', 'necmedals@yandex.ru')
;;

insert into users.user_details (user_id, description)
values (1, 'Owner User 1 - dept 2'),
       (2, 'User 2 - dept 3'),
       (3, 'User 3 - dept 3'),
       (4, 'User 4 - dept 4')
;;

insert into users.user_roles (user_id, role_code)
values (1, 'O'),
       (1,'A'),
       (1,'U'),
       (2,'U'),
       (3,'U'),
       (4,'U')
;;

-- AWARDS

insert into md.award(dept_id, name, start_date, end_date, type_code)
values (3, 'Award a1-d3', '2023-01-01', '2024-01-01', 'P')
;;

insert into md.award_details (award_id, description)
values (1, 'a1')
;;

insert into md.activity (date, user_id, award_id, action_code, is_activ, dept_id, auth_id)
values ('2023-01-01', 2, 1, 'P', false, 3, 1),
       ('2023-01-02', 2, 1, 'A', false, 3, 1),
       ('2023-01-03', 2, 1, 'D', false, 3, 1),
       ('2023-01-04', 2, 1, 'A', true, 3, 1),

       ('2023-01-03', 3, 1, 'P', false, 3, 1),
       ('2023-01-04', 3, 1, 'D', false, 3, 1)
;;