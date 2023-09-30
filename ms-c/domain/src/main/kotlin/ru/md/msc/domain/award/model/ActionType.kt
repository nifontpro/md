package ru.md.msc.domain.award.model

import ru.md.msc.domain.base.Config.WEB_APP_URL
import ru.md.msc.domain.user.model.User

enum class ActionType(val code: String, val message: String) {
	NOMINEE("P", "Номинация сотрудника на награду"),
	AWARD("A", "Присвоение награды сотруднику"),
	DELETE("D", "Снятие награды с сотрудника"),
	UNDEF("N", "Нет")
}


fun ActionType.actionMessage(): String {
	return when (this) {
		ActionType.NOMINEE -> "Поздравляем! Вы номинированы на награду"
		ActionType.AWARD -> "Поздравляем! Вы награждены наградой"
		ActionType.DELETE -> "С Вас снято награждение"
		ActionType.UNDEF -> ""
	}
}

fun ActionType.actionMessageHtml(user: User, authUser: User, award: Award): String {
	val awardImg = if (award.mainImg.isNullOrBlank()) "" else """
		<img
		        src="${award.mainImg}"
		        style="object-fit: cover"
		        width="100" height="100"
		>
	""".trimIndent()

	return when (this) {
		ActionType.NOMINEE -> """
			<html>
			<p>
			<h4>Привет, ${user.firstname}!</h4>
			</p>
			
			<div style="justify-content: center">
			<p>
			Поздравляем, ты номинирован на медаль «<a href="$WEB_APP_URL/award/${award.id}">${award.name}</a>»!
			</p>
			$awardImg
			</div>
			
			<p>
			Заходи в свой <a href="$WEB_APP_URL/user/${user.id}">профиль</a>, чтобы ознакомиться с условиями номинации, 
			выполнить их и точно получить заслуженную награду. 
			</p>

			<p>Желаем победы!</p>
			<p>Болеем за тебя!</p>

			<p>
			Тебя номинировал на медаль
			<a href="$WEB_APP_URL/user/${authUser.id}">${authUser.getFirstAndLastName()}</a>
			</p>
			
			<p>
			Команда Медалист.
			</p>
			</html>
		""".trimIndent()
		ActionType.AWARD -> """
<html>
			<p>
			<h4>Привет, ${user.firstname}!</h4>
			</p>
			
			<div style="justify-content: center">
			<p>
			Поздравляем, тебе вручили медаль «<a href="$WEB_APP_URL/award/${award.id}">${award.name}</a>»!
			</p>
			$awardImg
			</div>
			
			<p>
			Заходи в свой <a href="$WEB_APP_URL/user/${user.id}">профиль</a>, чтобы полюбоваться на медаль 
			и посмотреть сколько монет она добавила на твой счет! 
			</p>

			<p>Награда всегда найдет героя!</p>
			<p>Так держать!</p>
			
			<p>
			Тебя наградил медалью
			<a href="$WEB_APP_URL/user/${authUser.id}">${authUser.getFirstAndLastName()}</a>
			</p>
			
			<p>
			Команда Медалист.
			</p>
			</html>			
		""".trimIndent()
		ActionType.DELETE -> "С Вас снято награждение"
		ActionType.UNDEF -> ""
	}
}