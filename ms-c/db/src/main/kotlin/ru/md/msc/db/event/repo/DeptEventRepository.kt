package ru.md.msc.db.event.repo

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import ru.md.msc.db.event.model.DeptEventEntity
import ru.md.msc.db.event.model.IBaseEvent
import ru.md.msc.db.event.model.IShortEvent

@Repository
interface DeptEventRepository : JpaRepository<DeptEventEntity, Long> {

	@Query(
		"""
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
		       d.classname                        deptClassName,
					 d.level														deptLevel
		from env.user_event e
		         inner join users.user_data u on e.user_id = u.id
		         inner join dep.dept d on u.dept_id = d.id
		where d.id in :deptsIds and extract(DOY FROM e.event_date) >= (table nowDays)
		
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
		       d.classname                    deptClassName,
					 d.level												deptLevel
		from env.dept_event e
		         inner join dep.dept d on e.dept_id = d.id
		where d.id in :deptsIds and extract(DOY FROM e.event_date) >= (table nowDays)
		order by days, entityName)
		
		union all
		
		(select e.id,
		        e.event_date                       	eventDate,
		        extract(DOY FROM event_date)       	days,
		        e.name                             	eventName,
		        (u.lastname || ' ' || u.firstname) 	entityName,
		        u.main_img                         	imageUrl,
		        u.id                               	userId,
		        d.id                               	deptid,
		        d.name                             	deptName,
		        d.classname                        	deptClassName,
						d.level															deptLevel
		 from env.user_event e
		          inner join users.user_data u on e.user_id = u.id
		          inner join dep.dept d on u.dept_id = d.id
		 where d.id in :deptsIds and extract(DOY FROM e.event_date) < (table nowDays)
		
		 union
		
		 select e.id,
		        e.event_date                   		eventDate,
		        extract(DOY FROM event_date)   		days,
		        e.name                         		eventName,
		        (d.classname || ' ' || d.name) 		entityName,
		        d.main_img                     		imageUrl,
		        0                              		userId,
		        d.id                           		deptid,
		        d.name                         		deptName,
		        d.classname                    		deptClassName,
						d.level														deptLevel
		 from env.dept_event e
		          inner join dep.dept d on e.dept_id = d.id
		 where d.id in :deptsIds and extract(DOY FROM e.event_date) < (table nowDays)
		 order by days, entityName);
	""",
		countQuery = """
with nowDays as (values (extract(DOY FROM now())))
select (select count(*)
        from env.user_event e 
							inner join users.user_data u on e.user_id = u.id
		         	inner join dep.dept d on u.dept_id = d.id
        where d.id in :deptsIds and extract(DOY FROM e.event_date) >= (table nowDays)) +
       (select count(*)
        from env.dept_event e 
							inner join dep.dept d on e.dept_id = d.id
        where d.id in :deptsIds and extract(DOY FROM e.event_date) >= (table nowDays)) +
       (select count(*)
        from env.user_event e
							inner join users.user_data u on e.user_id = u.id
		         	inner join dep.dept d on u.dept_id = d.id				
        where d.id in :deptsIds and extract(DOY FROM e.event_date) < (table nowDays)) +
       (select count(*)
        from env.dept_event e 
							inner join dep.dept d on e.dept_id = d.id
        where d.id in :deptsIds and extract(DOY FROM e.event_date) < (table nowDays));			
		""",
		nativeQuery = true
	)
	fun getEvents(
		deptsIds: List<Long>,
		pageable: Pageable
	): Page<IBaseEvent>

	@Query(
		"""
		select 
			e.id,
			e.event_date eventDate,
			(extract(DOY FROM e.event_date)) days,
			e.name eventName
		 from env.dept_event e where e.dept_id=:deptId 
		 order by days, eventName
	""",
		nativeQuery = true
	)
	fun findByDeptId(deptId: Long): List<IShortEvent>
}