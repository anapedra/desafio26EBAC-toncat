package org.anasantana.daos.pagination.impl;

import org.anasantana.daos.pagination.Pageable;

public class SimplePageRequest implements Pageable {
    private final int pageNumber;
    private final int pageSize;

    public SimplePageRequest(int pageNumber, int pageSize) {
        this.pageNumber = pageNumber;
        this.pageSize = pageSize;
    }

    @Override
    public int getPageNumber() {
        return pageNumber;
    }

    @Override
    public int getPageSize() {
        return pageSize;
    }
}
