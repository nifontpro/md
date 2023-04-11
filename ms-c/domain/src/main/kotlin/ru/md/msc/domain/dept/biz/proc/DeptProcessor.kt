package ru.md.msc.domain.dept.biz.proc

import org.springframework.stereotype.Component
import ru.md.base.dom.biz.IBaseProcessor
import ru.md.base.dom.workers.finishOperation
import ru.md.base.dom.workers.initStatus
import ru.md.base.dom.workers.operation
import ru.md.cor.rootChain
import ru.md.msc.domain.dept.service.DeptService

@Component
class DeptProcessor(
	private val deptService: DeptService
) : IBaseProcessor<DeptContext> {

	override suspend fun exec(ctx: DeptContext) = businessChain.exec(ctx.also { it.deptService = deptService })

	companion object {

		private val businessChain = rootChain<DeptContext> {
			initStatus()

			operation("Создать отдел", DeptCommand.CREATE) {

			}

			finishOperation()
		}.build()
	}
}