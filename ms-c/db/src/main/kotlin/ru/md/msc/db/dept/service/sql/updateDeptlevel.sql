update dep.dept d set level = dep.get_level(d.id);

update dep.dept d set top_level = level < 3