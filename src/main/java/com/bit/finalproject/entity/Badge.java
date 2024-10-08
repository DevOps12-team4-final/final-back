package com.bit.finalproject.entity;

import com.bit.finalproject.dto.BadgeDto;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@SequenceGenerator(
        name ="BadgeSeqGenerator",
        sequenceName = "BADGE_SEQ",
        initialValue = 1,
        allocationSize = 1
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Badge {
    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "BadgeSeqGenerator"
    )
    private Long badgeId;
    private String badgeName;
    private String badgeContent;
    private String badgeGrade;
    private String badgeImage;
    @ManyToOne
    @JoinColumn(name="userId", referencedColumnName = "UserId")
    private Member member;
    private LocalDateTime createTime;
    @OneToMany
//    @JoinColum()

    public BadgeDto toDto(){
        return BadgeDto.builder()
                .badgeId(this.badgeId)
                .badgeName(this.badgeName)
                .badgeContent(this.badgeContent)
                .badgeGrade(this.badgeGrade)
                .badgeImage(this.badgeImage)
                .userId(this.member.getUserId())
                .createTime(this.createTime)
                .build();
    }
}
