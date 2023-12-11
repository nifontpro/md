package ru.md.award.db.reward.model.mappers

import ru.md.award.db.medal.model.mappers.toMedalObj
import ru.md.award.db.reward.model.RewardEntity
import ru.md.award.domain.reward.model.Reward
import ru.md.base_db.user.model.mappers.toUser

fun RewardEntity.toReward() = Reward(
	id = id ?: 0,
	date = date,
	medalObj = medalObjEntity.toMedalObj(),
	user = userEntity.toUser()
)