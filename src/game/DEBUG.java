/**
 * 
 */
package game;

/**
 * @author lyubick
 *
 */
public class DEBUG {
	/*
	 * 0: TALKY MODE
	 * 1: LOGS and HIGHER
	 * 2: SYSTEM ONLY
	 * 3: SILENT MODE
	 * > 3 IGNORE
	 */
    
    enum DebugTypes {
        LOG_DEBUG, LOG_LOGGER, LOG_SYSTEM, LOG_ERROR, LOG_NONE
    }
    
	public static DebugTypes LEVEL = DebugTypes.LOG_NONE;
	
	public static void WRITE(String msg, DebugTypes type){
		String Predicate = "JAVA_";
		switch (type){
			case LOG_DEBUG:  Predicate += "DEBUG!: "; break;
			case LOG_LOGGER: Predicate += "LOGGER: "; break;
			case LOG_SYSTEM: Predicate += "SYSTEM: "; break;
			case LOG_ERROR:  Predicate += "ERROR!: "; break;
			default: break;
		}
		if (type.ordinal() >= LEVEL.ordinal()){
			System.out.println(Predicate + msg);
		}
		
	}
	
}
