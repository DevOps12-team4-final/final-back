package com.bit.finalproject.service;

import com.bit.finalproject.dto.FeedCommentDto;
import com.bit.finalproject.entity.FeedComment;
import com.bit.finalproject.entity.Member;

import java.util.List;

public interface FeedCommentService {

    FeedCommentDto createComment(FeedCommentDto feedCommentDto);
}
