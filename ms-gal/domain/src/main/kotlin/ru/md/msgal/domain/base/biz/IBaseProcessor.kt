package ru.md.msgal.domain.base.biz

interface IBaseProcessor<T> {
	suspend fun exec(ctx: T)
}
