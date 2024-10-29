package com.bit.finalproject.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QChatAlarm is a Querydsl query type for ChatAlarm
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QChatAlarm extends EntityPathBase<ChatAlarm> {

    private static final long serialVersionUID = 1583481617L;

    public static final QChatAlarm chatAlarm = new QChatAlarm("chatAlarm");

    public final NumberPath<Integer> count = createNumber("count", Integer.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath message = createString("message");

    public final NumberPath<Long> room_id = createNumber("room_id", Long.class);

    public final NumberPath<Long> user_id = createNumber("user_id", Long.class);

    public QChatAlarm(String variable) {
        super(ChatAlarm.class, forVariable(variable));
    }

    public QChatAlarm(Path<? extends ChatAlarm> path) {
        super(path.getType(), path.getMetadata());
    }

    public QChatAlarm(PathMetadata metadata) {
        super(ChatAlarm.class, metadata);
    }

}

