package com.ohadr.spring_batch_dynamic_composite.item;

import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.support.CompositeItemProcessor;
import com.ohadr.spring_batch_dynamic_composite.core.BatchBeanTypeEnum;
import com.ohadr.spring_batch_dynamic_composite.core.CompositeBatchBeanEntity;

public class DynamicCompositeItemProcessor<I, O> extends AbstractDynamicCompositeItem 
	implements ItemProcessor<I, O>
{
	private static Logger log = Logger.getLogger(DynamicCompositeItemProcessor.class);

	//aggregate CompositeItemProcessor<I, O> rather than extending it:
	protected CompositeItemProcessor<I, O> delegate;
	
	protected void getProcessorsList()
	{
		List<ItemProcessor<I, O>> delegates = new ArrayList<ItemProcessor<I, O>>();

		BatchBeanTypeEnum batchBeanType = BatchBeanTypeEnum.PROCESSOR;
		List<CompositeBatchBeanEntity> processorsList = compositeBatchBeanManager.getBatchBeanList(taskName, batchBeanType);
		if (processorsList.isEmpty() && !acceptEmptyFiltersList)
		{
			String message = "No " + batchBeanType + " were found for taskName=" + taskName;
			log.error(message);
			throw new RuntimeException(message);
		}

		for (CompositeBatchBeanEntity compositeProcessorEntity : processorsList)
		{
			// load generic filter by name
			ItemProcessor<I, O> processor = applicationContext.getBean(compositeProcessorEntity.getName(), ItemProcessor.class);
			delegates.add( processor );
		}

		delegate.setDelegates(delegates);
		log.debug("processors list: " + delegates);		
	}

	@Override
	protected void getItemsList()
	{
		getProcessorsList();	
	}

	@Override
	public O process(I item) throws Exception
	{
		return delegate.process(item);
	}
}
