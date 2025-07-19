package com.side.infrastructure.jooq.repository.base;

import com.side.infrastructure.jooq.config.RecordAuditListenerGenerator;
import org.jooq.Configuration;
import org.jooq.DSLContext;
import org.jooq.RecordListener;
import org.jooq.impl.DSL;
import org.jooq.impl.UpdatableRecordImpl;

import java.time.Instant;
import java.util.function.Consumer;

import static com.side.security.service.SecurityHelper.getAuthenticatedUser;

/**
 * JOOQ Repository들의 공통 기능을 제공하는 추상 기본 클래스
 *
 * @param <R> JOOQ Record 타입
 */
public abstract class AutoAuditJooqRepository<R extends UpdatableRecordImpl<R>> {

    protected final DSLContext dsl;

    protected AutoAuditJooqRepository(DSLContext dsl, Class<R> recordClass) {
        Configuration config = dsl.configuration().derive();
        config.set(createAuditListener(recordClass));
        this.dsl = DSL.using(config);
    }

    /**
     * 커스텀 Audit 로직이 필요한 경우 사용할 수 있는 생성자
     */
    protected AutoAuditJooqRepository(DSLContext dsl, RecordListener customAuditListener) {
        Configuration config = dsl.configuration().derive();
        config.set(customAuditListener);
        this.dsl = DSL.using(config);
    }

    /**
     * 표준 Audit 리스너를 생성합니다.
     * 생성 시: createdAt, createdBy 설정
     * 수정 시: modifiedAt, modifiedBy 설정
     */
    private RecordListener createAuditListener(Class<R> recordClass) {
        Consumer<R> createAudit = record -> {
            setAuditFieldsReflectively(record, "setCreatedAt", Instant.now());
            setAuditFieldsReflectively(record, "setCreatedBy", getAuthenticatedUser().uniqueId());
        };

        Consumer<R> updateAudit = record -> {
            setAuditFieldsReflectively(record, "setModifiedAt", Instant.now());
            setAuditFieldsReflectively(record, "setModifiedBy", getAuthenticatedUser().uniqueId());
        };

        return new RecordAuditListenerGenerator<R>().generate(
                recordClass,
                createAudit,
                updateAudit
        );
    }

    /**
     * 리플렉션을 사용하여 audit 필드를 설정합니다.
     */
    private void setAuditFieldsReflectively(R record, String methodName, Object value) {
        try {
            var method = record.getClass().getMethod(methodName, value.getClass());
            method.invoke(record, value);
        } catch (Exception e) {
            // 해당 필드가 없는 Record의 경우 무시 (일부 테이블에는 audit 필드가 없을 수 있음)
        }
    }

    /**
     * 커스텀 Audit 리스너를 생성하는 헬퍼 메서드
     */
    protected RecordListener createCustomAuditListener(Class<R> recordClass,
                                                       Consumer<R> createAudit,
                                                       Consumer<R> updateAudit) {
        return new RecordAuditListenerGenerator<R>().generate(
                recordClass,
                createAudit,
                updateAudit
        );
    }

    /**
     * 표준 생성 Audit 로직
     */
    protected Consumer<R> getStandardCreateAudit() {
        return record -> {
            setAuditFieldsReflectively(record, "setCreatedAt", Instant.now());
            setAuditFieldsReflectively(record, "setCreatedBy", getAuthenticatedUser().uniqueId());
        };
    }

    /**
     * 표준 수정 Audit 로직
     */
    protected Consumer<R> getStandardUpdateAudit() {
        return record -> {
            setAuditFieldsReflectively(record, "setModifiedAt", Instant.now());
            setAuditFieldsReflectively(record, "setModifiedBy", getAuthenticatedUser().uniqueId());
        };
    }
}