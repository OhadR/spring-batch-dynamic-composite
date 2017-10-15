package com.ohadr.spring_batch_dynamic_composite.repository;

import java.util.List;
import java.util.Set;
import com.ohadr.spring_batch_dynamic_composite.core.BatchBeanTypeEnum;
import com.ohadr.spring_batch_dynamic_composite.core.CompositeBatchBeanEntity;

public interface CompositeBatchBeanDao 
{

	List<CompositeBatchBeanEntity> getCompositeBatchBean(String taskName, BatchBeanTypeEnum batchBeanType);

	Set<String> getAllTaskNames();

	Set<String> getAllValuesOfBeanTypes();

}
