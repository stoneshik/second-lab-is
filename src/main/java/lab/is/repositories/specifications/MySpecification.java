package lab.is.repositories.specifications;

import java.time.LocalDateTime;

import org.springframework.data.jpa.domain.Specification;

import jakarta.persistence.criteria.JoinType;

public abstract class MySpecification<T> {
    protected MySpecification() {}

    protected Specification<T> fieldStringValueLike(
        FieldName fieldName,
        String fieldValue
    ) {
        if (fieldValue == null || fieldValue.isBlank()) return null;
        String pattern = "%" + fieldValue.trim().toLowerCase() + "%";
        return (root, query, criteriaBuilder) ->
            criteriaBuilder.like(
                criteriaBuilder.lower(
                    root.get(
                        fieldName.getFieldName()
                    )
                ),
                pattern
            );
    }

    protected Specification<T> fieldStringValueFromEntityWithJoinLike(
        FieldName fieldNameEntity,
        FieldName fieldName,
        String fieldValue
    ) {
        if (fieldValue == null || fieldValue.isBlank()) return null;
        String pattern = "%" + fieldValue.trim().toLowerCase() + "%";
        return (root, query, criteriaBuilder) -> {
            var join = root.join(
                fieldNameEntity.getFieldName(),
                JoinType.INNER
            );
            return criteriaBuilder.like(
                criteriaBuilder.lower(
                    join.get(
                        fieldName.getFieldName()
                    )
                ),
                pattern
            );
        };
    }

    protected Specification<T> fieldValueEquals(
        FieldName fieldName,
        String fieldValue
    ) {
        if (fieldValue == null) return null;
        return (root, query, criteriaBuilder) ->
            criteriaBuilder.equal(
                root.get(
                    fieldName.getFieldName()
                ),
                fieldValue
            );
    }

    protected Specification<T> fieldValueFromEntityEquals(
        FieldName fieldNameEntity,
        FieldName fieldName,
        Long fieldValue
    ) {
        if (fieldValue == null) return null;
        return (root, query, criteriaBuilder) ->
            criteriaBuilder.equal(
                root.get(
                    fieldNameEntity.getFieldName()
                ).get(fieldName.getFieldName()),
                fieldValue
            );
    }

    protected Specification<T> fieldDatetimeValueAfterOrEq(
        FieldName fieldName,
        LocalDateTime from
    ) {
        if (from == null) return null;
        return (root, query, criteriaBuilder) ->
            criteriaBuilder.greaterThanOrEqualTo(
                root.get(fieldName.getFieldName()),
                from
            );
    }

    protected Specification<T> fieldDatetimeValueBeforeOrEq(
        FieldName fieldName,
        LocalDateTime to
    ) {
        if (to == null) return null;
        return (root, query, criteriaBuilder) ->
            criteriaBuilder.lessThanOrEqualTo(
                root.get(fieldName.getFieldName()),
                to
            );
    }
}
