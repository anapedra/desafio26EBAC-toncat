package org.anasantana.utils;

import org.anasantana.daos.pagination.Pageable;

public class PageableMock implements Pageable {
    private int page;
    private int size;

    public PageableMock(int page, int size) {
        this.page = page;
        this.size = size;
    }

    @Override
    public int getPageNumber() {
        return page;
    }

    @Override
    public int getPageSize() {
        return size;
    }
}
