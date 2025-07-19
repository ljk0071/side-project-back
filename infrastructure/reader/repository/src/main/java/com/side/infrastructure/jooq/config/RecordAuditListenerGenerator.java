package com.side.infrastructure.jooq.config;

import org.jooq.RecordContext;
import org.jooq.RecordListener;
import org.jooq.impl.UpdatableRecordImpl;

import java.util.function.Consumer;

public class RecordAuditListenerGenerator<R extends UpdatableRecordImpl<R>> {

    public RecordListener generate(
            Class<R> recordClass,
            Consumer<R> insertStart,
            Consumer<R> updateStart
    ) {
        return new RecordListener() {
            @Override
            public void insertStart(RecordContext ctx) {
                insertStart.accept(recordClass.cast(ctx.record()));
            }

            @Override
            public void updateStart(RecordContext ctx) {
                updateStart.accept(recordClass.cast(ctx.record()));
            }
        };
    }
}