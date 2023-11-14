package ru.md.base_domain.errors

import ru.md.base_domain.biz.proc.BaseMedalsContext

fun BaseMedalsContext.deleteImageHandler(exception: Throwable) {
	log.error(exception.message)
	when (exception) {
		is ImageNotFoundException -> imageNotFoundError()
		is S3DeleteException -> s3DeleteError()
		else -> deleteImageError()
	}
}