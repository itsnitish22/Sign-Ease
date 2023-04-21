package com.teamdefine.domain.interactors.main

import com.teamdefine.domain.repository.ApiRepository
import javax.inject.Inject

class GetNameUseCase @Inject constructor(val apiRepository: ApiRepository) {
    operator fun invoke() = apiRepository.getName()
}