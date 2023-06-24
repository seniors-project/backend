package com.side.common.constant;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ReportStatus {

    WAIT("접수"),
    CONFIRM("확인"),
    COMPLETE("완료"),
    FAIL("실패");

    private final String desc;

    @JsonValue
    public String getValue() {
        return this.name();
    }

    @JsonCreator
    public static ReportStatus of(String name) {
        for (ReportStatus obj : ReportStatus.values()) {
            if (name.equalsIgnoreCase(obj.name())) {
                return obj;
            }
        }

        throw new IllegalArgumentException(String.format("Invalid type.(%s)", name));
    }
}
