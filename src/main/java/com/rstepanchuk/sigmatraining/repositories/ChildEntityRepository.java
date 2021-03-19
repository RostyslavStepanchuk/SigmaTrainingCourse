package com.rstepanchuk.sigmatraining.repositories;

import java.util.List;
import java.util.Map;

public interface ChildEntityRepository<T> {
  List<T> getAllByParentId(Long parentId);
  Map<Long, List<T>> getAllGroupedByParentId();
  void connectToParent(List<T> entities, Long parentId);
}
