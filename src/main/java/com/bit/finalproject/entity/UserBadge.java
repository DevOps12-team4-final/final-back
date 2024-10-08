package com.bit.finalproject.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

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
    private Badge badge;
}
