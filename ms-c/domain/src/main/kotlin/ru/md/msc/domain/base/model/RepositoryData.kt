package ru.md.msc.domain.base.model

data class RepositoryData<out T>(
	val data: T? = null,
	val success: Boolean = true,
	val error: RepositoryError? = null
) {
	companion object {
		fun <T> success(data: T? = null) = RepositoryData(data = data, success = true)
		fun error(error: RepositoryError) = RepositoryData(data = null, success = false, error = error)
	}
}

data class RepositoryError(
	val repository: String,
	val violationCode: String,
	val description: String,
)