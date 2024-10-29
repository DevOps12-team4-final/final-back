package com.bit.finalproject.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QUser is a Querydsl query type for User
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QUser extends EntityPathBase<User> {

    private static final long serialVersionUID = 2090652563L;

    public static final QUser user = new QUser("user");

    public final StringPath email = createString("email");

    public final DateTimePath<java.time.LocalDateTime> lastlogindate = createDateTime("lastlogindate", java.time.LocalDateTime.class);

    public final DateTimePath<java.time.LocalDateTime> moddate = createDateTime("moddate", java.time.LocalDateTime.class);

    public final StringPath nickname = createString("nickname");

    public final StringPath password = createString("password");

    public final StringPath profileimage = createString("profileimage");

    public final DateTimePath<java.time.LocalDateTime> regdate = createDateTime("regdate", java.time.LocalDateTime.class);

    public final NumberPath<Long> user_id = createNumber("user_id", Long.class);

    public final StringPath username = createString("username");

    public final StringPath userstatus = createString("userstatus");

    public QUser(String variable) {
        super(User.class, forVariable(variable));
    }

    public QUser(Path<? extends User> path) {
        super(path.getType(), path.getMetadata());
    }

    public QUser(PathMetadata metadata) {
        super(User.class, metadata);
    }

}

