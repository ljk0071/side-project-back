package com.side.infrastructure.jpa.converter;

import com.side.domain.enums.PartyApplicationStatusTypeEnum;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import lombok.NonNull;

@Converter(autoApply = true)
public class PartyApplicationStatusConverter implements AttributeConverter<PartyApplicationStatusTypeEnum, String> {

    @Override
    public String convertToDatabaseColumn(@NonNull PartyApplicationStatusTypeEnum attribute) {
        return attribute.getValue();
    }

    @Override
    public PartyApplicationStatusTypeEnum convertToEntityAttribute(String dbData) {
        return PartyApplicationStatusTypeEnum.fromCode(dbData);
    }
}