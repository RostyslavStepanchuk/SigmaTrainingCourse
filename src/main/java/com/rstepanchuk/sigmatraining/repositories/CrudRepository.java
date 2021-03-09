package com.rstepanchuk.sigmatraining.repositories;

import java.util.List;
import java.util.Optional;

public interface CrudRepository <T> {
  Optional<T> create (T entity);
  Optional<T> update (T entity);
  void delete(T entity);
  default void delete(Long id) {
    getById(id).ifPresent(this::delete);
  }
  Optional<T> getById(Long id);
  List<T> getAll();
}
