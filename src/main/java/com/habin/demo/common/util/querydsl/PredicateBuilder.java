package com.habin.demo.common.util.querydsl;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Predicate;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PredicateBuilder {

    protected final List<Predicate> andExpressions = new ArrayList<>();
    protected final List<Predicate> orExpressions = new ArrayList<>();

    public static PredicateBuilder builder() {
        return new PredicateBuilder();
    }

    public <P extends Predicate> PredicateBuilder and(P pr) {
        andExpressions.add(pr);
        return this;
    }

    public <P extends Predicate> PredicateBuilder or(P pr) {
        orExpressions.add(pr);
        return this;
    }

    public FluentOperationFragment and() {
        return new AndFragment(this);
    }

    public FluentOperationFragment or() {
        return new OrFragment(this);
    }

    public Predicate build() {
        return ExpressionUtils.allOf(
                ExpressionUtils.allOf(andExpressions),
                new BooleanBuilder().andAnyOf(orExpressions.toArray(Predicate[]::new))
        );
    }

}

