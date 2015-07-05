package crawingspackage;

import javax.jdo.JDOHelper;
import javax.jdo.PersistenceManagerFactory;

/**
 * Class allows access to the PersistanceManagerFactory for storage purposes
 * 
 * @author Guillaume Boell
 * @version 20th November 2014
 */
public final class PMF {
	private static final PersistenceManagerFactory pmfInstance = JDOHelper
			.getPersistenceManagerFactory("transactions-optional");

	/**
	 * private constructor should never actaully be instantiated
	 */
	private PMF() {
	}

	/**
	 * pretty standard method to actually return the persistance factor
	 * 
	 * @return as above
	 */
	public static PersistenceManagerFactory get() {
		return pmfInstance;
	}
}