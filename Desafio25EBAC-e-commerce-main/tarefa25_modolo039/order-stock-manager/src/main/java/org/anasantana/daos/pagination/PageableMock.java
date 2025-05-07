package org.anasantana.daos.pagination;

public class PageableMock implements Pageable {

    private final int pageNumber;
    private final int pageSize;

    public PageableMock(int pageNumber, int pageSize) {
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
