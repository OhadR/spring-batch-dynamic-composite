package com.ohadr.spring_batch_dynamic_composite.repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Set;
import org.springframework.jdbc.core.RowMapper;
import com.ohadr.spring_batch_dynamic_composite.core.BatchBeanTypeEnum;
import com.ohadr.spring_batch_dynamic_composite.core.CompositeBatchBeanEntity;

public class JdbcCompositeBatchBeanDao implements CompositeBatchBeanDao
{

	@Override
	public List<CompositeBatchBeanEntity> getCompositeBatchBean(String taskName, BatchBeanTypeEnum batchBeanType)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<String> getAllTaskNames()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<String> getAllValuesOfBeanTypes()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void update(CompositeBatchBeanEntity processor)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void delete(Long compositeBatchBeanId)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public CompositeBatchBeanEntity get(Long compositeBatchBeanId)
	{
		// TODO Auto-generated method stub
		return null;
	}

	private final class CompositeBatchBeanEntityRowMapper implements RowMapper<CompositeBatchBeanEntity> {

		@Override
		public CompositeBatchBeanEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
			CompositeBatchBeanEntity compositeBatchBeanEntity = 
					new CompositeBatchBeanEntity(rs.getLong(1), rs.getString(2), rowNum, null, null);
			// should always be at version=0 because they never get updated
			compositeBatchBeanEntity.incrementVersion();
			return compositeBatchBeanEntity;
		}
	}
}
