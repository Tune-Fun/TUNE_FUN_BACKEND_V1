package com.tune_fun.v1.common.util.querydsl;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class FluentOperationFragment {

    protected final PredicateBuilder predicateBuilder;

    protected abstract PredicateBuilder operate(Predicate predicate);

    protected abstract PredicateBuilder operate(List<Predicate> predicates);

    PredicateBuilder operate() { // 아무 조건이 안 들어간 경우
        return predicateBuilder;
    }

    public PredicateBuilder eqString(StringExpression column, String value) {
        if (StringUtils.hasText(value)) {
            return operate(column.eq(value));
        }

        return operate();
    }

    public PredicateBuilder containsString(StringExpression column, String value) {
        if (StringUtils.hasText(value)) {
            return operate(column.contains(value));
        }

        return operate();
    }

    public PredicateBuilder containsIgnoreCaseString(StringExpression column, String value) {
        if (StringUtils.hasText(value)) {
            return operate(column.containsIgnoreCase(value));
        }

        return operate();
    }

    public PredicateBuilder containsStringList(ListPath<String, StringPath> column, List<String> values) {
        if (values != null && !values.isEmpty()) {
            BooleanExpression[] booleanExpressions = values.stream()
                    .map(column::contains)
                    .toArray(BooleanExpression[]::new);
            return operate(new BooleanBuilder().andAnyOf(booleanExpressions));
        }

        return operate();
    }

    public PredicateBuilder containsStringDesc(StringExpression column1, StringExpression column2, String value) {
        if (StringUtils.hasText(value)) {
            return operate(new BooleanBuilder()
                    .andAnyOf(column2.contains(value), column2.contains(value)));
        }

        return operate();
    }

    public <N extends Number & Comparable<?>> PredicateBuilder betweenNumberDynamic(NumberExpression<N> column, N min, N max) {
        List<Predicate> predicates = new ArrayList<>();

        if (min != null) {
            predicates.add(column.goe(min));
        }

        if (max != null) {
            predicates.add(column.loe(max));
        }

        return predicates.isEmpty() ? operate() : operate(predicates);
    }

    public <N extends Number & Comparable<?>> PredicateBuilder eqNumber(NumberExpression<N> column, N value) {
        if (value != null) {
            return operate(column.eq(value));
        }

        return operate();
    }

    public <N extends Number & Comparable<?>> PredicateBuilder neNumber(NumberExpression<N> column, N value) {
        if (value != null) {
            return operate(column.ne(value));
        }

        return operate();
    }

    public <N extends Number & Comparable<?>> PredicateBuilder inNumber(NumberExpression<N> column, List<N> valueList) {
        if (valueList != null) {
            return operate(column.in(valueList));
        }

        return operate();
    }

    public <E extends Enum<E>> PredicateBuilder eqEnum(EnumExpression<E> column, E value) {
        if (value != null) {
            return operate(column.eq(value));
        }

        return operate();
    }

    public <E extends Enum<E>> PredicateBuilder inEnum(EnumExpression<E> column, List<E> value) {
        if (value != null) {
            return operate(column.in(value));
        }

        return operate();
    }

    public PredicateBuilder betweenDateTime(DateTimeExpression<LocalDateTime> column, LocalDateTime startDate, LocalDateTime endDate) {
        if (startDate != null && endDate != null) {
            return operate(column.between(startDate, endDate));
        }

        return operate();
    }

    public PredicateBuilder betweenDateTimeDynamic(DateTimeExpression<LocalDateTime> column, LocalDateTime startDate, LocalDateTime endDate) {
        List<Predicate> predicates = new ArrayList<>();

        if (startDate != null) {
            predicates.add(column.goe(startDate));
        }

        if (endDate != null) {
            predicates.add(column.loe(endDate));
        }

        return predicates.isEmpty() ? operate() : operate(predicates);
    }

    public PredicateBuilder betweenDate(DateTimeExpression<LocalDate> column, LocalDate startDate, LocalDate endDate) {
        if (startDate != null && endDate != null) {
            return operate(column.between(startDate, endDate));
        }

        return operate();
    }

    public PredicateBuilder betweenDateDynamic(DateTimeExpression<LocalDate> column, LocalDate startDate, LocalDate endDate) {
        List<Predicate> predicates = new ArrayList<>();

        if (startDate != null) {
            predicates.add(column.goe(startDate));
        }

        if (endDate != null) {
            predicates.add(column.loe(endDate));
        }

        return predicates.isEmpty() ? operate() : operate(predicates);
    }

    public PredicateBuilder eqDateTime(DateTimeExpression<LocalDateTime> column, LocalDateTime value) {
        if (value != null) {
            return operate(column.eq(value));
        }

        return operate();
    }

    public PredicateBuilder eqDate(DateTimeExpression<LocalDate> column, LocalDate value) {
        if (value != null) {
            return operate(column.eq(value));
        }

        return operate();
    }

}
