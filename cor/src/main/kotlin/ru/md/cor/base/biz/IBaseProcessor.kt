package ru.md.cor.base.biz

interface IBaseProcessor<T> {
	suspend fun exec(ctx: T)
}
