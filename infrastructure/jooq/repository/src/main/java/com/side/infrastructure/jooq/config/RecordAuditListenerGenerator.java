package com.side.infrastructure.jooq.config;

import java.util.function.Consumer;

import org.jooq.RecordContext;
import org.jooq.RecordListener;
import org.jooq.impl.UpdatableRecordImpl;

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