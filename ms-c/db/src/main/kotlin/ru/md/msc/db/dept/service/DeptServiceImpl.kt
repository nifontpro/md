package ru.md.msc.db.dept.service

import jakarta.transaction.Transactional
import org.springframework.stereotype.Service
import ru.md.msc.db.dept.model.mappers.toDept
import ru.md.msc.db.dept.repo.DeptRepository
import ru.md.msc.domain.dept.model.Dept
import ru.md.msc.domain.dept.service.DeptService

@Service
@Transactional
class DeptServiceImpl(
	private val deptRepository: DeptRepository
) : DeptService {

	override fun findAll(): List<Dept> {
		return deptRepository.findAll().map {
			it.toDept()
		}
	}
}