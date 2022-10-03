package com.hanghae.mungnayng.domain.member;

import com.hanghae.mungnayng.domain.Timestamped;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@Entity
public class Member extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long memberId;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false, unique = true)
    private String nickname;

    @Column(nullable = false)
    private String password;

    @Column(unique = true)
    private Long kakaoId;

    private String role;

    @Column(columnDefinition = "boolean default false", nullable = false)
    private boolean toggle;

    public void updateToggle(boolean toggle) {
        this.toggle = toggle;
    }

    public Member(String email, String nickname, String password) {
        this.email = email;
        this.nickname = nickname;
        this.password = password;
        this.kakaoId = null;
        role = "ROLE_USER";
    }
    public Member(String email, String nickname, String password, Long kakaoId) {
        this.email = email;
        this.nickname = nickname;
        this.password = password;
        this.kakaoId = kakaoId;
        role = "ROLE_USER";
    }
}
