package loyalty;

import atg.repository.Repository;
import atg.repository.MutableRepository;
import atg.repository.RepositoryException;
import atg.repository.RepositoryItem;
import atg.repository.MutableRepositoryItem;

import javax.transaction.TransactionManager;
import javax.transaction.SystemException;

import atg.dtm.TransactionDemarcation;
import atg.dtm.TransactionDemarcationException;

import java.util.Collection;
import java.util.Iterator;

public class LoyaltyManager extends atg.nucleus.GenericService {

  private TransactionManager transactionManager = null;
  private Repository loyaltyRepository = null;
  private Repository userRepository = null;
  private String rolePath = null;
   
  public void setTransactionManager(TransactionManager transactionManager) {
    this.transactionManager = transactionManager;
  }
  
  public TransactionManager getTransactionManager() {
    return transactionManager;
  }

  public void setLoyaltyRepository(Repository loyaltyRepository) {
    this.loyaltyRepository = loyaltyRepository;
  }
  
  public Repository getLoyaltyRepository() {
    return loyaltyRepository;
  }

  public void setUserRepository(Repository userRepository) {
    this.userRepository = userRepository;
  }
  
  public Repository getUserRepository() {
    return userRepository;
  }

  public String getRolePath() {
	return rolePath;
  }

  public void setRolePath(String rolePath) {
	this.rolePath = rolePath;
  }

public void addLoyaltyPointsToUser(String loyaltyTransactionId) throws RepositoryException {
  	
  	if (isLoggingDebug()) {
          logDebug("adding loyalty points from" + loyaltyTransactionId + " to user`s transactions list and count amount");
	}

	MutableRepository mutRepository = (MutableRepository) getUserRepository();
	Repository loyaltyRepository = getLoyaltyRepository();

	RepositoryItem loyaltyTransaction = loyaltyRepository.getItem(loyaltyTransactionId, "loyaltyTransaction");
	String userId = (String) loyaltyTransaction.getPropertyValue("user");
 
	try {
		TransactionDemarcation td = new TransactionDemarcation();
		td.begin(getTransactionManager(), TransactionDemarcation.REQUIRED);
		try {
			MutableRepositoryItem mutUser = mutRepository.getItemForUpdate(userId, "user");
			Collection loyaltyTransactionsList = (Collection) mutUser.getPropertyValue("loyaltyTransactions");
			loyaltyTransactionsList.add(loyaltyTransaction);
			int amount = 0;
            if (isLoggingDebug()) {
                logDebug("count amount for user: " + userId);
            }
			for (Iterator iterator = loyaltyTransactionsList.iterator(); iterator.hasNext();) {
				RepositoryItem userLoyaltyTransaction = (RepositoryItem) iterator.next();
				int amountValue = (Integer) userLoyaltyTransaction.getPropertyValue("amount");
				amount += amountValue;				
			}
			mutUser.setPropertyValue("loyaltyTransactions", loyaltyTransactionsList);
			mutUser.setPropertyValue("loyaltyAmount", (Integer) amount);
			mutRepository.updateItem(mutUser);

		} catch (Exception e){
			 if (isLoggingError()){
                 logError("Exception occured trying to add loyaltyTransaction", e); 
			 }
             try {
                 getTransactionManager().setRollbackOnly();
             } catch (SystemException se) {
                 if (isLoggingError()) {
                     logError("Unable to set rollback for transaction", se);
                 }
             }
		} finally {
			td.end();
		}
	} catch (TransactionDemarcationException e){
        if (isLoggingError()) {
            logError("creating transaction demarcation failed, no loyalty points added", e);
        }
	}
  }

}