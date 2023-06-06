SELECT dept_id, count(*) FROM md.award group by dept_id;
-- https://databasefaqs.com/postgresql-group-by/

SELECT DATE_TRUNC('day', date), count(*) FROM md.activity
    where action_code='A' and is_activ
    group by DATE_TRUNC('day',date);

SELECT a.dept_id,
       (select count(*) from md.activity i where i.dept_id = a.dept_id and i.is_activ and i.action_code='A') as award_count,
       (select count(*) from md.activity i where i.dept_id = a.dept_id and i.is_activ and i.action_code='P') as nominee_count,
       (select count(*) from md.activity i where i.dept_id = a.dept_id and i.is_activ and i.action_code='D') as delete_count
from md.activity a where a.dept_id in (81,87) group by a.dept_id;
