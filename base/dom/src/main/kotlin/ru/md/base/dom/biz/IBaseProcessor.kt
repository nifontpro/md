package ru.md.base.dom.biz

interface IBaseProcessor<T> {
	suspend fun exec(ctx: T)
}
