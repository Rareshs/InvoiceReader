package org.scrum.domain.project;

import lombok.Builder;
import lombok.Data;

@Data @Builder
public class ProjectData {
    private Integer projectNo;
    private String name;

    static void test(){
        ProjectData data = ProjectData.builder().projectNo(1).name("P1").build();
    }
}
