package com.exmple.task.dto.response.task;

import lombok.*;

@RequiredArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class TaskResponse {
    private long id;
    private String title;
    private String text;
    private long time;
}
