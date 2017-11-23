package game;

import java.io.File;
import java.util.List;
import java.util.ArrayList;

import org.armedbear.lisp.*;
import org.armedbear.lisp.Package;

public class WitchGameMain {
    //private static Object CurrentGame;
    
    /** This method is called from LISP to start a game;
     * 
     * @return WitchGame object to be able to call non-static methods of WitchGame from LISP
     */
    public static Object launchGame(){
       return new WitchGame();
    }
	
    /** This will get CL-USER package from LISP instance
     * 
     * @return Package "CL-USER"
     */
	public static org.armedbear.lisp.Package getDefaultPackage(){
		Package Pcgs[] = Packages.getAllPackages();
		int i;
		for (i = 0; i < Pcgs.length; i++){
			if (Pcgs[i].getNickname() == "CL-USER"){
				break;
			}
		}
		return Pcgs[i];
	}
	
	
	public static void main(String[] args) {
		
		try
		{
			if (args.length > 0){
				DEBUG.LEVEL = DEBUG.DebugTypes.LOG_DEBUG;
			}
			
			//WitchGameMain ThisObject = new WitchGameMain();
			Interpreter LispInterpreter = Interpreter.createInstance();
			
			List<String> Results = new ArrayList<String>();
			
			DEBUG.WRITE("Starting...", DEBUG.DebugTypes.LOG_SYSTEM);
			
			File[] Files = new File("src/LISP").listFiles();
			DEBUG.WRITE("Saerching src/LISP...", DEBUG.DebugTypes.LOG_SYSTEM);

			for (File InputFile : Files) {
			    if (InputFile.isFile()) {
			        Results.add(InputFile.getName());
			        DEBUG.WRITE("Found -> [" + InputFile.getName() + "]", DEBUG.DebugTypes.LOG_SYSTEM);
			    }
			}
			DEBUG.WRITE("Saerching DONE!", DEBUG.DebugTypes.LOG_SYSTEM);
			
			DEBUG.WRITE("Loading files...", DEBUG.DebugTypes.LOG_SYSTEM);
			for (String FileName : Results) {
			    if (FileName.endsWith(".lisp")) {
			    	DEBUG.WRITE("-> [" + FileName + "]", DEBUG.DebugTypes.LOG_SYSTEM);
			        LispInterpreter.eval("(load \"src/LISP/" + FileName + "\")");
			    }
			}
			DEBUG.WRITE("Loading DONE!", DEBUG.DebugTypes.LOG_SYSTEM);
			    
			org.armedbear.lisp.Package defaultPackage = getDefaultPackage();
			DEBUG.WRITE("Package -> " + defaultPackage.getNickname(), DEBUG.DebugTypes.LOG_SYSTEM);

            Symbol StartGameLispSymbol = defaultPackage.findAccessibleSymbol("StartWitchGame".toUpperCase());
            DEBUG.WRITE("Function set to -> " + StartGameLispSymbol.getName(), DEBUG.DebugTypes.LOG_SYSTEM);
            Function startGameLisp = (Function) StartGameLispSymbol.getSymbolFunction();
            DEBUG.WRITE("Function called -> " + startGameLisp.getLambdaName(), DEBUG.DebugTypes.LOG_SYSTEM);
            
            DEBUG.WRITE("Starting LISP...", DEBUG.DebugTypes.LOG_SYSTEM);
			startGameLisp.execute();
			DEBUG.WRITE("LISP stoped...", DEBUG.DebugTypes.LOG_SYSTEM);
			DEBUG.WRITE("Terminating program ...", DEBUG.DebugTypes.LOG_SYSTEM);
			//System.exit(0);
			
		}
		catch (Throwable Ex)
		{
			DEBUG.WRITE("EXCEPTION caught!", DEBUG.DebugTypes.LOG_ERROR);
			Ex.printStackTrace();
		}
	}
}

