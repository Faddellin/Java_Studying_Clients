package org.example.clientsbackend.Application.Models.Common;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
@Schema(description = "Pagination model")
public class PaginationModel {

    @Schema(description = "Current page", example = "1")
    private Integer page;

    @Schema(description = "Page size", example = "5")
    private Integer pageSize;

    @Schema(description = "Count of pages", example = "7")
    private Integer pagesCount;

    public PaginationModel(Integer elementsCount, Integer pageSize, Integer currentPage){
        pagesCount = (elementsCount / pageSize) +
                (elementsCount % pageSize >= 1 ? 1 : 0);
        page = Math.min(Math.max(1,currentPage), pagesCount);
        this.pageSize = pageSize;
    }

}
