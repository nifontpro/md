package ru.md.base_domain.dirty.service

import ru.md.base_domain.dirty.model.DirtyLink

interface DirtyLinkService {
	fun add(dirtyLink: DirtyLink): DirtyLink
}