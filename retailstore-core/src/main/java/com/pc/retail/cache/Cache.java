package com.pc.retail.cache;

import com.pc.retail.dao.DataAccessException;

public interface Cache {

	public void refresh() throws DataAccessException;
	
}
