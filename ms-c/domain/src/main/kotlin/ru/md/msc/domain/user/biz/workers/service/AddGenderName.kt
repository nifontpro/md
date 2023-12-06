package ru.md.msc.domain.user.biz.workers.service

import ru.md.base_domain.biz.proc.ContextState
import ru.md.cor.ICorChainDsl
import ru.md.cor.worker
import ru.md.msc.domain.user.biz.proc.UserContext
import java.io.BufferedReader
import java.io.File
import java.io.FileReader

fun ICorChainDsl<UserContext>.addGender(title: String) = worker {

	this.title = title
	on { state == ContextState.RUNNING }

	handle {
//		val file = File("/Users/nifont/prog/less/ru-names/lists/male_names_rus.txt")
		//		val file = File("/Users/nifont/prog/less/ru-names/lists/female_names_rus.txt")
		val file = File("/Users/nifont/prog/less/ru-names/lists/male_surnames_rus.txt")
		BufferedReader(FileReader(file)).use { br ->
			var name: String?
			while (br.readLine().also { name = it } != null) {
//				name?.let { genderService.addMaleName(it) }
//				name?.let { genderService.addFemaleName(it) }
				name?.let { genderService.addMaleLastname(it) }
			}
		}
	}

	except {
		log.error(it.message)
	}

}