package ru.md.base_rest

object Constants {
	const val MINI_COMPRESS_IMAGE_SIZE = 100
	const val NORM_COMPRESS_IMAGE_SIZE = 800
	const val LOCAL_FOLDER = "files"
}

private const val ENDPOINT = "--> Endpoint:"
private const val REQUEST = "-> Request:"

fun logEndpoint(url: String) = "$ENDPOINT $url"
fun <T> logRequest(body: T) = "$REQUEST ${body.toString()}"