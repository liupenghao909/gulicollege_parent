package com.ant.eduService.entity.chapter;

import lombok.Data;

import java.util.List;

/**
 * 课程章节 VO类
 *
 */
@Data
public class ChapterVO {
    String id;

    String title;

    List<VideoVO> children;
}
