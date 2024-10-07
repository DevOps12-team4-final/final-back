package com.bit.finalproject.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
}
