package com.ohadr.spring_batch_dynamic_composite.item;

import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.support.CompositeItemProcessor;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.util.StringUtils;
import com.ohadr.spring_batch_dynamic_composite.core.BatchBeanTypeEnum;
import com.ohadr.spring_batch_dynamic_composite.core.CompositeBatchBeanEntity;
import com.ohadr.spring_batch_dynamic_composite.core.CompositeBatchBeanManager;

public class DynamicCompositeItemProcessor<I, O> 
	extends CompositeItemProcessor<I, O> 
	implements StepExecutionListener, ApplicationContextAware
{
	private static Logger log = Logger.getLogger(DynamicCompositeItemProcessor.class);

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

	//hide super's implementation
	@Override
	public void afterPropertiesSet() throws Exception 
	{
	}

	@Override
	public ExitStatus afterStep(StepExecution stepExecution)
	{
		return stepExecution != null ? stepExecution.getExitStatus() : null;
	}

	protected void getProcessorsList()
	{
		List<ItemProcessor<I, O>> delegates = new ArrayList<ItemProcessor<I, O>>();

		BatchBeanTypeEnum batchBeanType = BatchBeanTypeEnum.PROCESSOR;
		List<CompositeBatchBeanEntity> processorsList = compositeBatchBeanManager.getBatchBeanList(taskName, batchBeanType);
		if (processorsList.isEmpty() && !acceptEmptyFiltersList)
		{
			log.error("No processors were found for taskName=" + taskName + " and batchBeanType=" + batchBeanType);

			throw new RuntimeException("no processors were found");
		}

		for (CompositeBatchBeanEntity compositeProcessorEntity : processorsList)
		{
			// load generic filter by name
			ItemProcessor<I, O> processor = applicationContext.getBean(compositeProcessorEntity.getName(), ItemProcessor.class);
			delegates.add( processor );
		}

		setDelegates(delegates);
		log.debug("processors list: " + delegates);		
	}

	@Override
	public void beforeStep(StepExecution stepExecution)
	{
		taskName = stepExecution.getJobExecution().getJobInstance().getJobName();
		
		if (StringUtils.isEmpty(taskName))
		{
			String message = getClass().getSimpleName() + " beforeStep: taskName is null or empty.";
	//		Log.error(message);
			throw new RuntimeException(message);
		}
		
		//get processors list from DB
		getProcessorsList();
	}
}
