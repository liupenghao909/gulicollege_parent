package com.ant.eduService.entity.chapter;

import lombok.Data;

/**
 *
 * 课程小节 VO类
 */
@Data
public class VideoVO {

    String id;

    String title;

    // 视频播放源id
    String videoSourceId;
}
