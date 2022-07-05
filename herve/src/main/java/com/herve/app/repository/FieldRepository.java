package com.herve.app.repository;

import com.herve.app.domain.Field;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Field entity.
 */
@Repository
public interface FieldRepository extends JpaRepository<Field, Long>, JpaSpecificationExecutor<Field> {
    @Query("select distinct field from Field field left join fetch field.sensors where field.user.login = ?#{principal.username}")
    List<Field> findByUserIsCurrentUser();

    default Optional<Field> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<Field> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<Field> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select distinct field from Field field left join fetch field.user",
        countQuery = "select count(distinct field) from Field field"
    )
    Page<Field> findAllWithToOneRelationships(Pageable pageable);

    @Query("select distinct field from Field field left join fetch field.user")
    List<Field> findAllWithToOneRelationships();

    @Query("select field from Field field left join fetch field.user where field.id =:id")
    Optional<Field> findOneWithToOneRelationships(@Param("id") Long id);
}
