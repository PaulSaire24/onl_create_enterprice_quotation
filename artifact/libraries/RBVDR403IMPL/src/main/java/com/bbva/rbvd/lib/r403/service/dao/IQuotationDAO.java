package com.bbva.rbvd.lib.r403.service.dao;

import java.util.Map;

public interface IQuotationDAO {
     void insertQuotation(Map<String, Object> argument);
      void deleteQuotation(Map<String, Object> argument);
}
