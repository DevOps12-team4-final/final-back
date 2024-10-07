package com.bit.finalproject.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@SequenceGenerator(
        name = "fallowSeqGenerator",
        sequenceName = "FALLOW_SEQ",
        initialValue = 1,
        allocationSize = 1
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "fallow")
public class Fallow {

    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "fallowSeqGenerator"
    )
    private Long fallowId;

    @ManyToOne
    @JoinColumn(name = "fallower_id", referencedColumnName = "userId")
    private Member fallower;  // 필드명 수정

    @ManyToOne
    @JoinColumn(name = "fallowing_id", referencedColumnName = "userId")
    private Member fallowing;  // 필드명 수정

    // FallowDto에서 Fallow 엔티티로 변환하는 메서드 (toEntity)
    public Fallow toEntity(Member fallower, Member fallowing) {
        return Fallow.builder()
                .fallowId(this.fallowId)
                .fallower(fallower)        // 팔로워 Member 객체 설정
                .fallowing(fallowing)      // 팔로잉 Member 객체 설정
                .build();
    }
}
