package com.side.infrastructure.jooq.converter;

import com.side.domain.YesNoDeleteStatus;
import lombok.NonNull;
import org.jooq.Converter;

import java.util.Arrays;

public class YesNoDeleteStatusConverter implements Converter<String, YesNoDeleteStatus> {
    @Override
    public YesNoDeleteStatus from(String databaseObject) {
        if (databaseObject == null) return null;
        return Arrays.stream(YesNoDeleteStatus.values())
                     .filter(status -> status.getValue().equals(databaseObject))
                     .findFirst()
                     .orElse(null);
    }

    @Override
    public String to(YesNoDeleteStatus userObject) {
        return userObject != null ? userObject.getValue() : null;
    }

    @Override
    @NonNull
    public Class<String> fromType() {
        return String.class;
    }

    @Override
    @NonNull
    public Class<YesNoDeleteStatus> toType() {
        return YesNoDeleteStatus.class;
    }
}