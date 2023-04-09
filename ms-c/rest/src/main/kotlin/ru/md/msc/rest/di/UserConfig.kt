package ru.md.msc.rest.di

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import ru.md.msc.domain.user.biz.TestBiz

@Configuration
class UserConfig {

	@Bean
	fun testBiz() = TestBiz()

//	@Bean
//	fun userProcessor() = UserProcessor()
}