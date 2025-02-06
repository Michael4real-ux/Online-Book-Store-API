package com.dammy.bookstoreapi.utils;

import java.util.List;

public class BookIdsRequest {
    private List<Long> bookIds;
    private List<BookQuantity> bookQuantities;

    public List<Long> getBookIds() {
        return bookIds;
    }

    public void setBookIds(List<Long> bookIds) {
        this.bookIds = bookIds;
    }

    public List<BookQuantity> getBookQuantities() {
        return bookQuantities;
    }

    public void setBookQuantities(List<BookQuantity> bookQuantities) {
        this.bookQuantities = bookQuantities;
    }
}