with nowDays as (values (extract(DOY FROM now())))
    (select e.id,
            e.event_date                       eventDate,
            extract(DOY FROM event_date)       days,
            e.name                             eventName,
            (u.lastname || ' ' || u.firstname) entityName,
            u.main_img                         imageUrl,
            u.id                               userId,
            d.id                               deptid,
            d.name                             deptName,
            d.classname                        deptClassName
     from env.user_event e
              inner join users.user_data u on e.user_id = u.id
              inner join dep.dept d on u.dept_id = d.id
     where extract(DOY FROM e.event_date) >= (table nowDays)

     union

     select e.id,
            e.event_date                   eventDate,
            extract(DOY FROM event_date)   days,
            e.name                         eventName,
            (d.classname || ' ' || d.name) entityName,
            d.main_img                     imageUrl,
            0                              userId,
            d.id                           deptid,
            d.name                         deptName,
            d.classname                    deptClassName
     from env.dept_event e
              inner join dep.dept d on e.dept_id = d.id
     where extract(DOY FROM e.event_date) >= (table nowDays)
     order by days, entityName)

union all

(select e.id,
        e.event_date                       eventDate,
        extract(DOY FROM event_date)       days,
        e.name                             eventName,
        (u.lastname || ' ' || u.firstname) entityName,
        u.main_img                         imageUrl,
        u.id                               userId,
        d.id                               deptid,
        d.name                             deptName,
        d.classname                        deptClassName
 from env.user_event e
          inner join users.user_data u on e.user_id = u.id
          inner join dep.dept d on u.dept_id = d.id
 where extract(DOY FROM e.event_date) < (table nowDays)

 union

 select e.id,
        e.event_date                   eventDate,
        extract(DOY FROM event_date)   days,
        e.name                         eventName,
        (d.classname || ' ' || d.name) entityName,
        d.main_img                     imageUrl,
        0                              userId,
        d.id                           deptid,
        d.name                         deptName,
        d.classname                    deptClassName
 from env.dept_event e
          inner join dep.dept d on e.dept_id = d.id
 where extract(DOY FROM e.event_date) < (table nowDays)
 order by days, entityName);

