package com.ohadr.spring_batch_dynamic_composite.core;

import java.io.Serializable;

public class CompositeBatchBeanEntity implements Comparable<CompositeBatchBeanEntity>, Serializable
{
	private static final long serialVersionUID = 6582817624632934459L;

	private Long id;

	private String name;

	private Integer priority;

	private String taskName;

	private BatchBeanTypeEnum batchBeanType;

	public Long getId()
	{
		return id;
	}

	public void setId(Long id)
	{
		this.id = id;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public Integer getPriority()
	{
		return priority;
	}

	public void setPriority(Integer priority)
	{
		this.priority = priority;
	}

	public String getTaskName()
	{
		return taskName;
	}

	public void setTaskName(String taskName)
	{
		this.taskName = taskName;
	}

	public BatchBeanTypeEnum getBatchBeanType()
	{
		return batchBeanType;
	}

	public void setBatchBeanType(BatchBeanTypeEnum batchBeanType)
	{
		this.batchBeanType = batchBeanType;
	}

	@Override
	public int compareTo(CompositeBatchBeanEntity compositeProcessorEntity)
	{
		// descending order
		return compositeProcessorEntity.getPriority() - this.getPriority();
	}

}
