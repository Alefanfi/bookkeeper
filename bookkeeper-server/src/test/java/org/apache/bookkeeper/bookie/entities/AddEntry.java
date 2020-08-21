package org.apache.bookkeeper.bookie.entities;

import io.netty.buffer.ByteBuf;
import org.apache.bookkeeper.proto.BookkeeperInternalCallbacks;

public class AddEntry {

    private ByteBuf entry;
    private Boolean wayAckAsync;
    private BookkeeperInternalCallbacks.WriteCallback writeCallback;
    private Object ctx;
    private byte[] key;
    private Boolean valid;
    private Long ledgerId;
    private Long entryId;
    private Boolean expected;
    private String content;

    public AddEntry(ByteBuf entry, Boolean wayAckAsync, BookkeeperInternalCallbacks.WriteCallback writeCallback, Object ctx, byte[] masterkey, Boolean valid, Boolean expected) {

        this.entry = entry;
        this.wayAckAsync = wayAckAsync;
        this.writeCallback = writeCallback;
        this.ctx = ctx;
        this.key = masterkey;
        this.valid = valid;
        this.expected = expected;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public ByteBuf getEntry() {
        return entry;
    }

    public void setEntry(ByteBuf entry) {
        this.entry = entry;
    }

    public Boolean getWayAckAsync() {
        return wayAckAsync;
    }

    public void setWayAckAsync(Boolean wayAckAsync) {
        this.wayAckAsync = wayAckAsync;
    }

    public BookkeeperInternalCallbacks.WriteCallback getWriteCallback() {
        return writeCallback;
    }

    public void setWriteCallback(BookkeeperInternalCallbacks.WriteCallback writeCallback) {
        this.writeCallback = writeCallback;
    }

    public Object getCtx() {
        return ctx;
    }

    public void setCtx(Object ctx) {
        this.ctx = ctx;
    }

    public byte[] getKey() {
        return key;
    }

    public void setKey(byte[] masterkey) {
        this.key = masterkey;
    }

    public Boolean getValid() {
        return valid;
    }

    public void setValid(Boolean valid) {
        this.valid = valid;
    }

    public Long getLedgerId() {
        return ledgerId;
    }

    public void setLedgerId(Long ledgerId) {
        this.ledgerId = ledgerId;
    }

    public Long getEntryId() {
        return entryId;
    }

    public void setEntryId(Long entryId) {
        this.entryId = entryId;
    }

    public Boolean getExpected() {
        return expected;
    }

    public void setExpected(Boolean expected) {
        this.expected = expected;
    }
}
