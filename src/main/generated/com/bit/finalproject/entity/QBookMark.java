package com.bit.finalproject.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QBookMark is a Querydsl query type for BookMark
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QBookMark extends EntityPathBase<BookMark> {

    private static final long serialVersionUID = 211464228L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QBookMark bookMark = new QBookMark("bookMark");

    public final NumberPath<Long> bookMarkId = createNumber("bookMarkId", Long.class);

    public final QFeed feed;

    public final QUser user;

    public QBookMark(String variable) {
        this(BookMark.class, forVariable(variable), INITS);
    }

    public QBookMark(Path<? extends BookMark> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QBookMark(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QBookMark(PathMetadata metadata, PathInits inits) {
        this(BookMark.class, metadata, inits);
    }

    public QBookMark(Class<? extends BookMark> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.feed = inits.isInitialized("feed") ? new QFeed(forProperty("feed"), inits.get("feed")) : null;
        this.user = inits.isInitialized("user") ? new QUser(forProperty("user")) : null;
    }

}

