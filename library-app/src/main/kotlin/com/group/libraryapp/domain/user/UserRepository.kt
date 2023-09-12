package com.group.libraryapp.domain.user

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import java.util.Optional

interface UserRepository : JpaRepository<User, Long>, UserRepositoryCustom{

    fun findByName(name: String): User?
}