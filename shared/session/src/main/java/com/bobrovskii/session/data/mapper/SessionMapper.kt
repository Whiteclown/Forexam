package com.bobrovskii.session.data.mapper

import com.bobrovskii.session.data.dto.SessionDto
import com.bobrovskii.session.domain.entity.Session

fun SessionDto.toEntity() =
	Session(
		token = token,
		roles = roles
	)