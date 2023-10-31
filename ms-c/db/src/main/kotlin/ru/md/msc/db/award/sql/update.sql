update md.award
set description = a.description
from md.award_details a
where id = a.award_id
