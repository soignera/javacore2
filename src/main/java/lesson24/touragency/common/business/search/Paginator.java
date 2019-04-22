package lesson24.touragency.common.business.search;

import lesson24.touragency.common.application.ApplicationConfigurations;

public class Paginator {
    private int limit = ApplicationConfigurations.PAGE_SIZE;
    private int offset;

    public Paginator(int offset) {
        this.offset = offset;
    }

    public Paginator() {
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }


}
