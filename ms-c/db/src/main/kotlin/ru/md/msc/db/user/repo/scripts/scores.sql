select sum(a.score)
from md.activity i
         left join md.award a on i.award_id = a.id
where i.user_id = 2
  and i.is_activ
  and i.action_code = 'A'