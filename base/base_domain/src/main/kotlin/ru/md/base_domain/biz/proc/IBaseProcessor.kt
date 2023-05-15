package ru.md.base_domain.biz.proc

interface IBaseProcessor<T> {
	suspend fun exec(ctx: T)
}
