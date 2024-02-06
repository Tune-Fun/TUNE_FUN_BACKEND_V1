package com.tune_fun.v1.common.util.querydsl;

import com.querydsl.core.types.Predicate;

import java.util.List;

public class AndFragment extends FluentOperationFragment {
    protected AndFragment(PredicateBuilder predicateBuilder) {
        super(predicateBuilder);
    }

    @Override
    protected PredicateBuilder operate(Predicate predicate) {
        super.predicateBuilder.andExpressions.add(predicate);
        return super.predicateBuilder;
    }

    @Override
    protected PredicateBuilder operate(List<Predicate> predicates) {
        super.predicateBuilder.andExpressions.addAll(predicates);
        return super.predicateBuilder;
    }
}
