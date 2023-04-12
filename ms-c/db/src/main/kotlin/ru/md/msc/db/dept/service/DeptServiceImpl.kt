package ru.md.msc.db.dept.service

import jakarta.transaction.Transactional
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import ru.md.msc.domain.base.model.RepositoryData
import ru.md.msc.db.dept.model.mappers.toDept
import ru.md.msc.db.dept.model.mappers.toDeptDetails
import ru.md.msc.db.dept.model.mappers.toDeptDetailsEntity
import ru.md.msc.db.dept.repo.DeptDetailsRepository
import ru.md.msc.db.dept.repo.DeptRepository
import ru.md.msc.domain.dept.model.Dept
import ru.md.msc.domain.dept.model.DeptDetails
import ru.md.msc.domain.dept.service.DeptService

@Service
@Transactional
class DeptServiceImpl(
	private val deptRepository: DeptRepository,
	private val deptDetailsRepository: DeptDetailsRepository
) : DeptService {

	override fun create(deptDetails: DeptDetails): RepositoryData<DeptDetails> {
		val deptDetailsEntity = deptDetails.toDeptDetailsEntity(create = true)
		try {
			deptDetailsRepository.save(deptDetailsEntity)
		} catch (e: Exception) {
			log.error(e.message)
			return DeptErrors.createDept()
		}
		return RepositoryData.success(deptDetailsEntity.toDeptDetails())
	}

	override fun findAll(): List<Dept> {
		return deptRepository.findAll().map {
			it.toDept()
		}
	}

	companion object {
		var log: Logger = LoggerFactory.getLogger(DeptServiceImpl::class.java)
	}

}