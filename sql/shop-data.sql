-- DEPTS

insert into shop.product
    (dept_id, name, short_description, price, qnt)
values (3, 'Приз 1 отдела 3', 'Короткое описание 1 приза', 100, 10),
       (3, 'Приз 2 отдела 3', 'Короткое описание 2 приза', 40, 5),
       (3, 'Приз 3 отдела 3', 'Короткое описание 2 приза', 50, 0),
       (3, 'Приз 4 отдела 3', 'Короткое описание 2 приза', 60, 5),
       (3, 'Приз 5 отдела 3', 'Короткое описание 2 приза', 70, 0)
;;

insert into shop.product_details (product_id, description, site_url, place)
values
    (1, 'Описание 1 приза', 'https://site1.ru', 'place 1'),
    (2, 'Описание 2 приза', 'https://site2.ru', 'place 2'),
    (3, 'Описание 2 приза', 'https://site3.ru', 'place 3'),
    (4, 'Описание 2 приза', 'https://site4.ru', 'place 4'),
    (5, 'Описание 2 приза', 'https://site5.ru', 'place 5')
;;

insert into shop.product_image(product_id, image_url, image_key, type_code, main, mini_url, mini_key, origin_url, origin_key)
values (1, 'url1', 'key1', 'U', false, 'm1', 'mk1', 'o1', 'ok1'),
       (1, 'url2', 'key2', 'U', false, 'm2', 'mk2', 'o2', 'ok2'),
       (1, 'url3', 'key3', 'U', true, 'm3', 'mk3', 'o3', 'ok3')
;;

-- Баланс 1 Сотрудника должен быть 1000 для тестирования
insert into shop.user_pay(user_id, balance)
values
    (2, 1000),
    (3, 2000)
