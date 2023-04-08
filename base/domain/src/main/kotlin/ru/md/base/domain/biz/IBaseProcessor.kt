package ru.md.base.domain.biz

interface IBaseProcessor<T> {
	suspend fun exec(ctx: T)
}
