package ru.md.base_s3.di

import com.amazonaws.auth.AWSStaticCredentialsProvider
import com.amazonaws.auth.BasicAWSCredentials
import com.amazonaws.auth.profile.ProfileCredentialsProvider
import com.amazonaws.client.builder.AwsClientBuilder
import com.amazonaws.services.s3.AmazonS3
import com.amazonaws.services.s3.AmazonS3ClientBuilder
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class S3Module(
	@Value("\${s3.access.id}") private val accessKey: String,
	@Value("\${s3.access.key}") private val secretKey: String,
	@Value("\${s3.service.endpoint}") private val serviceEndpoint: String,
	@Value("\${s3.signing.region}") private val signingRegion: String,
) {

	@Bean
	fun amazonS3(): AmazonS3 {
		val credentials = try {
			ProfileCredentialsProvider().credentials
		} catch (e: Exception) {
			BasicAWSCredentials(accessKey, secretKey)
		}

		return AmazonS3ClientBuilder.standard()
			.withCredentials(AWSStaticCredentialsProvider(credentials))
			.withEndpointConfiguration(
				AwsClientBuilder.EndpointConfiguration(serviceEndpoint, signingRegion)
			)
			.build()
	}
}