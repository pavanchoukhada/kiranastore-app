package com.pc.retail.cache;

import com.pc.retail.dao.DataAccessException;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public abstract class AbstractCache implements Cache {

	private static ReadWriteLock cacheLock = new ReentrantReadWriteLock();
	
	public Lock getReadLock(){
		return cacheLock.readLock();
	}
	

	public Lock getWriteLock(){
		return cacheLock.writeLock();
	}


    public void refresh() throws DataAccessException {
        try{
            getWriteLock().lock();
            poulateCache();
        }finally{
            getWriteLock().unlock();
        }
    }

    public abstract void poulateCache() throws DataAccessException;

}
