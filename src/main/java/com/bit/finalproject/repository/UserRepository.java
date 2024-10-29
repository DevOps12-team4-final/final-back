package com.bit.finalproject.repository;

import com.bit.finalproject.dto.UserDto;
import com.bit.finalproject.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String Email);

    long countByEmail(String Email);

    long countByNickname(String nickname);

    UserDto findByUserId(Long userId);
    // ACTIVE 상태의 유저 목록을 가져오기
    List<User> findByUserStatus(String status);


    // ACTIVE 상태의 유저 수를 계산
    long countByUserStatus(String status);
    // 총 사용자 수
    long count();

    // 최근 30일 내에 가입한 신규 사용자 수
    @Query("SELECT COUNT(User) FROM User User WHERE User.regdate > :fromDate")
    long countNewUsersSince(LocalDateTime fromDate);

    // 활성 사용자 수 (예시: 최근 30일 내에 로그인한 사용자)
    @Query("SELECT COUNT(User) FROM User User WHERE User.lastLoginDate > :fromDate")
    long countActiveUsersSince(LocalDateTime fromDate);

    // 사용자 이름이나 이메일로 검색 (예: 검색어를 포함하는 사용자 찾기)
    @Query("SELECT u FROM User u WHERE u.username LIKE %:keyword% OR u.email LIKE %:keyword%")
    Page<User> findByKeyword(String keyword, Pageable pageable);

    // 특정 역할을 가진 사용자 필터링
    Page<User> findByRole(String role, Pageable pageable);

    // 팔로워 수 계산 (특정 사용자를 팔로우하는 사람 수)
    @Query("SELECT COUNT(f) FROM Follow f WHERE f.following.userId = :memberId")
    int countFollowers(Long memberId);

    // 팔로잉 수 계산 (특정 사용자가 팔로우하는 사람 수)
    @Query("SELECT COUNT(f) FROM Follow f WHERE f.follower.userId = :memberId")
    int countFollowing(Long memberId);
    long countByTel(String tel);

    User findByTel(String tel);
}
