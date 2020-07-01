
package com.wednesday.demo.dto;

import java.util.ArrayList;
import java.util.List;


public class TransactionInfo {
  private boolean returnStatus = true;
  private Object data = null;
  private List<String> errorList = new ArrayList<String>();

  /**
   * @author Jignesh.Rathod
   */
  public TransactionInfo(final Object returnObject) {
    setData(returnObject);
  }


  public TransactionInfo(final boolean returnstatus) {
    setReturnStatus(returnstatus);
  }

  public void setData(final Object object) {
    this.data = object;
  }

  public Object getData() {
    return data;
  }

  public void setReturnStatus(final boolean returnstatus) {
    this.returnStatus = returnstatus;
  }

  public boolean getReturnStatus() {
    return returnStatus;
  }

  public List<String> getErrorList() {
    return errorList;
  }

  public void setErrorList(final List<String> errorlist) {
    this.errorList = errorlist;
  }

  /**
   * @param errormessage
   *          Description param errormessage
   * @return Description return value
   */
  public TransactionInfo addErrorList(final String errormessage) {
    this.errorList.add(errormessage);
    return this;
  }

}