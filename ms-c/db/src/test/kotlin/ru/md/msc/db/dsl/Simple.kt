package ru.md.msc.db.dsl

import ru.md.msc.db.dsl.user.dsl.buildUser
import ru.md.msc.db.dsl.user.models.Action

fun sout(block: () -> Any?) {
	val result = block()
	println(result)
}

class MyContext {
	fun time() = System.currentTimeMillis()
//    fun time(): Long {
//        return System.currentTimeMillis()
//    }
}

fun soutWithTimestamp(block: MyContext.() -> Any?) {
	val context = MyContext()
	print(context.time().toString() + ":")
	val result = block(context)
	println(result)
}

infix fun String.time(value: String): String {
	return "$this:$value"
}


fun main() {

	val user = buildUser {
		name {
			first = "Nifont"
			last = "Bus"
		}
		actions {
			add(Action.ADD)
			add("WRITE")
			+Action.CREATE
			+Action.UPDATE
		}
	}
	println(user)

//	sout {
//		123
//	}
//
//	soutWithTimestamp {
//		"test"
//	}
//
//	val pair = Pair("key", "value")
//
//	val pairNew = "key" to "value"
//
//	val myTimeOld = "12".time("30")
//
//	val myTime = "12" time "30"
//	println(myTime)
}
