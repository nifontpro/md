insert into rew.medal(dept_id, name, score)
values (3, 'Test medal 1', 10)
;;

insert into rew.medal_details (medal_id, description)
values (1, 'm1 description')
;;

insert into rew.medal_image(medal_id, image_url, image_key, type_code, main, mini_url, mini_key, origin_url, origin_key)
values (1, 'url1', 'key1', 'U', false, 'm1', 'mk1', 'o1', 'ok1'),
       (1, 'url2', 'key2', 'U', false, 'm2', 'mk2', 'o2', 'ok2'),
       (1, 'url3', 'key3', 'U', true, 'm3', 'mk3', 'o3', 'ok3')
;;
