package com.satish.datamodels.filter;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author satish.thulva.
 **/
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class LeafFilter implements Filter {
    private FilterField field;
    private FilterCondition condition;
    private Object value;

    @Override
    public String toQueryComponent() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(field.getDatastoreFieldName());
        stringBuilder.append(" ");
        stringBuilder.append(toFilterConditionQueryRepresentation());
        stringBuilder.append(" ");
        stringBuilder.append(toQueryFieldValueRepresentation());
        return stringBuilder.toString();
    }

    private String toQueryFieldValueRepresentation() {
        if (value instanceof String) {
            return "'" + value + "'";
        }

        return value.toString();
    }

    private String toFilterConditionQueryRepresentation() {
        switch (condition) {
            case EQ:
                return " = ";

            case NE:
                return " <> ";

            case LT:
                return " < ";

            case GT:
                return " > ";
        }

        return null;
    }

}
