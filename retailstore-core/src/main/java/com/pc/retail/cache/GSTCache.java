package com.pc.retail.cache;

import com.pc.retail.dao.DataAccessException;
import com.pc.retail.dao.DataSourceManager;
import com.pc.retail.dao.ReferenceDataDAO;
import com.pc.retail.vo.GSTGroupModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by pavanc on 7/23/17.
 */
public class GSTCache extends AbstractCache {

    private static GSTCache gstCache;
    private static volatile boolean initialized = false;
    private Map<String, GSTGroupModel> gstGroupModelMap;
    public static String CACHE_NAME = "GSTGroupCache";

    private GSTCache(){
        gstGroupModelMap = new ConcurrentHashMap<>();
    }

    public static GSTCache getInstance() throws DataAccessException {
        if(initialized == false){
            synchronized(ProductCache.class){
                if(!initialized){
                    initialized = initialize();
                }
                return gstCache;
            }
        }else{
            return gstCache;
        }
    }

    private static boolean initialize() throws DataAccessException{
        gstCache = new GSTCache();
        gstCache.poulateCache();
        return true;
    }

    public  void poulateCache() throws DataAccessException{
        List<GSTGroupModel> gstGroupModelList = getDAO().getGSTModelList();
        for(GSTGroupModel gstGroupModel : gstGroupModelList){
            gstGroupModelMap.put(gstGroupModel.getGroupCode(), gstGroupModel);
        }
    }

    private ReferenceDataDAO getDAO() throws DataAccessException{
        try {
            return DataSourceManager.getInstance().getReferenceDataDAO();
        } catch (DataAccessException e) {
            throw new DataAccessException(e.getMessage());
        }
    }



    public GSTGroupModel get(String groupCode) {
        return gstGroupModelMap.get(groupCode);
    }

    public List<GSTGroupModel> getAll(){
        return new ArrayList<>(gstGroupModelMap.values());
    }

    public void updateCache(GSTGroupModel gstGroupModel) {
        gstGroupModelMap.put(gstGroupModel.getGroupCode(), gstGroupModel);
    }
}
