package org.apache.bookkeeper.bookie.entities;

import io.netty.buffer.ByteBuf;
import org.apache.bookkeeper.proto.BookkeeperInternalCallbacks;

public class AddEntry {

    private ByteBuf entry;
    private Boolean wayAckAsync;
    private BookkeeperInternalCallbacks.WriteCallback writeCallback;
    private Object ctx;
    private byte[] key;
    private Long ledgerId;
    private long entryId = 12345;
    private Boolean expected;
    private String content;
    private Boolean isValid;

    public AddEntry(ByteBuf entry, Boolean wayAckAsync, BookkeeperInternalCallbacks.WriteCallback writeCallback,
                    Object ctx, byte[] masterkey, Boolean isValiid, Boolean expected) {

        this.entry = entry;
        this.wayAckAsync = wayAckAsync;
        this.writeCallback = writeCallback;
        this.ctx = ctx;
        this.key = masterkey;
        this.isValid = isValiid;
        this.expected = expected;
    }

    public Boolean getValid() {
        return isValid;
    }

    public void setValid(Boolean valid) {
        isValid = valid;
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

    public Long getLedgerId() {
        return ledgerId;
    }

    public void setLedgerId(Long ledgerId) {
        this.ledgerId = ledgerId;
    }

    public long getEntryId() {
        return entryId;
    }

    public void setEntryId(long entryId) {
        this.entryId = entryId;
    }

    public Boolean getExpected() {
        return expected;
    }

    public void setExpected(Boolean expected) {
        this.expected = expected;
    }
}
