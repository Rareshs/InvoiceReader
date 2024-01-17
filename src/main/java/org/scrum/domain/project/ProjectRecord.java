package org.scrum.domain.project;

import lombok.Builder;
import lombok.With;

import java.util.Objects;

@Builder(toBuilder = true) @With
public record ProjectRecord(Integer projectNo, String name) {
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ProjectRecord that = (ProjectRecord) o;

        return Objects.equals(projectNo, that.projectNo);
    }

    @Override
    public int hashCode() {
        return projectNo != null ? projectNo.hashCode() : 0;
    }

    static void test(){
        ProjectRecord data = ProjectRecord.builder().projectNo(1).name("P1").build();
    }
}

// https://medium.com/@miguelangelperezdiaz444/comparing-java-mapping-frameworks-a-performance-analysis-f36d68c729e3