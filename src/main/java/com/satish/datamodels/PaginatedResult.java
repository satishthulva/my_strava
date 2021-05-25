package com.satish.datamodels;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * Generic abstraction for paginated calls. In addition to current page to return,
 * it has meta info that is useful to find out whether there are more pages or not.
 *
 * @author satish.thulva.
 **/
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PaginatedResult<T> {
    private List<T> page;
    private long totalResults;
    private int entriesInCurrentPage;
}
