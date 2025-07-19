package com.side.infrastructure.jpa.converter;

import com.side.domain.YesNoDeleteStatus;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import lombok.NonNull;

@Converter(autoApply = true)
public class YesNoDeleteStatusConverter implements AttributeConverter<YesNoDeleteStatus, String> {

    @Override
    public String convertToDatabaseColumn(@NonNull YesNoDeleteStatus attribute) {
        return attribute.getValue();
    }

    @Override
    public YesNoDeleteStatus convertToEntityAttribute(String dbData) {
        return YesNoDeleteStatus.fromCode(dbData);
    }
}