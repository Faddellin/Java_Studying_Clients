package org.example.clientsbackend.Application.Models.Common;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class PaginationModel {
    private Integer page;
    private Integer pageSize;
    private Integer pagesCount;

    public PaginationModel(Integer elementsCount, Integer pageSize, Integer currentPage){
        pagesCount = (elementsCount / pageSize) +
                (elementsCount % pageSize >= 1 ? 1 : 0);
        page = Math.min(Math.max(1,currentPage), pagesCount);
        this.pageSize = pageSize;
    }

}
