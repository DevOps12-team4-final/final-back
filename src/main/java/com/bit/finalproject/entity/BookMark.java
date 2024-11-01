package com.bit.finalproject.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookMark {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bookMarkId;

    @ManyToOne
    @JoinColumn(name = "feed_id")
    private Feed feed;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
