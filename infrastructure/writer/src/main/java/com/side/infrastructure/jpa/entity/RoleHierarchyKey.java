package com.side.infrastructure.jpa.entity;

import jakarta.persistence.Embeddable;
import lombok.*;
import org.hibernate.annotations.Comment;

import java.io.Serial;
import java.io.Serializable;

@Getter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Embeddable
public class RoleHierarchyKey implements Serializable {

    @Serial
    private static final long serialVersionUID = -5285826462359427736L;

    @Comment("하위 역할")
    private String lowerRole;

    @Comment("상위 역할")
    private String higherRole;
}