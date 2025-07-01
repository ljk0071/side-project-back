package com.side.infrastructure.jpa.entity;

import java.util.ArrayList;
import java.util.List;

import com.side.infrastructure.jpa.common.MetadataEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Comment;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@Entity
@Table(name = "role")
@EntityListeners(AuditingEntityListener.class)
public class RoleEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Comment("역할id")
    private Long id;

    @Version
    @Column(name = "revision", nullable = false)
    @Comment("버전")
    private Long revision;

    @Column(name = "code", length = 30, nullable = false)
    @Comment("역할코드")
    private String code;

    @Column(name = "name", length = 50, nullable = false)
    @Comment("역할명")
    private String name;

    @Embedded
    private MetadataEntity metadata;

    // 양방향 연관관계
    @OneToMany(mappedBy = "role", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    private List<UserRoleEntity> userRoles = new ArrayList<>();

    @OneToMany(mappedBy = "higherRole", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    private List<RoleHierarchyEntity> higherRoles = new ArrayList<>();

    @OneToMany(mappedBy = "lowerRole", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    private List<RoleHierarchyEntity> lowerRoles = new ArrayList<>();
}
