package ru.md.msc.s3.di

import com.amazonaws.AmazonClientException
import com.amazonaws.auth.AWSStaticCredentialsProvider
import com.amazonaws.auth.BasicAWSCredentials
import com.amazonaws.auth.profile.ProfileCredentialsProvider
import com.amazonaws.client.builder.AwsClientBuilder
import com.amazonaws.services.s3.AmazonS3
import com.amazonaws.services.s3.AmazonS3ClientBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class S3Module {

	@Bean
	fun amazonS3(): AmazonS3 {
		val credentials = try {
			ProfileCredentialsProvider().credentials
		} catch (e: Exception) {

			val accessKey = System.getenv("S3_ACCESS_KEY")
			val secretKey = System.getenv("S3_SECRET_KEY")
			if (accessKey == null || secretKey == null) {
				throw AmazonClientException(
					"Cannot load the credentials from the credential profiles file. " +
							"Please make sure that your credentials file is at the correct " +
							"location (~/.aws/credentials), and is in valid format.",
					e
				)
			} else {
				BasicAWSCredentials(accessKey, secretKey)
			}
		}

		val s3: AmazonS3 = AmazonS3ClientBuilder.standard()
			.withCredentials(AWSStaticCredentialsProvider(credentials))
			.withEndpointConfiguration(
				AwsClientBuilder.EndpointConfiguration(
					"storage.yandexcloud.net", "ru-central1"
				)
			)
			.build()
		return s3
	}
}