package com.bit.finalproject.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@SequenceGenerator(
        name = "UserBadgeSeqGenerator",
        sequenceName = "USER_BADGE_SEQ",
        initialValue = 1,
        allocationSize = 1
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class UserBadge {
    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "UserBadgeSeqGenerator"
    )
    private Long userBadgeId;
    @ManyToOne
    @JoinColumn(name = "dtail_id", referencedColumnName = "dtail_id")
    @JsonBackReference
    private MemberDtail memberDtail;
    @ManyToOne
    @JoinColumn(name = "badge_id", referencedColumnName = "badge_id")
    private Badge badge;

    private LocalDateTime regdate;
}
