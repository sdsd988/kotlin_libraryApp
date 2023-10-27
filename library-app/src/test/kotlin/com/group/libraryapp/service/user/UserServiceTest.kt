package com.group.libraryapp.service.user

import com.group.libraryapp.domain.user.User
import com.group.libraryapp.domain.user.UserRepository
import com.group.libraryapp.domain.user.userloanhistory.UserLoanHistory
import com.group.libraryapp.domain.user.userloanhistory.UserLoanHistoryRepository
import com.group.libraryapp.domain.user.userloanhistory.UserLoanStatus
import com.group.libraryapp.dto.user.request.UserCreateRequest
import com.group.libraryapp.dto.user.request.UserUpdateRequest
import org.assertj.core.api.Assertions.*
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.transaction.annotation.Transactional

@SpringBootTest
class UserServiceTest @Autowired constructor(
    private val userRepository: UserRepository,
    private val userService: UserService,
    private val userLoanHistoryRepository: UserLoanHistoryRepository,
){

    @AfterEach
    fun clean(){
        println("CLEAN 시작")
        userRepository.deleteAll()
    }

    @Test
    @DisplayName("회원 저장 테스트")
    fun saveUserTest() {
        //given
        val request = UserCreateRequest("정상윤", null)

        //when
        userService.saveUser(request)

        //then
        val results = userRepository.findAll()
        assertThat(results).hasSize(1)
        assertThat(results[0].name).isEqualTo("정상윤")
        assertThat(results[0].age).isNull()

    }

    @Test
    @DisplayName("회원 조회 테스트")
    fun getUsersTest(){
        //given
        userRepository.saveAll(
            listOf(
                User("A", 20),
                User("B", null)
            ))

        //when
        val results = userService.getUsers()

        //then
        assertThat(results).hasSize(2)
        assertThat(results).extracting("name").containsExactlyInAnyOrder("A","B")
        assertThat(results).extracting("age").containsExactlyInAnyOrder(20, null)

    }

    @Test
    @DisplayName("회원 수정 테스트")
    fun updateUserNameTest(){
        //given
        val savedUser = userRepository.save(User("A", null))
        val request = UserUpdateRequest(savedUser.id!!, "B")

        //when
        userService.updateUserName(request)

        //then
        val result = userRepository.findAll()[0]
        assertThat(result.name).isEqualTo("B")

    }

    @Test
    @DisplayName("회원 삭제 테스트")
    fun deleteUserTest(){
        //given
        userRepository.save(User("A", null))

        //when
        userService.deleteUser("A")


        //then

        assertThat(userRepository.findAll()).isEmpty()

    }


    @Test
    @DisplayName("대출 기록이 없는 유저도 응답에 포함된다")
    fun getUserLoanHistoriesTest1(){

        //given
        userRepository.save(User("A", null))

        //when
        val result = userService.getUserLoanHistories()

        //then
        assertThat(result).hasSize(1)
        assertThat(result[0].name).isEqualTo("A")
        assertThat(result[0].books).isEmpty()


    }


    @Test
    @DisplayName("대출 기록이 많은 유저도 응답에 포함된다")
    fun getUserLoanHistoriesTest2(){

        //given
        val savedUser = userRepository.save(User("A", null))
        userLoanHistoryRepository.saveAll(listOf(
            UserLoanHistory.fixture(savedUser,"책1",UserLoanStatus.LOANED),
            UserLoanHistory.fixture(savedUser,"책2",UserLoanStatus.LOANED),
            UserLoanHistory.fixture(savedUser,"책3",UserLoanStatus.RETURNED)
        ))
        //when
        val result = userService.getUserLoanHistories()

        //then
        assertThat(result).hasSize(1)
        assertThat(result[0].name).isEqualTo("A")
        assertThat(result[0].books).hasSize(3)
        assertThat(result[0].books).extracting("name")
            .containsExactlyInAnyOrder("책1","책2","책3")
        assertThat(result[0].books).extracting("isReturn")
            .containsExactlyInAnyOrder(false,false,true)



    }

    @Test
    fun getUserLoanHistoriesTest3(){

    }






}