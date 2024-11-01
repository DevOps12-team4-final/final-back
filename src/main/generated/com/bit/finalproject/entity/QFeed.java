package com.bit.finalproject.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QFeed is a Querydsl query type for Feed
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QFeed extends EntityPathBase<Feed> {

    private static final long serialVersionUID = 1280906092L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QFeed feed = new QFeed("feed");

    public final StringPath content = createString("content");

    public final SetPath<FeedFile, QFeedFile> feedFileList = this.<FeedFile, QFeedFile>createSet("feedFileList", FeedFile.class, QFeedFile.class, PathInits.DIRECT2);

    public final NumberPath<Long> feedId = createNumber("feedId", Long.class);

    public final SetPath<FeedLike, QFeedLike> likes = this.<FeedLike, QFeedLike>createSet("likes", FeedLike.class, QFeedLike.class, PathInits.DIRECT2);

    public final DateTimePath<java.time.LocalDateTime> moddate = createDateTime("moddate", java.time.LocalDateTime.class);

    public final StringPath profileImage = createString("profileImage");

    public final DateTimePath<java.time.LocalDateTime> regdate = createDateTime("regdate", java.time.LocalDateTime.class);

    public final QUser user;

    public QFeed(String variable) {
        this(Feed.class, forVariable(variable), INITS);
    }

    public QFeed(Path<? extends Feed> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QFeed(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QFeed(PathMetadata metadata, PathInits inits) {
        this(Feed.class, metadata, inits);
    }

    public QFeed(Class<? extends Feed> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.user = inits.isInitialized("user") ? new QUser(forProperty("user")) : null;
    }

}

