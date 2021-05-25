package com.satish.datamodels.filter;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * @author satish.thulva.
 **/
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class OrFilter implements Filter {
    private List<Filter> filters;

    @Override
    public String toQueryComponent() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("(");
        stringBuilder.append(filters.get(0).toQueryComponent());
        stringBuilder.append(")");

        for (int i = 1; i < filters.size(); i += 1) {
            stringBuilder.append(" OR ");

            stringBuilder.append("(");
            stringBuilder.append(filters.get(i).toQueryComponent());
            stringBuilder.append(")");
        }

        return stringBuilder.toString();
    }
}
