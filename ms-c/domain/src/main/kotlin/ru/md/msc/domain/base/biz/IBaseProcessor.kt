package ru.md.msc.domain.base.biz

interface IBaseProcessor<T> {
	suspend fun exec(ctx: T)
}
