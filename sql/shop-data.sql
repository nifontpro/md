-- DEPTS

insert into shop.product
    (dept_id, name, description, price, qnt)
values (3, 'Приз 1 отдела 3', 'Описание 1 приза', 100, 10),
       (3, 'Приз 2 отдела 3', 'Описание 2 приза', 40, 5),
       (3, 'Приз 3 отдела 3', 'Описание 2 приза', 50, 0),
       (3, 'Приз 4 отдела 3', 'Описание 2 приза', 60, 5),
       (3, 'Приз 5 отдела 3', 'Описание 2 приза', 70, 0)
;;

insert into shop.product_details (product_id,  site_url, place)
values
    (1, 'https://site1.ru', 'place 1'),
    (2, 'https://site2.ru', 'place 2'),
    (3, 'https://site3.ru', 'place 3'),
    (4, 'https://site4.ru', 'place 4'),
    (5, 'https://site5.ru', 'place 5')
;;

-- Баланс 1 Сотрудника должен быть 1000 для тестирования
insert into shop.user_pay(user_id, balance)
values
    (2, 1000),
    (3, 2000)
