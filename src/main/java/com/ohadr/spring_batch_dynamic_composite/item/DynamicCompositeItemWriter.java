package com.ohadr.spring_batch_dynamic_composite.item;

import java.util.ArrayList;
import java.util.List;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.support.CompositeItemWriter;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.util.StringUtils;
import com.ohadr.spring_batch_dynamic_composite.core.BatchBeanTypeEnum;
import com.ohadr.spring_batch_dynamic_composite.core.CompositeBatchBeanEntity;
import com.ohadr.spring_batch_dynamic_composite.core.CompositeBatchBeanManager;

public class DynamicCompositeItemWriter<T>
	extends CompositeItemWriter<T>
	implements StepExecutionListener, ApplicationContextAware
{
	protected String taskName;

	@Autowired
	protected CompositeBatchBeanManager compositeBatchBeanManager;

	// if false, filter result is false if any filter is defined
	protected Boolean acceptEmptyFiltersList = false;

	private ApplicationContext applicationContext;

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException
	{
		this.applicationContext = applicationContext;
	}

	@Override
	public void beforeStep(StepExecution stepExecution)
	{
		if (StringUtils.isEmpty(taskName))
		{
			String message = getClass().getSimpleName() + " beforeStep: taskName is null or empty.";
	//		Log.error(message);
			throw new RuntimeException(message);
		}
		
		//get processors list from DB
		try
		{
			getWritersList();
		}
		catch (Exception e)
		{
			throw new RuntimeException("no processors were found");
		}
	}

	@Override
	public ExitStatus afterStep(StepExecution stepExecution)
	{
		return stepExecution != null ? stepExecution.getExitStatus() : null;
	}

	protected void getWritersList() throws Exception
	{
		List<ItemWriter<? super T>> delegates = new ArrayList<ItemWriter<? super T>>();

		BatchBeanTypeEnum batchBeanType = BatchBeanTypeEnum.WRITER;
		List<CompositeBatchBeanEntity> processorsList = compositeBatchBeanManager.getBatchBeanList(taskName, batchBeanType);
		if (processorsList.isEmpty() && !acceptEmptyFiltersList)
		{
//TODO			Log.debug("No filters found for Item . Item will not be accepted . filterName: " + this.getClass().getSimpleName() + " , item:" + item.getItemKey());

			throw new Exception();	//TODO better exception
		}

		for (CompositeBatchBeanEntity compositeProcessorEntity : processorsList)
		{
			// load generic filter by name
			ItemWriter<T> writer = applicationContext.getBean(compositeProcessorEntity.getName(), ItemWriter.class);
			delegates.add( writer );
		}

		setDelegates(delegates);
//TODO add relevant log		Log.trace("Item have been accepted. " + item);
		
	}

}
