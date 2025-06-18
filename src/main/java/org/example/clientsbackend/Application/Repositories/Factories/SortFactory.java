package org.example.clientsbackend.Application.Repositories.Factories;

import org.example.clientsbackend.Application.Models.Enums.SortOrder;
import org.springframework.data.domain.Sort;

public class SortFactory {
    public Sort createSort(SortOrder sortOrder, String value){
        Sort newSort = Sort.by(value);
        newSort = switch (sortOrder) {
            case Ascending -> newSort.ascending();
            default -> newSort.descending();
        };
        return newSort;
    }
}
