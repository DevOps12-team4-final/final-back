package com.bit.boardappbackend.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QRoomChat is a Querydsl query type for RoomChat
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QRoomChat extends EntityPathBase<RoomChat> {

    private static final long serialVersionUID = 2042652315L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QRoomChat roomChat = new QRoomChat("roomChat");

    public final DateTimePath<java.time.LocalDateTime> createdAt = createDateTime("createdAt", java.time.LocalDateTime.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath message = createString("message");

    public final QRoom room;

    public final StringPath type = createString("type");

    public final NumberPath<Long> user_id = createNumber("user_id", Long.class);

    public QRoomChat(String variable) {
        this(RoomChat.class, forVariable(variable), INITS);
    }

    public QRoomChat(Path<? extends RoomChat> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QRoomChat(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QRoomChat(PathMetadata metadata, PathInits inits) {
        this(RoomChat.class, metadata, inits);
    }

    public QRoomChat(Class<? extends RoomChat> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.room = inits.isInitialized("room") ? new QRoom(forProperty("room")) : null;
    }

}

