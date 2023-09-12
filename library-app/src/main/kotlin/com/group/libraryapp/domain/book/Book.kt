package com.group.libraryapp.domain.book

import javax.persistence.*

@Entity
class Book (
    val name: String,

    @Enumerated(EnumType.STRING)
    val type: BookType,

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    //코틀린에서 JPA 사용을 위한 기본생성자를 생성하기 위해서는 플러그인 추가 필요

){

    init {
        if (name.isBlank()) {
            throw IllegalArgumentException("이름은 비어 있을 수 없습니다")
        }
    }

    companion object{
        fun fixture(
            name: String = "오브젝트",
            type: BookType = BookType.COMPUTER,
            id: Long? = null,
        ): Book{
            return Book(
                name = name,
                type = type,
                id = id,
            )
        }
    }
}