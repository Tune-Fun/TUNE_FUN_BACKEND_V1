package com.habin.demo.common.util.querydsl;

import com.querydsl.core.types.Predicate;

import java.util.List;

public class OrFragment extends FluentOperationFragment {
    protected OrFragment(PredicateBuilder predicateBuilder) {
        super(predicateBuilder);
    }

    @Override
    protected PredicateBuilder operate(Predicate predicate) {
        super.predicateBuilder.orExpressions.add(predicate);
        return super.predicateBuilder;
    }

    @Override
    protected PredicateBuilder operate(List<Predicate> predicates) {
        super.predicateBuilder.orExpressions.addAll(predicates);
        return super.predicateBuilder;
    }

}
