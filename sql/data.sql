-- DEPTS

insert into dep.dept
    (parent_id, name, code, classname)
values (null, '/', 'R', 'ROOT'),
       (1, 'Owner Dept id=2', 'U', 'Owner Dept'),
       (2, 'Company 3, id=3', 'D', 'Dept'),
       (3, 'Dept 4, id=4', 'D', 'Dept'),
       (4, 'Dept 5, id=5', 'D', 'Dept')
;;

insert into dep.dept_details (dept_id, description)
values (1, 'ROOT DEPT'),
       (2, 'Descr. dept 2'),
       (3, 'Descr. dept 3'),
       (4, 'Descr. dept 4'),
       (5, 'Descr. dept 5')
;;

insert into dep.dept_image(dept_id, image_url, image_key, type_code, main, mini_url, mini_key, origin_url, origin_key)
values (3, 'url1', 'key1', 'U', true, 'm1', 'mk1', 'o1', 'ok1'),
       (3, 'url2', 'key2', 'U', false, 'm2', 'mk2', 'o2', 'ok2'),
       (3, 'url3', 'key3', 'U', false, 'm3', 'mk3', 'o3', 'ok3')
;;

-- USERS

insert into users.user_data (dept_id, firstname, lastname, auth_email)
values (2, 'Owner User 1 - dept 2', 'User 1', 'necmedals@yandex.ru'),
       (4, 'User 2 - dept 4', 'User 2', 'necmedals@yandex.ru'),
       (4, 'User 3 - dept 4', 'User 3', 'necmedals@yandex.ru'),
       (5, 'User 4 - dept 5', 'User 4', 'necmedals@yandex.ru')
;;

insert into users.user_details (user_id, description)
values (1, 'Owner User 1 - dept 2'),
       (2, 'User 2 - dept 4'),
       (3, 'User 3 - dept 4'),
       (4, 'User 4 - dept 5')
;;

insert into users.user_roles (user_id, role_code)
values (1, 'O'),
       (1, 'A'),
       (1, 'U'),
       (2, 'U'),
       (3, 'U'),
       (3, 'A'),
       (4, 'U')
;;

-- AWARDS

insert into md.award(dept_id, name, description, start_date, end_date, type_code, main_img)
values (3, 'Award a1-d3', 'a1', '2023-01-01', '2024-01-01', 'P',
        'https://sun9-76.userapi.com/impg/nsvppU3QjXd_JZXaT4-PSBEiHkLKtKMyOjVfYQ/uh-9Seyagjg.jpg?size=888x828&quality=96&sign=98445680dd6e52fd86d636244719704f&c_uniq_tag=jRYqhUs8XdRwbaOFaNkn1Y3Q4qYo-l9qyYW_0MtBTK0&type=album'),
       (3, 'Award a2-d3', 'a2', '2023-01-01', '2024-01-01', 'S',
        'https://sun9-76.userapi.com/impg/nsvppU3QjXd_JZXaT4-PSBEiHkLKtKMyOjVfYQ/uh-9Seyagjg.jpg?size=888x828&quality=96&sign=98445680dd6e52fd86d636244719704f&c_uniq_tag=jRYqhUs8XdRwbaOFaNkn1Y3Q4qYo-l9qyYW_0MtBTK0&type=album')
;;

insert into md.award_details (award_id, criteria)
values (1, 'a1'),
       (2, 'a2')
;;

insert into md.activity (date, user_id, award_id, action_code, is_activ, auth_id)
values ('2023-01-01', 2, 1, 'P', false, 1),
       ('2023-01-02', 2, 1, 'A', false, 1),
       ('2023-01-03', 2, 1, 'D', false, 1),
       ('2023-01-04', 2, 1, 'A', true, 1),

       ('2023-01-03', 3, 1, 'P', false, 1),
       ('2023-01-04', 3, 1, 'D', false, 1)
;;