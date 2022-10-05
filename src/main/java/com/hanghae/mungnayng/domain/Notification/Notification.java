package com.hanghae.mungnayng.domain.Notification;

import com.hanghae.mungnayng.domain.Notification.Dto.NotificationRequestDto;
import com.hanghae.mungnayng.domain.Timestamped;
import com.hanghae.mungnayng.domain.member.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;


@Entity
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Notification extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column
    @Enumerated(EnumType.STRING)
    private Type type;

    @Column
    private String message;

    @Column(nullable = false)
    private boolean readState;

    @ManyToOne
    @JoinColumn
    private Member member;

    public Notification(NotificationRequestDto requestDto, Member member) {
        this.type = requestDto.getType();
        this.message = requestDto.getMessage();;
        this.readState = false;
        this.member = member;
    }

    public void changeState(){
        this.readState = true;
    }
}
