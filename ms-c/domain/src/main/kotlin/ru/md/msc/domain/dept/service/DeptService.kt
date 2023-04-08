package ru.md.msc.domain.dept.service

import ru.md.msc.domain.dept.model.Dept

interface DeptService {
	fun findAll(): List<Dept>
}