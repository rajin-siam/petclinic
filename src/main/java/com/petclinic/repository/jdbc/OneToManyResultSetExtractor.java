package com.petclinic.repository.jdbc;

import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.util.Assert;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public abstract class OneToManyResultSetExtractor<R, C, K> implements ResultSetExtractor<List<R>> {

	public enum ExpectedResults {
		ANY,
		ONE_AND_ONLY_ONE,
		ONE_OR_NONE,
		AT_LEAST_ONE
	}

	protected final ExpectedResults expectedResults;
	protected final RowMapper<R> rootMapper;
	protected final RowMapper<C> childMapper;

	protected OneToManyResultSetExtractor(RowMapper<R> rootMapper, RowMapper<C> childMapper) {
		this(rootMapper, childMapper, null);
	}

    protected OneToManyResultSetExtractor(RowMapper<R> rootMapper, RowMapper<C> childMapper, ExpectedResults expectedResults) {

		Assert.notNull(rootMapper, "Root RowMapper must not be null!");
		Assert.notNull(childMapper, "Child RowMapper must not be null!");

		this.childMapper = childMapper;
		this.rootMapper = rootMapper;
		this.expectedResults = expectedResults == null ? ExpectedResults.ANY : expectedResults;
	}

	public List<R> extractData(ResultSet rs) throws SQLException {
		List<R> results = new ArrayList<>();
		int row = 0;
		boolean more = rs.next();
		if (more) {
			row++;
		}
		while (more) {
			R root = rootMapper.mapRow(rs, row);
			K primaryKey = mapPrimaryKey(rs);
			if (mapForeignKey(rs) != null) {
				while (more && primaryKey.equals(mapForeignKey(rs))) {
					addChild(root, childMapper.mapRow(rs, row));
					more = rs.next();
					if (more) {
						row++;
					}
				}
			}
			else {
				more = rs.next();
				if (more) {
					row++;
				}
			}
			results.add(root);
		}
		if ((expectedResults == ExpectedResults.ONE_AND_ONLY_ONE || expectedResults == ExpectedResults.ONE_OR_NONE) &&
				results.size() > 1) {
			throw new IncorrectResultSizeDataAccessException(1, results.size());
		}
		if ((expectedResults == ExpectedResults.ONE_AND_ONLY_ONE || expectedResults == ExpectedResults.AT_LEAST_ONE) &&
				results.isEmpty()) {
			throw new IncorrectResultSizeDataAccessException(1, 0);
		}
		return results;
	}

	protected abstract K mapPrimaryKey(ResultSet rs) throws SQLException;

	protected abstract K mapForeignKey(ResultSet rs) throws SQLException;

	protected abstract void addChild(R root, C child);

}
