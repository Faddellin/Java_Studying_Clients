package org.example.clientsbackend.Application.Repositories.Factories;

import org.example.clientsbackend.Application.Models.Enums.SortOrder;
import org.springframework.data.domain.Sort;

public class SortFactory {
    public Sort createSort(SortOrder sortOrder, String value){
        Sort newSort = Sort.by(value);
        switch (sortOrder){
            case Ascending:
                newSort = newSort.ascending();
                break;
            default:
                newSort = newSort.descending();
                break;
        }
        return newSort;
    }
}
