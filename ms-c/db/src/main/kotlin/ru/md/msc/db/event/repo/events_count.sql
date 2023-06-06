with nowDays as (values (extract(DOY FROM now())))
select (select count(*)
        from env.user_event e
        where extract(DOY FROM e.event_date) >= (table nowDays)) +
       (select count(*)
        from env.dept_event e
        where extract(DOY FROM e.event_date) >= (table nowDays)) +
       (select count(*)
        from env.user_event e
        where extract(DOY FROM e.event_date) < (table nowDays)) +
       (select count(*)
        from env.dept_event e
        where extract(DOY FROM e.event_date) < (table nowDays));
