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
