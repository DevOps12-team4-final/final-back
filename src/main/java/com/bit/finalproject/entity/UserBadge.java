package com.bit.finalproject.entity;

import com.bit.finalproject.dto.UserBadgeDto;
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
    @JoinColumn(name = "detail_id", referencedColumnName = "detailId")
    @JsonBackReference
    private UserDetail userDetail;

    @ManyToOne
    @JoinColumn(name = "badge_id", referencedColumnName = "badgeId")
    private Badge badge;

    private LocalDateTime regdate;

    // UserBadge를 DTO로 변환하는 메서드
    public UserBadgeDto toDto() {
        return UserBadgeDto.builder()
                .userBadgeId(this.userBadgeId)
                .userDetailId(this.userDetail != null ? this.userDetail.getDetailId() : null)
                .badgeId(this.badge != null ? this.badge.getBadgeId() : null)
                .regdate(this.regdate)
                .build();
    }
}
