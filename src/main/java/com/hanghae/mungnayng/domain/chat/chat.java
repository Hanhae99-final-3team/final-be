package com.hanghae.mungnayng.domain.chat;

import com.hanghae.mungnayng.domain.Timestamped;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

public class chat extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
}
