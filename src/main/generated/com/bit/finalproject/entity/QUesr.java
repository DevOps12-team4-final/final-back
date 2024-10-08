package com.bit.finalproject.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QUesr is a Querydsl query type for Uesr
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QUesr extends EntityPathBase<Uesr> {

    private static final long serialVersionUID = 1281353405L;

    public static final QUesr uesr = new QUesr("uesr");

    public final StringPath email = createString("email");

    public final DateTimePath<java.time.LocalDateTime> lastLoginDate = createDateTime("lastLoginDate", java.time.LocalDateTime.class);

    public final ListPath<FeedLike, QFeedLike> likes = this.<FeedLike, QFeedLike>createList("likes", FeedLike.class, QFeedLike.class, PathInits.DIRECT2);

    public final DateTimePath<java.time.LocalDateTime> moddate = createDateTime("moddate", java.time.LocalDateTime.class);

    public final StringPath nickname = createString("nickname");

    public final StringPath password = createString("password");

    public final StringPath profileImage = createString("profileImage");

    public final DateTimePath<java.time.LocalDateTime> regdate = createDateTime("regdate", java.time.LocalDateTime.class);

    public final StringPath role = createString("role");

    public final NumberPath<Long> userId = createNumber("userId", Long.class);

    public final StringPath username = createString("username");

    public final EnumPath<UserStatus> userStatus = createEnum("userStatus", UserStatus.class);

    public QUesr(String variable) {
        super(Uesr.class, forVariable(variable));
    }

    public QUesr(Path<? extends Uesr> path) {
        super(path.getType(), path.getMetadata());
    }

    public QUesr(PathMetadata metadata) {
        super(Uesr.class, metadata);
    }

}

