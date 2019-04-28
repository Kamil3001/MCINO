# MCINO
Software Engineering Project 3

MCINO Code Smeller

----------------------------
HOW TO RUN THE JAR FILE:
----------------------------
Just checking what will get changed....
1.	Make sure you have Java 8 installed to run the jar file.

2.	Download Mcino.jar from the github repository.

3.	Try running the jar file on your computer. If it doesn't work
	download jarfix.exe from the github repository.
	
4.	Run jarfix.exe, if the notification mentions a Java version
	different to Java 8 follow the steps below, otherwise 
	MCINO.jar should now run.
	
--------------------------------------
jarfix.exe finds diiferent JDK/JRE
--------------------------------------
1.	Create a jarfix.ini file and include the following:
		"[jarfix]
		action="C:\PATH_TO_JAVA8_BIN_FOLDER\javaw.exe" -Xmx512m -jar "%1" %* "

2.  Ensure that file is saved as jarfix.ini (extension must be .ini) and that
    it's saved in same directory as jarfix.exe

2.  MCINO.jar should now run.
