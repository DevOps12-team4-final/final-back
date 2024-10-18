package com.bit.finalproject.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QFeedFile is a Querydsl query type for FeedFile
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QFeedFile extends EntityPathBase<FeedFile> {

    private static final long serialVersionUID = -1985288440L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QFeedFile feedFile = new QFeedFile("feedFile");

    public final QFeed feed;

    public final NumberPath<Long> feedFileId = createNumber("feedFileId", Long.class);

    public final StringPath filename = createString("filename");

    public final StringPath fileoriginname = createString("fileoriginname");

    public final StringPath filepath = createString("filepath");

    public final StringPath filestatus = createString("filestatus");

    public final StringPath filetype = createString("filetype");

    public final StringPath newfilename = createString("newfilename");

    public QFeedFile(String variable) {
        this(FeedFile.class, forVariable(variable), INITS);
    }

    public QFeedFile(Path<? extends FeedFile> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QFeedFile(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QFeedFile(PathMetadata metadata, PathInits inits) {
        this(FeedFile.class, metadata, inits);
    }

    public QFeedFile(Class<? extends FeedFile> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.feed = inits.isInitialized("feed") ? new QFeed(forProperty("feed"), inits.get("feed")) : null;
    }

}

