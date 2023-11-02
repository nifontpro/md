-- DEPTS

insert into shop.product
    (dept_id, name, price, qnt)
values (3, 'Приз 1 отдела 3', 100, 10),
       (3, 'Приз 2 отдела 3', 40, 5)
;;

insert into shop.product_details (product_id, description, site_url)
values
    (1, 'Описание 1 приза', 'https://site1.ru'),
    (2, 'Описание 2 приза', 'https://site2.ru')
;;

-- Баланс 1 Сотрудника должен быть 1000 для тестирования
insert into shop.user_pay(user_id, balance)
values
    (2, 1000),
    (3, 2000)
